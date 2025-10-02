package com.techfoot.stockspree.Business.DataContracts.GetSingleProduct;

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
public class GetSingleProductInput_IP {
    private String code;
    private String name;
}
