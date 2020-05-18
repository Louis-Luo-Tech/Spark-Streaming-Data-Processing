# Spark Streaming Realtime Processing Project

## Project Information

### Project Needs
* Client access information and the geographic information distribution
* Geographic information: ip conversion
* Client: User agent
* Process: Offline Spark/MapReduce


### Project Steps
* ip information,useragent
* Statistical analysis: Spark/MapReduce

### project Architecture
* Log collection: Flume
* Offline analysis: Spark/MapReduce
* Graphical display of statistical results

## Real-time Processing and Stream Processing

### Real-time Computing
Real-time Computing is a concept that describes any computing system that must respond to changes in the environment according to definite time constraints, usually on the order of milliseconds.

### Offline batch processing
Batch data processing is an extremely efficient way to process large amounts of data that is collected over a period of time.  
Batch processing is the processing of transactions in a group or batch. No user interaction is required once batch processing is underway.While batch processing can be carried out at any time, it is particularly suited to end-of-cycle processing, such as for processing a bank's reports at the end of a day

### Stream Computing
Processing the data and streaming it back out as a single flow. Stream computing enables organizations to process data streams which are always on and never ceasing.

### Real-time Stream Processing
Real-time stream processing is the process of taking action on data at the time the data is generated or published. Historically, real-time processing simply meant data was “processed as frequently as necessary for a particular use case.

## Offline Computing and Real-time Computing
* Data Source
  * Offline: HDFS, historical data(large valume)
  * Real-time: Message queue(Kafka), real-time newly-updated data
* Computing Process
  * Offline: MapReduce: Map + Reduce
  * Real-time: Spark(DStream/SS)
* Process Speed
  * Offline: Slow
  * Real-time: Fast
* Processes
  * Offline: Start -> Destroy
  * Real-time: 7*24

## Real-time Stream Processing Frameworks
### Apache Storm
Apache Storm is a free and open source distributed realtime computation system. Apache Storm makes it easy to reliably process unbounded streams of data, doing for realtime processing what Hadoop did for batch processing. Apache Storm is simple, can be used with any programming language, and is a lot of fun to use!

### Apache Spark Streaming
Spark Streaming is an extension of the core Spark API that enables scalable, high-throughput, fault-tolerant stream processing of live data streams. 

### Linkedin Kafka
A Distributed Streaming Platform.

### Apache Flink
Apache Flink is a framework and distributed processing engine for stateful computations over unbounded and bounded data streams. 

# Flume
Flume is a distributed, reliable, and available service for efficiently collecting, aggregating, and moving large amounts of log data. It has a simple and flexible architecture based on streaming data flows. It is robust and fault tolerant with tunable reliability mechanisms and many failover and recovery mechanisms. It uses a simple extensible data model that allows for online analytic application.

## Getting Started
Web Server ==> Flume ==> HDFS(Target)

1. **Key Components**
 * Source: Collect
 * Channel: Aggregrate
 * Sink: Output
 
 
2. **System Requirements**
 * Java 1.7+(Java 1.8 Recommended)
 * Sufficient memory
 * Sufficient disk space
 * Directory Permissions
 
## Configuring Flume
1. **Download from [here](https://flume.apache.org/download.html)**
2. **Export to PATH**
3. **Configure Flume**
   <pre>
   $ cp flume-env.sh.template flume-env.sh
   </pre>
   
   Export JAVA_HOME in this configuration file
 
   Check Flume Version
   <pre>
   $ flume-ng version
   </pre>
   <pre>
   xiangluo@Xiangs-MacBook-Pro ~ % flume-ng version      
   Flume 1.9.0
   Source code repository: https://git-wip-us.apache.org/repos/asf/flume.git
   Revision: d4fcab4f501d41597bc616921329a4339f73585e
   Compiled by fszabo on Mon Dec 17 20:45:25 CET 2018
   From source with checksum 35db629a3bda49d23e9b3690c80737f9
   </pre>
   
## Flume Example
1. **Flume Example1**
     Collect the data from one specific netwok port and print the information in the console
     
     One example.conf file is for a single-node Flume configuration
     a1:agent name  
     r1:source name  
     k1:sink name  
     c1:channel name  
     <pre>
     # example.conf: A single-node Flume configuration

     # Name the components on this agent
     a1.sources = r1
     a1.sinks = k1
     a1.channels = c1

     # Describe/configure the source
     a1.sources.r1.type = netcat
     a1.sources.r1.bind = localhost
     a1.sources.r1.port = 43444

     # Describe the sink
     a1.sinks.k1.type = logger

     # Use a channel which buffers events in memory
     a1.channels.c1.type = memory
     a1.channels.c1.capacity = 1000
     a1.channels.c1.transactionCapacity = 100

     # Bind the source and sink to the channel
     a1.sources.r1.channels = c1
     a1.sinks.k1.channel = c1
     </pre>
     
     Start the Flume agent
     
     <pre>
     flume-ng agent \
     --name a1 \
     --conf $FLUME_HOME/conf \
     --conf-file $FLUME_HOME/conf/example.conf \
     -Dflume.root.logger=INFO,console
     </pre>
     
     Test Flume agent with telnet in another terminal
     
     telnet localhost 43444
     
     Enter "hello" and "world" in the console
     <pre>
     2020-04-23 23:03:31,177 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO -     org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 68 65 6C 6C 6F 0D                               hello. }
     2020-04-23 23:03:35,186 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 77 6F 72 6C 64 0D                               world. }
     </pre>
     
     Event is the basic unit in Flume data transfer
     
     Event = optional header + byte array
     
