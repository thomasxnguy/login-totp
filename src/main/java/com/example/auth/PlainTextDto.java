package com.example.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlainTextDto {

    @JsonProperty("recipients")
    @Valid
    @Size(max = 1)
    private List<Recipient> recipients;

    public PlainTextDto(String phoneNumber, String message, String... tags) {
        this.recipients = Collections.singletonList(new Recipient(new BigInteger(phoneNumber)));
        this.message = new Message(message);
        if (tags != null) {
            this.tags = new HashSet<>();
            this.tags.addAll(Arrays.asList(tags));
        }
    }

    @JsonProperty("message_type")
    private final int messageType = 206; // plain text message type

    @JsonProperty("message")
    @Valid
    @NotNull
    private Message message;

    @JsonProperty("kpi_tag")
    private Set<String> tags;

    @Data
    @AllArgsConstructor
    private static class Recipient {
        @NotNull
        @Min(1)
        @JsonProperty("phone_number")
        private BigInteger phone_number;
    }

    @Data
    @AllArgsConstructor
    private static class Message {
        @NotEmpty
        @JsonProperty("#txt")
        private String text;

        @JsonProperty("#tracking_data")
        private final String trackingData = UUID.randomUUID().toString();
    }

}

