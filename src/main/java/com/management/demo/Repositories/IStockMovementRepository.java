package com.management.demo.Repositories;

import com.management.demo.Entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IStockMovementRepository extends JpaRepository<StockMovement, UUID> {
}
