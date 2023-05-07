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

    // todo:
    //  1) 파일수신 잡의 가장 앞에는 항상 해당 파일이 도착했는지 확인하는 step 을 위치시키는게 어떨까?
    //  2) 여러 파일에 대해 동일한 배치잡을 돌려야하는데, 이건 어떻게 우아하게 할 수 있지?
    //    2-1) MultiResourceItemReader 사용?
    //     ㄴ https://prateek-ashtikar512.medium.com/spring-batch-read-from-multiple-flat-files-261305fe63c8
    //     ㄴ https://www.yawintutor.com/how-to-read-csv-file-in-spring-boot-batch/
    //     ㄴ https://renuevo.github.io/spring/batch/spring-batch-chapter-2/
    //    2-2) 동일 잡을 독립적인 스케쥴러를 통해 돌리고 파일 경로를 job parameter 로 받아 실행
    //  3) 반복 & 스킵 & 재시도를 해야할 요구사항이 있을까? 있다면 어떻게 할 수 있지?
    //  4) 병렬처리해야할 니즈가 있을까? 있다면 어떻게 할 수 있지?

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