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

# What is Spark Streaming

Spark Streaming is an extension of the core Spark API that enables scalable, high-throughput, fault-tolerant stream processing of live data streams. Data can be ingested from many sources like Kafka, Flume, Kinesis, or TCP sockets, and can be processed using complex algorithms expressed with high-level functions like map, reduce, join and window. Finally, processed data can be pushed out to filesystems, databases, and live dashboards. In fact, you can apply Spark’s machine learning and graph processing algorithms on data streams.


Spark Streaming is released with Spark 0.9

Spark Sql is released with Spark 1.0.0

# Example

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

