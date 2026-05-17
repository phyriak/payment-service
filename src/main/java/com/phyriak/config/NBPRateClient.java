package com.phyriak.config;

import com.phyriak.dto.NbpResponse;
import com.phyriak.exceptions.CurrencyNotFoundError;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
@Getter
@Setter
public class NBPRateClient {
    private static String NBP_RATES_URI = "https://api.nbp.pl/api/exchangerates/rates/{table}/{code}/";
    private static String NBP_TABLE_TYPE = "A";

    private RestClient client;

    public NBPRateClient(RestClient.Builder builder) {
        this.client = builder.build();
    }

    public NbpResponse.Rate getCurrencyRate(String code) {
        return Objects.requireNonNull(client.get()
                        .uri(NBP_RATES_URI, NBP_TABLE_TYPE, code)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(NbpResponse.class))
                .rates()
                .stream()
                .findAny()
                .orElseThrow(()-> new CurrencyNotFoundError(""));
    }
}
