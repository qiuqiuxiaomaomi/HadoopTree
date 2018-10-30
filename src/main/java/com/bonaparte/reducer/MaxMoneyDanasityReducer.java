package com.bonaparte.reducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by yangmingquan on 2018/10/28.
 */
public class MaxMoneyDanasityReducer extends Reducer<Text, IntWritable, Text, IntWritable>{

    @Override
    public void reduce(Text text, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for(IntWritable item : values){
            count += item.get();
        }
        context.write(text, new IntWritable(count));
    }
}
