package tech.sqlclub.metrics.exporter.node

import tech.sqlclub.metrics.exporter.core.ExporterLaunch

object Launch extends ExporterLaunch {

  def main(args: Array[String]): Unit = {
    // 调度系统所有的job
    run
  }

}

