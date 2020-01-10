package com.mulani.zkcuratordemo

import org.apache.curator.RetryPolicy
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.recipes.leader.LeaderLatchListener
import org.apache.curator.retry.ExponentialBackoffRetry
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PreDestroy

@SpringBootApplication
class ZkCuratorDemoApplication(var isPrimary:Boolean = false) : CommandLineRunner {
	private val primaryPath: String = "/com/mulani/instance_primary"
	private val retryPolicy: RetryPolicy = ExponentialBackoffRetry(1000, 3) as RetryPolicy
	private val zkConnectionString: String = System.getenv("ZK_HOST_PORT")

	private val newClient : CuratorFramework = CuratorFrameworkFactory.newClient(zkConnectionString, retryPolicy)
	private val leaderLatch = LeaderLatch(newClient, primaryPath)

	override fun run(vararg args: String?) {
		println(zkConnectionString)

		newClient.start()
		newClient.zookeeperClient.blockUntilConnectedOrTimedOut()

		var maxCount = 10_000
		println("hello world")
		if (args.isNotEmpty()) maxCount = Integer.parseInt(args[0])

		leaderLatch.addListener(PrimaryLatchListener(this))
		leaderLatch.start()

		Runnable {
			var counter = 0
			while (counter++ < maxCount) {
				println("is primary = $isPrimary, hasLeadership from latch ${leaderLatch.hasLeadership()}")
				Thread.sleep(1000)
			}
		}.run()

	}

	@PreDestroy
	public fun destroy() : Unit {
		println("closing connections")
		leaderLatch.close()
		newClient.close()
	}
}


fun main(args: Array<String>) {
	runApplication<ZkCuratorDemoApplication>(*args)
	readLine()
}
