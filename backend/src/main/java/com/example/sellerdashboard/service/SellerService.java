
package com.example.sellerdashboard.service;

import com.example.sellerdashboard.dto.SellerSummaryDto;
import com.example.sellerdashboard.entity.Seller;
import com.example.sellerdashboard.repository.SaleRepository;
import com.example.sellerdashboard.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private SellerRepository sellerRepo;

    @Cacheable(value = "sellerSummary", key = "#sellerId")
    public SellerSummaryDto getSellerSummary(Long sellerId) {
        Seller seller = sellerRepo.findById(sellerId).orElseThrow(() -> new RuntimeException("NOT_FOUND"));

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        LocalDate lastWeekStart = weekStart.minusDays(7);
        LocalDate lastWeekEnd = weekStart.minusDays(1);

        List<Object[]> thisWeekDaily = saleRepo.aggregateDailyForRange(sellerId, weekStart, today);
        List<Object[]> lastWeekDaily = saleRepo.aggregateDailyForRange(sellerId, lastWeekStart, lastWeekEnd);

        int thisWeekSales = 0;
        BigDecimal thisWeekRevenue = BigDecimal.ZERO;
        int thisWeekReturns = 0;

        for (Object[] row : thisWeekDaily) {
            Number qty = (Number) row[1];
            Number rev = (Number) row[2];
            Number ret = (Number) row[3];
            thisWeekSales += qty == null ? 0 : qty.intValue();
            thisWeekRevenue = thisWeekRevenue.add(rev == null ? BigDecimal.ZERO : BigDecimal.valueOf(((Number)rev).doubleValue()));
            thisWeekReturns += ret == null ? 0 : ((Number)ret).intValue();
        }

        int lastWeekSales = 0;
        for (Object[] row : lastWeekDaily) {
            Number qty = (Number) row[1];
            lastWeekSales += qty == null ? 0 : qty.intValue();
        }

        double returnRate = thisWeekSales == 0 ? 0.0 : (thisWeekReturns * 1.0 / thisWeekSales);

        List<String> alerts = new ArrayList<>();
        if (lastWeekSales > 0) {
            double drop = (lastWeekSales - thisWeekSales) / (double) lastWeekSales;
            if (drop > 0.30) alerts.add("Sales dropped by more than 30% vs last week");
        }
        if (returnRate > 0.10) alerts.add("Return rate above 10%");

        SellerSummaryDto dto = new SellerSummaryDto();
        dto.setSellerId(sellerId);
        dto.setSellerName(seller.getName());
        dto.setTotalSales(thisWeekSales);
        dto.setTotalRevenue(thisWeekRevenue);
        dto.setReturnRate(returnRate);
        dto.setAlerts(alerts);
        return dto;
    }
}
