package com.iboxpay.elastic.job;

import java.util.ArrayList;
import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class MyDataFlowJob implements DataflowJob<String> {

  private static boolean flag = true;

  private static boolean flag2 = true;

  @Override
  public List<String> fetchData(ShardingContext shardingContext) {
    List<String> data = new ArrayList<>();

    if (!flag && !flag2) {
      // 返回值为空或者为空集合时不会继续处理数据
      return data;
    }

    switch (shardingContext.getShardingItem()) {
      case 0:
        System.err.println("分片项为0的机器抓取前50条数据!");
        data.add("nick");
        data.add("melinda");
        break;
      case 1:
        System.err.println("分片项为1的机器抓取后50条数据!");
        data.add("tom");
        data.add("tony");
        break;
    }

    return data;

  }

  @Override
  public void processData(ShardingContext shardingContext, List<String> data) {
    switch (shardingContext.getShardingItem()) {
      case 0:
        System.err.println("分片项为0的机器处理前50条数据->" + data);
        flag = false;
        break;
      case 1:
        System.err.println("分片项为1的机器处理后50条数据->" + data);
        flag2 = false;
        break;
    }
  }

  public static void main(String[] args) {
    // 作业核心配置
    JobCoreConfiguration jobCoreConfig = JobCoreConfiguration.newBuilder("dataflowJob", "0/10 * * * * ?", 2).build();
    // 数据流作业配置，开启流式处理，流式处理开启后，fetchData的返回值为空时或者集合长度为空时，不会调用processData方法继续进行处理，否则会持续抓取数据、处理数据
    DataflowJobConfiguration dataFlowJobConfig =
        new DataflowJobConfiguration(jobCoreConfig, MyDataFlowJob.class.getName(), true);
    // lite job配置
    LiteJobConfiguration liteJobConfig = LiteJobConfiguration.newBuilder(dataFlowJobConfig).build();
    // 注册中心配置
    CoordinatorRegistryCenter registryCenter =
        new ZookeeperRegistryCenter(new ZookeeperConfiguration("localhost:2181", "dataflowJob"));
    registryCenter.init();
    // 作业调度器
    JobScheduler jobScheduler = new JobScheduler(registryCenter, liteJobConfig);
    jobScheduler.init();
  }
}
