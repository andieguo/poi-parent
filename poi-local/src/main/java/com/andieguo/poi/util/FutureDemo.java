package com.andieguo.poi.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *  Future类中重要方法包括get()和cancel()。
　　get()获取数据对象，如果数据没有加载，就会阻塞直到取到数据，而 cancel()是取消数据加载。
　　另外一个get(timeout)操作，表示如果在timeout时间内没有取到就失败返回，而不再塞。
 * @author andieguo
 *
 */
public class FutureDemo {

	public static void main(String[] args) {
		FutureDemo demo = new FutureDemo();
		demo.excuteJob();
	}
	
	public class QueryThread implements Runnable{
		Exchanger<Integer> exchanger;
		
		public QueryThread(Exchanger<Integer> exchanger){
			this.exchanger = exchanger;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println(Thread.currentThread().getName()+"正在执行");
				exchanger.exchange(1);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 与主线程交换信息
		}
		
	}
	public class MyCallable implements Callable<String>{// 使用Callable接口作为构造参
		
		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
			/**====================耗时操作开始==================================**/
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Exchanger<Integer> exchanger = new Exchanger<Integer>();//子线程与主线程交换数据
			for (int i = 0; i < 10; i++) {
				executor.execute(new QueryThread(exchanger));
			}
			Integer totalResult = Integer.valueOf(0);
			for(int i=0;i<10;i++){
				 //当主线程调用Exchange对象的exchange()方法后，他会陷入阻塞状态
				 //直到queryRunnable线程也调用了exchange()方法，然后以线程安全的方式交换数据，之后主线程继续运行
				Integer partialResult = exchanger.exchange(Integer.valueOf(0));
				if (partialResult != 0) {
					totalResult = totalResult + partialResult;
					//更新进度条
					System.out.println(String.format("Progress: %s/%s",totalResult, 10));
				}
			}
			executor.shutdown();//关闭线程池
			/**====================耗时代码块结束==================================**/
			return "线程执行完成";
		}
	}
	//将FutureTask放入线程池
	public void excuteJob() {
		//可以将FutureTask放入线程池，也可以放入单个线程执行
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask<String>(new MyCallable());
		executor.execute(futureTask);
		// 在这里可以做别的任何事情
		System.out.println("我在做其他的事.........................");
		try {
			// 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
			String result = futureTask.get(9000, TimeUnit.MILLISECONDS);
			System.out.println("返回结果"+result);
		} catch (InterruptedException e) {
			futureTask.cancel(true);
		} catch (ExecutionException e) {
			futureTask.cancel(true);
		} catch (TimeoutException e) {
			System.out.println("超时.........................");
			futureTask.cancel(true);
			System.exit(0);//强制退出
		} finally {
			executor.shutdown();
		}
	}
	
	//将FutureTask放入单个线程执行
	public void excuteJobThread() {
		//可以将FutureTask放入线程池，也可以放入单个线程执行
		FutureTask<String> futureTask = new FutureTask<String>(new MyCallable());
		new Thread(futureTask).start();
		// 在这里可以做别的任何事情
		System.out.println("我在做其他的事.........................");
		try {
			// 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
			String result = futureTask.get(9000, TimeUnit.MILLISECONDS);
			System.out.println("返回结果"+result);
		} catch (InterruptedException e) {
			futureTask.cancel(true);
		} catch (ExecutionException e) {
			futureTask.cancel(true);
		} catch (TimeoutException e) {
			System.out.println("超时.........................");
			futureTask.cancel(true);
			System.exit(0);//强制退出
		} 
	}
}
