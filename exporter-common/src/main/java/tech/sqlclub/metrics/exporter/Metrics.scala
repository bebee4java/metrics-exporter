package tech.sqlclub.metrics.exporter

import tech.sqlclub.common.utils.TimeUtils

/**
  *
  * Created by songgr on 2020/01/02.
  */
class Metrics {
  lazy val time = TimeUtils.currentLocalDate
  val collect_time:String = TimeUtils.dateFormat(time)
  def timestamp = time.getTime
}
