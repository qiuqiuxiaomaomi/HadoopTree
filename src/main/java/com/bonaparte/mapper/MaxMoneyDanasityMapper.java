package com.bonaparte.mapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by yangmingquan on 2018/10/28.
 */
public class MaxMoneyDanasityMapper extends Mapper<LongWritable,
        Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = line.split(",");
        for(String item : words){
            context.write(new Text(item), new IntWritable());
        }
    }
}
