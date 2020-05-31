package com.louis.spark.project.spark

import com.louis.spark.project.dao.ProductClickCountDAO
import com.louis.spark.project.domain.{ClickLog, ProductClickCount}
import com.louis.spark.project.utils.DateUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.ListBuffer

object StreamingApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.err.println("usage: zkQuorum, group, topics, numThreads")
      System.exit(1)
    }
    val Array(zkQuorum, group, topics, numThreads) = args

    val sparkConf: SparkConf = new SparkConf().setAppName("StreamingApp").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    val topicsMap = topics.split((",")).map((_, numThreads.toInt)).toMap

    val message = KafkaUtils.createStream(ssc, zkQuorum, group, topicsMap)

    //    message.map(_._2).count().print()
    //data cleaning

    val logs = message.map(_._2)

    val cleanData = logs.map(line => {
      //      43.29.63.167	2020-05-30 15:50:25	"GET /product/14610854.html HTTP/1.1"	500	https://www.bing.com/search?q=ps4
      val infos = line.split("\t")
      val url = infos(2).split(" ")(1)
      var productId = 0
      //      "GET /product/12910052.html HTTP/1.1"
      if (url.startsWith("/product")) {
        val productIdHTML = url.split("/")(2)
        productId = productIdHTML.substring(0, productIdHTML.lastIndexOf(".")).toInt
      }
      ClickLog(infos(0), DateUtils.parseToMinute(infos(1)), productId, infos(3).toInt, infos(4))
    }).filter(ClickLog => ClickLog.productId != 0)

    //    cleanData.print()

    cleanData.map(x => {
      //rowkey
      (x.time.substring(0, 8) + "_" + x.productId, 1)
    }).reduceByKey(_ + _).foreachRDD(rdd => {
      rdd.foreachPartition(partitionRecords => {
        val list = new ListBuffer[ProductClickCount]
        partitionRecords.foreach(pair => {
          list.append(ProductClickCount(pair._1, pair._2))
        })
        ProductClickCountDAO.save(list)
      })
    })


    ssc.start()
    ssc.awaitTermination()
  }
}
