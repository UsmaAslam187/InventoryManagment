package com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Valid
@Getter
public class Input_CreateProductIA {

    @JsonProperty("products")
    @NotNull(message = "Products is required")
    @Size(min = 1, message = "At least one product is required")
    @Valid
    private List<Product> products;
    @JsonProperty("operation")
    private String operation;

    public List<String> validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Input_CreateProductIA>> violations = validator.validate(this);

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<Input_CreateProductIA> violation : violations) {
            errors.add("Field '" + violation.getPropertyPath() + "': " + violation.getMessage());
        }

        // Also validate each product in the list
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                List<String> productErrors = product.validate();
                for (String error : productErrors) {
                    errors.add("products[" + i + "]." + error);
                }
            }
        }

        return errors;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
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


