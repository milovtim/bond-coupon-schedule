package ru.milovtim.bonds.service;

import ru.milovtim.bonds.pojo.BondPaymentSchedule;

public interface BondScrappingService {

    void getBondData(String isin);

    BondPaymentSchedule getBondSchedule(String isin);
}
