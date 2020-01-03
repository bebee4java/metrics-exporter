package tech.sqlclub.metrics.exporter.node

import tech.sqlclub.metrics.exporter.Metrics

/**
  *
  * Created by songgr on 2020/01/02.
  */
case class NodeMetrics(node:Node, cpu:Cpu, mem:Mem, swap: Swap) extends Metrics {

}

case class Node(
      osName:String, //操作系统名称
      osVersion:String, //操作系统版本
      os:String, //操作系统
      ip:String, //ip地址
      Broadcast:String, //网关广播地址
      macAddress:String, //网卡MAC地址
      hostName:String //主机名
)

case class Cpu(
      cores:Int, //cpu核数
      sysUsedPercent:Double, //cpu系统利用率
      userUsedPercent:Double, //cpu用户利用率
      usedPercent:Double, //cpu总利用率
      idlePercent:Double, // cpu当前空闲率
      waitPercent:Double //cpu当前等待率
)

case class Mem(
      total:Double, //内存总量
      free:Double, //内存剩余量
      used:Double, //内存使用量
      freePercent:Double, //内存空闲率
      usedPercent:Double //内存使用率
)

case class Swap(
      total:Double, //交换区总量
      free:Double, //交换区剩余量
      used:Double, //交换区使用量
      freePercent:Double, //交换区空闲率
      usedPercent:Double //交换区使用率
)
