package com.makssobolevs.quartz.jobs

import akka.actor.typed.ActorSystem
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 *
 * @author makssobolevs
 */
@DisallowConcurrentExecution
class ExampleJob2 extends Job {

  // use ask pattern to deal with actor system
  private var actorSystem: Option[ActorSystem[_]] = None

  override def execute(context: JobExecutionContext): Unit = {
    val toSay = context.getMergedJobDataMap.getString("testKey")

    println(s"Job 2 is performing, data: $toSay")
    Thread.sleep(10000)
  }

  def setActorSystem(actorSystem: ActorSystem[_]): Unit = this.actorSystem = Option(actorSystem)
}
