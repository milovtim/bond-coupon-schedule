package ru.milovtim.bonds;

public interface BondCoupon {
    String getId();

    String getDate();

    String getCouponValue();

    String getCouponNominalPart();

    String getCouponMarketPart();
}
