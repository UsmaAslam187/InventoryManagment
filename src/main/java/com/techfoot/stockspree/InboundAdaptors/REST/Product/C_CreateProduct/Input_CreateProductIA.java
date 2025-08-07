package com.techfoot.stockspree.InboundAdaptors.REST.Product.C_CreateProduct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.constraints.*;
import jakarta.validation.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Input_CreateProductIA {

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 100, message = "Code must be between 1 and 100 characters")
    private String code;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @Size(max = 100, message = "Tax must not exceed 100 characters")
    private String tax;

    @NotNull(message = "Type is required")
    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String type;

    @NotNull(message = "Sales account is required")
    @Min(value = 0, message = "Sales account must be greater than or equal to 0")
    private Integer salesAccount;

    @NotNull(message = "Purchase account is required")
    @Min(value = 0, message = "Purchase account must be greater than or equal to 0")
    private Integer purchaseAccount;

    /**
     * Validates the current object instance using Bean Validation
     */
    public List<String> validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Input_CreateProductIA>> violations = validator.validate(this);

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<Input_CreateProductIA> violation : violations) {
            errors.add("Field '" + violation.getPropertyPath() + "': " + violation.getMessage());
        }
        return errors;
    }

    public static List<String> validateRequest(String requestBody) {
        List<String> errors = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
    /**
     * Static method to validate a JSON request string against this class
     */        Input_CreateProductIA input = objectMapper.readValue(requestBody, Input_CreateProductIA.class);
            errors = input.validate();

        } catch (UnrecognizedPropertyException e) {
            errors.add("Field '" + e.getPropertyName() + "': Unknown field");

        } catch (MismatchedInputException e) {
            String fieldName = extractFieldFromPath(e.getPath());
            String expectedType = getExpectedType(e.getTargetType());
            if (fieldName != null && expectedType != null) {
                errors.add("Field '" + fieldName + "': Invalid data type. Expected " + expectedType);
            } else {
                errors.add("Invalid data type in request");
            }

        } catch (JsonProcessingException e) {
            errors.add("Invalid JSON format: " + e.getOriginalMessage());

        }catch (Exception e) {
            errors.add("Unexpected error: " + e.getMessage());
        }

        return errors;
    }

    private static String extractFieldFromPath(List<JsonMappingException.Reference> path) {
        if (path != null && !path.isEmpty()) {
            return path.get(0).getFieldName();
        }
        return "unknown";
    }

    private static String getExpectedType(Class<?> targetType) {
        if (targetType == null) return "valid type";
        if (targetType == String.class) return "string";
        if (targetType == Integer.class || targetType == int.class) return "integer";
        if (targetType == Double.class || targetType == double.class) return "number";
        if (targetType == Boolean.class || targetType == boolean.class) return "boolean";
        return targetType.getSimpleName().toLowerCase();
    }
}
