package cn.com.liandisys.infa.web.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.infa.entity.sys.DataBase;
import cn.com.liandisys.infa.entity.sys.Informatica;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.service.task.TaskService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;
import cn.com.liandisys.infa.util.workflow.WorkFlowException;
import cn.com.liandisys.infa.util.workflow.WorkFlowOperateUtil;

/**
 * 系统管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/sys")
public class InformaticaController {

	private static Logger logger = LoggerFactory
			.getLogger(InformaticaController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	@Autowired
	private TaskService taskService;
	private List<HashMap> rows = null;
	private boolean success = false;
	/**
	 * 任务List
	 */
	private List<Informatica> InformaticaList = null;

	/**
	 * 调试检索LIST。
	 * 
	 */
	public List<Informatica> getInformaticaList() {
		return InformaticaList;
	}

	private int type = 0;
	private int UPDATE = 1;
	private int ADD = 2;
	/**
	 * 开始页码数
	 */
	private int page = 0;

	/**
	 * 每页表示件数
	 */
	private int limit = 10;

	public void setPage(int page) {
		this.page = page;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 总件数
	 */
	private long total = 0;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("验证成功，跳转到SYS页面！");
		return "/sys/informaticaList";
	}

	/**
	 * 初始化一览数据
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("ELT: list！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		// row当前显示多少条
		logger.debug("Informatica的rows==" + request.getParameter("rows"));
		// page当前第几页，从1开始
		logger.debug("Informatica的page==" + request.getParameter("page"));
		InformaticaList = infaCommonService.findObjects(
				"informatica.findAllInformatica", null);
		total = infaCommonService.countObject("informatica.InformaticaCount",
				null);

		PagingUtil pu = new PagingUtil(InformaticaList,
				Integer.parseInt(request.getParameter("rows")),
				Integer.parseInt(request.getParameter("page")), total);

		return JasonUtil.JasonTOString(pu.getList());
	}
	/**
	 * 查询可以关联的数据库
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/finddb")
	@ResponseBody
	public String finddb(HttpServletRequest request, Model model)
			throws BusinessException {
		DataBase db = (DataBase) infaCommonService.findObject(
				"db.findAlldbuse_Informatica", null);
		if(null == db){
			return JasonUtil.JasonTOString(false);
		}
		logger.debug(JasonUtil.JasonTOString(db));
		return JasonUtil.JasonTOString(db);
	}

	/**
	 * 查询重名
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
		logger.debug("Informatica: findbyname！");
		Informatica informatica1 = new Informatica();
		String id = request.getParameter("id");
		logger.debug("informaticaid====================:" + id);
		if (null != id) {
			logger.debug("informaticaid========================:" + id);
			informatica1.setId(Long.valueOf(id));
			Informatica rinformatica = (Informatica) infaCommonService
					.findObject("informatica.findByid", informatica1);
			logger.debug("rinformatica.getServer_name()========================:" + rinformatica.getServer_name());
			if (rinformatica.getServer_name().equals(
					request.getParameter("server_name"))) {
				return "true";
			}
		}
		Informatica informatica = new Informatica();
		String name = request.getParameter("server_name");
		informatica.setServer_name(name);
		long i = (Long) infaCommonService.findObject("informatica.findByName",
				informatica);
		if (i == 0) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 根据id查找记录
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/findbyid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("Informatica: findbyid！");
		Informatica informatica = new Informatica();
		String id = request.getParameter("id");
		logger.debug("informaticaid:" + id);
		informatica.setId(Long.valueOf(id));
		Informatica rinformatica = (Informatica) infaCommonService.findObject(
				"informatica.findByid", informatica);
		logger.debug(rinformatica.getServer_name());
		logger.debug(JasonUtil.JasonTOString(rinformatica));
		return JasonUtil.JasonTOString(rinformatica);
	}

	/**
	 * 增加
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public String add(HttpServletRequest request, Model model)
			throws BusinessException {
		type = ADD;
		logger.debug("add Informatic数据！");
		infaCommonService.addObject("informatica.insertInformatica",
				getInformaticas(request));
		// addInformatica = setInfo(informatica)
		return JasonUtil.getJsonResult(true);
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
		type = UPDATE;
		logger.debug("update Informatic数据！");
		logger.debug(getInformaticas(request).getServer_name());
		infaCommonService.modifyObject("informatica.updateInformatica",
				getInformaticas(request));
		// addInformatica = setInfo(informatica)
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 删除（多行）
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public String delete(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("Informatic: delete！");
		InformaticaList = new ArrayList<Informatica>();
		String selectIdStr = request.getParameter("ids");
		logger.debug("informaticaid:" + selectIdStr);
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			Informatica informatica = new Informatica();
			logger.debug("informaticaid:" + selectedIdArr[i]);
			informatica.setId(Long.valueOf(selectedIdArr[i]));
			InformaticaList.add(informatica);
		}
		infaCommonService.batchRemoveObject("informatica.deleteInformatica",
				InformaticaList);
		return JasonUtil.getJsonResult(true);
	}

	/**
	 * 测试连接
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/testConnection")
	@ResponseBody
	public String testConnection(HttpServletRequest request, Model model)
			throws BusinessException {
		String flag = getStatus(request);
//		logger.info(flag + ":::::flag:::::");
		if (flag.equals("connected")) {
			return JasonUtil.getJsonResult("connected");
		} else if (flag.equals("-109") || flag.equals("-204")|| flag.equals("-117")) {
			return JasonUtil.getJsonResult("failed");
		} else if (flag.equals("-1")) {
			return JasonUtil.getJsonResult("wait");
		}
		return "";
	}

	public String getStatus(HttpServletRequest request) {
		String flag = "";
		DataBase db = new DataBase();
		db.setId(Long.valueOf(request.getParameter("knowledge_base")));
		try {
			DataBase rdb = (DataBase) infaCommonService.findObject("db.findByid", db);
			flag = WorkFlowOperateUtil.getConnectStatus(
					request.getParameter("ip"), request.getParameter("port"),
					rdb.getDbname(),
					request.getParameter("user_name"),
					request.getParameter("password"));			
		} catch (WorkFlowException e) {
			logger.info("e.getErrCode() =   "+e.getErrCode()+e.getMessage());
			return String.valueOf(e.getErrCode());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 查询可连接的DB
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/db_id")
	@ResponseBody
	public String find_infomatic_id(HttpServletRequest request, Model model, HttpServletResponse response)
			throws BusinessException {
		logger.debug("Informaticas: db_id！");
		
		
//		CodeMasterDetail detail = new CodeMasterDetail();
//		detail.setCodetype(request.getParameter("codetype"));
//		logger.info("codetype===" + request.getParameter("codetype"));
		List<DataBase> datalist = infaCommonService.findObjects(
				"db.findDB", null);
		int rows = datalist.size();
		logger.debug("dictentry  Select  查到" + rows + "记录。");
		String json = JSONArray.fromObject(datalist).toString();
		logger.debug("json=" + json);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		rows = infaCommonService.findObjects("db.findDB", null);
//		success = true;
//		logger.info("DataBase: find_infomatic_id！" + rows.toString());
//		return JasonUtil.JasonTOString(rows);
		//
		return null;
	}

	/**
	 * 查询当前的DB
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/find_DB")
	@ResponseBody
	public String find_infomatic(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("DataBase: find_infomatic");

		DataBase db = new DataBase();
		String id = request.getParameter("id");
		logger.debug("DataBase:find_infomatic：" + id);
		if (id == null || id.equals("")) {
			return JasonUtil.JasonTOString(false);
		}
		db.setId(Long.valueOf(id));
		DataBase rdb = (DataBase) infaCommonService.findObject("db.findByid",
				db);
		logger.debug("DataBase:find_infomatic：" + JasonUtil.JasonTOString(rdb));
//		if(null == rdb){
//			return JasonUtil.JasonTOString(false);
//		}
		return JasonUtil.JasonTOString(rdb);
		//
	}

	// Informatica赋值
	private Informatica getInformaticas(HttpServletRequest request) {
		Informatica infor = new Informatica();
		if (type == UPDATE) {
			infor.setId(Long.valueOf(request.getParameter("id")));
		}
		infor.setServer_name(request.getParameter("server_name")); // 服务器名称
		infor.setIp(request.getParameter("ip"));// 服务器IP
		infor.setPort(request.getParameter("port"));// 服务器端口
		infor.setDomain(request.getParameter("domain"));// Domain
		infor.setKnowledge_base(request.getParameter("knowledge_base"));// 知识库
		infor.setIntegration_server(request.getParameter("integration_server"));// 集成服务器
		infor.setUser_name(request.getParameter("user_name"));// 用户名
		infor.setPassword(request.getParameter("password"));// 密码
		infor.setWrlogpath(request.getParameter("wrlogpath"));
		infor.setSesslogpath(request.getParameter("sesslogpath"));
		return infor;
	}

}
