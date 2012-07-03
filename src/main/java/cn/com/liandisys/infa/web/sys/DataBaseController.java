package cn.com.liandisys.infa.web.sys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import cn.com.liandisys.infa.entity.sys.DataBase;
import cn.com.liandisys.infa.entity.sys.Informatica;
import cn.com.liandisys.infa.service.InfaCommonService;
import cn.com.liandisys.infa.util.JasonUtil;
import cn.com.liandisys.infa.util.PagingUtil;

/**
 * 系统管理模块ACTION，
 * 
 * 真正任务的POST请求由Filter完成
 * 
 */
@Controller
@RequestMapping(value = "/db")
public class DataBaseController {

	private static Logger logger = LoggerFactory
			.getLogger(DataBaseController.class);

	@Autowired
	private InfaCommonService infaCommonService;
	/**
	 * 任务List
	 */
	private List<DataBase> DBlist = null;
	private List<HashMap> rows = null;
	private int type = 0;
	private int UPDATE = 1;
	private int ADD = 2;
	private boolean success = false;
	private long total;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("验证成功，跳转到SYS页面！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		return "/sys/DataBaseList";
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
		logger.debug("DataBase: list！");
		// 返回的视图名称,/WEB-INF/view/task/index.jsp
		List<DataBase> list = infaCommonService.findObjects(
				"db.findAllDataBase", null);
		total = infaCommonService.countObject("db.DataBaseCount", null);

		PagingUtil pu = new PagingUtil(list, Integer.parseInt(request
				.getParameter("rows")), Integer.parseInt(request
				.getParameter("page")), total);

		logger.info(JasonUtil.JasonTOString(list));
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
	@RequestMapping(value = "/findbyid")
	@ResponseBody
	public String findbyid(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("DataBase: findbyid！");
		DataBase db = new DataBase();
		String id = request.getParameter("id");
		logger.info("DataBase:" + id);
		db.setId(Long.valueOf(id));
		DataBase rdb = (DataBase) infaCommonService.findObject("db.findByid",
				db);
		logger.info(rdb.getDbname());
		logger.info(JasonUtil.JasonTOString(rdb));
		return JasonUtil.JasonTOString(rdb);
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
		DataBase db1 = new DataBase();
		String id = request.getParameter("id");
		if (null != id) {
			logger.info("DataBase:" + id);
			db1.setId(Long.valueOf(id));
			DataBase rdb1 = (DataBase) infaCommonService.findObject(
					"db.findByid", db1);
			if (rdb1.getTitle().equals(request.getParameter("title"))) {
				return "true";
			}
		}
		DataBase db = new DataBase();
		String name = request.getParameter("title");
		db.setTitle(name);
		long i = (Long) infaCommonService.findObject("db.findByDBName", db);
		if (i == 0) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 查询可连接的infomatic
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/infomatic_id")
	@ResponseBody
	public String find_infomatic_id(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.debug("DataBase: find_infomatic_id！");

		rows = infaCommonService.findObjects("informatica.findInfomatic", null);
		HashMap map = new HashMap();
		map.put("VALUE", "");
		map.put("NAME", "请选择");
		rows.add(0, map);

		success = true;
		logger.debug("DataBase: find_infomatic_id！" + rows.toString());
		return JasonUtil.JasonTOString(rows);
		//
	}

	/**
	 * 查询当前的infomatic
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/find_infomatic")
	@ResponseBody
	public String find_infomatic(HttpServletRequest request, Model model)
			throws BusinessException {
		logger.info("DataBase: find_infomatic");

		Informatica info = new Informatica();
		String id = request.getParameter("id");
		logger.debug("DataBase:find_infomatic：" + id);
		if (id == null || id.equals("")) {
			return JasonUtil.JasonTOString(false);
		}
		info.setId(Long.valueOf(id));
		Informatica rdb = (Informatica) infaCommonService.findObject(
				"informatica.findByid", info);
		logger.debug("DataBase:find_infomatic：" + JasonUtil.JasonTOString(rdb));
		return JasonUtil.JasonTOString(rdb);
		//
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
		logger.debug("add DataBase数据！");
		infaCommonService.addObject("db.insertDataBase",
				getInformaticas(request));
		// addInformatica = setInfo(informatica)
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
		type = ADD;
		Connection conn = null;
		logger.debug("testConnection DataBase数据！");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(request.getParameter("dbtype") + ":@");
			sb.append(request.getParameter("ip") + ":");
			sb.append(request.getParameter("port") + ":");
			sb.append(request.getParameter("server_name"));
			conn = (Connection) DriverManager.getConnection(sb.toString(),
					request.getParameter("username"),
					request.getParameter("password"));
			// Connection conn = (Connection)
			// DriverManager.getConnection("jdbc:oracle:thin:@LDNS-DT-1758:1521:orcl","inf04","inf04");
			if (!conn.isClosed()) {
				return JasonUtil.getJsonResult(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return JasonUtil.getJsonResult(false);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// addInformatica = setInfo(informatica)
		return JasonUtil.getJsonResult(false);
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
		logger.debug("update DataBase数据！");
		logger.debug(getInformaticas(request).getDbname());
		infaCommonService.modifyObject("db.updateDataBase",
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
		logger.debug("DataBase: delete！");
		DBlist = new ArrayList<DataBase>();
		String selectIdStr = request.getParameter("ids");
		logger.debug("DataBase:" + selectIdStr);
		String[] selectedIdArr = selectIdStr.split(",");
		for (int i = 0; i < selectedIdArr.length; i++) {
			DataBase db = new DataBase();
			logger.info("DataBase:" + selectedIdArr[i]);
			db.setId(Long.valueOf(selectedIdArr[i]));
			DBlist.add(db);
		}
		infaCommonService.batchRemoveObject("db.deleteDataBase", DBlist);
		return JasonUtil.getJsonResult(true);
	}

	// DataBase赋值
	private DataBase getInformaticas(HttpServletRequest request) {
		DataBase db = new DataBase();
		if (type == UPDATE) {
			db.setId(Long.valueOf(request.getParameter("id")));
		}
		db.setTitle(request.getParameter("title"));
		db.setDbname(request.getParameter("dbname"));
		db.setDbtype(request.getParameter("dbtype"));
		db.setUsername(request.getParameter("username"));
		db.setPassword(request.getParameter("password"));
		db.setPort(request.getParameter("port"));
		db.setIp(request.getParameter("ip"));
		if (null == request.getParameter("dbuse")
				|| request.getParameter("dbuse").equals("")) {
			db.setDbuse("0");
		} else {
			db.setDbuse(request.getParameter("dbuse"));
		}
		db.setServer_name(request.getParameter("server_name"));
		logger.debug("当前的dbuse()" + request.getParameter("dbuse"));
		return db;
	}

}
