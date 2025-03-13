package com.management.demo.stockmovements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IStockMovementRepository extends JpaRepository<StockMovement, UUID> {
}
