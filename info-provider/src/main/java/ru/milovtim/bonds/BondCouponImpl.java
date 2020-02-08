package ru.milovtim.bonds;

public class BondCouponImpl implements BondCoupon {
    private String id;
    private String date;
    private String couponValue;
    private String couponNominalPart;
    private String couponMarketPart;

    public BondCouponImpl(BondCoupon original) {
        id = original.getId();
        date = original.getDate();
        couponValue = original.getCouponValue();
        couponNominalPart = original.getCouponNominalPart();
        couponMarketPart = original.getCouponMarketPart();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getCouponValue() {
        return couponValue;
    }

    @Override
    public String getCouponNominalPart() {
        return couponNominalPart;
    }

    @Override
    public String getCouponMarketPart() {
        return couponMarketPart;
    }

    @Override
    public String toString() {
        return "BondCouponImpl{" +
            "id='" + id + '\'' +
            ", date='" + date + '\'' +
            ", couponValue='" + couponValue + '\'' +
            ", couponNominalPart='" + couponNominalPart + '\'' +
            ", couponMarketPart='" + couponMarketPart + '\'' +
            '}';
    }
}
