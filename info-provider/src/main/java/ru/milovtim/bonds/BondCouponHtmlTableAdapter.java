package ru.milovtim.bonds;

import org.jsoup.nodes.Element;

public class BondCouponHtmlTableAdapter implements BondCoupon {

    private Element tableRowElement;

    public BondCouponHtmlTableAdapter(Element tableRowElement) {
        this.tableRowElement = tableRowElement;
    }

    private String childrenTextByIdx(int index) {
        return tableRowElement.children().get(index).text();
    }

    @Override
    public String getId() {
        return childrenTextByIdx(0);
    }

    @Override
    public String getDate() {
        return childrenTextByIdx(1);
    }

    @Override
    public String getCouponValue() {
        return childrenTextByIdx(2);
    }

    @Override
    public String getCouponNominalPart() {
        return childrenTextByIdx(3);
    }

    @Override
    public String getCouponMarketPart() {
        return childrenTextByIdx(4);
    }
}
