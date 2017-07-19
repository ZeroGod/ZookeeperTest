package com.zero;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * @author zhouxiong
 * @date 2017年1月21日
 */
public class ZooSessionTest {
	static ZooKeeper zk = null;

	public static void main(String[] args) {

		try {
			zk = new ZooKeeper("localhost:2181", 5000, new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					System.out.println(event.getState());
					if (event.getState() == KeeperState.SyncConnected) {
						System.out.println("连接");
						show();
					} else if (event.getState() == KeeperState.Expired) {
						System.out.println("[SUC-CORE] session expired. now rebuilding");
					}
				}
			});
		} catch (IOException e) {
			System.out.println("错误");
			// e.printStackTrace();
		}
		while (true) {
			if (zk != null) {
				System.out.println("zookeeper己经连接上了" + zk.getSessionId());
				try {
					Thread.sleep(12000);
				} catch (InterruptedException e) {
					System.out.println("错误");
				}
			}

		}
	}

	public static void show() {
		if (zk != null)
			System.out.println("" + zk.getSessionId());
		else
			System.out.println("错误,连接断开了。");
	}

}
