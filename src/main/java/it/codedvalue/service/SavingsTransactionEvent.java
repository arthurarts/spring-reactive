package it.codedvalue.service;


import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SavingsTransactionEvent {
    private long id;
    private BigDecimal amount;
    private Date transactionDate;

}
