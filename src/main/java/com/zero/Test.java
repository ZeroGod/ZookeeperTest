package com.zero;

import java.io.IOException;

/**
 *
 * @author zhouxiong
 * @date 2017年1月22日
 */
public class Test {

	public static void main(String[] args) throws InterruptedException {
		ConnectionWatcher wathcher = new ConnectionWatcher(false);
		String hosts = "192.168.11.118:2181,192.168.11.118:2182,192.168.11.118:2183";
		try {
			wathcher.connect(hosts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("出现错误");
		}
		Thread.sleep(Long.MAX_VALUE);  

	}
}
