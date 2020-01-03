package tech.sqlclub.metrics.exporter.schedule

import org.quartz.{Job, JobExecutionContext}

/**
  *
  * Created by songgr on 2020/01/03.
  */
abstract class TimingJob extends Job {
  override def execute(jobExecutionContext: JobExecutionContext): Unit = ???
}
