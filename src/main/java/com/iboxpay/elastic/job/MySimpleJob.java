package com.iboxpay.elastic.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class MySimpleJob implements SimpleJob {

  @Override
  public void execute(ShardingContext shardingContext) {
    switch (shardingContext.getShardingParameter()) {
      case "first":
        System.err.println("分片参数为first的机器处理前50条数据");
        break;
      case "second":
        System.err.println("分片参数为second的机器处理后50条数据");
        break;
    }
  }

  public static void main(String[] args) {
    String shardingItemParams = "0=first,1=second";
    // 作业核心配置，主要配置作业名称，cron表达式和分片数
    JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder("mySimpleJob", "0/5 * * * * ?", 2)
        .shardingItemParameters(shardingItemParams).build();
    // 简单作业配置
    SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(jobCoreConfig, MySimpleJob.class.getName());
    // Lite作业配置
    LiteJobConfiguration liteJobConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
    // 注册中心配置
    CoordinatorRegistryCenter registryCenter =
        new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "mySimpleJob"));
    registryCenter.init();

    JobScheduler jobScheduler = new JobScheduler(registryCenter, liteJobConfig);
    jobScheduler.init();
  }

}
