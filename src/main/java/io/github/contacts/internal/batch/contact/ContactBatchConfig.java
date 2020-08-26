package io.github.contacts.internal.batch.contact;

import com.google.common.collect.ImmutableList;
import io.github.contacts.config.FileUploadsProperties;
import io.github.contacts.internal.Mapping;
import io.github.contacts.internal.excel.ExcelFileDeserializer;
import io.github.contacts.internal.model.ContactEVM;
import io.github.contacts.internal.service.BatchService;
import io.github.contacts.service.ContactsFileUploadService;
import io.github.contacts.service.dto.ContactDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ContactBatchConfig {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<ContactEVM> deserializer;

    @Autowired
    private ContactsFileUploadService fileUploadService;

    @Autowired
    private FileUploadsProperties fileUploadsProperties;

    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @Value("#{jobParameters['startUpTime']}")
    private static long startUpTime;

    @Autowired
    private JobExecutionListener persistenceJobListener;

    @Autowired
    private BatchService<ContactDTO> contactBatchService;

    @Autowired
    private Mapping<ContactEVM, ContactDTO> mapping;


    @Bean("contactPersistenceJob")
    public Job contactPersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("contactPersistenceJob")
            .preventRestart()
            .listener(persistenceJobListener)
            .incrementer(new RunIdIncrementer())
            .flow(readContactListFromFile())
            .end()
            .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<ContactDTO>> contactItemWriter() {

        return items -> items.stream().peek(contactBatchService::save).forEach(contactBatchService::index);
    }

    @Bean
    public ItemProcessor<List<ContactEVM>, List<ContactDTO>> contactItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean("readContactListFromFile")
    public Step readContactListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readContactListFromFile")
            .<List<ContactEVM>, List<ContactDTO>>chunk(2)
            .reader(contactListItemReader(fileId))
            .processor(contactItemProcessor())
            .writer(contactItemWriter())
            .build();
        // @formatter:off
    }

    @Bean("contactListItemReader")
    @JobScope
    public ContactListItemReader contactListItemReader(@Value("#{jobParameters['fileId']}") long fileId) {

        return new ContactListItemReader(deserializer, fileUploadService, fileId, fileUploadsProperties);
    }
}
