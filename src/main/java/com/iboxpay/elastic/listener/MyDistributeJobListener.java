package com.iboxpay.elastic.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;

public class MyDistributeJobListener extends AbstractDistributeOnceElasticJobListener {

  public MyDistributeJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
    super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
  }

  @Override
  public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
    System.err.println("最后一个作业:" + shardingContexts.getJobName() + "开始执行....");
  }

  @Override
  public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
    System.err.println("最后一个作业:" + shardingContexts.getJobName() + "执行完成....");
  }

}
