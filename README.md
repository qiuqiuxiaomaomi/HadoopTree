# HadoopTree
Hadoop技术研究

![](https://i.imgur.com/APk33CU.png)

![](https://i.imgur.com/quSemDU.png)

<pre>
   Hadoop为用户提供了一个可靠的共享存储和分析系统
       1) HDFS实现数据的存储
       2) MapReduce实现数据的分析与处理

   MapReduce比较适合以批处理方式处理需要分析整个数据集的问题，尤其是动态分析。
         适合一次写入，多次读取数据的应用
         半结构化数据比较松散，虽然可能有格式但是经常被忽略，MapReduce对非结构化（如图片）或者半结构化的数据非常有效，因为它是在处理数据时才对数据进      行解释。	 
		      
   RDBMS 
         适用于点查询和更新，数据集被索引之后，数据库系统能够提供低延迟的数据检索和快速的少 量数据更新
         适合持续更新的数据集
		 结构化数据（如满足特定预定义格式的数据库表，XML文档等）这是RDBMS包括的内容。
		 关系型数据库是规范的.

   MapReduce尽量在计算节点上存储数据，以实现数据的本地快速访问。数据本地化特性是MapReduce的核心特征，并因此获得良好的性能。

   Hadoop是Apache Lucene创始人Doug Cutting创建的，Lucene是一个应用广泛的文本搜索系统 库，Hadoop起源于开源的网络搜索引擎Apache Nutch，它本身也是Lucene项目的一部分。
</pre>

<pre>
Hadoop的发展简史
   Nutch项目开始于2002年，一个可以运行的网页爬虫工具和搜索引擎系统很快面试，但后来，开发者认为这一架构的灵活性不够，不足以解决数十亿网页的搜索问题。
     1）一篇发表于2003年的论文为此提供了帮助，文中描述的是谷歌的产品架构，该架构称为"谷歌分布式文件系统"，简称GFS。GFS或类似的架构，可以解决他们在网页
	 爬取和索引过程中产生的超大文件的存储需求，特别关键的是，GFS能够节省系统管理（如管理存储节点）所花的时间，在2004年，他们开始着手做开源版本的实现，
	 即Nutch分布式文件系统（NDFS）
	 2）2004年，谷歌发表论文向全世界介绍他们的MapReduce系统，2005年，Nutch的开发人员在Nutch上实现了一个MapReduce系统，到年中，Nutch的所有主要算法均已移植，
	 用MapReduce和NDFS来运行
	 Nutch的NDFS和MapReduce实现不只适用于搜索领域，2006年2月，开发人员将NDFS和MapReducee移出Nutch形成Lucene的子项目，命名为Hadoop
</pre>

<pre>
Hadoop生态系统
    1）Common：一系列组件和接口，用于分布式文件系统和通用I/O(序列化，JAVA RPC和持久化数据结构)
    2）Avro：一种序列化系统，用于支持高校，跨语言的RPC和持久化数据存储
    3）MapReduce：分布式数据处理模型和执行环境，运行于大型商用机器集群
    5）HDFS：分布式文件系统，运行于大型商用机集群
    6）Pig: 数据流语言和运行环境，用以探究非常庞大的数据集,Pig运行在MapReduce和HDFS集群上
    7）Hive: 一种分布式的，按列存储到额数据仓库，Hive管理HDFS中存储的数据，并提供基于SQL的查询语言(由运行时翻译成MapReduce作业)用以查询数据
    8）HBase：一种分布式的，按列存储的数据库。HBase使用HDFS作为底层存储，同时支持MapReduce的批量式计算和点查询
    9）Zookeeper：一种分布式的，可用性高的协调服务，Zookeeper提供分布式锁之类的基本服务用语构建分布式应用
    10）Sqoop：该工具用语在结构化数据存储（如关系型数据库）和HDFS之间高效批量传输数据
    11）Oozie: 该服务用于运行和调度Hadoop作业（如MapReduce， Pig, Hive, Sqoop作业）
</pre>

