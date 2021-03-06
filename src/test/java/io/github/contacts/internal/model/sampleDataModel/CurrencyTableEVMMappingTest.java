package io.github.contacts.internal.model.sampleDataModel;

import io.github.contacts.internal.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.contacts.service.dto.CurrencyTableDTO;

import static io.github.contacts.domain.enumeration.CurrencyLocality.FOREIGN;
import static io.github.contacts.domain.enumeration.CurrencyLocality.LOCAL;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTableEVMMappingTest {

    private Mapping<CurrencyTableEVM, CurrencyTableDTO> currencyTableEVMMapping;

    @BeforeEach
    void setUp() {
        currencyTableEVMMapping = new CurrencyTableEVMMappingImpl();
    }

    @Test
    void conversionToDTO() {

        CurrencyTableEVM evm1 = CurrencyTableEVM.builder().country("KENYA").currencyCode("KES").currencyName("SHILLING").locality("LOCAL").build();
        CurrencyTableEVM evm2 = CurrencyTableEVM.builder().country("UGANDA").currencyCode("UGX").currencyName("SHILLING").locality("FOREIGN").build();

        assertThat(currencyTableEVMMapping.toValue2(evm1).getLocality()).isEqualTo(LOCAL);
        assertThat(currencyTableEVMMapping.toValue2(evm2).getLocality()).isEqualTo(FOREIGN);
    }

    @Test
    void conversionToEVM() {

        CurrencyTableDTO dto1 = new CurrencyTableDTO();
        dto1.setCountry("KENYA");
        dto1.setCurrencyCode("KES");
        dto1.setCurrencyName("KENYA SHILLING");
        dto1.setLocality(LOCAL);

        CurrencyTableDTO dto2 = new CurrencyTableDTO();
        dto2.setCountry("UGANDA");
        dto2.setCurrencyCode("UGX");
        dto2.setCurrencyName("UGANDA SHILLING");
        dto2.setLocality(FOREIGN);

        assertThat(currencyTableEVMMapping.toValue1(dto1).getLocality()).isEqualTo("LOCAL");
        assertThat(currencyTableEVMMapping.toValue1(dto2).getLocality()).isEqualTo("FOREIGN");
    }
}
