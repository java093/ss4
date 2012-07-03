package cn.com.liandisys.infa.service.plan;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.com.liandisys.idev.ssi.exception.BusinessException;
import cn.com.liandisys.idev.ssi.service.impl.BaseServiceIbatisImpl;
import cn.com.liandisys.infa.entity.job.QuartzPlan;
import cn.com.liandisys.infa.util.QuartzTriggersUtil;
import cn.com.liandisys.infa.web.plan.PlanController;
/**
 * PLAN用Service
 * @author zhangr2
 * @param <T>
 */
@Service("planService")
public class PlanService<T> extends BaseServiceIbatisImpl<T>{
	private static Logger logger = LoggerFactory.getLogger(PlanController.class);	
	
	/**
	 * 调用QuartzTriggersUtil 创建quartz触发器
	 * @author zhangr2
	 * @param HttpServletRequest 
	 *                      request
	 * @param String 
	 *           taskID 
	 * @param String 
	 *           planID 
	 * @param String  
	 *           type  操作类型   （0:编辑，1:删除，2:新增）
	 */
	public void addJob(HttpServletRequest request,String taskID,String planID,String type) throws BusinessException {
		try {
			QuartzPlan qp = new QuartzPlan();
			qp.setTaskid(taskID);
			List<QuartzPlan> list = (List<QuartzPlan>) findObjects("plan.findBytaskid", qp);
			logger.info("list size ;" + list.size());
			
				new QuartzTriggersUtil(request,taskID,planID,list,type);	
			

			logger.info("job创建完成");
		} catch (BusinessException e) {
			e.printStackTrace();
			logger.info("job创建失败 : " + e);
			throw e;
		}
	}
}
