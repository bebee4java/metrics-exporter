package tech.sqlclub.metrics.exporter.node

import org.hyperic.sigar.{OperatingSystem, Sigar}
import tech.sqlclub.common.context.YamlContext
import tech.sqlclub.metrics.exporter.{Exporter, Metrics}

/**
  * 采集机器的(CPU/MEM/NET etc.)各类指标
  * Created by songgr on 2020/01/02.
  */

class NodeExporter extends Exporter {
 private lazy val sigar = addDllReturnSigar

 private var os:OperatingSystem = _

 def addDllReturnSigar = {
  val dllpath = this.getClass.getClassLoader.getResource("sigarlib").getPath
  var librarypath = System.getProperty("java.library.path")
  if (System.getProperty("os.name").toLowerCase().contains("windows"))
   librarypath += ";" + dllpath else librarypath += ":" + dllpath
  System.setProperty("java.library.path", librarypath)

  os = OperatingSystem.getInstance()
  new Sigar
 }

 val KB = (value:Long) => value/1024d
 val MB = (value:Long) => value/1048576d
 val GB = (value:Long) => value/1073741824d
 lazy val unit = YamlContext.getStringValue(Constants.memUnit, "GB")
 lazy val sizeUnit = {
  unit match {
   case "GB" => GB
   case "MB" => MB
   case "KB" => KB
  }
 }

 def convert(value: Long, unit:Long=>Double):Double = unit(value)

 def PERCENT(dividend:Long, divisor:Long) = if (divisor == 0 ) 0d else dividend*100d / divisor

  override def collect(): Metrics = {

   val netInfo = sigar.getNetInterfaceConfig
   val node = Node(os.getVendorName, os.getVersion, os.getArch, netInfo.getAddress,
    netInfo.getBroadcast, netInfo.getHwaddr, sigar.getNetInfo.getHostName)

   val cpuInfo = sigar.getCpuPerc
   val cpu = Cpu(sigar.getCpuList.length, cpuInfo.getSys * 100, cpuInfo.getUser * 100, cpuInfo.getCombined * 100,
    cpuInfo.getIdle * 100, cpuInfo.getWait * 100)


   val memInfo = sigar.getMem
   val mem = Mem(convert(memInfo.getTotal, sizeUnit),convert(memInfo.getFree, sizeUnit),
    convert(memInfo.getUsed, sizeUnit), memInfo.getFreePercent, memInfo.getUsedPercent)

   val swapInfo = sigar.getSwap
   val swap = Swap(convert(swapInfo.getTotal, sizeUnit),convert(swapInfo.getFree, sizeUnit),
    convert(swapInfo.getUsed, sizeUnit), PERCENT(swapInfo.getFree, swapInfo.getTotal),
    PERCENT(swapInfo.getUsed, swapInfo.getTotal))

   NodeMetrics(node, cpu, mem, swap)
  }
}
