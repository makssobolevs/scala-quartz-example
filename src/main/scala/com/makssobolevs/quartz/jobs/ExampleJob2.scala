package com.makssobolevs.quartz.jobs

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 *
 * @author makssobolevs
 */
@DisallowConcurrentExecution
class ExampleJob2 extends Job {
  override def execute(context: JobExecutionContext): Unit = {
    val toSay = context.getMergedJobDataMap.getString("testKey")

    println(s"Job 2 is performing, data: $toSay")
    Thread.sleep(10000)
  }
}
