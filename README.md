## Metrics Exporter
> Collect and push various metrics for sqlalarm

Metrics Exporter is for various metrics such as node metrics, JVM metrics, etc, written in scala with pluggable metric collectors.

### Extended Support
You can customize the implementation of various metrics-exporter, only need to implement the trait [Exporter](exporter-common/src/main/java/tech/sqlclub/metrics/exporter/Exporter.scala):
```scala
trait Exporter extends TimingJob {

  def collect() : Metrics

}
```

### Scheduling Support
The system will automatically schedule your exporter based on your configuration, for example:

```java
quartz-job:
  - name:  NodeExporter
    class: tech.sqlclub.metrics.exporter.node.NodeExporter
    cron: 0/10 * * * * ?
  ...
```

The above example will create a scheduled job which collecting machine metrics every 10s.

### About
This is an active open-source project. We are always open to people who want to use the system or contribute to it.
Welcome everyone to pay attention and commit contributions.