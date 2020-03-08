package ru.milovtim.bonds.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.*;
import java.util.UUID;
import java.util.stream.IntStream;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.junit.jupiter.api.Test;

class CalendarExporterServiceTest {

    @Test
    void testCreatingCal() throws IOException {

        Calendar cal = new Calendar();
        cal.getProperties().add(new ProdId("-//Mixed Forest LTD//Bonds schedule exporter//RU"));
        cal.getProperties().add(Version.VERSION_2_0);

        IntStream.range(1, 4).forEach(value -> addEvent(cal, value, 0, 0));

        CalendarOutputter out = new CalendarOutputter();
        OutputStream baos = new FileOutputStream("test.ics");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        out.output(cal, baos);
//        System.out.println(new String(baos.toByteArray()));
    }

    private void addEvent(Calendar cal, int year, int month, int dayOfMonth) {
        ZonedDateTime zdtStart = ZonedDateTime.of(year, month, dayOfMonth,
            0, 0, 0, 0, ZoneId.of("Europe/Moscow"));

        Date date = new Date(zdtStart.toInstant().toEpochMilli());
        VEvent evt = new VEvent();
        evt.getProperties().add(new DtStart(date, false));
        evt.getProperties().add(new Uid(UUID.randomUUID().toString()));
        evt.getProperties().add(new Summary("Test event " + year));
        evt.getProperties().add(new Description("This is the test event description that may contain details about bond coupons payments"));
        cal.getComponents().add(evt);
    }
}