package com.techfoot.stockspree.InboundAdaptors.REST.Product.GetSingleProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Input_GetSingleProductIA {
    @JsonProperty("method")
    private String method;

    @JsonProperty("action")
    private String action;

    @JsonProperty("apiVersion")
    private String apiVersion;

    @NotNull(message = "Product code is required")
    @NotBlank(message = "Product code cannot be null or empty")
    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;    
} 