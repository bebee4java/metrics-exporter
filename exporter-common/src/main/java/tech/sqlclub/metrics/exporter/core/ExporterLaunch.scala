package tech.sqlclub.metrics.exporter.core

import tech.sqlclub.metrics.exporter.schedule.QuartzJobManager

trait ExporterLaunch {

  // 加载系统所有的调度job
  def run = QuartzJobManager.loadSysJobs

}
