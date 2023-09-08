package br.com.matheushramos.batch.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FoodData {

    private String name;
    private String refId;
    private BigDecimal price;

}