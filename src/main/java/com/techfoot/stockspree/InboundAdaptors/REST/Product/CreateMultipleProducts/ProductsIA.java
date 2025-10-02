package com.techfoot.stockspree.InboundAdaptors.REST.Product.CreateMultipleProducts;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsIA {

    @JsonProperty("products")
    private List<Product> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        @JsonProperty("name")
        @NotNull(message = "Name is required")
        @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
        private String name;

        @JsonProperty("code")
        @NotBlank(message = "Code is required")
        @Size(min = 1, max = 100, message = "Code must be between 1 and 100 characters")
        private String code;

        @JsonProperty("price")
        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be greater than or equal to 0")
        private Double price;

        @JsonProperty("tax")
        @Size(max = 100, message = "Tax must not exceed 100 characters")
        private String tax;

        @JsonProperty("type")
        @NotNull(message = "Type is required")
        @Size(max = 100, message = "Type must not exceed 100 characters")
        private String type;

        @JsonProperty("salesAccount")
        @Min(value = 0, message = "Sales account must be greater than or equal to 0")
        private Integer salesAccount;

        @JsonProperty("purchaseAccount")
        @Min(value = 0, message = "Purchase account must be greater than or equal to 0")
        private Integer purchaseAccount;

        public List<String> validate() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Product>> violations = validator.validate(this);

            List<String> errors = new ArrayList<>();
            for (ConstraintViolation<Product> violation : violations) {
                errors.add("Field '" + violation.getPropertyPath() + "': " + violation.getMessage());
            }
            return errors;
        }
    }
}