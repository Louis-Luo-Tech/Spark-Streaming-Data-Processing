package com.louis.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamingExample {
  def main(args: Array[String]): Unit = {
//    StreamingExamples.setStreamingLogLevels()

    val sparkconf: SparkConf = new SparkConf().setMaster("local").setAppName("SparkStreamingExample")
    val ssc = new StreamingContext(sparkconf, Seconds(1))
    val lines = ssc.socketTextStream("localhost",9977)
    val words = lines.flatMap(_.split(" "))
    val wordscount = words.map(x=>(x,1)).reduceByKey(_+_)
    wordscount.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
