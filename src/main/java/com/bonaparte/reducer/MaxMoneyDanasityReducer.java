package com.bonaparte.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Created by yangmingquan on 2018/10/28.
 */
public class MaxMoneyDanasityReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

    public void reduce(Text text, Iterable<IntWritable> values){

    }
}
