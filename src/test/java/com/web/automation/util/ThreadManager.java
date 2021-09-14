package com.web.automation.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
	
	public static void executeParallel(Runnable... args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		for (Runnable arg : args) {
			executor.execute(arg);
		}
		executor.shutdown();
	}

}
