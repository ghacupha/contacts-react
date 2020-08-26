package io.github.contacts.internal.batch.contact;

import io.github.contacts.config.FileUploadsProperties;
import io.github.contacts.internal.batch.ListPartition;
import io.github.contacts.internal.batch.currencyTable.CurrencyTableListItemReader;
import io.github.contacts.internal.excel.ExcelFileDeserializer;
import io.github.contacts.internal.model.ContactEVM;
import io.github.contacts.internal.model.sampleDataModel.CurrencyTableEVM;
import io.github.contacts.service.ContactsFileUploadService;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

@Scope("job")
public class ContactListItemReader implements ItemReader<List<ContactEVM>> {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CurrencyTableListItemReader.class);
    private final FileUploadsProperties fileUploadsProperties;

    private final ExcelFileDeserializer<ContactEVM> deserializer;
    private final ContactsFileUploadService fileUploadService;
    private long fileId;

    private ListPartition<ContactEVM> contactEVMPartition;

    ContactListItemReader(final ExcelFileDeserializer<ContactEVM> deserializer, final ContactsFileUploadService fileUploadService, @Value("#{jobParameters['fileId']}") long fileId,
                                final FileUploadsProperties fileUploadsProperties) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;
        this.fileUploadsProperties = fileUploadsProperties;
    }

    @PostConstruct
    private void resetIndex() {

        final List<ContactEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        contactEVMPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

        log.info("Contact table items deserialized : {}", unProcessedItems.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Every time this method is called, it will return a List of unprocessed items the size of which is dictated by the maximumPageSize;
     * <p>
     * Once the list of unprocessed items hits zero, the method call will return null;
     * </p>
     */
    @Override
    public List<ContactEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<ContactEVM> forProcessing = contactEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
