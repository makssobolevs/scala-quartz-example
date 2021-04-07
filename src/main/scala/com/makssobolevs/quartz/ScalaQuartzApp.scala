package com.makssobolevs.quartz

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.makssobolevs.quartz.endpoints.JobRoutes
import com.makssobolevs.quartz.jobs.ExampleJob1
import com.typesafe.config.ConfigFactory
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.SimpleScheduleBuilder
import org.quartz.TriggerBuilder
import org.quartz.impl.StdSchedulerFactory

import java.util.Properties
import scala.util.Failure
import scala.util.Success

/**
 *
 * @author makssobolevs
 */
object ScalaQuartzApp {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load().getConfig("quartz")

    val properties = new Properties()
    config.entrySet().forEach(entry => properties.setProperty(entry.getKey, config.getString(entry.getKey)))

    val schedulerFactory = new StdSchedulerFactory(properties)
    val scheduler = schedulerFactory.getScheduler

    // alternative to xml configuration
    scheduleJob1(scheduler)

    scheduler.start()



    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val routes = new JobRoutes(scheduler)(context.system)
      startHttpServer(routes.jobRoutes)(context.system)

      Behaviors.empty
    }
    ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
  }


  def scheduleJob1(scheduler: Scheduler): Unit = {

    val job1 = JobBuilder.newJob(classOf[ExampleJob1])
      .withIdentity("myJob", "group1")
      .usingJobData("jobSays", "Hello World!")
      .build()

    val trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startNow()
      .withSchedule(
        SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
          .repeatForever()
      )
      .build()

    scheduler.scheduleJob(job1, trigger)
  }


  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {

    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }


}
