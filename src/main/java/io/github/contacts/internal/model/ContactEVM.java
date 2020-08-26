package io.github.contacts.internal.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContactEVM implements Serializable {

    @ExcelRow
    private Long id;

    @ExcelCell(0)
    private String contactName;

    @ExcelCell(1)
    private String department;

    @ExcelCell(2)
    private String telephoneExtension;

    @ExcelCell(3)
    private String phoneNumber;
}
