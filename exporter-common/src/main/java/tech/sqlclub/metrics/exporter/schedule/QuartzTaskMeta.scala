package tech.sqlclub.metrics.exporter.schedule

import java.util.Date

import tech.sqlclub.metrics.exporter.common.Constants

/**
  *
  * Created by songgr on 2020/01/06.
  */
case class QuartzTaskMeta (
    jobName: String,                                           // 作业名称
    jobClass:String,                                           // 作业实例class
    cronExpression:String,                                     // 作业调度计划表达式
    jobDataMap:Map[String,AnyRef],                             // 作业属性map
    jobGroup:String = Constants.YAML_QUARTZ_JOB_GROUP_DEFAULT, // 作业归属组名称
    schBeginTime:Date = new Date()                             // 作业调度开始时间
)