2. **Flume Example2**

     Monitor a file and collect real-time updated new data and print them in the console
     
     Create a new conf file, exec_memory_logger.conf
     
     <pre>
     # example.conf: A single-node Flume configuration

     # Name the components on this agent
     a1.sources = r1
     a1.sinks = k1
     a1.channels = c1

     # Describe/configure the source
     a1.sources.r1.type = exec
     a1.sources.r1.command = tail -F /Users/xiangluo/data/example.log
     a1.sources.r1.shell = /bin/sh -c

     # Describe the sink
     a1.sinks.k1.type = logger

     # Use a channel which buffers events in memory
     a1.channels.c1.type = memory
     a1.channels.c1.capacity = 1000
     a1.channels.c1.transactionCapacity = 100

     # Bind the source and sink to the channel
     a1.sources.r1.channels = c1
     a1.sinks.k1.channel = c1
     </pre>
     
     Create a empty log file in the target folder
     
     <pre>
     $ touch example.log
     </pre>
     
     Start the Flume agent
     
     <pre>
     flume-ng agent \
     --name a1 \
     --conf $FLUME_HOME/conf \
     --conf-file $FLUME_HOME/conf/exec_memory_logger.conf \
     -Dflume.root.logger=INFO,console
     </pre>     
     
     Add some data in the monitored log file
     
     <pre>
     $ echo hello >> example.log
     </pre>
     
     <pre>
     2020-04-23 23:17:41,926 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 68 65 6C 6C 6F                                  hello }
     </pre>
     
3. **Flume Example3**
     
     Collect the log from Server A and transfer it to Server B
     
     Create the first conf file, exec-memory-avro.conf
     <pre>
     # example.conf: A single-node Flume configuration

     # Name the components on this agent
     exec-memory-avro.sources = exec-source
     exec-memory-avro.sinks = avro-sink
     exec-memory-avro.channels = memory-channel

     # Describe/configure the source
     exec-memory-avro.sources.exec-source.type = exec
     exec-memory-avro.sources.exec-source.command = tail -F /Users/xiangluo/data/example.log
     exec-memory-avro.sources.exec-source.shell = /bin/sh -c

     # Describe the sink
     exec-memory-avro.sinks.avro-sink.type = avro
     exec-memory-avro.sinks.avro-sink.hostname = localhost
     exec-memory-avro.sinks.avro-sink.port = 44444

     # Use a channel which buffers events in memory
     exec-memory-avro.channels.memory-channel.type = memory
     exec-memory-avro.channels.memory-channel.capacity = 1000
     exec-memory-avro.channels.memory-channel.transactionCapacity = 100

     # Bind the source and sink to the channel
     exec-memory-avro.sources.exec-source.channels = memory-channel
     exec-memory-avro.sinks.avro-sink.channel = memory-channel
     </pre>
     
     Create the second conf file, avro-memory-logger.conf
     
     <pre>
     # example.conf: A single-node Flume configuration

     # Name the components on this agent
     avro-memory-logger.sources = avro-source
     avro-memory-logger.sinks = logger-sink
     avro-memory-logger.channels = memory-channel

     # Describe/configure the source
     avro-memory-logger.sources.avro-source.type = avro
     avro-memory-logger.sources.avro-source.bind = localhost
     avro-memory-logger.sources.avro-source.port = 44444

     # Describe the sink
     avro-memory-logger.sinks.logger-sink.type = logger

     # Use a channel which buffers events in memory
     avro-memory-logger.channels.memory-channel.type = memory
     avro-memory-logger.channels.memory-channel.capacity = 1000
     avro-memory-logger.channels.memory-channel.transactionCapacity = 100

     # Bind the source and sink to the channel
     avro-memory-logger.sources.avro-source.channels = memory-channel
     avro-memory-logger.sinks.logger-sink.channel = memory-channel
     </pre>
     
     Start the Flume agent
     <pre>
     First:
     flume-ng agent \
     --name avro-memory-logger \
     --conf $FLUME_HOME/conf \
     --conf-file $FLUME_HOME/conf/avro-memory-logger.conf \
     -Dflume.root.logger=INFO,console
     
     Second:
     flume-ng agent \
     --name exec-memory-avro \
     --conf $FLUME_HOME/conf \
     --conf-file $FLUME_HOME/conf/exec-memory-avro.conf \
     -Dflume.root.logger=INFO,console
     </pre>
     
     Add some data in the monitored log file
     
     <pre>
     $ echo hello flink >> example.log
     </pre>
     
     <pre>
     2020-04-23 23:50:47,353 (SinkRunner-PollingRunner-DefaultSinkProcessor) [INFO - org.apache.flume.sink.LoggerSink.process(LoggerSink.java:95)] Event: { headers:{} body: 68 65 6C 6C 6F 20 66 6C 69 6E 6B                hello flink }
     </pre>
     
     
