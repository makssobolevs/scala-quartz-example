package com.makssobolevs.quartz.endpoints

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.pathEnd
import akka.http.scaladsl.server.Directives.post
import akka.http.scaladsl.server.Route
import org.quartz.JobDataMap
import org.quartz.JobKey
import org.quartz.Scheduler
import spray.json.DefaultJsonProtocol

import scala.jdk.CollectionConverters._
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
 *
 * @author makssobolevs
 */
class JobRoutes (val scheduler: Scheduler)(implicit val system: ActorSystem[_]) {

  import DefaultJsonProtocol._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  case class JobResponse(success: Boolean, runningJobs: Set[String])

  implicit val jobResponseFormat = jsonFormat2(JobResponse)

  val jobRoutes: Route =
    path("job") {
      pathEnd {
        post {
          complete ({
            val runningJobs = scheduler
              .getCurrentlyExecutingJobs.asScala
              .map(context => context.getJobDetail.getKey.getName)
              .toSet // could be always checked

            Try (
              scheduler.triggerJob(
                new JobKey("example-job-2"),
                new JobDataMap(Map("testKey" -> "MANUAL RUN!").asJava)
              )
            ) match {
              case Success(_) =>
                JobResponse(success = true, runningJobs)
              case Failure(e) =>
                e.printStackTrace()
                JobResponse(success = false, runningJobs)
            }
          })
        }
      }
    }
}
