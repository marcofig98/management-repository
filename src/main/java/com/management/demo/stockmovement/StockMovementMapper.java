package com.management.demo.stockmovement;

import com.management.demo.item.ItemMapper;

import java.util.List;
import java.util.stream.Collectors;

public class StockMovementMapper {


    public static StockMovementDTO toDTO(StockMovement stockMovement) {

        return new StockMovementDTO(
                stockMovement.getId(),
                stockMovement.getCreationDate(),
                ItemMapper.toDTO(stockMovement.getItem()),
                stockMovement.getQuantity()
        );
    }

    public static List<StockMovementDTO> toDTOList(List<StockMovement> stockMovements) {
        return stockMovements.stream()
                .map(StockMovementMapper::toDTO)
                .collect(Collectors.toList());
    }
}

