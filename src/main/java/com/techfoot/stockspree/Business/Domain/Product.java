package com.techfoot.stockspree.Business.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends DomainAggregate {

    public Product() {
        super();
        this.setSimpleAttributesType("", "", 0.0, "", "", 0, 0);
    }
    
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
    
    
    public Map<String, Object> getHeaderMessage() {
        Map<String, Object> header = new HashMap<>();
        header.put("status", false);
        header.put("message", "Following Errors: ");
        return header;
    }

    public void setSimpleAttributesType(String name, String code, Double price, String tax, String type, Integer salesAccount, Integer purchaseAccount) {
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
    
    public List<String> validateProduct(Map<String, Object> data) {
        List<String> validationErrors = new ArrayList<>();
        
        // Set data from the input map
        if (data.containsKey("name")) this.name = (String) data.get("name");
        if (data.containsKey("code")) this.code = (String) data.get("code");
        if (data.containsKey("price")) this.price = (Double) data.get("price");
        if (data.containsKey("tax")) this.tax = (String) data.get("tax");
        if (data.containsKey("type")) this.type = (String) data.get("type");
        if (data.containsKey("salesAccount")) this.salesAccount = (Integer) data.get("salesAccount");
        if (data.containsKey("purchaseAccount")) this.purchaseAccount = (Integer) data.get("purchaseAccount");
        
        // Spring validation will be handled by the framework
        // Additional business logic validation
        validationErrors.addAll(validatePolicy());
        
        return validationErrors;
    }
}
