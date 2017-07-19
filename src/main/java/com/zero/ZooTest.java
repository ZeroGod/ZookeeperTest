package com.zero;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * @author zhouxiong
 * @date 2017年1月20日
 */
public class ZooTest {
	private static CountDownLatch connectedSignal = new CountDownLatch(1);
	// 10 秒会话时间 ，避免频繁的session expired
	private static final int SESSION_TIMEOUT = 10000;

	// 30秒
	private static final int CONNECT_TIMEOUT = 30000;

	public static void main(String[] args) throws InterruptedException, KeeperException {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper("localhost:2181", SESSION_TIMEOUT, new Watcher() {
				// 监控所有被触发的事件
				public void process(WatchedEvent event) {
					if (event.getState() == KeeperState.SyncConnected) {
						System.out.println("事件！");
						System.out.println("22221212");
						connectedSignal.countDown();

					}
					if (event.getState().equals(KeeperState.Disconnected)) {
						System.out.println("断开！");
					}
					if (event.getState().equals(KeeperState.Expired)) {
						System.out.println("又连接上了！");
					}
					try {
						System.out.println("已经触发了" + event.getType() + "事件！");
					} catch (Exception e) {
						System.out.println(e.toString());

					}
				}
			});
			// 连接有超时哦

			connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

			System.out.println("11111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		}
		System.out.println(zk.getSessionId());

		// 创建一个目录节点
		// zk.create("/testRootPath", "testRootData".getBytes(),
		// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(new String(zk.getData("/testRootPath", false, null)));
		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", false));
		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "modifyChildDataOne".getBytes(), -1);
		System.out.println("目录节点状态：[" + zk.exists("/testRootPath", false) + "]");
		System.out.println("内容" + new String(zk.getData("/testRootPath/testChildPathOne", false, null)));
		// 创建另外一个子目录节点
		zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("内容" + new String(zk.getData("/testRootPath/testChildPathTwo", false, null)));
		// 删除子目录节点
		zk.delete("/testRootPath/testChildPathTwo", -1);
		zk.delete("/testRootPath/testChildPathOne", -1);
		// 删除父目录节点
		// zk.delete("/testRootPath", -1);
		// 关闭连接
		zk.close();

	}

}
