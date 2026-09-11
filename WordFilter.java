package basic;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;


public class WordFilter {

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        Configuration c = new Configuration();
        String[] files = new GenericOptionsParser(c, args).getRemainingArgs();

        // arquivo de entrada
        Path input = new Path(files[0]);

        // arquivo de saida
        Path output = new Path(files[1]);

        // criacao do job e seu nome
        Job j = new Job(c, "wordcount");

        //class registries
        // main
        j.setJarByClass(WordFilter.class);
        // mapper
        j.setMapperClass(MapForWordCount.class);
        // reduce
        j.setReducerClass(ReduceForWordCount.class);
        // define the IO types
        // Map
        j.setMapOutputKeyClass(Text.class);
        j.setMapOutputValueClass(IntWritable.class);

        // Reduce
        j.setOutputKeyClass(Text.class);
        j.setOutputValueClass(IntWritable.class);

        // Create and Map the IO files
        FileInputFormat.addInputPath(j,input);
        FileOutputFormat.setOutputPath(j,output);
        // launch Job
        System.exit(j.waitForCompletion(true)?0:1);
    }

    public static class MapForWordCount extends Mapper<LongWritable, Text, Text, IntWritable> {

        public void map(LongWritable key, Text value, Context con)
                throws IOException, InterruptedException {
            String line = value.toString();

            String[] words = line.split(" ");
            //output ex: (key,value) ---> (text,Int)

            for (String w: words) {
                // add the word filter.
                if (w.trim().equalsIgnoreCase("for")) {
                    Text tup_key = new Text(w);
                    IntWritable tup_value = new IntWritable(1);
                    con.write(tup_key,tup_value); // "writes" the data on network, converts to bin
                }
            }

            }
        }

    public static class ReduceForWordCount extends Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterable<IntWritable> values, Context con)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable v:values){
                sum += v.get();
            }
            con.write(key, new IntWritable(sum));
        }
    }

}
