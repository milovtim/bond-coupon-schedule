package ru.milovtim.bonds.service;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.milovtim.bonds.module.ThirdPartyConfig;
import ru.milovtim.bonds.pojo.BondPaymentSchedule;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BondScrappingServiceImplTest {

    BondScrappingServiceImpl service;

    @Mock SmartLabHttpClient client;
    @Mock ThirdPartyConfig config;

    @BeforeEach
    void setUp() {
        service = new BondScrappingServiceImpl(client, config);
    }

    @Test
    void getBondSchedule() throws Exception {
        when(config.getSmartlabHost()).thenReturn("http://hello.com");
        when(client.getBondPage(anyString())).thenReturn(Observable.just(getRespBodyFromFile()));
        BondPaymentSchedule schedule = service.getBondSchedule("hello");
        assertNotNull(schedule);
    }

    @NotNull
    private ResponseBody getRespBodyFromFile() throws IOException {
        InputStream httpRespInFile = getClass().getResourceAsStream("/smartlab-response.html");
        return ResponseBody.create(MediaType.get("text/html"), ByteStreams.toByteArray(httpRespInFile));
    }

    @Test
    void playWithJsoup() throws Exception {
        Document doc = Jsoup.parse(getRespBodyFromFile().byteStream(), UTF_8.name(), "https://smart-lab.ru");
 (th       assertNotNull(doc);
    }
}