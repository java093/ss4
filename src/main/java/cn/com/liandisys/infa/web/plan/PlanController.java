package cn.com.liandisys.infa.web.plan;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.modules.security.service.SecurityService;
import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.job.QuartzTaskPlan;
import cn.com.liandisys.infa.entity.plan.Plan;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.service.plan.PlanService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.QuartzTriggersUtil;

/**
 * 计划管理模块ACTION，
 * 真正任务的POST请求由Filter完成
 * @author zhangr2
 */
@Controller
@RequestMapping(value = "/plan")
public class PlanController {
	private static Logger logger = LoggerFactory.getLogger(PlanController.class);
	private long total;
    private int currentpage;
	private int limit;
	private Map<String, Object> pageCondition;
	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private PlanService planService;
	@Autowired
	private SecurityService securityService;//当前用户
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.debug("验证成功，跳转到plan页面-任务！");
		//返回的视图名称,/WEB-INF/view/plan/index.jsp
		return "/plan/planList";
	}
	
	@RequestMapping(value="/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.debug("PLAN: list！");
					
		//返回的视图名称,/WEB-INF/view/plan/index.jsp
		List<Plan> list = infaCommonService.findObjects("plan.findAllPlan", null);
		total = infaCommonService.countObject("plan.planCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request.getParameter("rows")), Integer.parseInt(request.getParameter("page")) , total);
		
		return JasonUtil.JasonTOString(pu.getList());
	}
		
	/**新增数据**/
	@RequestMapping(value="/add")
	@ResponseBody
	public String add(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.debug("PLAN: add！");
		//获取前台数据
		Plan plan = new Plan();
		plan.setName(request.getParameter("plan_name"));
		plan.setType(request.getParameter("plan_type"));
		plan.setStart_flag(request.getParameter("plan_start").equals("on") ? "1":"0" );
		plan.setExplain(request.getParameter("explain"));
		plan.setTime(request.getParameter("time"));
		plan.setStartedtime(request.getParameter("stardate"));
		plan.setEnddate(request.getParameter("enddate"));	

		//进行日期格式的转换
		if(null != request.getParameter("day_plan_month") && request.getParameter("day_plan_month").length() > 0){
			String str[] = request.getParameter("day_plan_month").split("null,");
			plan.setDay_plan_month(str[1]);
		}
		
		String type = plan.getType();
		String choiceDay = plan.getDay_plan_month();//天
		String time[] = plan.getTime().split(":");
		String hourPart  = time[0];  //时
		String minutePart  = time[1];//分
		String secondPart  = time[2];//秒
		
		//通过前台传递的执行时间，动态生成CRON表达式
		plan.setCron_expression(getCRON(type,choiceDay,hourPart,minutePart,secondPart));
		plan.setUpdateuserid(securityService.getCurrentUser().getId().toString());
		
		logger.debug("Time == " + getCRON(type,choiceDay,hourPart,minutePart,secondPart));
		
    	infaCommonService.addObject("plan.insertPlan", plan);
    	logger.debug("PLAN: list！");
		return JasonUtil.getJsonResult(true);
	}
	
	
	/**
	 * 删除（多行）
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.debug("plan: delete！");
		List<QuartzTaskPlan> list = null;
		Long z ;
        String selectIdStr = request.getParameter("ids");
    	logger.debug("planid:"+selectIdStr);
        String[] selectedIdArr=selectIdStr.split(",");
        for (int i = 0; i < selectedIdArr.length; i++) {
        	Plan plan =new Plan();
        	plan.setId(Long.valueOf(selectedIdArr[i])); 
        	//获取taskID
        	QuartzTaskPlan tp = new QuartzTaskPlan();
        	tp.setPlanid(Long.valueOf(selectedIdArr[i]));
        	list  =  infaCommonService.findObjects("plan.gettaskid2", tp);
        	
        	//遍历list
        	for(int b=0 ; b<list.size() ;b++){
        		//记录删除前，在taskplan表中，有几条符合条件的数据
	        	z = (Long) infaCommonService.findObject("plan.gettaskcount", list.get(b).getTaskid());
	        	//记录删除前的taskID
	        	String taskID = list.get(b).getTaskid();
	        	//删除infaplan
	            infaCommonService.removeObject("plan.deletePlan", plan);      
	            //删除taskplan
	            infaCommonService.removeObject("plan.deleteTaskPlan", plan);     
	            
	            if(null !=list && list.size() > 0){
	            	//若只剩一条记录，直接删除JOB
	            	if(z <= 1){
	            		planService.addJob(request, taskID,null, QuartzTriggersUtil.OPERATETYPE_DEL);
	            	}else{     
	            		//通过之前记录的taskID，重新生成新的JOB
	            		for (int x = 0; x < list.size(); x++) {
	            			planService.addJob(request, list.get(x).getTaskid(),null, QuartzTriggersUtil.OPERATETYPE_EDIT);
	            		}
	            	}
	            }
        }

        }
        return JasonUtil.getJsonResult(true);
	}
	
	
	


	/**
	 * 根据id查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbyid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.debug("Informatica: findbyid！");
		Plan plan = new Plan();
		String id = request.getParameter("id");
		logger.debug("informaticaid:"+id);	
		plan.setId(Long.valueOf(id));	
		
		Plan rinformatica = (Plan)infaCommonService.findObject("plan.findByid",plan);
		logger.debug(rinformatica.getName());
		return JasonUtil.JasonTOString(rinformatica);
	}
	
	
	
	/**
	 * 编辑
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("update Informatic数据！");
		
		Plan plan = new Plan();
		plan.setId(Long.valueOf(request.getParameter("id")));
		plan.setName(request.getParameter("plan_name"));
		plan.setType(request.getParameter("plan_type"));
		plan.setStart_flag(request.getParameter("plan_start").equals("on") ? "1":"0" );
		plan.setExplain(request.getParameter("explain"));
		plan.setTime(request.getParameter("time"));
		plan.setStartedtime(request.getParameter("stardate"));
		plan.setEnddate(request.getParameter("enddate"));	

		if(null != request.getParameter("day_plan_month") && request.getParameter("day_plan_month").length() > 0){
			String str[] = request.getParameter("day_plan_month").split("null,");
			plan.setDay_plan_month(str[1]);
		}
		
		String type = plan.getType();
		String choiceDay = plan.getDay_plan_month();//天
		String time[] = plan.getTime().split(":");
		String hourPart = time[0];  //时
		String minutePart  = time[1];//分
		String secondPart  = time[2];//秒

		plan.setCron_expression(getCRON(type,choiceDay,hourPart,minutePart,secondPart));
		plan.setUpdateuserid(securityService.getCurrentUser().getId().toString());
		logger.debug("Time == " + getCRON(type,choiceDay,hourPart,minutePart,secondPart));
		
		//通过planID获取中间表中相对应的taskid
    	QuartzTaskPlan tp = new QuartzTaskPlan();
    	tp.setPlanid(plan.getId());
		List<QuartzTaskPlan> list  =  infaCommonService.findObjects("plan.gettaskid2", tp);

		//编辑infaplan
		infaCommonService.modifyObject("plan.updatePlan",plan);
		
		//遍历list，通过之前记录的taskID重新生成新的JOB
		for(int x=0 ; x< list.size() ;x++){
			planService.addJob(request, list.get(x).getTaskid(),null, QuartzTriggersUtil.OPERATETYPE_EDIT);
		}
		
		
		return JasonUtil.getJsonResult(true);
	}
	
	@RequestMapping(value = "/listBySearch")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("PLAN: listBySearch！" );
		String name = request.getParameter("name");
		logger.debug("name:"+name);
		String type = request.getParameter("type");
		logger.debug("type:"+type);
		if(null!=name){
			if(name.equals("")){
				  name = null;
			}
		}
		if(null!=type){
			if(type.equals("")){
				type = null;
			}
		}
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", name);
		pageCondition.put("type", type);
		total = infaCommonService.countObject("plan.plansearchTaskCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<Plan> list = infaCommonService.findObjects("plan.findTaskSearhPlan", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
			
	}
	
	
	
	@RequestMapping(value = "/listBySearch1")
	@ResponseBody
	public String listByName1(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("PLAN: listBySearch 查询！" );
		String name = request.getParameter("name");
		logger.debug("name:"+name);
		String type = request.getParameter("type");
		logger.debug("type:"+type);
		if(null!=name){
			if(name.equals("")){
				  name = null;
			}
		}
		if(null!=type){
			if(type.equals("")){
				type = null;
			}
		}
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.debug("行数："+request.getParameter("rows"));
		logger.debug("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", name);
		pageCondition.put("type", type);
		total = infaCommonService.countObject("plan.plansearchCount", pageCondition);	
		logger.debug("total:"+total);
		logger.debug("==="+pageCondition);
		List<Plan> list = infaCommonService.findObjects("plan.findSearhPlan", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
			
	}
	
	/**CORN表达式转换
	 * @param  
	 * 			String choiceDay  天
	 * 			String hourPart   时
	 * 			String minutePart 分
	 * 			String secondPart 秒
	 * */
	public String getCRON(String type ,String choiceDay , String hourPart , String minutePart ,String secondPart){
		if(type.equals("0")){
			return null;
		}else if(type.equals("1")){
			//按天执行,cron表达式(分，时)
			String mycron = secondPart + " "+ minutePart + " " + hourPart + " 1/1 * ?";
			return mycron;
		}else{
			//按月执行,cron表达式(分，时，天)
			String mycron = secondPart + " "+minutePart+" "+hourPart+" "+choiceDay+" 1/1 ? * ";
			return mycron;
		}
	}
	
	/**
	 * 查询是否同名
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findbyname")
	@ResponseBody
	public String findbyname(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("DataBase: findbyname！");
		Plan plan = new Plan();
		String id = request.getParameter("id");
		if (null != id) {
			logger.debug("DataBase:" + id);
			plan.setId(Long.valueOf(id));
			Plan rdb1 = (Plan) infaCommonService.findObject(
					"plan.findByid", plan);
			if (rdb1.getName().equals(request.getParameter("plan_name"))) {
				return "true";
			}
		}
		Plan db = new Plan();
		String name = request.getParameter("plan_name");
		db.setName(name);
		long i = (Long) infaCommonService.findObject("plan.findByDBName", db);
		if (i == 0) {
			return "true";
		} else {
			return "false";
		}
	}
	
	
	/**
	 * 查询是否同名
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findbyname2")
	@ResponseBody
	public String findbyname2(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("DataBase: findbyname！");
		Plan plan = new Plan();
		String id = request.getParameter("id");
		if (null != id) {
			plan.setId(Long.valueOf(id));
			Plan rdb1 = (Plan) infaCommonService.findObject(
					"plan.findByid", plan);
			if (rdb1.getName().equals(request.getParameter("name"))) {
				return "true";
			}
		}
		Plan db = new Plan();
		String name = request.getParameter("name");
		db.setName(name);
		long i = (Long) infaCommonService.findObject("plan.findByDBName", db);
		if (i == 0) {
			return "true";
		} else {
			return "false";
		}
	}
	
}
