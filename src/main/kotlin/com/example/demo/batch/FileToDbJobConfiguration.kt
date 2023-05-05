package com.example.demo.batch

import com.example.demo.batch.entity.DBEntity
import com.example.demo.batch.entity.FileToDBDto
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.transform.FixedLengthTokenizer
import org.springframework.batch.item.file.transform.Range
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.persistence.EntityManagerFactory

@Configuration
class FileToDbJobConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory
) {
    private val log = KotlinLogging.logger {}

    @Bean
    fun fileToDbJob(): Job {
        return jobBuilderFactory.get("fileToDbJob")
            .start(fileToDbStep())
            .build()
    }

    @Bean
    fun fileToDbStep(): Step {
        return stepBuilderFactory.get("fileToDbStep")
            .chunk<FileToDBDto, DBEntity>(4)
            .reader(fileToDbItemReader())
            .processor(FileToDbItemProcessor())
            .writer(fileToDbItemWriter())
            .build()
    }

    @Bean
    fun fileToDbItemReader(): ItemReader<FileToDBDto> {
        val itemReader = FlatFileItemReader<FileToDBDto>().apply {
            setResource(ClassPathResource("test.txt"))
            setLinesToSkip(1)
        }
        val lineMapper = CustomJavaLineMapper<FileToDBDto>().apply {
            setLineTokenizer(
                FixedLengthTokenizer().apply {
                    setNames("partnerCode", "managementNumber", "sequenceNumber")
                    setColumns(Range(2, 4), Range(5, 14), Range(15, 24))
                }
            )
            setFieldSetMapper(
                BeanWrapperFieldSetMapper<FileToDBDto>().apply {
                    setTargetType(FileToDBDto::class.java)
                }
            )
        }
        itemReader.setLineMapper(lineMapper)
        itemReader.open(ExecutionContext())
        return itemReader
    }

    @Bean
    fun fileToDbItemWriter(): ItemWriter<DBEntity> {
        return JpaItemWriterBuilder<DBEntity>()
            .entityManagerFactory(entityManagerFactory)
            .usePersist(false) // todo true 로 쓸 수 있는 상황은?? => 항상 새로운 엔티티를 저장하는게 보장될때만!
            .build()
    }
}