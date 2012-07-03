package cn.com.liandisys.infa.web.job;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.modules.security.service.SecurityService;
import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.job.Task;
import cn.com.liandisys.infa.entity.job.TaskPlans;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.service.plan.PlanService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.QuartzTriggersUtil;

/**
 * 任务管理模块ACTION，
 * 真正任务的POST请求由Filter完成
 *  
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController {
	
	private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	private long total;
	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private PlanService planService;
	@Autowired
	private SecurityService securityService;


	private Map<String, Object> pageCondition;
    /**
     * 任务List
     */
    private List rows= null;
    private int currentpage;
    private int limit;

	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("验证成功，跳转到task页面-任务！");
		return "/job/taskList";
	}
	
	@RequestMapping(value="/maintaskindex",method=RequestMethod.GET)
	public String maintaskindex(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("验证成功，跳转到task页面-任务！");
		return "/job/maintaskList";
	}
	
	/**
	 * 初始化一览数据
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("TASK: list！");
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		total = infaCommonService.countObject("task.taskCount", pageCondition);	
		logger.info("total:"+total);
		List<Task> list = infaCommonService.findObjects("task.findAllTask", pageCondition);
		
	
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
	
	/**
	 * 初始化一览数据(主任务)
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/mainlist")
	@ResponseBody
	public String mainlist(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("TASK: mainlist！");
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		pageCondition = PagingUtil.pageCondition(currentpage, limit);
		total = infaCommonService.countObject("task.maintaskCount", pageCondition);	
		logger.info("total:"+total);
		List<Task> list = infaCommonService.findObjects("task.findAllMainTask", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
	
	/**
	 * 增加
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public String add(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("TASK: add！");
		Task task = new Task();
		logger.info("taskname: "+request.getParameter("taskname"));
		logger.info("runtype："+request.getParameter("runtype"));
		logger.info("explain: "+request.getParameter("explain"));
		logger.info("type:"+request.getParameter("type"));
		String type = request.getParameter("type");
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
		
		task.setTASKNAME(request.getParameter("taskname"));
		task.setTYPE(request.getParameter("type"));
	    task.setRUNTYPE(request.getParameter("runtype"));
        task.setEXPLAIN(request.getParameter("explain"));
        task.setCOMPLETEFLAG(request.getParameter("noticeflag"));
        task.setMAIL_ID(request.getParameter("emailID"));
	   //task.setUPDATESERID("123");
         task.setUPDATESERID(String.valueOf(securityService.getCurrentUser().getId()));
	    task.setCREATETIME(time);
	    task.setUPDATETIME(time);
	    logger.info("插入TASK表");
	    BigDecimal id = (BigDecimal)infaCommonService.findObject("task.getSequence", null);
	    task.setId(id.longValue());
	    logger.info("task.getSequence: " + id.toString());
    	infaCommonService.addObject("task.insertTask", task);//插入TASK表
    	
    	// 插入步骤执行列表
    	String stepListStr = request.getParameter("stepRows");
    	logger.info(stepListStr);
    	JSONObject jsonObj = JSONObject.fromObject(stepListStr);
    	JSONArray stepRows = jsonObj.getJSONArray("rows");
    	List<Object> stepList = new ArrayList<Object>();
    	HashMap stepMap;
    	logger.info("步骤的个数："+stepRows.size());
    	if(stepRows.size()>0){
	    	for(int i = 0; i < stepRows.size(); i++){
	    		stepMap = new HashMap();
	    		logger.info("workflowid:"+String.valueOf(stepRows.getJSONObject(i).get("workflowid")));
	    		logger.info("subtaskid:"+String.valueOf(stepRows.getJSONObject(i).get("subtaskid")));
	    		logger.info("index:"+stepRows.getJSONObject(i).get("index").toString());
	    		String workid = String.valueOf(stepRows.getJSONObject(i).get("workflowid"));
	    		String subtaskid=String.valueOf(stepRows.getJSONObject(i).get("subtaskid"));
	    		if(workid.equals("")||null==subtaskid){
	    			workid="";
	    		}
	    		if(subtaskid.equals("")||null==subtaskid){
	    			subtaskid="";
	    		}
	    		logger.info("workid:"+workid+";subtaskid:"+subtaskid);
	    	    stepMap.put("taskid", id);
	    		stepMap.put("workid", workid);
	    		stepMap.put("subtaskid",subtaskid);
	    		stepMap.put("orders",BigDecimal.valueOf(Long.valueOf(String.valueOf(stepRows.getJSONObject(i).get("index")))));
	    		stepList.add(stepMap);
	    	}
	    	 logger.info("插入TASKDETAIL表");
	    	 infaCommonService.batchAddObject("task.insertStep", stepList);
    	}
    	if(type.equals("0")){
    	// 插入计划执行列表
    	String planListStr = request.getParameter("planRows");
    	jsonObj = JSONObject.fromObject(planListStr);
    	JSONArray planRows = jsonObj.getJSONArray("rows");
    	List<Object> planpList = new ArrayList<Object>();
    	HashMap planMap;
    	logger.info("计划的个数："+planRows.toString());
    	if(planRows.size()>0){
	    	for(int i = 0; i < planRows.size(); i++){
	    		planMap = new HashMap();
	    		logger.info("planid:"+BigDecimal.valueOf(Long.valueOf(String.valueOf(planRows.getJSONObject(i).get("planid")))).toString());
	    		planMap.put("taskid", id);
	    		planMap.put("planid", BigDecimal.valueOf(Long.valueOf(String.valueOf(planRows.getJSONObject(i).get("planid")))));
	    		planpList.add(planMap);
	    	}
	    	 logger.info("插入TASKPLAN表");
	    	infaCommonService.batchAddObject("task.insertPlan", planpList);
    	}
    	}
    	planService.addJob(request, id.toString() , null ,QuartzTriggersUtil.OPERATETYPE_IN);
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
		logger.info("TASK: findbyid！");
		Task task = new Task();
		String id = request.getParameter("id");
		logger.info("taskid:"+id);	
		task.setId(Long.valueOf(id));
		rows = new ArrayList();
		HashMap map = new HashMap();
		Task rtask = (Task)infaCommonService.findObject("task.findById",task);
		//步骤列表
		logger.info("TASK: findStepsById！");
	    List taskStepsList = infaCommonService.findObjects("task.findStepsById",rtask);
		//计划列表
		logger.info("TASK: findPlansById！");
	    List<TaskPlans> taskPlansList = infaCommonService.findObjects("task.findPlansById",rtask);
	    logger.info("TASK: 组织JSON！");
	    map.put("task",rtask);
	  	map.put("taskStepsList",taskStepsList);
	  	map.put("taskPlansList",taskPlansList);
	  	Set set = map.keySet();
		logger.info(JasonUtil.getJsonResultEdit(map,true));
		
		return JasonUtil.getJsonResultEdit(map,true);
	}
	
	
	/**
	 * 根据id查找记录
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbyname" )
	@ResponseBody
	public String findbyname(HttpServletRequest request, Model model) 
			throws BusinessException {
		
		logger.info("TASK: findbyname！");
		Task task = new Task();
		String name = request.getParameter("TASKNAME");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		logger.info("taskname:"+name);	
		logger.info("id:"+id);	
		if(!id.equals("0")){
			task.setId(Long.valueOf(id));
			Task rtask = (Task)infaCommonService.findObject("task.findById",task);
			if(rtask.getNAME().equals(name)){
				return "true";
			}
		}
		task.setTASKNAME(name);
		task.setTYPE(type);
		long  i = (Long)infaCommonService.findObject("task.findByTaskName",task);
	    if(i==0){
	    	return "true";
	    }else{
	    	return "false";
	    }
				
	}
	
	/**
	 * 编辑
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public String edittask(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("TASK: edit！");
		
		SchedulerFactoryBean.getConfigTimeTaskExecutor();
		
		Long id = Long.valueOf(request.getParameter("taskid"));
		logger.info("edit-Taskid:"+id);
		
		
		
		Task task = new Task();
		logger.info(request.getParameter("taskname"));
		logger.info(request.getParameter("runtype"));
		logger.info(request.getParameter("explain"));
		String type = request.getParameter("type");
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
		task.setTASKNAME(request.getParameter("taskname"));
		task.setTYPE(type);
	    task.setRUNTYPE(request.getParameter("runtype"));
        task.setEXPLAIN(request.getParameter("explain"));
        task.setCOMPLETEFLAG(request.getParameter("noticeflag"));
        task.setMAIL_ID(request.getParameter("emailID"));
	   // task.setUPDATESERID("123");
	    task.setUPDATESERID(String.valueOf(securityService.getCurrentUser().getId()));
	    task.setUPDATETIME(time);
	    logger.info("修改TASK表");
	
	    task.setId(id.longValue());

    	infaCommonService.addObject("task.updateTask", task);//更新updateTask
        infaCommonService.removeObject("task.deleteTaskDetail",task);//删除该任务下的所有workflow
        infaCommonService.removeObject("task.deleteTaskPlan", task);//删除该任务下的所有计划
    	
    	
    	// 插入步骤执行列表
    	String stepListStr = request.getParameter("stepRows");
    	logger.info(stepListStr);
    	JSONObject jsonObj = JSONObject.fromObject(stepListStr);
    	JSONArray stepRows = jsonObj.getJSONArray("rows");
    	List<Object> stepList = new ArrayList<Object>();
    	HashMap stepMap;
    	logger.info("步骤的个数："+stepRows.size());
    	if(stepRows.size()>0){
	    	for(int i = 0; i < stepRows.size(); i++){
	    		stepMap = new HashMap();
	    		logger.info("workflowid:"+String.valueOf(stepRows.getJSONObject(i).get("workflowid")));
	    		logger.info("subtaskid:"+String.valueOf(stepRows.getJSONObject(i).get("subtaskid")));
	    		logger.info("index:"+stepRows.getJSONObject(i).get("index").toString());
	    		String workid = String.valueOf(stepRows.getJSONObject(i).get("workflowid"));
	    		String subtaskid=String.valueOf(stepRows.getJSONObject(i).get("subtaskid"));
	    		if(workid.equals("0")||null==subtaskid){
	    			workid="";
	    		}
	    		if(subtaskid.equals("0")||null==subtaskid){
	    			subtaskid="";
	    		}
	    		logger.info("workid:"+workid+";subtaskid:"+subtaskid);
	    	    stepMap.put("taskid", id);
	    		stepMap.put("workid", workid);
	    		stepMap.put("subtaskid",subtaskid);
	    		stepMap.put("orders",BigDecimal.valueOf(Long.valueOf(String.valueOf(stepRows.getJSONObject(i).get("index")))));
	    		stepList.add(stepMap);
	    	}
	    	 logger.info("插入TASKDETAIL表");
	    	 infaCommonService.batchAddObject("task.insertStep", stepList);
    	}
    	if(type.equals("0")){
    	// 插入计划执行列表
    	String planListStr = request.getParameter("planRows");
    	jsonObj = JSONObject.fromObject(planListStr);
    	JSONArray planRows = jsonObj.getJSONArray("rows");
    	List<Object> planpList = new ArrayList<Object>();
    	HashMap planMap;
    	logger.info("计划的个数："+planRows.toString());
    	if(planRows.size()>0){
	    	for(int i = 0; i < planRows.size(); i++){
	    		planMap = new HashMap();
	    		logger.info("planid:"+BigDecimal.valueOf(Long.valueOf(String.valueOf(planRows.getJSONObject(i).get("planid")))).toString());
	    		planMap.put("taskid", id);
	    		planMap.put("planid", BigDecimal.valueOf(Long.valueOf(String.valueOf(planRows.getJSONObject(i).get("planid")))));
	    		planpList.add(planMap);
	    	}
	    	 logger.info("插入TASKPLAN表");
	    	infaCommonService.batchAddObject("task.insertPlan", planpList);
    	}
    	}
    	planService.addJob(request, id.toString() , null ,QuartzTriggersUtil.OPERATETYPE_EDIT);
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
		logger.info("TASK: delete！");
		rows = new ArrayList<Task>();
        String selectIdStr = request.getParameter("ids");
        String type = request.getParameter("type");
    	logger.info("taskid:"+selectIdStr+"type:"+type);
        String[] selectedIdArr=selectIdStr.split(",");
        for (int i = 0; i < selectedIdArr.length; i++) {
        	Task task =new Task();
        	task.setId(Long.valueOf(selectedIdArr[i])); 	
            rows.add(task);
            planService.addJob(request, Long.valueOf(selectedIdArr[i]).toString() , null ,QuartzTriggersUtil.OPERATETYPE_DEL);
        }
        infaCommonService.batchRemoveObject("task.deleteTask", rows);
        if(type.equals("0")){
            infaCommonService.batchRemoveObject("task.deleteTaskDetail", rows);
            infaCommonService.batchRemoveObject("task.deleteTaskPlan", rows);
        }else if(type.equals("1")){
        	infaCommonService.batchRemoveObject("task.deleteTaskDetail", rows);
            infaCommonService.batchRemoveObject("task.deleteTaskPlan", rows);
            logger.info("================shanchu");
            infaCommonService.batchRemoveObject("task.deleteTaskDetailTask", rows);
        }
        return JasonUtil.getJsonResult(true);
	}
	

	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("TASK: listByName！");
		String name = request.getParameter("name");
		
		String type = request.getParameter("type");
		String runtype = request.getParameter("runtype");
		if(name.equals("")){
			  name = null;
		}
		if(type.equals("")){
			type = null;
		}
		if(runtype.equals("")){
			runtype = null;
		}
		logger.info("taskname:"+name+",tasktype:"+type+",runtype:"+runtype);
		
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.info("行数："+request.getParameter("rows"));
		logger.info("当前页数："+request.getParameter("page"));		
	
		pageCondition = PagingUtil.pageCondition(currentpage, limit);	
	    pageCondition.put("type", type);
	    pageCondition.put("name", name);
	    pageCondition.put("runtype", runtype);
		// 查询TASK表的数据
		List<Task> list = infaCommonService.findObjects(
				"task.findByName", pageCondition);
		total = infaCommonService.countObject("task.tasksearchCount", pageCondition);
		logger.info("total:"+total);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}
}
