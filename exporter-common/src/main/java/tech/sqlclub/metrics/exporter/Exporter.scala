package tech.sqlclub.metrics.exporter

trait Exporter {

  def collect() : Metrics

}
