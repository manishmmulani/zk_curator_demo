package com.mulani.zkcuratordemo

import org.apache.curator.framework.recipes.leader.LeaderLatchListener

class PrimaryLatchListener(private val app: ZkCuratorDemoApplication) : LeaderLatchListener {

    override fun notLeader() {
        println("notLeader called")
        app.isPrimary = false
    }

    override fun isLeader() {
        println("isLeader called")
        app.isPrimary = true
    }
}