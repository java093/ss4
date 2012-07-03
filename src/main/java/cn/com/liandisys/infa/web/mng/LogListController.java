package cn.com.liandisys.infa.web.mng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.mng.InfaSessLog;
import cn.com.liandisys.infa.entity.mng.LogList;
import cn.com.liandisys.infa.entity.mng.LogRelation;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.service.task.TaskService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.TimeUtil;
import cn.com.liandisys.infa.util.workflow.WorkFlowException;

/**
 * 任务管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/mng")
public class LogListController {

	private static Logger logger = LoggerFactory
			.getLogger(LogListController.class);

	@Autowired
	private InfaCommonService infaCommonService;

	@Autowired
	private TaskService  taskService;
	

	@RequestMapping(value = "/taskloglist", method = RequestMethod.GET)
	public String taskloglist(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("验证成功，跳转到mng页面-日志管理！");
		// 返回的视图名称,/WEB-INF/view/mng/loglist.jsp
		return "/mng/taskloglist";
	}

	@RequestMapping(value = "/loglist", method = RequestMethod.GET)
	public String loglist(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("验证成功，跳转到mng页面-任务！");
		// 返回的视图名称,/WEB-INF/view/mng/loglist.jsp
		return "/mng/loglist";
	}

	@RequestMapping(value = "/taskloglisttotal")
	@ResponseBody
	public String taskloglisttotal(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("MNG: list！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List<LogList> list = infaCommonService.findObjects(
				"log.findtaskloglisttotal", null);
		logger.info("查询出的条数:"+list.size());
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), list.size());
		logger.info(JasonUtil.JasonTOString(pu.getList()));
		return JasonUtil.JasonTOString(pu.getList());
	}


	@RequestMapping(value = "/maintaskLog")
	@ResponseBody
	public String maintaskLog(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {
		logger.info("MNG: maintaskLog！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List list = infaCommonService.findObjects(
				"log.findAllLog", null);
		logger.info("符合条件的记录数："+list.size());
		for (int i = 0; i < list.size(); i++) {
			HashMap log = (HashMap)list.get(i);
			LogList logMain = new LogList();
			logger.info("主任务LoG_ID ："+log.get("ID").toString());
			logMain.setId(Long.valueOf(log.get("ID").toString()));
			long  findMainDetailLog = infaCommonService.countObject(
					"log.findMainDetailLog", logMain);
			long  findMainSubLog = infaCommonService.countObject(
					"log.findMainSubLog", logMain);
			logger.info("主任务子任务的LOG数 ："+ findMainSubLog);
			logger.info("主任务WORKFLOW的LOG数 ："+ findMainDetailLog);
			if(findMainDetailLog+findMainSubLog>0){
				log.put("STATE", "CLOSED");
			}else{
				log.put("STATE", "OPEN");
			}
		}
		logger.info("查询出的条数:"+list.size());
		return JasonUtil.JasonTOString(list);

	}
	
	
	@RequestMapping(value = "/taskDetailLog")
	@ResponseBody
	public String taskDetailLog(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("MNG: taskDetailLog！");
		String tasklog = request.getParameter("logid");
		String level = request.getParameter("levelid");
		logger.info("LOGID："+tasklog);
		logger.info("层次："+level);
		if(level.equals("1")){
			LogList mainlog =new LogList();
			mainlog.setId(Long.valueOf(tasklog));
			//取主任务下子任务的log
			logger.info("MNG1: findTaskDetailLog！");
			List list = infaCommonService.findObjects(
					"log.findTaskDetailLog",mainlog);
			
			logger.info("MNG2: findTaskWorkflowLog！");
	        //判读子任务下是否有workflowlog
			for (int i = 0; i < list.size(); i++) {
				HashMap subtasklog = (HashMap)list.get(i);
				LogList sublog = new LogList();
				String id = subtasklog.get("ID").toString();
				sublog.setId(Long.valueOf(id));
				
				List sutasksLog  = infaCommonService.findObjects(
						"log.findTaskWorkflowLog", sublog);
				logger.info("子任务下的workflowLog："+sutasksLog.size());
				if(sutasksLog.size()>0){
					subtasklog.put("STATE", "CLOSED");
				}else{
					subtasklog.put("STATE", "OPEN");
				}
			}
			logger.info("MNG3: findMainTaskWorkflowLog！");
			//取主任务下的workflowlog
			logger.info("主任务下的workflowlog:"+mainlog.getId());
			List workflowList =infaCommonService.findObjects(
					"log.findMainTaskWorkflowLog",mainlog);
			logger.info("主任务下的workflowlog大小:"+workflowList.size());
			for (int i = 0; i < workflowList.size(); i++) {
				HashMap workflowlog = (HashMap)workflowList.get(i);		
				workflowlog.put("STATE", "OPEN");
				logger.info(workflowlog.get("ID").toString());
				list.add(workflowlog);
			}
		     return JasonUtil.JasonTOString(list);
	   
		}else{//子任务查询
			LogList subtasklog =new LogList();
			subtasklog.setId(Long.valueOf(tasklog));
			List workflowList =infaCommonService.findObjects(
					"log.findTaskWorkflowLog",subtasklog);
			logger.info("主任务下的workflowlog大小:"+workflowList.size());
			for (int i = 0; i < workflowList.size(); i++) {
				HashMap workflowlog = (HashMap)workflowList.get(i);		
				workflowlog.put("STATE", "OPEN");
				logger.info(workflowlog.get("ID").toString());
			}
			return JasonUtil.JasonTOString(workflowList);
		}
	}
	
	@RequestMapping(value = "/start")
	@ResponseBody
	public String start(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("MNG: start！");	
		String level = request.getParameter("level");
		if(level.equals("1")){
			logger.info("MNG: 主任务start！");	
			String id = request.getParameter("id");
			taskService.startTask(Long.valueOf(id), 1, Long.valueOf(0));
		}else if(level.equals("2")){
			logger.info("MNG: 子任务start！");	
			String flag = request.getParameter("flag"); 
			if(flag.equals("1")){
				String id = request.getParameter("id"); 
				String parentTaskID = request.getParameter("parentTaskID");
				logger.info("id："+id);
				logger.info("parentTaskID："+parentTaskID);
				taskService.startTask(Long.valueOf(id),-1, Long.valueOf(parentTaskID));	
			}else if(flag.equals("2")){
				logger.info("MNG: 主任务下workflowstart！");	
				String id = request.getParameter("id"); 
				String mainTaskID = request.getParameter("mainTaskID");
				logger.info("id："+id);
				logger.info("mainTaskID："+mainTaskID);
				//修改主任务下workflow启动传参值和传参顺序 2012-06-29 jinxl
				//taskService.startTask(Long.valueOf(id),0,Long.valueOf(mainTaskID));
				taskService.startTask(Long.valueOf(id),Long.valueOf(mainTaskID),Long.valueOf(0));
			}
			
			
		}else if(level.equals("3")){
			logger.info("MNG: 子任务下workflowstart！");	
			String id = request.getParameter("id"); 
			String subtaskid = request.getParameter("subtaskID");
			String maintaskid = request.getParameter("mainTaskID");
			logger.info(id+"==="+subtaskid+"=="+maintaskid);
			//修改子任务下workflow启动传参值和传参顺序  2012-06-29 jinxl
			//taskService.startTask(Long.valueOf(id), Long.valueOf(subtaskid),Long.valueOf(maintaskid));
			taskService.startTask(Long.valueOf(id), Long.valueOf(maintaskid),Long.valueOf(subtaskid));
		}
//				
//		// 返回的视图名称,/WEB-INF/view/task/index.jsp
//		List<LogList> list = infaCommonService.findObjects(
//				"log.findAllLog", null);
//		logger.info("查询出的条数:"+list.size());
		return JasonUtil.getJsonResult("true");

	}
	
	@RequestMapping(value = "/mainList")
	@ResponseBody
	public String mainList(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {
		
		logger.info("MNG: mainList！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		String idtask = request.getParameter("ID");
		List list = infaCommonService.findObjects(
				"log.findtaskdetailbyid", idtask);
		logger.info("查询出主任务的条数:"+list.size());
		for (int i = 0; i < list.size(); i++) {
			HashMap mainTask =(HashMap)list.get(i);
	        String maintaskid =  mainTask.get("ID").toString();
	        if(mainTask.get("RUN_COUNT") != null){
	        	Long time =  Long.valueOf(mainTask.get("RUN_COUNT").toString().trim());
	        	mainTask.put("RUN_COUNT",TimeUtil.changetime(time));
	        }
	    	mainTask.put("ID", maintaskid);//设置treegrid唯一键
	    	mainTask.put("LEVEl", "1");
	        logger.info("主任务id:"+maintaskid);
//	        HashMap temp = new HashMap();
//	        temp.put("taskid",Long.valueOf(maintaskid));     
//	        HashMap mainTaskLog =(HashMap)infaCommonService.findObject("log.findMainTaskLog",temp);
//	        if(mainTaskLog!=null){
//	        	String run_count = (String)mainTaskLog.get("RUN_COUNT");
//	        	logger.info("runcount:"+run_count);
//	        	if(run_count!=null){
//		        	long time = Long.valueOf(run_count.trim());
//		        	if(time<0){
//		        		mainTask.put("END_TIME","");
//		        		mainTask.put("RUN_COUNT","");
//		        	}else{
//		        		mainTask.put("END_TIME", mainTaskLog.get("END_TIME"));
//		        		mainTask.put("RUN_COUNT",TimeUtil.changetime(time));
//		        	}
//	        	}
//	        	mainTask.put("LOGID", mainTaskLog.get("ID"));	      
//	        	mainTask.put("DETAIL", mainTaskLog.get("DETAIL"));
//	        	mainTask.put("START_TIME", mainTaskLog.get("START_TIME"));
//	        	mainTask.put("RUN_STATUS_CODE", mainTaskLog.get("RUN_STATUS_CODE"));
//	        }else{
//	        	mainTask.put("LOGID","0");
//	        }
	        //判断日志闭合
	        long count = infaCommonService.countObject("log.countMainTaskLog", idtask);
	        if(count>0){      	
	        	mainTask.put("STATE", "CLOSED");
			}else{
				mainTask.put("STATE", "OPEN");
//				mainTask.remove("children");
			}
	        
		}
		logger.info(JasonUtil.JasonTOString(list));
		return JasonUtil.JasonTOString(list);
	}
	
	@RequestMapping(value = "/taskdetailtree")
	@ResponseBody
	public String taskdetailtree(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("MNG: taskdetailtree！");
		String idtask = request.getParameter("ID");
		logger.info("~~~~~~~~~~~idtask=="+idtask);
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List list = infaCommonService.findObjects("log.findtaskdetailbyid",
				idtask);
		HashMap mapSecClildSubTask = new HashMap();
		HashMap mapSecClildSubTaskThree = new HashMap();
		HashMap mapSecClildWorkFlow = new HashMap();
		HashMap mapSecClildSession = new HashMap();
//		HashMap mapSession = new HashMap();
//		HashMap map = new HashMap();
		HashMap mapParentTemp = new HashMap();
		List paramSecList = new ArrayList();
		List paramSecListThree = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			mapSecClildSubTask = (HashMap) list.get(i);
			String id = mapSecClildSubTask.get("ID").toString();
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~id=="+id);
			List listclildSubTask = infaCommonService.findObjects(
					"log.findTaskClildSubTaskLog", idtask);
			List listclildSubWorkFlow = infaCommonService.findObjects(
					"log.findTaskClildWorkFlow", idtask);

//			listclildSubWorkFlow = GetNewArrayList(listclildSubWorkFlow);

			// ////////////////////////////////查询runid
			for (int k = 0; k < listclildSubTask.size(); k++) {
				mapSecClildWorkFlow = (HashMap) listclildSubTask.get(k);
				String workflowlogid = mapSecClildWorkFlow.get("ID").toString();
				LogRelation logrelation = new LogRelation();
				logrelation.setTASK_LOGID(idtask);
				logrelation.setWORKFLOW_ID(workflowlogid);
				mapSecClildWorkFlow.put("ID",idtask+workflowlogid);
				List runidlist = infaCommonService.findObjects(
						"log.findrunidbylogrelation", logrelation);
				if (runidlist.size() <= 0) {
					logger.info("没有session日志");
				} else if (runidlist.size() > 1) {
					logger.info("数据库数据有错误");
				} else {
					LogRelation lr = (LogRelation) runidlist.get(0);
					String runid = lr.getRUN_ID();
					logger.info("runid=" + runid);
				}
			}
			// ///////////////////////////////
			
			for(int k = 0;k <listclildSubWorkFlow.size();k++){
				mapSecClildSession = (HashMap) listclildSubWorkFlow.get(k);
				String workflow_id = mapSecClildSession.get("WORKFLOW_ID").toString();
				String workflow_run_id = mapSecClildSession.get("WORKFLOW_RUN_ID").toString();
				String workflowid = mapSecClildSession.get("ID").toString();
				mapSecClildSession.put("ID",idtask+workflowid);
				if(mapSecClildSession.get("RUN_COUNT") != null){
					Long time =  Long.valueOf(mapSecClildSession.get("RUN_COUNT").toString().trim());
					mapSecClildSession.put("RUN_COUNT",TimeUtil.changetime(time));
				}
				InfaSessLog session = new InfaSessLog();
				session.setWorkflowId(Long.valueOf(workflow_id));
				session.setWorkflowRunId(Long.valueOf(workflow_run_id));
				List listclildSubSession = infaCommonService.findObjects(
						"log.findWorkFlowClildSession", session);
				
				for(int s =0;s <listclildSubSession.size();s++){
					HashMap map = new HashMap();
					map = (HashMap) listclildSubSession.get(s);
					String sessionid = map.get("ID").toString();
					map.put("ID",idtask+workflowid+sessionid);
					if(map.get("RUN_COUNT") != null){
						Long time =  Long.valueOf(map.get("RUN_COUNT").toString().trim());
						map.put("RUN_COUNT",TimeUtil.changetime(time));
					}
				}
				if(listclildSubSession.size()>0){
					mapSecClildSession.put("children", listclildSubSession);
					mapSecClildSession.put("STATE", "CLOSED");
				}else {
					mapSecClildSession.remove("children");
				}
			}
			List listnew = new ArrayList();
			listnew.addAll(listclildSubTask);
			listnew.addAll(listclildSubWorkFlow);

			if(mapSecClildSubTask.get("RUN_COUNT") != null){
				Long time =  Long.valueOf(mapSecClildSubTask.get("RUN_COUNT").toString().trim());
				mapSecClildSubTask.put("RUN_COUNT",TimeUtil.changetime(time));
			}
			if (listnew.size() > 0) {
				mapSecClildSubTask.put("children", listnew);
				mapSecClildSubTask.put("STATE", "CLOSED");
			} else {
				mapSecClildSubTask.remove("children");
			}
			paramSecList.add(mapSecClildSubTask); // 二级List{Map，Map。。。。}
			if (listclildSubTask.size() > 0) {
				for (int j = 0; j < listclildSubTask.size(); j++) {
					mapSecClildSubTaskThree = (HashMap) listclildSubTask.get(j);
					String NewID = mapSecClildSubTaskThree.get("SUBTASKID")
							.toString();
					if(mapSecClildSubTaskThree.get("RUN_COUNT") != null){
						Long time =  Long.valueOf(mapSecClildSubTaskThree.get("RUN_COUNT").toString().trim());
						mapSecClildSubTaskThree.put("RUN_COUNT",TimeUtil.changetime(time));
					}
					List listclildSubWorkFlowThree = infaCommonService
							.findObjects("log.findSubTaskClildWorkFlow", NewID);
//					listclildSubWorkFlowThree = GetNewArrayList(listclildSubWorkFlowThree);
					if (listclildSubWorkFlowThree.size() > 0) {
						mapSecClildSubTaskThree.put("children",
								listclildSubWorkFlowThree);
						mapSecClildSubTaskThree.put("STATE", "CLOSED");
					}

					// ///////////////////////////
					String subtaskid = mapSecClildSubTaskThree.get("ID")
							.toString();
					for (int m = 0; m < listclildSubWorkFlowThree.size(); m++) {
						mapSecClildSession = (HashMap) listclildSubWorkFlowThree
								.get(m);
						String workflowid = mapSecClildSession.get("ID").toString();
						mapSecClildSession.put("ID", subtaskid+workflowid);
						String workflow_id = mapSecClildSession.get("WORKFLOW_ID").toString();
						String workflow_run_id = mapSecClildSession.get("WORKFLOW_RUN_ID").toString();
						InfaSessLog session = new InfaSessLog();
						session.setWorkflowId(Long.valueOf(workflow_id));
						session.setWorkflowRunId(Long.valueOf(workflow_run_id));
						List listclildSubSession = infaCommonService.findObjects(
								"log.findWorkFlowClildSession", session);
						if(mapSecClildSession.get("RUN_COUNT") != null){
							Long time =  Long.valueOf(mapSecClildSession.get("RUN_COUNT").toString().trim());
							mapSecClildSession.put("RUN_COUNT",TimeUtil.changetime(time));
						}
						for(int n=0;n <listclildSubSession.size();n++){
							HashMap map = new HashMap();
							map = (HashMap) listclildSubSession.get(n);
							String sessionid = map.get("ID").toString();
							map.put("ID", subtaskid+workflowid+sessionid);
							if(map.get("RUN_COUNT") != null){
								Long time =  Long.valueOf(map.get("RUN_COUNT").toString().trim());
								map.put("RUN_COUNT",TimeUtil.changetime(time));
							}
						}
						if(listclildSubSession.size()>0){
							mapSecClildSession.put("children", listclildSubSession);
							mapSecClildSession.put("STATE", "CLOSED");
						}else {
							mapSecClildSession.remove("children");
						}
//						LogRelation logrelation = new LogRelation();
//						logrelation.setTASK_LOGID(idtask);
//						logrelation.setSUB_TASKLOGID(subtaskid);
//						logrelation.setWORKFLOW_ID(workflowlogid);
//						logger.info("logrelation="+JasonUtil.JasonTOString(logrelation));
//						List runidlist = infaCommonService.findObjects(
//								"log.findrunidbylogrelation", logrelation);
//						logger.info("runidlist="+JasonUtil.JasonTOString(runidlist));
//						if (runidlist.size() <= 0) {
//							logger.info("没有session日志");
//						} else if (runidlist.size() > 1) {
//							logger.info("数据库数据有错误");
//						} else {
//							LogRelation lr = (LogRelation) runidlist.get(0);
//							String runid = lr.getRUN_ID();
//							logger.info("runid=" + runid);
//						}
					}
					
				}
			}
		}

		logger.info(JasonUtil.JasonTOString(paramSecList));
		return JasonUtil.JasonTOString(paramSecList);
	}

//	@RequestMapping(value = "/list")
//	@ResponseBody
//	public String list(HttpServletRequest request, Model model)
//			throws BusinessException, WorkFlowException, ParseException {
//
//		logger.info("MNG: list！");
//		// 返回的视图名称,/WEB-INF/view/task/index.jsp
//		List list = infaCommonService.findObjects("log.findAllLog", null);
//
//		HashMap mapSecClildSubTask = new HashMap();
//		HashMap mapSecClildSubTaskThree = new HashMap();
//		HashMap mapParentTemp = new HashMap();
//		List paramSecList = new ArrayList();
//		List paramSecListThree = new ArrayList();
//		for (int i = 0; i < list.size(); i++) {
//			mapSecClildSubTask = (HashMap) list.get(i);
//			String id = mapSecClildSubTask.get("ID").toString();
//			List listclildSubTask = infaCommonService.findObjects(
//					"log.findTaskClildSubTask", id);
//			List listclildSubWorkFlow = infaCommonService.findObjects(
//					"log.findTaskClildWorkFlow", id);
//			listclildSubWorkFlow = GetNewArrayList(listclildSubWorkFlow);
//
//			List listnew = new ArrayList();
//			listnew.addAll(listclildSubTask);
//			listnew.addAll(listclildSubWorkFlow);
//			if (listnew.size() > 0) {
//				mapSecClildSubTask.put("children", listnew);
//				mapSecClildSubTask.put("STATE", "CLOSED");
//			} else {
//				mapSecClildSubTask.remove("children");
//			}
//			paramSecList.add(mapSecClildSubTask); // 二级List{Map，Map。。。。}
//			if (listclildSubTask.size() > 0) {
//				for (int j = 0; j < listclildSubTask.size(); j++) {
//					mapSecClildSubTaskThree = (HashMap) listclildSubTask.get(j);
//					String NewID = mapSecClildSubTaskThree.get("SUBTASKID")
//							.toString();
//					List listclildSubWorkFlowThree = infaCommonService
//							.findObjects("log.findTaskClildWorkFlow", NewID);
//					listclildSubWorkFlowThree = GetNewArrayList(listclildSubWorkFlowThree);
//					if (listclildSubWorkFlowThree.size() > 0) {
//						mapSecClildSubTaskThree.put("children",
//								listclildSubWorkFlowThree);
//						mapSecClildSubTaskThree.put("STATE", "CLOSED");
//					}
//				}
//			}
//		}
//		logger.info(jasonutil.JasonTOString(paramSecList));
//		return jasonutil.JasonTOString(paramSecList);
//	}

	@RequestMapping(value = "/detailedloglist")
	@ResponseBody
	public String detailedloglist(HttpServletRequest request, Model model)
			throws BusinessException {

		logger.info("MNG: DetailedLogList！");
		String id = request.getParameter("ID");
		logger.info("MNG:ID=" + id);
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		// rows = infaCommonService.findObjects("log.findDetailedLogListByid",
		// id,page,limit);

		List<LogList> list = infaCommonService.findObjects(
				"log.findDetailedLogListByid", id);
		Long total = infaCommonService.countObject(
				"log.findDetailedLogListByidcount", id);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);
		logger.info(JasonUtil.JasonTOString(pu.getList()));
		return JasonUtil.JasonTOString(pu.getList());
		// return "";
	}

//	@RequestMapping(value = "/workflowstart")
//	@ResponseBody
//	public String workflowstart(HttpServletRequest request, Model model)
//			throws JLMException {
//
//		String workflowid = request.getParameter("WORKFLOWID");
//
//		IJLMWorkflow wk;
//		try {
//			wk = WorkFlowOperateUtil.getWorkFlow(host, port, server, username,
//					userpwd, "infa2", "pc2infa2wk");
//			WorkFlowOperateUtil.startWorkFlow(wk);
//		} catch (WorkFlowException e) {
//			// TODO Auto-generated catch block
//			return e.getMessage();
//		}
//
//		return "";
//	}
//
//	@RequestMapping(value = "/workflowpause")
//	@ResponseBody
//	public String workflowpause(HttpServletRequest request, Model model)
//			throws JLMException {
//
//		String workflowid = request.getParameter("WORKFLOWID");
//		IJLMWorkflow wk;
//		try {
//			wk = WorkFlowOperateUtil.getWorkFlow(host, port, server, username,
//					userpwd, "infa2", "pc2infa2wk");
//			WorkFlowOperateUtil.abortWorkFlow(wk);
//		} catch (WorkFlowException e) {
//			// TODO Auto-generated catch block
//			return e.getMessage();
//		}
//
//		return "";
//	}
//
//	@RequestMapping(value = "/workflowstop")
//	@ResponseBody
//	public String workflowstop(HttpServletRequest request, Model model)
//			throws JLMException {
//
//		String workflowid = request.getParameter("WORKFLOWID");
//		IJLMWorkflow wk;
//		try {
//			wk = WorkFlowOperateUtil.getWorkFlow(host, port, server, username,
//					userpwd, "infa2", "pc2infa2wk");
//			WorkFlowOperateUtil.stopWorkFlow(wk);
//		} catch (WorkFlowException e) {
//			// TODO Auto-generated catch block
//			return e.getMessage();
//		}
//
//		return "";
//	}
//
//	/*
//	 * 动态获取工作流的状态，开始时间，运行时间
//	 */
//	public List GetNewArrayList(List listOld) throws BusinessException,
//			WorkFlowException, ParseException {
//
//		HashMap mapSec = new HashMap();
//		List listNew = listOld;
//		for (int i = 0; i < listNew.size(); i++) {
//			mapSec = (HashMap) listNew.get(i);
//			String workflowid = mapSec.get("ID").toString();
//			List<WorkFlow> list = infaCommonService.findObjects(
//					"log.findWorkFlow", workflowid);
//
//			if (list.size() == 1) {
//				WorkFlow workflow = list.get(0);
//				String subject_AREA = workflow.getSUBJECT_AREA();
//				String workflowName = workflow.getWORKFLOW_NAME();
//				logger.info("~~~~~~~~~subject_AREA=="+subject_AREA+"~~~~~workflowName=="+workflowName);
//				IJLMWorkflow wk = WorkFlowOperateUtil.getWorkFlow(host, port,
//						server, username, userpwd, subject_AREA, workflowName);
//				String workFlowStatus = WorkFlowOperateUtil
//						.getWorkFlowStatus(wk);
//				String startTime = WorkFlowOperateUtil.getWorkFlowStartTime(wk);
//				String endTime = WorkFlowOperateUtil.getWorkFlowEndTime(wk);
//
//				SimpleDateFormat format = new SimpleDateFormat(
//						"yyyy/mm/dd hh:mm:ss");
//				Calendar cEndTime = Calendar.getInstance();
//				cEndTime.setTime(format.parse(endTime));
//				Calendar cStartTime = Calendar.getInstance();
//				cStartTime.setTime(format.parse(startTime));
//
//				int totalSeconds = (int) ((cEndTime.getTimeInMillis() - cStartTime
//						.getTimeInMillis()) / 1000);
//				mapSec.put("RUN_STATUS_CODE", workFlowStatus);
//				mapSec.put("START_TIME", startTime);
//				mapSec.put("END_TIME", startTime);
//				mapSec.put("RUN_COUNT", String.valueOf(totalSeconds));
//				mapSec.put("DETAIL", "workflow详情");
//			}
//		}
//		return listNew;
//	}

	@RequestMapping(value = "/slectTaskByCondition")
	@ResponseBody
	public String slectTaskByCondition(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		String txtTaskName = request.getParameter("TXTTASKNAME");
		String selectTaskState = request.getParameter("SELECTTASKSTATE");
		String selectTaskType = request.getParameter("SELECTTASKTYPE");
		String txtStartTime = request.getParameter("TXTSTARTTIME");
		String txtStopTime = request.getParameter("TXTSTOPTIME");

		logger.info("MNG: slectTaskByCondition！");
		logger.info("txtTaskName=" + txtTaskName);
		logger.info("selectTaskState=" + selectTaskState);
		logger.info("selectTaskType=" + selectTaskType);
		logger.info("txtStartTime=" + txtStartTime);
		logger.info("txtStopTime=" + txtStopTime);
		return "";
	}

	@RequestMapping(value = "/slectTaskLogByCondition")
	@ResponseBody
	public String slectTaskLogByCondition(HttpServletRequest request,
			Model model) throws BusinessException, WorkFlowException, Exception {

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   
		
		String txtTaskName = request.getParameter("TXTTASKNAME");
		String selectTaskState = request.getParameter("SELECTTASKSTATE");
		String txtStartTime = request.getParameter("TXTSTARTTIME");
		
		String txtStopTime = request.getParameter("TXTSTOPTIME");
		
		logger.info("~~~~~txtTaskName=="+txtTaskName);
		logger.info("~~~~~selectTaskState=="+selectTaskState);
		logger.info("~~~~~txtStartTime=="+txtStartTime);
		logger.info("~~~~~txtStopTime=="+txtStopTime);
//	    Date StartTime= new Date(sdf.parse(txtStartTime).getTime());
//	    Date StopTime= new Date(sdf.parse(txtStopTime).getTime()); 
//	    logger.info("~~~~~StartTime=="+StartTime);
//	    logger.info("~~~~~StopTime=="+StopTime);
		LogList loglist = new LogList();
		loglist.setName(txtTaskName);

		if (selectTaskState != "" && selectTaskState != null) {
			loglist.setRun_status_code(selectTaskState);
		}
		if (txtStartTime != "" && txtStartTime != null) {
			loglist.setStart(txtStartTime);
		}
		if (txtStopTime != "" && txtStopTime != null) {
			loglist.setEnd(txtStopTime);
		}
		List<LogList> list = infaCommonService.findObjects(
				"log.findtaskloglisttotalbycondition", loglist);
		logger.info("MNG: slectTaskLogByCondition" + list);
		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), list.size());
		logger.info(JasonUtil.JasonTOString(pu.getList()));
		return JasonUtil.JasonTOString(pu.getList());

	}
	
	@RequestMapping(value = "/mainLogList")
	@ResponseBody
	public String mainLogList(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {

		logger.info("MNG: mainLogList！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List list = infaCommonService.findObjects(
				"log.findMainTask", null);
		logger.info("查询出主任务的条数:"+list.size());
		for (int i = 0; i < list.size(); i++) {
			HashMap mainTask =(HashMap)list.get(i);
	        String maintaskid =  mainTask.get("TASK_ID").toString();
	    	mainTask.put("ID", maintaskid);//设置treegrid唯一键
	    	mainTask.put("LEVEl", "1");
	        logger.info("主任务id:"+maintaskid);
	        HashMap temp = new HashMap();
	        temp.put("taskid",Long.valueOf(maintaskid));     
	        HashMap mainTaskLog =(HashMap)infaCommonService.findObject("log.findMainTaskLog",temp);
	        if(mainTaskLog!=null){
	        	String run_count = (String)mainTaskLog.get("RUN_COUNT");
	        	logger.info("runcount:"+run_count);
	        	if(run_count!=null){
		        	long time = Long.valueOf(run_count.trim());
		        	if(time<0){
		        		mainTask.put("END_TIME","");
		        		mainTask.put("RUN_COUNT","");
		        	}else{
		        		mainTask.put("END_TIME", mainTaskLog.get("END_TIME"));
		        		mainTask.put("RUN_COUNT",TimeUtil.changetime(time));
		        	}
	        	}
	        	mainTask.put("LOGID", mainTaskLog.get("ID"));	      
	        	mainTask.put("DETAIL", mainTaskLog.get("DETAIL"));
	        	mainTask.put("START_TIME", mainTaskLog.get("START_TIME"));
	        	mainTask.put("RUN_STATUS_CODE", mainTaskLog.get("RUN_STATUS_CODE"));
	        }else{
	        	mainTask.put("LOGID","0");
	        }
	        //判断日志闭合
	        long count = infaCommonService.countObject("log.countMainTask", temp);
	        if(count>0){      	
	        	mainTask.put("STATE", "CLOSED");
			}else{
				mainTask.put("STATE", "OPEN");
			}
	        
		}
		logger.info(JasonUtil.JasonTOString(list));
		return JasonUtil.JasonTOString(list);
	}
	
	
	@RequestMapping(value = "/detailLogList")
	@ResponseBody
	public String detailLogList(HttpServletRequest request, Model model)
			throws BusinessException, WorkFlowException, ParseException {
		logger.info("MNG: detailLogList！");
		String level = request.getParameter("levelid");
		String taskid = request.getParameter("task_id");
		String logid = request.getParameter("logid");
		logger.info(level+"======"+taskid+"======="+logid);
		//
		if(level.equals("1")){
			if(!logid.equals("0")){
				HashMap temp = new HashMap();
				temp.put("taskid", Long.valueOf(taskid));
				List sublist = infaCommonService.findObjects(//查子任务的条数
						"log.find_DetailList", temp);
				logger.info("子任务的条数："+sublist.size());
				for(int i = 0; i < sublist.size(); i++) {
					HashMap detail =(HashMap)sublist.get(i);
					logger.info("detail(hashmap):"+detail);
					String subtaskid = detail.get("SUBTASKID").toString();
					logger.info("subtaskid:"+subtaskid);
					detail.put("ID",taskid+subtaskid);//taskname已加
					detail.put("LEVEl", "2");
					detail.put("TASK_ID",taskid);
					detail.put("WORKFLOWLOGID", "0");
					detail.put("WORKFLOWID", "0");
					temp=new HashMap();
					temp.put("subtaskid", Long.valueOf(subtaskid));
					temp.put("logid", Long.valueOf(logid));
					
					List list =infaCommonService.findObjects(//
							"log.findDetailList", temp);
				    logger.info("查询出workflowLog："+list);
				    HashMap sublog = null;
				    if(list.size()!=0){
				    	sublog = (HashMap)list.get(0);
				    }				
					
					logger.info("查询子任务日志："+sublog);
                    if(sublog!=null){
                    	String run_count = (String)sublog.get("RUN_COUNT");
        	        	logger.info("runcount:"+run_count);
        	        	logger.info("endtime:"+sublog.get("END_TIME"));
        	        	if(run_count!=null){
        		        	long time = Long.valueOf(run_count.trim());
        		        	if(time<0){
        		        		detail.put("END_TIME","");
        		        		detail.put("RUN_COUNT","");
        		        	}else{
        		        		detail.put("END_TIME", sublog.get("END_TIME"));
        		        		detail.put("RUN_COUNT",TimeUtil.changetime(time));
        		        	}
        	        	}
                    	detail.put("LOGID", logid);
                    	detail.put("SUBLOGID", sublog.get("ID"));
                    	detail.put("START_TIME", sublog.get("START_TIME"));
                    	detail.put("DETAIL", sublog.get("DETAIL"));
                    	
                    	detail.put("RUN_STATUS_CODE", sublog.get("RUN_STATUS_CODE"));
                    }else{
                    	detail.put("LOGID","0" );
                    	detail.put("SUBLOGID", "0");

                    }
                    temp=new HashMap();
                    temp.put("taskid",Long.valueOf(subtaskid));
                    logger.info("判断闭合子任务下ID:"+subtaskid);
                    long count = infaCommonService.countObject("log.countMainTask", temp);
                    logger.info(detail.get("TASKNAME").toString());
                    logger.info("子任务下的workflow:"+count);
                    if(count>0){      	
                    	detail.put("STATE", "CLOSED");
        			}else{
        				logger.info(detail.get("TASKNAME").toString());
        				detail.put("STATE", "OPEN");
        			}
                    
				}
				
				temp = new HashMap();
				temp.put("taskid", Long.valueOf(taskid));
				List worklist = infaCommonService.findObjects(//查workflow
						"log.findWorkflowList", temp);
				logger.info("workflow的条数："+worklist.size()+"==="+worklist);
				for (int i = 0; i < worklist.size(); i++) {
					HashMap workflow =(HashMap)worklist.get(i);
					logger.info("workflow的内容："+workflow);
					String workflowid = workflow.get("WORKFLOWID").toString();
					workflow.put("ID",taskid+workflowid);
					workflow.put("LEVEl", "2");
					workflow.put("TASK_ID",taskid);
					
					temp=new HashMap();
				
					temp.put("workflowid", Long.valueOf(workflowid));
					temp.put("logid", Long.valueOf(logid));
					temp.put("sublogid", null);
					String runid = (String)infaCommonService.findObject("log.findrunid", temp);
					logger.info("runid=======:"+runid);
					if(runid==null||runid.equals("-1")){
						workflow.put("WORKFLOWLOGID", "0");
						workflow.put("SUBLOGID","0");
						workflow.put("SUBTASKID","0");
						workflow.put("LOGID", "0");
						if(runid!=null){
						workflow.put("RUN_STATUS_CODE", "7");
						}
					}else{								
				    logger.info("查workflowlog的条件："+temp);
				    List list =infaCommonService.findObjects(
							"log.findWorkLogList", temp);
				    logger.info("查询出workflowLog："+list);
				    HashMap worklog = null;
				    if(list.size()!=0){
				    	worklog = (HashMap)list.get(0);
				    }					
					if(worklog!=null){					
						String run_count = (String)worklog.get("RUN_COUNT");
        	        	logger.info("runcount:"+run_count);
        	         	logger.info("endtime:"+worklog.get("END_TIME"));
        	        	if(run_count!=null){
        		        	long time = Long.valueOf(run_count.trim());
        		        	if(time<0){
        		        		workflow.put("END_TIME","");
        		        		workflow.put("RUN_COUNT","");
        		        	}else{
        		        		workflow.put("END_TIME", worklog.get("END_TIME"));
        		        		workflow.put("RUN_COUNT",TimeUtil.changetime(time));
        		        	}
        	        	}										
						workflow.put("WORKFLOWLOGID", worklog.get("ID"));
						workflow.put("LOGID", logid);
						workflow.put("SUBLOGID", "0");
						workflow.put("SUBTASKID", "0");
						workflow.put("DETAIL", worklog.get("DETAIL"));
						workflow.put("START_TIME", worklog.get("START_TIME"));
						workflow.put("RUN_STATUS_CODE", worklog.get("RUN_STATUS_CODE"));
					}else{
						workflow.put("WORKFLOWLOGID", "0");
						workflow.put("SUBLOGID", "0");
						workflow.put("SUBTASKID", "0");
						workflow.put("LOGID", "0");
					}
					}
					
					sublist.add(workflow);
					
				 }
				
				return JasonUtil.JasonTOString(sublist);
			}else{
				//主任务没有log的情况
				logger.info("主任务没有log的情况");
				HashMap temp = new HashMap();
				temp.put("taskid", Long.valueOf(taskid));
				List sublist = infaCommonService.findObjects(//查子任务
						"log.find_DetailList", temp);
				for(int i = 0; i < sublist.size(); i++) {
					HashMap detail =(HashMap)sublist.get(i);
					logger.info("detail(hashmap):"+detail);
					String subtaskid = detail.get("SUBTASKID").toString();
					logger.info("subtaskid:"+subtaskid);
					detail.put("ID",taskid+subtaskid);
					detail.put("TASK_ID",taskid);
					detail.put("LEVEl", "2");
					detail.put("LOGID","0" );
					detail.put("WORKFLOWID", "0");
					detail.put("WORKFLOWLOGID", "0");
					detail.put("SUBLOGID", "0");
					logger.info("执行查询子任务下workflow的操作count！");
					HashMap tempSublog = new HashMap();//增加subtaskid查询条件 2012-06-29 jinxl
					tempSublog.put("taskid", subtaskid);//增加subtaskid查询条件 2012-06-29 jinxl
					logger.info("查询条件subtaskid："+tempSublog);//增加subtaskid查询条件  2012-06-29 jinxl
					long count = infaCommonService.countObject("log.countMainTask", tempSublog);//增加subtaskid查询条件 2012-06-29 jinxl
	                logger.info(detail.get("TASKNAME")+"的workflow:"+count);
	                 if(count>0){      	
	                 	detail.put("STATE", "CLOSED");
	     			}else{
	     				detail.put("STATE", "OPEN");
	     			}
				}
				List worklist = infaCommonService.findObjects(//查workflow
						"log.findWorkflowList", temp);
				for (int i = 0; i < worklist.size(); i++) {
					HashMap workflow =(HashMap)worklist.get(i);
					logger.info("workflow的内容："+workflow);
					String workflowid = workflow.get("WORKFLOWID").toString();
					workflow.put("ID",taskid+workflowid);
					workflow.put("LOGID","0" );
					workflow.put("SUBLOGID", "0");
					workflow.put("SUBLOGID", "0");
					workflow.put("WORKFLOWLOGID", "0");
					workflow.put("LEVEl", "2");
					workflow.put("SUBTASKID","0" );
					workflow.put("TASK_ID",taskid);
					sublist.add(workflow);
				}
				return JasonUtil.JasonTOString(sublist);
			}	
			
		}else if(level.equals("2")){
			String subtaskid = request.getParameter("subtaskid");
			String sublogid = request.getParameter("sublogid");
			logger.info("subtaskid："+ subtaskid+"sublogid："+sublogid);
			if(!logid.equals("0")){//主任务log不为空
				if(!sublogid.equals("0")){//子任务log不为空
					    logger.info("主子任务LOG不为空");
					    HashMap search = new HashMap();
					    search.put("taskid", subtaskid);
			          	List subworklist = infaCommonService.findObjects("log.findSubWorkflowList", search);//找子任务下的workflow
			          	logger.info("子任务下的workflow数:"+subworklist.size());
			          	for (int i = 0; i < subworklist.size(); i++) {
			          	     
			          		HashMap subworkflow = (HashMap)subworklist.get(i);
			          		String workflowid  = subworkflow.get("WORKFLOWID").toString();
			          		subworkflow.put("ID", taskid+subtaskid+subworkflow.get("WORKFLOWID"));
			          		subworkflow.put("LEVEl", "3");
			          		subworkflow.put("TASK_ID", taskid);
							subworkflow.put("SUBLOGID",sublogid);
							logger.info("子任务下workflow日志："+subworkflow);
			          		search = new HashMap();
			          		search.put("logid", logid);
			          		search.put("sublogid", sublogid);
			          		search.put("workflowid", workflowid);
			          		
			          		String runid = (String)infaCommonService.findObject("log.findrunid", search);
			        		logger.info("子任务下workflow日志查询条件："+search);
							logger.info("runid=======:"+runid);
			          		if(runid==null||runid.equals("-1")){
								subworkflow.put("WORKFLOWLOGID","0");
								subworkflow.put("LOGID","0");
			          			subworkflow.put("SUBLOGID","0");
			          			if(runid!=null){
			          			subworkflow.put("RUN_STATUS_CODE","7");}
			          		}else{	
			          			List list = infaCommonService.findObjects("log.findSubWorkflowLog", search);
							    HashMap subworkflowlog = null;
							    if(list.size()!=0){
							    	subworkflowlog = (HashMap)list.get(0);
							    }						         
			          		if(subworkflowlog!=null){			          			
			          			String run_count = (String)subworkflowlog.get("RUN_COUNT");
		        	        	logger.info("运作时间:"+run_count);
		        	        	logger.info("END_TIME:"+ subworkflowlog.get("END_TIME"));
		        	        	if(run_count!=null){
		        		        	long time = Long.valueOf(run_count.trim());
		        		        	if(time<0){
		        		        		subworkflow.put("END_TIME","");
		        		        		subworkflow.put("RUN_COUNT","");
		        		        	}else{
		        		        		subworkflow.put("END_TIME", subworkflowlog.get("END_TIME"));
		        		        		subworkflow.put("RUN_COUNT",TimeUtil.changetime(time));
		        		        	}
		        	        	}									
			          			subworkflow.put("WORKFLOWLOGID", subworkflowlog.get("ID"));
			          			subworkflow.put("LOGID", logid);
			          			subworkflow.put("SUBLOGID",sublogid);
			          			subworkflow.put("START_TIME", subworkflowlog.get("START_TIME"));			   
			          			subworkflow.put("DETAIL", subworkflowlog.get("DETAIL"));
			          			subworkflow.put("RUN_STATUS_CODE", subworkflowlog.get("RUN_STATUS_CODE"));
							}else{
								subworkflow.put("WORKFLOWLOGID","0");
								subworkflow.put("LOGID","0");
			          			subworkflow.put("SUBLOGID","0");
							}
			          	}
						} 
			          	logger.info("======"+JasonUtil.JasonTOString(subworklist));
			          	return JasonUtil.JasonTOString(subworklist);
				}else{
					   
				}
				
			}else{
				 logger.info("主子任务LOG为空");
				    HashMap search = new HashMap();
				    search.put("taskid", subtaskid);
		          	List subworklist = infaCommonService.findObjects("log.findSubWorkflowList", search);//找子任务下的workflow
		          	for (int i = 0; i < subworklist.size(); i++) {
		          		HashMap subworkflow = (HashMap)subworklist.get(i);
		          		logger.info("子任务下workflow信息"+subworkflow);
		          		subworkflow.put("ID", taskid+subtaskid+subworkflow.get("WORKFLOWID"));		          		
		          		subworkflow.put("TASK_ID", taskid);
		          		subworkflow.put("LEVEl", "3");
						subworkflow.put("SUBTASKID", subtaskid);
						subworkflow.put("WORKFLOWID", subworkflow.get("WORKFLOWID"));
		          		subworkflow.put("LOGID","0");
						subworkflow.put("SUBLOGID","0");
						subworkflow.put("WORKFLOWLOGID","0");						
					}
		          	return JasonUtil.JasonTOString(subworklist);
			}						
		}
	
	return null;
		
	}
	
	
}
