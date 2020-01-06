package tech.sqlclub.metrics.exporter

import tech.sqlclub.metrics.exporter.schedule.TimingJob

trait Exporter extends TimingJob {

  def collect() : Metrics

}
