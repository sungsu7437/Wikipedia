package kr.ac.kookmin.cs.bigdata;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WikiProject extends Configured implements Tool {
	public static final String[] year = { "2001","2002","2003","2004","2005", "2006",	"2007", "2008", 
		"2009", "2010", "2011", 		"2012", "2013", "2014", "2015", "2016" };	//arrayString of years which i need
	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));
		int res = ToolRunner.run(new Configuration(), new WikiProject(), args);

		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));

		Job job = Job.getInstance(getConf());
		job.setJarByClass(WikiProject.class);

		job.setMapperClass(Map.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setPartitionerClass(DatePartitioner.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(16);

		job.setInputFormatClass(TextInputFormat.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);

		return 0;
	}

	public static class Map extends
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable ONE = new IntWritable(1);
		private Text title = new Text();
		private String temp = "";

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] token = value.toString().split("\\s+");

			for (int i = 1; i < token.length; i++) {
				temp = "";
				temp += token[0]; // token[0] is always title
				temp += "\t";
				String[] month_token = token[i].toString().split("-");

				for (int j = 0; j < month_token.length - 1; j++)					// time data form is 2012-09-20T08:56:13Z so i split them					
					temp += month_token[j].toString();														//       with "-" and i just need year and month so take 2012 09

				title.set(temp);
				context.write(title, ONE); // data form ex) HelloWiki_201604
			}
		}
	}

	public static class Reduce extends
			Reducer<Text, IntWritable, Text, IntWritable> {
		private Text w = new Text();

		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	public static class DatePartitioner extends Partitioner<Text, IntWritable> {
		@Override
		public int getPartition(Text key, IntWritable value, int numReduceTasks) {
			String[] str = key.toString().split("\t");		
			int date = Integer.parseInt(str[1]);
			date /= 100;
			date %= 100;
			date -= 1;			//start year is 2001 so i change it 0, so i can start num 0

			if (numReduceTasks == 0) {
				return 0;
			}
			for (String token : year) {	//data's year start from 2001 to 2016
				if (str[1].startsWith(token))	//str[0] is title and str[1] is date ex(201601, i just need year part so use startWith
					return date;
			}
			return 0;
		}
	}
}