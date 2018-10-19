package com.iboxpay.elastic.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

public class MyElasticJobListener implements ElasticJobListener {

  @Override
  public void beforeJobExecuted(ShardingContexts shardingContexts) {
    System.err.println(shardingContexts.getJobName() + " is ready to execute.");
  }

  @Override
  public void afterJobExecuted(ShardingContexts shardingContexts) {
    System.err.println(shardingContexts.getJobName() + " has been executed .");
  }

}
