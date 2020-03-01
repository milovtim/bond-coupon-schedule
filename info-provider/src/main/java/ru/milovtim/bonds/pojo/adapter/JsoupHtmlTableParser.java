package ru.milovtim.bonds.pojo.adapter;

import java.util.Collection;

import org.jsoup.nodes.Document;

public abstract class JsoupHtmlTableParser<P, I> {
    private final Document htmlTableDoc;

    public JsoupHtmlTableParser(Document htmlTableDoc) {
        this.htmlTableDoc = htmlTableDoc;
    }

    public Document getHtmlTableDoc() {
        return htmlTableDoc;
    }

    public abstract Collection<? extends P> getPayments();

    public abstract I getInfo();
}
