# HadoopTree
Hadoop技术研究

<pre>
   Hadoop为用户提供了一个可靠的共享存储和分析系统
       1) HDFS实现数据的存储
       2) MapReduce实现数据的分析与处理

   MapReduce比较适合以批处理方式处理需要分析整个数据集的问题，尤其是动态分析。
         适合一次写入，多次读取数据的应用
		 半结构化数据比较松散，虽然可能有格式但是经常被忽略，MapReduce对非结构化（如图片）或者半结构化的数据非常有效，因为它是在处理数据时才对数据进行解释。	 
		      
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