# Kafka

A Distributed Streaming Platform.

Kafka is generally used for two broad classes of applications:

Building real-time streaming data pipelines that reliably get data between systems or applications

Building real-time streaming applications that transform or react to the streams of data

## Getting Started

1. **Key Components**
 * Producer
 * Consumer
 * Broker
 * Topic
 
2. **System Requirements**
 * Zookeeper
 
  Kafka is run as a cluster on one or more servers that can span multiple datacenters. The Kafka cluster stores streams of records in categories called topics. Each record consists of a *key, a value, and a timestamp*.
 
## Configuring Kafka
1. **Download Zookeeper
2. **Export to PATH**
     Add to PATH and change the dataDir
     
     Start Zookeeper Server
     <pre>
     $ zkServer.sh start
     </pre>
     
     Login Zookeeper Server
     <pre>
     $ zkCli.sh
     </pre>
     
3. **Configure Kafka**
     Download Kafka(Note the version of Scala, here we use 0.9.0.0)
     
     Export to PATH
     
     Change the path of log.dirs
     
     Single node single broker setup
     
     * Start Kafka Server
     <pre>
     $ kafka-server-start.sh $KAFKA_HOME/config/server.properties 
     </pre>
     
     * Create a topic
     <pre>
     $ kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic kafkatest
     </pre>
     
     * Check all the topics
     <pre>
     $ kafka-topics.sh --list --zookeeper localhost:2181
     </pre>
     
     * Send some messages
     <pre>
     $ kafka-console-producer.sh --broker-list localhost:9092 --topic kafkatest
     </pre>
     
     * Start a consumer
     <pre>
     $ kafka-console-consumer.sh --zookeeper localhost:2181 --topic kafkatest --from-beginning
     </pre>
     
     * Check the information of the topic
     <pre>
     $ kafka-topics.sh --describe --zookeeper localhost:2181
     </pre>
     
      <pre>
     $ kafka-topics.sh --describe --zookeeper localhost:2181 --topic kafkatest
     </pre>
     
     Single node multi-broker cluster setup
     
     Create config file
     
     <pre>
     $ cp config/server.properties config/server-1.properties
     $ cp config/server.properties config/server-2.properties
     $ cp config/server.properties config/server-3.properties
     </pre>
     
     Edit the config files with the following properties
     
     <pre>
     config/server-1.properties:
     broker.id=1
     listeners=PLAINTEXT://:9093
     log.dirs=/tmp/kafka-logs-1
 
     config/server-2.properties:
     broker.id=2
     listeners=PLAINTEXT://:9094
     log.dirs=/tmp/kafka-logs-2

     config/server-3.properties:
     broker.id=3
     listeners=PLAINTEXT://:9095
     log.dirs=/tmp/kafka-logs-3
     </pre>
     
     Start Kafka Server
     <pre>
     $ kafka-server-start.sh -daemon $KAFKA_HOME/config/server-1.properties 
     $ kafka-server-start.sh -daemon $KAFKA_HOME/config/server-2.properties
     $ kafka-server-start.sh -daemon $KAFKA_HOME/config/server-3.properties
     </pre>
     
     Create topic
     <pre>
     $ kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
     </pre>
     
     Send some messages
     <pre>
     $ kafka-console-producer.sh --broker-list localhost:9093,localhost:9094,localhost:9095 --topic my-replicated-topic
     </pre>
     
     Start a consumer
     <pre>
     $ kafka-console-consumer.sh --zookeeper localhost:2181 --topic my-replicated-topic --from-beginning
     </pre>
     
