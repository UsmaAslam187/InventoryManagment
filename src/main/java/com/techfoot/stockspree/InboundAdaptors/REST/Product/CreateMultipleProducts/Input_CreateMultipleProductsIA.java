package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Input_CreateMultipleProductsIA {
    @JsonProperty("data")
    private ProductData data;

    @JsonProperty(value = "operation", defaultValue = "")
    private String operation;

    @JsonProperty("processInstanceId")
    private Integer processInstanceId; // sent from scheduler

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ProductData {

        @JsonProperty("productsData")
        private String productsData; // Used in the first JSON format

        @JsonProperty("products")
        private String products; // sent from scheduler

        @JsonProperty("workspace")
        private String workspace; // sent from scheduler

        @JsonProperty("sheduleTime")
        private ScheduleTime scheduleTime; // sent by a user
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ScheduleTime {
        private String date;
        private String time;
    }

    public String getOperation() {
        return operation != null ? operation : "";
    }
}