<pre>
使用Hadoop分析数据
    MapReduce任务过程分为两个处理阶段
	    1）Map阶段
	    2）Reduce阶段
		每个阶段都以键值对作为输入和输出。
</pre>

<pre>
MapReduce作业（job）是客户端需要执行的一个工作单元，它包括输入数据，MapReduce程序和配置信息. Hadoop将作业分成若干个小任务来执行，其中包括两类任务：map任务和reduce任务.

有两类节点控制着作业执行过程
      1）一个jobtracker
	     jobtracker通过调度tasktracker上运行的任务来协调所有运行在系统上的作业。
      2）一系列tasktracker
         tasktracker在运行任务的同时将运行进度报告发送给jobtracker，jobtracker由此记录每项作业任务的整体进度情况，如果一个
		 任务失败，jobtracker可以在另外一个tasktracker节点上重新调度该任务。
</pre>

<pre>
Hadoop将MapReduce的输入数据划分成等长的小数据块，称为输入分片，Hadoop为每个分片构建一个map任务，并由该任务来运行用户自定义的map函数从而处理分片中的每条记录。

Hadoop在存储有输入数据（HDFS中的数据）的节点上运行map任务，可以获得最佳性能。这就是所谓“数据本地化优化”，因为它无需使用宝贵的集群带宽资源。

Map任务将其输出写入本地硬盘，而非HDFS，这是因为Map的输出是中间结果：该中间结果由reduce任务处理后才产生最终输出结构，而且作业一旦完成，map的输出结果
就可以删除。因此，如果把它存储在HDFS中并实现备份，难免有些小题大作，如果该节点的map任务在将map中间结果传送给reduce任务之前失败，Hadoop将在另一个节点
上重新运行这个map任务以再次构建map中间结果。

Reduce任务并不具备数据本地化的优势，单个reduce任务的输入通常来自于所有mapper的输出，拍过序的map输出需要通过网络传输发送到运行reduce任务的节点。数据在reduce端合并，然后由用户定义的reduce函数处理，reduce的输出通常存储在HDFS中以实现可靠存储。对于每个reduce输出的HDFS块，第一个复本存储在本地节点上，其他复本存储在其他机架节点中，因此需要占用带宽。
</pre>

