package ma.emsi.ebank.dtos;

import lombok.Data;
import ma.emsi.ebank.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private Long id;
    private Date opDate;
    private double amount;
    private OperationType type;
    private String Description;
}