3. **Some errors**
     A Kafka instance in another process or thread is using this directory.

     Step1:
     
     remove .lock file
     
     Step2:
     
     remove the log folder
     
     Step3:
     
     Get new error
     
     Socket server failed to bind to 0.0.0.0:9092: Address already in use
     
     <pre>
     $ lsof -n -i :9092 | grep LISTEN
     $ kill -9 process_no
     </pre>
     
     Start the Kafka Server again, then it works
     
     
4. **Fault-Tolerant Test**

     If we shut down any Kafka Server, even including the Leader, the Kafka Server could still work as usual.
     
     ![Screenshot](images/kafka1.png)

# Integrate Flume and Kafka to collect data

## Configuration

   Here we can make some changes on the avro-memory-logger.conf to create a new avro-memory-kafka.conf file.
   
   <pre>
   # Name the components on this agent
   avro-memory-kafka.sources = avro-source
   avro-memory-kafka.sinks = kafka-sink
   avro-memory-kafka.channels = memory-channel

   # Describe/configure the source
   avro-memory-kafka.sources.avro-source.type = avro
   avro-memory-kafka.sources.avro-source.bind = localhost
   avro-memory-kafka.sources.avro-source.port = 44444

   # Describe the sink
   avro-memory-kafka.sinks.kafka-sink.type = org.apache.flume.sink.kafka.KafkaSink
   avro-memory-kafka.sinks.kafka-sink.topic = flume
   avro-memory-kafka.sinks.kafka-sink.brokerList = localhost:9095
   avro-memory-kafka.sinks.kafka-sink.batchSize = 5
   avro-memory-kafka.sinks.kafka-sink.requiredAcks = 1


   # Use a channel which buffers events in memory
   avro-memory-kafka.channels.memory-channel.type = memory
   avro-memory-kafka.channels.memory-channel.capacity = 1000
   avro-memory-kafka.channels.memory-channel.transactionCapacity = 100

   # Bind the source and sink to the channel
   avro-memory-kafka.sources.avro-source.channels = memory-channel
   avro-memory-kafka.sinks.kafka-sink.channel = memory-channel
   </pre>
   
## Start a Kafka Consumer
   <pre>
   $ kafka-console-consumer.sh --zookeeper localhost:2181 --topic flume
   </pre>

## Load new data into the example.log file
   <pre>
   $ echo 1 >> example.log
   </pre>
   
   Then the data will be consumed by Kafka Consumer.


# Building Spark
#


# Spark Streaming

## What is Spark Streaming

Spark Streaming is an extension of the core Spark API that enables scalable, high-throughput, fault-tolerant stream processing of live data streams. Data can be ingested from many sources like Kafka, Flume, Kinesis, or TCP sockets, and can be processed using complex algorithms expressed with high-level functions like map, reduce, join and window. Finally, processed data can be pushed out to filesystems, databases, and live dashboards. In fact, you can apply Spark’s machine learning and graph processing algorithms on data streams.


Spark Streaming is released with Spark 0.9

Spark Sql is released with Spark 1.0.0

## Example

Use spark-submit


Start a port
```
$ nc -lk 9999
```

```
spark-submit \
--class org.apache.spark.examples.streaming.NetworkWordCount \
--master local \
--name NetworkWordCount \
/Users/xiangluo/app/spark-2.4.5-bin-hadoop2.7/examples/jars/spark-examples_2.11-2.4.5.jar localhost 9999
```

Use spark-shell

```
spark-shell --master local

import org.apache.spark.streaming.{Seconds, StreamingContext}
val ssc = new StreamingContext(sc, Seconds(1))
val lines = ssc.socketTextStream("localhost", 9999)
val words = lines.flatMap(_.split(" "))
val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
wordCounts.print()
ssc.start()
ssc.awaitTermination()
```

Note:

