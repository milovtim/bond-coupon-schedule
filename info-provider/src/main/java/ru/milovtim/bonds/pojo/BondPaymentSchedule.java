package ru.milovtim.bonds.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.invest.openapi.models.MoneyAmount;

@Data
public class BondPaymentSchedule {
    
    List<BondPayment> payments;

    @Data
    @NoArgsConstructor()
    public static class BondPayment {
        LocalDate paymentDate;
        MoneyAmount couponValue;
        BigDecimal annualInterest;
        BigDecimal nominalPart;
        BigDecimal marketPart;
    }
}

