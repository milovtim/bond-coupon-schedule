package ru.milovtim.bonds.pojo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import ru.tinkoff.invest.openapi.data.InstrumentType;
import ru.tinkoff.invest.openapi.data.MoneyAmount;

@Data
public class AccountPortfolio {
    private List<AccountPosition> positions;

    @Data
    public static class AccountPosition {
            private String figi;
            private String ticker;
            private String isin;
            private InstrumentType instrumentType;
            private BigDecimal balance;
            private Integer lots;
            private MoneyAmount expectedYield;
            private MoneyAmount  averagePositionPrice;
            private String name;
    }

}
