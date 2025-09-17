package com.techfoot.stockspree.Business.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.techfoot.stockspree.Business.DataContracts.CreateProducts.CreateProductsInput_IP;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
@Getter
public class Product extends DomainAggregate {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    
    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 100, message = "Code must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Code must be alphanumeric")
    private String code;
    
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;
    
    @Size(max = 100, message = "Tax must not exceed 100 characters")
    private String tax;
    
    @NotNull(message = "Type is required")
    @Size(max = 100, message = "Type must not exceed 100 characters")
    @Pattern(regexp = "^(Consumable Product|Service Product)$", message = "Type must be either 'Consumable Product' or 'Service Product'")
    private String type;
    
    @Min(value = 0, message = "Sales account must be greater than or equal to 0")
    private Integer salesAccount;
    
    @Min(value = 0, message = "Purchase account must be greater than or equal to 0")
    private Integer purchaseAccount;

    private Validator validator;
    private List<Error_DomainEntity> errors;
 
    // Parameterless constructor
    public Product() {
        super();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.setSimpleAttributesType("", "", 0.0, "", "", 0, 0);
        this.errors = this.validate();
    }

    // Parameterized constructor
    public Product(String name, String code, Double price, String tax, String type, Integer salesAccount,
            Integer purchaseAccount) {
        super();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.setSimpleAttributesType(name, code, price, tax, type, salesAccount, purchaseAccount);
        this.errors = this.validate();
    }

    // Setters
    private void setName(String name) {
        if(name == null){
            name = "";
        }
        this.name = name;
    }

    private void setCode(String code) {
        if (code == null) {
            code = "";
        }
        if (!code.matches("^[a-zA-Z0-9]*$")) {
            throw new IllegalArgumentException("Code must be alphanumeric only");
        }
        this.code = code;
    }
    
    private void setPrice(Double price) {
        if(price == null){
            price = 0.0;
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must be greater than or equal to 0");
        }
        this.price = price;
    }
    
    private void setTax(String tax) {
        if(tax == null){
            tax = "";
        }
        if (tax.length() > 100) {
            throw new IllegalArgumentException("Tax must not exceed 100 characters");
        }
        this.tax = tax;
    }

    private void setType(String type) {
        if(type == null){
            type = "";
        }
        if (!type.equals("Consumable Product") && !type.equals("Service Product") && !type.isEmpty()) {
            throw new IllegalArgumentException("Type must be either 'Consumable Product' or 'Service Product'");
        }
        this.type = type;
    }

    private void setSalesAccount(Integer salesAccount) {
        if(salesAccount == null){
            salesAccount = 0;
        }
        if (salesAccount < 0) {
            throw new IllegalArgumentException("Sales account must be greater than or equal to 0");
        }
        this.salesAccount = salesAccount;
    }

    private void setPurchaseAccount(Integer purchaseAccount) {
        if(purchaseAccount == null){
            purchaseAccount = 0;
        }
        if (purchaseAccount < 0) {
            throw new IllegalArgumentException("Purchase account must be greater than or equal to 0");
        }
        this.purchaseAccount = purchaseAccount;
    }

    public boolean isValid() {
        this.errors = this.validate();
        return this.errors.isEmpty();
    }
    
    private void setSimpleAttributesType(String name, String code, Double price, String tax, String type, Integer salesAccount, Integer purchaseAccount) {
        this.setName(name);
        this.setCode(code);
        this.setPrice(price);
        this.setTax(tax);
        this.setType(type);
        this.setSalesAccount(salesAccount);
        this.setPurchaseAccount(purchaseAccount);
    }

    public List<String> validatePolicy() {
        List<String> errors = new ArrayList<>();
        
        // Policy validation: SalesAccount or PurchaseAccount must exist
        // Commented out as per Node.js version
        /*
        if (this.salesAccount == null && this.purchaseAccount == null) {
            errors.add("SalesAccount or PurchaseAccount must exist");
        }
        */
        
        return errors;
    }
    
    public List<Error_DomainEntity> validate() {
        Set<ConstraintViolation<Product>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            System.out.println(violations);
            List<Error_DomainEntity> violationMessages = violations.stream()
                .map(message -> new Error_DomainEntity(false, message.getMessage()))
                .collect(Collectors.toList());

            return violationMessages;
        } else {
            return new ArrayList<>();
        }
    }

    public List<String> setSimpleAttributesAndValidatePolicy(CreateProductsInput_IP.Product input) {
        List<String> errors = new ArrayList<>();
        
        try {
            this.setSimpleAttributesType(
                input.getName(), 
                input.getCode(), 
                input.getPrice(), 
                input.getTax(), 
                input.getType(), 
                input.getSalesAccount(), 
                input.getPurchaseAccount()
            );
            
            if(this.isValid()){
                errors.addAll(this.validatePolicy());
            } else {
                for(Error_DomainEntity error : this.errors) {
                    errors.add(error.getMessage());
                }
            }
        } catch (Exception e) {
            errors.add("Error setting product attributes: " + e.getMessage());
        }
        
        return errors;
    }
}
