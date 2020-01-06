package tech.sqlclub.metrics.exporter.schedule

import org.quartz.{Job, JobExecutionContext}
import tech.sqlclub.common.log.Logging
import tech.sqlclub.common.utils.JacksonUtils
import tech.sqlclub.metrics.exporter.Metrics

/**
  *
  * Created by songgr on 2020/01/03.
  */
trait TimingJob extends Job with Logging {
  override def execute(jobExecutionContext: JobExecutionContext): Unit = {

    val metrics = this.asInstanceOf[{ def collect() : Metrics }].collect()

    val json = JacksonUtils.toJson[Metrics](metrics)

    logInfo("metrics: " + json)

  }
}
