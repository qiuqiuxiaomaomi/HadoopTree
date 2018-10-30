package com.bonaparte;

import com.bonaparte.mapper.MaxMoneyDanasityMapper;
import com.bonaparte.reducer.MaxMoneyDanasityReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by yangmingquan on 2018/10/29.
 */
public class HadoopApplication {
    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(HadoopApplication.class);
        job.setMapperClass(MaxMoneyDanasityMapper.class);
        job.setReducerClass(MaxMoneyDanasityReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setCombinerClass(MaxMoneyDanasityReducer.class);

        FileInputFormat.setInputPaths(job, "E:\\tmp\\input.txt");
        FileOutputFormat.setOutputPath(job, new Path("E:\\tmp\\output.txt"));

        boolean b = job.waitForCompletion(true);
        System.exit(b?0:1);
    }
}