ssc.socketTextStream
ssc.textFileStream

For socketTextStream local[1] is fine, because it is a local file path, but for socketTextStream, local[?] ?>=2 is needed because there is a receiver that need thread

## DStream

Discretized Streams (DStreams)

Discretized Stream or DStream is the basic abstraction provided by Spark Streaming. It represents a continuous stream of data, either the input data stream received from source, or the processed data stream generated by transforming the input stream. Internally, a DStream is represented by a continuous series of RDDs, which is Spark’s abstraction of an immutable, distributed dataset (see Spark Programming Guide for more details). Each RDD in a DStream contains data from a certain interval, as shown in the following figure.



The operations applied on each DStream, such as map,flatmap can be regarded that the operation is applied on each RDD in DStream, because each DStream is consist of RDD in different batch.


## Input DStreams and Receivers

Input DStreams are DStreams representing the stream of input data received from streaming sources. In the quick example, lines was an input DStream as it represented the stream of data received from the netcat server. Every input DStream (except file stream, discussed later in this section) is associated with a Receiver (Scala doc, Java doc) object which receives the data from a source and stores it in Spark’s memory for processing.

## UpdateStateByKey function

updateStateByKey(func)	Return a new "state" DStream where the state for each key is updated by applying the given function on the previous state of the key and the new values for the key. This can be used to maintain arbitrary state data for each key.


```
object StatefulWordCount {
  def main(args: Array[String]): Unit = {

    val sparkconf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("NerworkWordCount") //local[?] ?>=2 is needed because there is a receiver that need thread
    val ssc = new StreamingContext(sparkconf, Seconds(1))
    
    //it is recommended that the checkpont should be a folder on HDFS
    ssc.checkpoint(".")

    val lines = ssc.socketTextStream("localhost", 8888)
    val words = lines.flatMap(_.split(" ")).map((_,1)) //(x => (x, 1))
    val state = words.updateStateByKey[Int](updateFunction _)

    state.print()
    ssc.start()
    ssc.awaitTermination()
  }


  /**
   * update the current data or old data
   * @param currentValues current data
   * @param preValues old data
   * @return
   */
  def updateFunction(currentValues: Seq[Int], preValues: Option[Int]): Option[Int] ={
    val current = currentValues.sum
    val pre = preValues.getOrElse(0)
    Some(current + pre)
  }
}
```
## Checkpointing

A streaming application must operate 24/7 and hence must be resilient to failures unrelated to the application logic (e.g., system failures, JVM crashes, etc.). For this to be possible, Spark Streaming needs to checkpoint enough information to a fault- tolerant storage system such that it can recover from failures. There are two types of data that are checkpointed.

Metadata checkpointing - Saving of the information defining the streaming computation to fault-tolerant storage like HDFS. This is used to recover from failure of the node running the driver of the streaming application (discussed in detail later). Metadata includes:

 Configuration - The configuration that was used to create the streaming application.

 DStream operations - The set of DStream operations that define the streaming application.

 Incomplete batches - Batches whose jobs are queued but have not completed yet.

Data checkpointing - Saving of the generated RDDs to reliable storage. This is necessary in some stateful transformations that combine data across multiple batches. In such transformations, the generated RDDs depend on RDDs of previous batches, which causes the length of the dependency chain to keep increasing with time. To avoid such unbounded increases in recovery time (proportional to dependency chain), intermediate RDDs of stateful transformations are periodically checkpointed to reliable storage (e.g. HDFS) to cut off the dependency chains.

To summarize, metadata checkpointing is primarily needed for recovery from driver failures, whereas data or RDD checkpointing is necessary even for basic functioning if stateful transformations are used.

When to enable Checkpointing
Checkpointing must be enabled for applications with any of the following requirements:

Usage of stateful transformations - If either updateStateByKey or reduceByKeyAndWindow (with inverse function) is used in the application, then the checkpoint directory must be provided to allow for periodic RDD checkpointing.
Recovering from failures of the driver running the application - Metadata checkpoints are used to recover with progress information.

Note that simple streaming applications without the aforementioned stateful transformations can be run without enabling checkpointing. The recovery from driver failures will also be partial in that case (some received but unprocessed data may be lost). This is often acceptable and many run Spark Streaming applications in this way. Support for non-Hadoop environments is expected to improve in the future.


## Window Operation(windowed computations)

Windowed computations, which allow you to apply transformations over a sliding window of data. The following figure illustrates this sliding window.

