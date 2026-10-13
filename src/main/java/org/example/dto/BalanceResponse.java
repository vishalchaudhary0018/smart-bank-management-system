package org.example.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class BalanceResponse {
    private BigDecimal totalExpense;
    private Map<String, BigDecimal> balances;
    private List<String> settlements;
}