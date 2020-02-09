package ru.milovtim.bonds.pojo;

import java.util.List;

import lombok.Data;

@Data
public class UserAccounts {
    private List<UserAccount> accounts;

    @Data
    public static class UserAccount {
        private String brokerAccountType;
        private String brokerAccountId;
    }
}
