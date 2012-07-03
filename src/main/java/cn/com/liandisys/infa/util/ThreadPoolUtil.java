package cn.com.liandisys.infa.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {
	public ExecutorService pool;

	/**
	 * 构造器
	 * @param int type
	 * 				0并行；1串行
	 */
	public ThreadPoolUtil(int type) {
		if(type == 0){
			pool = Executors.newFixedThreadPool(20);			
		}
	}

	public void setThread(Thread t) {
		pool.execute(t);
	}
	
	public void destroyThread(){
		pool.shutdownNow();
	}
}
