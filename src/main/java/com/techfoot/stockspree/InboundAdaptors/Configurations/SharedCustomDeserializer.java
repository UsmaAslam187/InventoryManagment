package com.techfoot.stockspree.InboundAdaptors.Configurations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Component
public class SharedCustomDeserializer {
 private final Map<Class<?>, List<FieldMetadata>> fieldMetadataCache = new ConcurrentHashMap<>();

    /**
     * Main deserialization method that handles any input type
     * @param jsonString - The JSON string to deserialize
     * @param targetClass - The target class to deserialize to
     * @param objectMapper - Jackson ObjectMapper instance
     * @return Deserialized object with validation errors
     */
    public <T> DeserializationResult<T> deserialize(String jsonString, Class<T> targetClass, ObjectMapper objectMapper) {
        List<String> validationErrors = new ArrayList<>();
        
        try {
            objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, 
                false
            );
            T result = objectMapper.readValue(jsonString, targetClass);
            
            return new DeserializationResult<>(result, validationErrors, false);
            
        } catch (Exception exception) {handleException(exception, jsonString, validationErrors, objectMapper, targetClass);
            return new DeserializationResult<>(null, validationErrors, true);
        }
    }
    private void handleException(Exception exception, String jsonString, List<String> validationErrors, 
                               ObjectMapper objectMapper, Class<?> targetClass) {
        if (exception instanceof InvalidFormatException) {
            handleInvalidFormatException((InvalidFormatException) exception, validationErrors);
            // For InvalidFormatException, we need to continue with manual parsing to find all errors
            // especially for array elements
            manualParseWithErrorCollection(jsonString, validationErrors, objectMapper, targetClass);
        } else if (exception instanceof UnrecognizedPropertyException) {
            handleUnrecognizedPropertyException((UnrecognizedPropertyException) exception, validationErrors);
        } else if (exception instanceof MismatchedInputException) {
            handleMismatchedInputException((MismatchedInputException) exception, validationErrors);
            // For MismatchedInputException, we need to continue with manual parsing to find all errors
            // especially for array elements
            manualParseWithErrorCollection(jsonString, validationErrors, objectMapper, targetClass);
        } else if (exception instanceof JsonProcessingException) {
            handleJsonProcessingException((JsonProcessingException) exception, validationErrors);
        } else {
            manualParseWithErrorCollection(jsonString, validationErrors, objectMapper, targetClass);
        }
    }

    private void handleInvalidFormatException(InvalidFormatException exception, List<String> validationErrors) {
        String fieldPath = extractFieldPath(exception.getPath());
        String expectedType = getExpectedType(exception.getTargetType());
        validationErrors.add(
                "Field '" + fieldPath + "': Invalid data type. Expected " + expectedType);
        
        // For array-related errors, we need to continue parsing to find all errors
        // The exception path will tell us if this is an array element error
        if (fieldPath.contains("[")) {
            // This is an array element error, we should continue parsing to find more errors
            // The manual parsing will be triggered by the catch block in deserialize method
        }
    }
    private void handleUnrecognizedPropertyException(UnrecognizedPropertyException exception, List<String> validationErrors) {
        // Skip unknown fields instead of adding them as errors
    }
    private void handleMismatchedInputException(MismatchedInputException exception, List<String> validationErrors) {
        String fieldPath = extractFieldPath(exception.getPath());
        String expectedType = getExpectedType(exception.getTargetType());
        validationErrors.add(
            "Field '" + fieldPath + "': Invalid data type. Expected " + expectedType
        );
        
        // For array-related errors, we need to continue parsing to find all errors
        // The exception path will tell us if this is an array element error
        if (fieldPath.contains("[")) {
            // This is an array element error, we should continue parsing to find more errors
            // The manual parsing will be triggered by the catch block in deserialize method
        }
    }

    private void handleJsonProcessingException(JsonProcessingException exception, List<String> validationErrors) {
        validationErrors.add(
            "Invalid JSON format: " + exception.getOriginalMessage()
        );
    }

    private void manualParseWithErrorCollection(String jsonString, List<String> validationErrors, 
                                              ObjectMapper objectMapper, Class<?> targetClass) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            validateJsonStructure(jsonNode, validationErrors, targetClass);
        } catch (Exception e) {
            validationErrors.add("JSON parsing failed: " + e.getMessage());
        }
    }

    /**
     * Validate JSON structure and collect errors using target class metadata
     */
    private void validateJsonStructure(JsonNode node, List<String> validationErrors, Class<?> targetClass) {
        validateJsonStructure(node, validationErrors, "", targetClass);
    }

    private void validateJsonStructure(JsonNode node, List<String> validationErrors, String path, Class<?> targetClass) {
        if (node.isObject()) {
            // Get field metadata for the target class
            List<FieldMetadata> fieldMetadataList = getFieldMetadata(targetClass);
            
            java.util.Iterator<String> fields = node.fieldNames();
            while (fields.hasNext()) {
                String fieldName = fields.next();
                String fieldPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                JsonNode fieldNode = node.get(fieldName);
                
                // Find corresponding field metadata
                FieldMetadata fieldMetadata = findFieldMetadata(fieldName, fieldMetadataList);
                
                // Validate field based on metadata
                validateField(fieldName, fieldNode, fieldPath, validationErrors, fieldMetadata);
                
                // If this is a List field and contains objects, validate each element
                if (fieldMetadata != null && List.class.isAssignableFrom(fieldMetadata.getFieldType()) && fieldNode.isArray()) {
                    validateListElements(fieldNode, fieldPath, validationErrors, fieldMetadata);
                }
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                String elementPath = path + "[" + i + "]";
                validateJsonStructure(node.get(i), validationErrors, elementPath, targetClass);
            }
        }
    }

    /**
     * Validate elements in a List field
     */
    private void validateListElements(JsonNode arrayNode, String arrayPath, List<String> validationErrors, FieldMetadata fieldMetadata) {
        // Get the generic type of the List if possible
        Class<?> elementType = getListElementType(fieldMetadata);
        
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode elementNode = arrayNode.get(i);
            String elementPath = arrayPath + "[" + i + "]";
            
            try {
                if (elementNode.isObject()) {
                    // Validate each object in the array using the element type
                    // If elementType is Object.class, try to infer from the field metadata
                    Class<?> typeToUse = elementType;
                    if (elementType.equals(Object.class) && fieldMetadata.getElementType() != null) {
                        typeToUse = fieldMetadata.getElementType();
                    }
                    if (typeToUse != null && !typeToUse.equals(Object.class)) {
                        validateJsonStructure(elementNode, validationErrors, elementPath, typeToUse);
                    } else {
                        // Fallback: validate as generic object
                        validateGenericObject(elementNode, elementPath, validationErrors);
                    }
                } else {
                    // Validate primitive array elements
                    validateField("element", elementNode, elementPath, validationErrors, 
                        new FieldMetadata("element", elementType != null ? elementType : Object.class, null, false, 0, 0, null));
                }
            } catch (Exception e) {
                // If validation fails for this element, log the error but continue with other elements
                validationErrors.add("Field '" + elementPath + "': Validation failed - " + e.getMessage());
            }
        }
       }

    /**
     * Validate a generic object when we don't know its specific type
     */
    private void validateGenericObject(JsonNode objectNode, String objectPath, List<String> validationErrors) {
        if (objectNode.isObject()) {
            java.util.Iterator<String> fields = objectNode.fieldNames();
            while (fields.hasNext()) {
                String fieldName = fields.next();
                JsonNode fieldNode = objectNode.get(fieldName);
                String fieldPath = objectPath + "." + fieldName;
                
                // Basic type validation for unknown fields
                if (fieldNode.isTextual()) {
                    // String field - no specific validation needed
                } else if (fieldNode.isNumber()) {
                    // Number field - no specific validation needed
                } else if (fieldNode.isBoolean()) {
                    // Boolean field - no specific validation needed
                } else if (fieldNode.isNull()) {
                    // Null field - no specific validation needed
                } else if (fieldNode.isArray()) {
                    // Array field - validate as generic array
                    validateGenericArray(fieldNode, fieldPath, validationErrors);
                } else if (fieldNode.isObject()) {
                    // Nested object - validate recursively
                    validateGenericObject(fieldNode, fieldPath, validationErrors);
                }
            }
        }
    }

    /**
     * Validate a generic array when we don't know its element type
     */
    private void validateGenericArray(JsonNode arrayNode, String arrayPath, List<String> validationErrors) {
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode elementNode = arrayNode.get(i);
            String elementPath = arrayPath + "[" + i + "]";
            
            if (elementNode.isObject()) {
                validateGenericObject(elementNode, elementPath, validationErrors);
            } else if (elementNode.isArray()) {
                validateGenericArray(elementNode, elementPath, validationErrors);
            }
            // For primitive elements, no specific validation needed
        }
    }

    /**
     * Get the element type of a List field
     */
    private Class<?> getListElementType(FieldMetadata fieldMetadata) {
        if (fieldMetadata != null && fieldMetadata.getElementType() != null) {
            return fieldMetadata.getElementType();
        }
        return Object.class;
    }

    /**
     * Find field metadata by JSON property name
     */
    private FieldMetadata findFieldMetadata(String jsonFieldName, List<FieldMetadata> fieldMetadataList) {
        for (FieldMetadata metadata : fieldMetadataList) {
            if (metadata.getJsonPropertyName().equals(jsonFieldName)) {
                return metadata;
            }
        }
        return null;
    }

    /**
     * Validate individual field using field metadata
     */
    private void validateField(String fieldName, JsonNode fieldNode, String fieldPath, 
                             List<String> validationErrors, FieldMetadata fieldMetadata) {
        
        try {
            if (fieldMetadata == null) {
                return;
            }
            if (fieldMetadata.isRequired() && (fieldNode.isNull() || fieldNode.isMissingNode())) {
                validationErrors.add("Field '" + fieldPath + "': Required field is missing");
                return;
            }
            if (fieldNode.isNull() || fieldNode.isMissingNode()) {
                return;
            }
            Class<?> expectedType = fieldMetadata.getFieldType();
            boolean isValid = validateFieldType(fieldNode, expectedType, fieldMetadata);
            
            if (!isValid) {
                String expectedTypeName = getExpectedType(expectedType);
                String actualType = getActualType(fieldNode);
                validationErrors.add(
                    "Field '" + fieldPath + "': Invalid data type. Expected " + expectedTypeName + ", got " + actualType
                );
            }

            // Validate string length constraints
            if (fieldNode.isTextual() && fieldMetadata.getMaxLength() > 0) {
                String value = fieldNode.asText();
                if (value.length() > fieldMetadata.getMaxLength()) {
                    validationErrors.add(
                        "Field '" + fieldPath + "': Length exceeds maximum of " + fieldMetadata.getMaxLength() + " characters"
                    );
                }
            }

            if (fieldNode.isTextual() && fieldMetadata.getMinLength() > 0) {
                String value = fieldNode.asText();
                if (value.length() < fieldMetadata.getMinLength()) {
                    validationErrors.add(
                        "Field '" + fieldPath + "': Length must be at least " + fieldMetadata.getMinLength() + " characters"
                    );
                }
            }

            // Validate numeric constraints
            if (fieldNode.isNumber() && fieldMetadata.getMinValue() != null) {
                double value = fieldNode.asDouble();
                if (value < fieldMetadata.getMinValue()) {
                    validationErrors.add(
                        "Field '" + fieldPath + "': Value must be at least " + fieldMetadata.getMinValue()
                    );
                }
            }
        } catch (Exception e) {
            // If validation fails for this field, log the error but continue with other fields
            validationErrors.add("Field '" + fieldPath + "': Validation failed - " + e.getMessage());
        }
    }

    /**
     * Validate field type against expected type
     */
    private boolean validateFieldType(JsonNode fieldNode, Class<?> expectedType, FieldMetadata fieldMetadata) {
        if (expectedType == String.class) {
            return fieldNode.isTextual() || isValidStringConversion(fieldNode);
        } else if (expectedType == Integer.class || expectedType == int.class) {
            return fieldNode.isNumber() || isValidIntegerString(fieldNode);
        } else if (expectedType == Double.class || expectedType == double.class) {
            return fieldNode.isNumber() || isValidNumberString(fieldNode);
        } else if (expectedType == Boolean.class || expectedType == boolean.class) {
            return fieldNode.isBoolean();
        } else if (expectedType == List.class) {
            return fieldNode.isArray();
        } else if (expectedType.isEnum()) {
            return fieldNode.isTextual() && isValidEnumValue(fieldNode.asText(), expectedType);
        }
        
        // For complex objects, allow object or null
        return fieldNode.isObject() || fieldNode.isNull();
    }

    /**
     * Check if node can be converted to string
     */
    private boolean isValidStringConversion(JsonNode node) {
        return node.isNumber() || node.isBoolean();
    }

    /**
     * Check if node is a valid number string
     */
    private boolean isValidNumberString(JsonNode node) {
        if (!node.isTextual()) return false;
        try {
            Double.parseDouble(node.asText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if node is a valid integer string
     */
    private boolean isValidIntegerString(JsonNode node) {
        if (!node.isTextual()) return false;
        try {
            Integer.parseInt(node.asText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if string value is valid for enum
     */
    private boolean isValidEnumValue(String value, Class<?> enumClass) {
        try {
            Enum.valueOf((Class<? extends Enum>) enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get field metadata for a class (with caching)
     */
    private List<FieldMetadata> getFieldMetadata(Class<?> targetClass) {
        return fieldMetadataCache.computeIfAbsent(targetClass, this::extractFieldMetadata);
    }

    /**
     * Extract field metadata using reflection
     */
    private List<FieldMetadata> extractFieldMetadata(Class<?> targetClass) {
        List<FieldMetadata> metadataList = new ArrayList<>();
        
        // Get all declared fields including inherited ones
        Class<?> currentClass = targetClass;
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            
            for (Field field : fields) {
                // Skip synthetic fields
                if (field.isSynthetic()) continue;
                
                FieldMetadata metadata = createFieldMetadata(field);
                if (metadata != null) {
                    metadataList.add(metadata);
                }
            }
            
            currentClass = currentClass.getSuperclass();
        }
        
        return metadataList;
    }

    /**
     * Create field metadata from a Field object
     */
    private FieldMetadata createFieldMetadata(Field field) {
        try {
            String jsonPropertyName = getJsonPropertyName(field);
            Class<?> fieldType = field.getType();
            boolean isRequired = isFieldRequired(field);
            int minLength = getMinLength(field);
            int maxLength = getMaxLength(field);
            Double minValue = getMinValue(field);
            
            // Handle generic types for List fields
            Class<?> actualElementType = fieldType;
            if (List.class.isAssignableFrom(fieldType)) {
                actualElementType = getGenericType(field);
            }
            
            return new FieldMetadata(
                jsonPropertyName,
                fieldType,
                actualElementType, // Add element type for generic fields
                isRequired,
                minLength,
                maxLength,
                minValue
            );
        } catch (Exception e) {
            // Log error and return null to skip this field
            System.err.println("Error creating metadata for field " + field.getName() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Get the generic type of a field (for List<T> fields)
     */
    private Class<?> getGenericType(Field field) {
        try {
            java.lang.reflect.ParameterizedType paramType = 
                (java.lang.reflect.ParameterizedType) field.getGenericType();
            if (paramType != null && paramType.getActualTypeArguments().length > 0) {
                java.lang.reflect.Type typeArg = paramType.getActualTypeArguments()[0];
                if (typeArg instanceof Class) {
                    return (Class<?>) typeArg;
                }
            }
        } catch (Exception e) {
            // If we can't determine the generic type, return Object.class
        }
        return Object.class;
    }

    /**
     * Get JSON property name from field
     */
    private String getJsonPropertyName(Field field) {
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null && !jsonProperty.value().isEmpty()) {
            return jsonProperty.value();
        }
        return field.getName();
    }

    /**
     * Check if field is required based on annotations
     */
    private boolean isFieldRequired(Field field) {
        return field.isAnnotationPresent(jakarta.validation.constraints.NotNull.class) ||
               field.isAnnotationPresent(jakarta.validation.constraints.NotBlank.class) ||
               field.isAnnotationPresent(jakarta.validation.constraints.NotEmpty.class);
    }

    /**
     * Get minimum length constraint
     */
    private int getMinLength(Field field) {
        jakarta.validation.constraints.Size size = field.getAnnotation(jakarta.validation.constraints.Size.class);
        if (size != null) {
            return size.min();
        }
        
        jakarta.validation.constraints.Min min = field.getAnnotation(jakarta.validation.constraints.Min.class);
        if (min != null && field.getType() == String.class) {
            return (int) min.value();
        }
        
        return 0;
    }

    /**
     * Get maximum length constraint
     */
    private int getMaxLength(Field field) {
        jakarta.validation.constraints.Size size = field.getAnnotation(jakarta.validation.constraints.Size.class);
        if (size != null) {
            return size.max();
        }
        return 0;
    }

    /**
     * Get minimum value constraint
     */
    private Double getMinValue(Field field) {
        jakarta.validation.constraints.Min min = field.getAnnotation(jakarta.validation.constraints.Min.class);
        if (min != null && (field.getType() == Integer.class || field.getType() == int.class || 
                           field.getType() == Double.class || field.getType() == double.class)) {
            return (double) min.value();
        }
        return null;
    }

    /**
     * Extract field path from exception path
     */
    private String extractFieldPath(List<JsonMappingException.Reference> path) {
        if (path == null || path.isEmpty()) return "unknown";
        
        StringBuilder fieldPath = new StringBuilder();
        for (JsonMappingException.Reference ref : path) {
            if (fieldPath.length() > 0) {
                fieldPath.append(".");
            }
            
            if (ref.getFieldName() != null) {
                fieldPath.append(ref.getFieldName());
            } else if (ref.getIndex() >= 0) {
                fieldPath.append("[").append(ref.getIndex()).append("]");
            } else {
                fieldPath.append("unknown");
            }
        }
        return fieldPath.toString();
    }

    /**
     * Get expected type name
     */
    private String getExpectedType(Class<?> targetType) {
        if (targetType == null) return "valid type";
        
        if (targetType == String.class) return "string";
        if (targetType == Integer.class || targetType == int.class) return "integer";
        if (targetType == Double.class || targetType == double.class) return "number";
        if (targetType == Boolean.class || targetType == boolean.class) return "boolean";
        if (targetType == List.class || targetType == ArrayList.class) return "list";
        if (targetType.isEnum()) return "enum (" + targetType.getSimpleName() + ")";
        
        return targetType.getSimpleName().toLowerCase();
    }

    /**
     * Get actual type name from JsonNode
     */
    private String getActualType(JsonNode node) {
        if (node.isTextual()) {
            // Try to determine if it's a number string
            try {
                Integer.parseInt(node.asText());
                return "string (numeric)";
            } catch (NumberFormatException e1) {
                try {
                    Double.parseDouble(node.asText());
                    return "string (numeric)";
                } catch (NumberFormatException e2) {
                    return "string";
                }
            }
        }
        if (node.isNumber()) {
            if (node.isInt()) return "integer";
            if (node.isLong()) return "long";
            if (node.isDouble()) return "number";
            return "number";
        }
        if (node.isBoolean()) return "boolean";
        if (node.isNull()) return "null";
        if (node.isArray()) return "array";
        if (node.isObject()) return "object";
        
        return node.getNodeType().toString().toLowerCase();
    }

    /**
     * Field metadata class to hold validation information
     */
    private static class FieldMetadata {
        private final String jsonPropertyName;
        private final Class<?> fieldType;
        private final Class<?> elementType; // For generic types like List<T>
        private final boolean required;
        private final int minLength;
        private final int maxLength;
        private final Double minValue;

        public FieldMetadata(String jsonPropertyName, Class<?> fieldType, Class<?> elementType, 
                           boolean required, int minLength, int maxLength, Double minValue) {
            this.jsonPropertyName = jsonPropertyName;
            this.fieldType = fieldType;
            this.elementType = elementType;
            this.required = required;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.minValue = minValue;
        }

        public String getJsonPropertyName() { return jsonPropertyName; }
        public Class<?> getFieldType() { return fieldType; }
        public Class<?> getElementType() { return elementType; }
        public boolean isRequired() { return required; }
        public int getMinLength() { return minLength; }
        public int getMaxLength() { return maxLength; }
        public Double getMinValue() { return minValue; }
    }

    /**
     * Result class to hold deserialization result and validation errors
     */
    public static class DeserializationResult<T> {
        private final T result;
        private final List<String> validationErrors;
        private final boolean hasErrors;

        public DeserializationResult(T result, List<String> validationErrors, boolean hasErrors) {
            this.result = result;
            this.validationErrors = validationErrors;
            this.hasErrors = hasErrors;
        }

        public T getResult() {
            return result;
        }

        public List<String> getValidationErrors() {
            return validationErrors;
        }

        public boolean hasErrors() {
            return hasErrors;
        }

        public boolean isValid() {
            return !hasErrors && validationErrors.isEmpty();
        }
    }
} 