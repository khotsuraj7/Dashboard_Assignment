
package com.example.sellerdashboard.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SellerSummaryDto {
    private Long sellerId;
    private String sellerName;
    private int totalSales;
    private BigDecimal totalRevenue;
    private double returnRate;
    private List<String> alerts;
}
