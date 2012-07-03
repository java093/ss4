package cn.com.liandisys.infa.web.job;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import cn.com.liandisys.infa.entity.job.WorkFlow;
import cn.com.liandisys.infa.entity.sys.DataBase;
import cn.com.liandisys.infa.entity.sys.Informatica;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.workflow.WorkFlowImportUtil;

/**
 * WorkFlow管理模块ACTION，
 * 
 * 真正WorkFlow的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/workflow")
public class WorkFlowController {

	private static Logger logger = LoggerFactory
			.getLogger(WorkFlowController.class);


	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private SecurityService securityService;

	private SchedulerFactoryBean quartzScheduler;

	private List<WorkFlow> rows = null;

	private long total;

	private int currentpage;
    private int limit;
    private Map<String, Object> pageCondition;
	/*
	 * 跳转的workflow一览页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("验证成功，跳转到WorkFlow一览页面-WorkFlow！");
		// 跳转到job下 的workflowList.jsp
		return "/job/workflowList";
	}


	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("WORKFLOW: list！");
	
		// 查询WORKFLOW表的数据
		List<WorkFlow> list = infaCommonService.findObjects(
				"workflow.findAllWorkFlow", null);
		// 分页显示
		total = infaCommonService.countObject("workflow.workflowCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		return JasonUtil.JasonTOString(pu.getList());

	}

	@RequestMapping(value = "/listworkflow")
	@ResponseBody
	public String listworkflow(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("WORKFLOW: list！");
		// 查询WORKFLOW表的数据
		List<WorkFlow> list = infaCommonService.findObjects(
				"workflow.findAllWorkFlow", null);
		// 分页显示
		total = infaCommonService.countObject("workflow.workflowCount", null);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		return JasonUtil.JasonTOString(pu.getList());

	}

	

	/**
	 * 根据id查找记录
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findById")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model)
			throws BusinessException {
		WorkFlow workflow = new WorkFlow();
		String id = request.getParameter("id");
		workflow.setId(Long.valueOf(id));
		WorkFlow wk = (WorkFlow) infaCommonService.findObject(
				"workflow.findByid", workflow);
		return JasonUtil.JasonTOString(wk);
	}

	/**
	 * 编辑
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(HttpServletRequest request, Model model)
			throws BusinessException {

		WorkFlow workflow = new WorkFlow();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String id = request.getParameter("id");
		workflow.setId(Long.valueOf(id));
		workflow.setALIAS(request.getParameter("ALIAS"));
		workflow.setEXPLAIN(request.getParameter("EXPLAIN"));
		workflow.setUPDATETIME(time);
		infaCommonService.modifyObject("workflow.updateWorkFlow", workflow);
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 根据workflow名称 模糊查询
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findListByName")
	@ResponseBody
	public String findListByName(HttpServletRequest request, Model model)
			throws BusinessException {

		String alias = request.getParameter("workflow");
		logger.info("别名是---------------------------------:" + alias);
		WorkFlow workflow = new WorkFlow();
		workflow.setALIAS(alias);

		List<WorkFlow> list = infaCommonService.findObjects(
				"workflow.findByName", workflow);
		logger.info("list大小---------------------------------:" + list.size());
		total = infaCommonService.countObject("workflow.workflowsearchCount",
				workflow);
		logger.info("total:" + total);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		return JasonUtil.JasonTOString(pu.getList());
	}

	/*
	 * 删除（多行）
	 */
	@RequestMapping(value = "/deleteWorkFlow")
	@ResponseBody
	public String deleteWorkFlow(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("WorkFlow: delete！");
		rows = new ArrayList<WorkFlow>();
		String selectIdStr = request.getParameter("ids");
		logger.info("workflowid:" + selectIdStr);
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			WorkFlow workflow = new WorkFlow();
			workflow.setId(Long.valueOf(selectedIdArr[i]));
			rows.add(workflow);
		}
		//删除关联的TaskDetail信息
		infaCommonService.batchRemoveObject("workflow.deleteTaskDetail", rows);
		infaCommonService.batchRemoveObject("workflow.deleteWorkFlow", rows);
		return JasonUtil.getJsonResult(true);
	}

	/*
	 * 获取workflow第一个下拉框数据
	 */
	@RequestMapping(value = "/workflowSelect")
	@ResponseBody
	public String workflowSelect(HttpServletRequest request, Model model,
			HttpServletResponse response) throws Exception {

		List<Informatica> inforList = infaCommonService.findObjects(
				"informatica.findAllInformatica", null);
		int rows = inforList.size();
		logger.info("  查到的第一个下拉框数据是: " + rows + " 条。");
		String json = JSONArray.fromObject(inforList).toString();
		response.getWriter().write(json);
		return null;

	}

	/*
	 * 获取workflow第二个下拉框数据
	 */
	@RequestMapping(value = "/workflowSelectDataBase")
	@ResponseBody
	public String workflowSelectDataBase(HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {

		String infomatic_id = request.getParameter("inforid");
		logger.info(" infomatic_id ----------------------" + infomatic_id);

		DataBase db = new DataBase();
		db.setInfomatic_id(infomatic_id);
		List<DataBase> dbList = infaCommonService.findObjects(
				"db.findByINFOMATIC_ID", db);
		logger.info(" 第二个下拉框的数据有" + dbList.size() + " 条");
		String json = JSONArray.fromObject(dbList).toString();
		response.getWriter().write(json);
		return null;

	}

	/*
	 * 查询服务器上的workflow
	 */
	@RequestMapping(value = "/confirmWorkFlow")
	@ResponseBody
	public String confirmWorkFlow(HttpServletRequest request, Model model)
			throws Exception {
		String workId = request.getParameter("workId");
		String title = request.getParameter("title");
		logger.info("ID: " + workId + " 服务器 ： " + title);
		
		//根据id查询informatica
		Informatica infor = new Informatica();
		infor.setId(Long.valueOf(workId));
		Informatica informa = (Informatica) infaCommonService.findObject("informatica.findByid", infor);
		logger.info("knowledge_base----------------- " + informa.getKnowledge_base());
		
		DataBase db = new DataBase();
		db.setId(Long.valueOf(informa.getKnowledge_base()));
		DataBase rdb = (DataBase) infaCommonService.findObject("db.findByid",
				db);
		logger.info("DataBase的信息   ：----------"+rdb.getDbname()+" -------------"
				+rdb.getUsername()+"----------------IP:"+rdb.getIp()+"---------"+rdb.getPort()
				+"--------"+rdb.getDbtype()+"----------"+rdb.getPassword()
				+"---"+rdb.getServer_name());
		
		long dbid = rdb.getId();
		long infa_id = Long.valueOf(workId);
		WorkFlowImportUtil workUtil = new WorkFlowImportUtil();

		// 把视图内容导入到目标数据库的中间表（INFA_WORKFLOW_TEMP）
		if ((Long.valueOf(workId)).equals(informa.getId())) {

			//清空中间表
			infaCommonService.removeObject("workflow.deleteWorkFlowTemp", null);
			ArrayList<WorkFlow> getList = workUtil.getWorkFlowList(
					rdb.getServer_name(), rdb.getDbtype(), rdb.getUsername(),
					rdb.getPort(), rdb.getIp(), rdb.getPassword());
			int rows = getList.size();
			logger.info(" 查到的workflow记录数是 : ---------------- " + rows);
			
			Iterator<WorkFlow> it = getList.iterator();
			while(it.hasNext()){
				WorkFlow workFlow = (WorkFlow) it.next();
				workFlow.setDB_ID(dbid);
				workFlow.setINFA_ID(infa_id);
			}
			//数据导入中间表
			infaCommonService.batchAddObject("workflow.insertWorkFlowTemp", getList);
		}

		// 获取中间表（INFA_WORKFLOW_TEMP）的内容,并分页展示。
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.info("行数："+request.getParameter("rows"));
		logger.info("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		
		total = infaCommonService.countObject("workflow.workflowsearchCount_Temp", pageCondition);	
		logger.info("total:--------------------"+total);
		logger.info("==="+pageCondition);
		List<WorkFlow> list = infaCommonService.findObjects("workflow.findSearch_Temp", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
		
	}

	/*
	 * 把选择的workflow导入到目标数据库。
	 */
	@RequestMapping(value = "/importToData")
	@ResponseBody
	public String importToData(HttpServletRequest request, Model model)
			throws Exception {

		ArrayList<WorkFlow> alist = new ArrayList<WorkFlow>();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		String selectIdStr = request.getParameter("ids");
		logger.info("workflowid:" + selectIdStr);
		String[] selectedIdArr = selectIdStr.split(",");
		
		ArrayList<String> selectList = new ArrayList<String>();
		for (int i =0; i < selectedIdArr.length;i++){	
			String sel = selectedIdArr[i];
			selectList.add(sel);
		}
		List<WorkFlow> findTempList = infaCommonService.findObjects("workflow.findAllWorkFlowTemp", null);
		
		//当多次点击“确认”按钮时，比较得到已存在中间表id，避免空id报空指针异常
		for (int i = 0; i < selectList.size(); i++){
			String selectid = selectList.get(i);
			for ( int j = 0; j < findTempList.size(); j++){
				String tempid = String.valueOf(findTempList.get(j).getId());
				if(selectid.equals(tempid)){
					logger.info("此id在中间表中存在 -------" +selectList.get(i));
					WorkFlow workflow = new WorkFlow();
					workflow.setId(Long.valueOf(selectList.get(i)));
					WorkFlow wk = (WorkFlow) infaCommonService.findObject(
							"workflow.findTempByid", workflow);
					wk.setCREATETIME(time);
					wk.setUPDATETIME(time);
					alist.add(wk);
				}
			}
		}
		
//		for (int i = 0; i < selectedIdArr.length; i++) {
//			WorkFlow workflow = new WorkFlow();
//			workflow.setId(Long.valueOf(selectedIdArr[i]));
//			WorkFlow wk = (WorkFlow) infaCommonService.findObject(
//					"workflow.findTempByid", workflow);
//			wk.setCREATETIME(time);
//			wk.setUPDATETIME(time);
//			alist.add(wk);
//
//		}
		logger.info("alist里有：  " + alist.size() + " 个workflow");
		WorkFlowImportUtil workflowUtil = new WorkFlowImportUtil();
		
		// 查询WORKFLOW表的数据
		List<WorkFlow> list = infaCommonService.findObjects(
				"workflow.findAllWorkFlow", null);
		
		Iterator<WorkFlow> it = alist.iterator();
		while(it.hasNext()){
			WorkFlow workFlow = (WorkFlow) it.next();
			workFlow.setALIAS(workFlow.getWORKFLOW_NAME());
			workFlow.setUPDATEUSERID(String.valueOf(securityService.getCurrentUser().getId()));
		}
		
		// 开始比较，去掉重复workflow
		int flag = workflowUtil.comparison(list, alist);
		logger.info("flag----------------------------" + flag);
		if(flag == 0){
			//workflow已存在就更新
			ArrayList<WorkFlow> uplist = new ArrayList<WorkFlow>();
			for (int i = 0; i< alist.size(); i++) {  
		        String org = String.valueOf(alist.get(i).getWORKFLOW_ID()); 
		        for (int j = 0; j < list.size(); j++) {  
		            String ep = String.valueOf(list.get(j).getWORKFLOW_ID());  
		            if (org.equals(ep)) {  
		            	WorkFlow wk = alist.get(i);
		            	uplist.add(wk);
		                
		            }  
		         }       
		       }
			logger.info("uplist.size----------------------------" + uplist.size());
			
			Iterator<WorkFlow> iter = uplist.iterator();
			while(iter.hasNext()){
				WorkFlow workFlow = (WorkFlow) iter.next();
				WorkFlow wk = (WorkFlow) infaCommonService.findObject(
						"workflow.findByWORKFLOW_ID", workFlow);
				workFlow.setALIAS(wk.getALIAS());
				workFlow.setEXPLAIN(wk.getEXPLAIN());
			}
			
			infaCommonService.batchModifyObject("workflow.refreshWorkFlow", uplist);
			for(int i = 0; i< alist.size(); i++){
			logger.info("alist.workflow_id----------------------------" + alist.get(i).getWORKFLOW_ID());
			}
		    for (int i = 0; i< alist.size(); i++) {  
	        String org = String.valueOf(alist.get(i).getWORKFLOW_ID()); 
	        for (int j = 0; j < list.size(); j++) {  
	            String ep = String.valueOf(list.get(j).getWORKFLOW_ID());  
	            if (org.equals(ep)) {  
	                alist.remove(i);  
	                i--;  
	            }  
	         }       
	       }
		    logger.info("alist.size----------------------------" + alist.size());
		    infaCommonService.batchAddObject("workflow.insertWorkFlow", alist); 
			
		}else {
			//workflow没有 就导入
			infaCommonService.batchAddObject("workflow.insertWorkFlow", alist);
		}		
		return JasonUtil.getJsonResult(true);
	}

	@RequestMapping(value = "/listByName")
	@ResponseBody
	public String listByName(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("WORKFLOW: listByName！");
		String alias = request.getParameter("name");
		String infa_id = request.getParameter("infa_id");
		if(alias!=null){
		if(alias.equals("")){
			alias = null;
		}
		}
		
		if(infa_id!=null){
			if(infa_id.equals("")){
				infa_id = null;
			}
			}
		
		logger.info("workflow参数：" + alias);
		currentpage =Integer.parseInt(request.getParameter("page"));
		limit = Integer.parseInt(request.getParameter("rows"));
		logger.info("行数："+request.getParameter("rows"));
		logger.info("当前页数："+request.getParameter("page"));		
		pageCondition = PagingUtil.pageCondition(currentpage, limit); 
		pageCondition.put("name", alias);
		pageCondition.put("infa_id", infa_id);
		total = infaCommonService.countObject("workflow.workflowsearchCount", pageCondition);	
		logger.info("total:"+total);
		logger.info("==="+pageCondition);
		List<WorkFlow> list = infaCommonService.findObjects("workflow.findByName", pageCondition);
		return JasonUtil.JasonTOString(PagingUtil.getPage(list, total));
	}

	/**
	 * workflow别名重名校验
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/findbyALIAS" )
	@ResponseBody
	public String findbyALIAS(HttpServletRequest request, Model model) 
			throws BusinessException {
		logger.info("ALIAS-findbyALIAS------:");	
		WorkFlow workflow = new WorkFlow();
		String alias = request.getParameter("ALIAS");
		String id = request.getParameter("id");
		logger.info("ALIAS-------:"+alias);	
		logger.info("id:-------"+id);	
		
		
//		workflow.setId(Long.valueOf(id));
//		WorkFlow wk = (WorkFlow) infaCommonService.findObject(
//					"workflow.findByid", workflow);
		
		if(!id.equals("0")){
			workflow.setId(Long.valueOf(id));
			WorkFlow wk = (WorkFlow) infaCommonService.findObject(
					"workflow.findByid", workflow);
			if(wk.getALIAS().equals(alias)){
				return "true";
			}
		}
			
		
		workflow.setALIAS(alias);
		long  i = (Long)infaCommonService.findObject("workflow.findByALIAS",workflow);
	    if(i==0){
	    	return "true";
	    }else{
	    	return "false";
	    }
		
		
	}
}