![](https://i.imgur.com/7tiOwaC.jpg)


Hadoop生态圈主要应用

![](https://i.imgur.com/cA7nESg.png)


HDFS结构图

![](https://i.imgur.com/taR5Tr5.png)

HDFS文件读取

![](https://i.imgur.com/dm7Ghvy.png)

HDFS文件写入

![](https://i.imgur.com/VryTbPU.png)

<pre>
HDFS:
     
      分布式文件系统
      是Hadoop体系中数据存储管理的基础，它是一个高度容错的系统，能检测和应对硬件故障。
 
      client:切分文件，访问HDFS,与Namenode交互，获取文件位置信息，与DataNode交互，读取
             和写入数据。

      namenode:master节点，在hadoop1.x中只有一个，管理HDFS的名称空间和数据块映射信息，
               配置副本策略，处理客户端请求。

      DataNode:slave节点，存储实际的数据，汇报存储信息给namenode.

      secondary namenode：辅助namenode，分担其工作量，定期合并fsimage和fsedits，推送给
                          namenode，紧急情况下辅助恢复namenode,但其并非namenode的
                          热备。

      文件写入原理：

                 1）客户端通过调用 DistributedFileSystem 的create方法，创建一个新的文件。
                 2）DistributedFileSystem 通过 RPC（远程过程调用）调用 NameNode，去创建一个
                   没有blocks关联的新文件。创建前，NameNode 会做各种校验，比如文件是否存在，客
                   户端有无权限去创建等。如果校验通过，NameNode 就会记录下新文件，否则就会抛出
                   IO异常。
                 3）前两步结束后会返回 FSDataOutputStream 的对象，和读文件的时候相似，
                   FSDataOutputStream 被封装成 DFSOutputStream，DFSOutputStream 可以协调
                   NameNode和 DataNode。客户端开始写数据到DFSOutputStream,DFSOutputStream
                   会把数据切成一个个小packet，然后排成队列 data queue。
                 5）DataStreamer 会去处理接受 data queue，它先问询 NameNode 这个新的 block
                   最适合存储的在哪几个DataNode里，比如重复数是3，那么就找到3个最适合的
                   DataNode，把它们排成一个 pipeline。DataStreamer 把 packet 按队列输出到
                   管道的第一个 DataNode 中，第一个 DataNode又把 packet 输出到第二个
                   DataNode 中，以此类推。
                 6）DFSOutputStream 还有一个队列叫 ack queue，也是由 packet 组成，等待
                   DataNode的收到响应，当pipeline中的所有DataNode都表示已经收到的时候，这时
                   ack queue才会把对应的packet包移除掉。
                 7) 客户端完成写数据后，调用close方法关闭写入流。
                 8) DataStreamer 把剩余的包都刷到 pipeline 里，然后等待 ack 信息，收到最后一
                   个 ack 后，通知 DataNode 把文件标示为已完成。


      文件读取原理：

                 1）首先调用FileSystem对象的open方法，其实获取的是一个DistributedFileSystem的实例。
                 
                 2）DistributedFileSystem通过RPC(远程过程调用)获得文件的第一批block的
                   locations，同一block按照重复数会返回多个locations，这些locations按照
                   hadoop拓扑结构排序，距离客户端近的排在前面。

                 3）前两步会返回一个FSDataInputStream对象，该对象会被封装成 DFSInputStream对
                   象，DFSInputStream可以方便的管理datanode和namenode数据流。客户端调用read
                   方法，DFSInputStream就会找出离客户端最近的datanode并连接datanode。
                
                 5）数据从datanode源源不断的流向客户端。
            
                 6）如果第一个block块的数据读完了，就会关闭指向第一个block块的datanode连接，接
                   着读取下一个block块。这些操作对客户端来说是透明的，从客户端的角度来看只是读一
                   个持续不断的流。

                 7）如果第一批block都读完了，DFSInputStream就会去namenode拿下一批blocks的
                   location，然后继续读，如果所有的block块都读完，这时就会关闭掉所有的流。
</pre>

<pre>
mapreduce:
 
      分布式计算框架
      mapreduce是一种计算模型，用于处理大数据量的计算，其中map对应数据集上的独立元素进行
      指定的操作，生成键-值对形式中间，reduce则对中间结果中相同的键的所有值进行规约，以得
      到最终结果。

      jobtracker:master节点，只有一个，管理所有作业，任务/作业的监控，错误处理等，将任务
                 分解成一系列任务，并分派给tasktracker.

      tasktracker:slave节点，运行map task, reduce task，并与jobtracker交互，汇报
                  任务状态。

      map task: 解析每条数据记录，传递给用户编写的map()并执行，将输出结果写入到本地磁盘
                （如果为map-only作业，则直接写入HDFS）

      reduce task:从map task执行结果中，远程读取输入数据，对数据进行排序，将数据分组
                  传递给用户编写的reduce函数执行。
   
</pre>

<pre>
hive:

      基于hadoop的数据仓库
      由facebook开源，最初用于解决海量结构化的日志数据统计问题。
      hive定义了一种类似于sql的查询语言hql,将sql转化为mapreduce任务在hadoop上执行。
</pre>

<pre>
hbase

      分布式列式数据库
      hbase是一个针对结构化数据的可伸缩，高可靠，高性能，分布式，面向列的动态模式数据库。和
      传统关系型数据库不同，hbase采用了bigtable的数据模型：增强了稀疏排序映射表
     （key/value）。其中键由行关键字，列关键字和时间戳构成，hbase提供了对大规模数据的随机
      ，实时读写访问，同时，hbase中保存的数据可以使用mapreduce来处理，它将数据存储与
      并行存储完美的结合在一起。
</pre>

<pre>
sqoop
 
     数据同步工具
     sqoop是sql-to-hadoop的缩写，只要用户传统数据库与hadoop之间传输数据。
     数据的导入和导出本质上是mapreduce程序，充分利用了MapReduce的并行化和容错性。
</pre>

<pre>
pig:
     
     基于hadoop的数据流系统
     定义了一种数据流语言pig latin，将脚本转换为mapreduce任务在hadoop上执行。
     通常用于离线分析。
</pre>

<pre>
mahout:

     数据挖掘算法库
     mahout的主要目的是创建一些可扩展的机器学习领域经典算法的实现，旨在帮助开发人员更加方便
     快捷的创建智能应用程序。mahout现在已经包含了聚类，分类，推荐引擎和频繁集挖掘等广发使用
     的数据挖掘方法，除了算法是，mahout还包含了数据的输入，输出工具，与其他存储系统集成等
     数据挖掘支持架构。
</pre>

<pre>
flume:

     日志收集工具
     cloudera开源的日志收集系统，具有分布式，高可靠，高容错，易于定制和扩展的特点。它将数据
     从产生，传输，处理并写入目标的路径抽象为数据流，在具体的数据流中，数据源支持在flume
     中定制数据发送方，从而支持数据集各种不同的协议数据。
</pre>

<pre>
YARN Mesos:

     随着互联网的高速发展，基于数据密集型应用的计算框架不断出现，从支持离线处理的mapreduce，
     到支持在线处理的storm，从迭代式计算框架到流式处理框架s4,...,在大部分互联网公司中，这几种
     框架可能都会采用，比如对于搜索引擎公司，可能的技术方法如下：网页建索引采用mapreduce框架，
     自然语言处理/数据挖掘采用spark，对性能要求高的数据挖掘算法用mpi等，公司一般将所有的这些
     框架部署到一个公共的集群中，让他们共享集群的资源，并对资源进行统一使用，这样便诞生了资源
     统一管理与调度平台，典型的代表是mesos和yarn
</pre>

<pre>
spark:

     spark是个开源的数据分析集群计算框架，建立在HDFS上，spark与hadoop一样，用于构建大规模，
     低延迟的数据分析应用，spark采用scala语言实现，使用scala作为应用框架。

     spark采用基于内存的分布式数据集，优化了迭代式的工作负载以及交互式查询。

     与hadoop不同的是，spark与scala紧密集成，scala对象管理本地collective对象那样管理分布式
     数据集，spark支持分布式数据集上的迭代式任务，实际上可以在hadoop文件系统上与hadoop一起运行。
</pre>

<pre>
storm:

     storm是一个分布式的，容错的计算系统，storm属于流处理平台，多用于实时计算更新数据库，storm
     也可以被用于连续计算，对于数据流做连续查询，在计算时将结果以流的形式输出给用户，他还可以被用
     于分布式RPC,以并行的方式运行昂贵的计算。
</pre>

<pre>
kafka:

     kafka是由apache软件基金会开发的一个开源流处理平台，由Scala和Java编写，Kafka是一种高吞吐
     量的分布式发布订阅消息系统，它可以处理大规模网站中的所有动作流数据，这种动作包括：网页浏览，
     搜索，和其他用户行动，是在现代网络上的许多社会功能的一个关键因素，这些数据通常是由于吞吐量
     的要求而通过处理日志和日志聚合来解决，对于像hadoop一样的日志数据和离线分析系统，但又要求
     实时处理的限制，这是一个可行的解决方案。kafka的目的是通过Hadoop的并行加载机制来统一线上
     和离线的消息处理，也是为了通过集群来提供实时的消息。
</pre>

<pre>
Oozie:

     引入：
         
         对于我们的工作，可能需要好几个hadoop作业（job）来协作完成，往往一个job的输出会被当做另
       一个job的输入来使用，这个时候就涉及到了数据流的处理。

       我们不可能就盯着程序，等它运行完再去运行下一个程序，所以，一般的做法就是通过shell来做，但
       是如果涉及到的工作流很复杂（比方说有1,2,3,4四个作业，1的输出作为2 3 4的输入，然后2 3的结
       果运算之后再和1的结果进行某种运算……最后再输出）是很费时费力的。这里就用到了oozie——一个能把
       多个MR作业组合为一个逻辑工作单元（一个工作流），从而自动完成任务调用的工具。
</pre>