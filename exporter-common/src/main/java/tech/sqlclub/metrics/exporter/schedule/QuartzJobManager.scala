package tech.sqlclub.metrics.exporter.schedule

import java.util.{Date, Properties}
import java.util

import org.quartz._
import org.quartz.impl.{JobDetailImpl, StdSchedulerFactory}
import tech.sqlclub.common.context.YamlContext
import tech.sqlclub.common.log.Logging
import tech.sqlclub.common.utils.ParamMapUtils
import tech.sqlclub.metrics.exporter.common.Constants._
import scala.collection.JavaConversions._

object QuartzJobManager extends Logging {

  lazy val quartzConf = YamlContext.getAnyRefMap(YAML_QUARTZ)

  lazy val scheduler =
  if (quartzConf != null && quartzConf.nonEmpty) {
    val props = new Properties
    quartzConf.map(kv => props.setProperty(kv._1, kv._2.toString))
    new StdSchedulerFactory(props).getScheduler
  } else new StdSchedulerFactory().getScheduler

  def scheduleJob(group: String, jobClass: Class[_ <: Job], jobName: String, cronExpress: String,
                  startTime: Date, dataMap:Map[String, AnyRef]): Unit = {
    val jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, group).build.asInstanceOf[JobDetailImpl]

    if (dataMap != null && dataMap.nonEmpty) {
      val jobDataMap = new JobDataMap(dataMap)
      jobDetail.setJobDataMap(jobDataMap)
    }

    val triggerName = jobName + "_trigger"
    val triggerTriggerBuilder = TriggerBuilder.newTrigger.withIdentity(triggerName, group)
      .withSchedule(CronScheduleBuilder.cronSchedule(cronExpress))

    if (startTime != null && startTime.getTime >= System.currentTimeMillis)
      triggerTriggerBuilder.startAt(startTime)

    val trigger = triggerTriggerBuilder.build
    try {
      val triggers = new util.TreeSet[Trigger]
      triggers.add(trigger)
      scheduler.scheduleJob(jobDetail, triggers, true)
    } catch {
      case e: ObjectAlreadyExistsException =>
        logError(e.getMessage, e)
      case e: Exception =>
        logError(e.getMessage, e)
    }
    scheduler.start()
  }


  def scheduleJob(quartzTaskMeta: QuartzTaskMeta):Unit = {
    if (quartzTaskMeta !=null && quartzTaskMeta.jobClass.nonEmpty)
      scheduleJob(
        quartzTaskMeta.jobGroup,
        getJobClass(quartzTaskMeta.jobClass),
        quartzTaskMeta.jobName,
        quartzTaskMeta.cronExpression,
        quartzTaskMeta.schBeginTime,
        quartzTaskMeta.jobDataMap
      )
  }

  def scheduleJob(quartzTaskMetaList:List[QuartzTaskMeta]):Unit = quartzTaskMetaList.map(scheduleJob _)

  def getJobClass(className:String)= Class.forName(className).asSubclass(classOf[Job])

  def loadSysJobs = {
    logInfo("QuartzJobManager load sysJobs start......")
    YamlContext.getListMap(YAML_QUARTZ_JOB).map{
      jobMap =>
        val paramMap = new ParamMapUtils(jobMap)
        QuartzTaskMeta(
          paramMap.getStringValue(YAML_QUARTZ_JOB_NAME),
          paramMap.getStringValue(YAML_QUARTZ_JOB_CLASS),
          paramMap.getStringValue(YAML_QUARTZ_JOB_CRON),
          paramMap.getAnyRefMap(YAML_QUARTZ_JOB_PARAM),
          paramMap.getStringValue(YAML_QUARTZ_JOB_GROUP, YAML_QUARTZ_JOB_GROUP_DEFAULT)
        )
    }.map(scheduleJob _ )
    logInfo("QuartzJobManager load sysJobs over!")
  }

}
