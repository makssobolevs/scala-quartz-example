package com.makssobolevs.quartz

import akka.actor.typed.ActorSystem
import com.makssobolevs.quartz.jobs.ExampleJob2
import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.simpl.SimpleJobFactory
import org.quartz.spi.TriggerFiredBundle

/**
 *
 * @author makssobolevs
 */
class CustomJobFactory(actorSystem: ActorSystem[_]) extends SimpleJobFactory {

  override def newJob(bundle: TriggerFiredBundle, Scheduler: Scheduler): Job = {
    val job = super.newJob(bundle, Scheduler)
    job match {
      case exampleJob2: ExampleJob2 =>
        exampleJob2.setActorSystem(actorSystem)
      case _ =>
    }
    job
  }

}
