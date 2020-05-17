package com.louis.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object FlumePushWordCount {
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf().setMaster("local[10]").setAppName("FlumePushWordCount")
    val ssc = new StreamingContext(sparkconf,Seconds(5))

    val flumeStream = FlumeUtils.createStream(ssc,"localhost",41414)

    flumeStream.map(x=> new String(x.event.getBody.array()).trim)
        .flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
        .print()

    ssc.start()
    ssc.awaitTermination()
  }
}
