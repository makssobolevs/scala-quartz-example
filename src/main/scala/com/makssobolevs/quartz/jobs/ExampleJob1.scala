package com.makssobolevs.quartz.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 *
 * @author makssobolevs
 */
class ExampleJob1 extends Job {
  override def execute(context: JobExecutionContext): Unit = {
    val toSay = context.getMergedJobDataMap.getString("jobSays")

    println(s"Job 1 is performing, data: $toSay")
  }
}
