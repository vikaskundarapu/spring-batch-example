package com.vikas.springboot;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.vikas.springboot.listener.JobCompletionNotificationListener;
import com.vikas.springboot.model.Leader;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Leader> reader() {
		return new FlatFileItemReaderBuilder<Leader>().name("leaderItemReader")
				.resource(new ClassPathResource("sample-data.csv")).delimited().names("firstName", "lastName")
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Leader>() {
					{
						setTargetType(Leader.class);
					}
				}).build();
	}

	@Bean
	public LeaderItemProcessor processor() {
		return new LeaderItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Leader> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Leader>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO LEADERS (first_name, last_name) VALUES (:firstName, :lastName)")
				.dataSource(dataSource).build();
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step1)
				.end().build();
	}

	@Bean
	public Step step1(JdbcBatchItemWriter<Leader> writer) {
		return stepBuilderFactory.get("step1").<Leader, Leader>chunk(10).reader(reader()).processor(processor())
				.writer(writer).build();
	}
}