As shown in the figure, every time the window slides over a source DStream, the source RDDs that fall within the window are combined and operated upon to produce the RDDs of the windowed DStream. In this specific case, the operation is applied over the last 3 time units of data, and slides by 2 time units. This shows that any window operation needs to specify two parameters.

window length - The duration of the window (3 in the figure).
sliding interval - The interval at which the window operation is performed (2 in the figure).
These two parameters must be multiples of the batch interval of the source DStream (1 in the figure).

Let’s illustrate the window operations with an example. Say, you want to extend the earlier example by generating word counts over the last 30 seconds of data, every 10 seconds. To do this, we have to apply the reduceByKey operation on the pairs DStream of (word, 1) pairs over the last 30 seconds of data. This is done using the operation reduceByKeyAndWindow.


## Filter Blacklist Example

Input (778,zs), (779,ls),(777,ww)

Blacklist (zs,ls)

(778,zs), (779,ls),(777,ww) left join (zs,ls)

==>(zs,(778,zs)),(ls,(779,ls)),(ww,(777,ww))  left join (zs,true), (ls,true)

==>(zs,((778,zs),true)),(ls,((779,ls),true)),(ww,((777,ww),flase))

join(otherDataset, [numPartitions])	When called on datasets of type (K, V) and (K, W), returns a dataset of (K, (V, W)) pairs with all pairs of elements for each key. Outer joins are supported through leftOuterJoin, rightOuterJoin, and fullOuterJoin.


# Spark Streaming + Flume Integration

## Approach 1: Flume-style Push-based Approach

Flume is designed to push data between Flume agents. In this approach, Spark Streaming essentially sets up a receiver that acts an Avro agent for Flume, to which Flume can push the data. Here are the configuration steps.

### Configure Flume

```
# example.conf: A single-node Flume configuration

# Name the components on this agent
simple-agent.sources = netcat-source
simple-agent.sinks = avro-sink
simple-agent.channels = memory-channel

# Describe/configure the source
simple-agent.sources.netcat-source.type = netcat
simple-agent.sources.netcat-source.bind = localhost
simple-agent.sources.netcat-source.port = 44444

# Describe the sink
simple-agent.sinks.avro-sink.type = avro
simple-agent.sinks.avro-sink.hostname = localhost
simple-agent.sinks.avro-sink.port = 41414

# Use a channel which buffers events in memory
simple-agent.channels.memory-channel.type = memory
simple-agent.channels.memory-channel.capacity = 1000
simple-agent.channels.memory-channel.transactionCapacity = 100

# Bind the source and sink to the channel
simple-agent.sources.netcat-source.channels = memory-channel
simple-agent.sinks.avro-sink.channel = memory-channel

```
 
### Application Development

Add depedency
```
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming-flume_2.11</artifactId>
            <version>${spark.version}</version>
        </dependency>
```

```
    if(args.length != 2){
      System.err.println("Usage: FlumePushWordCount <hostname> <port>")
      System.exit(1)
    }

    val Array(hostname,port) = args


    val sparkconf = new SparkConf()//.setMaster("local[10]").setAppName("FlumePushWordCount")
    val ssc = new StreamingContext(sparkconf,Seconds(5))

    val flumeStream = FlumeUtils.createStream(ssc,hostname,port.toInt)

    flumeStream.map(x=> new String(x.event.getBody.array()).trim)
        .flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
        .print()

    ssc.start()
    ssc.awaitTermination()
```


Start the application first then start the flume agent

```
flume-ng agent \
--name simple-agent \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/flume_push_streaming.conf \
-Dflume.root.logger=INFO,console
```

```
$ telnet localhost 44444
```

Enter some words then the wordcount information would shown in the console in the intellij

### Run the application with Spark-submit

```
spark-submit \
--class com.louis.spark.FlumePushWordCount \
--master "local[*]" \
--jars /Users/xiangluo/spark-streaming-flume-assembly_2.11-2.4.5.jar \
/Users/xiangluo/Documents/GitHub/Spark-Streaming-Data-Processing/target/Spark-Streaming-Data-Processing-1.0-SNAPSHOT.jar \
localhost 41414
```

Then start flume agent

```
flume-ng agent \
--name simple-agent \
--conf $FLUME_HOME/conf \
--conf-file $FLUME_HOME/conf/flume_push_streaming.conf \
-Dflume.root.logger=INFO,console
```

```
$ telnet localhost 44444
```

Enter some words then the wordcount information would shown in the console.
