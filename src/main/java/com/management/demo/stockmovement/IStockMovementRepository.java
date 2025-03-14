package com.management.demo.stockmovement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IStockMovementRepository extends JpaRepository<StockMovement, UUID> {

    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.item.id = :itemId")
    Integer getTotalQuantityByItem(@Param("itemId") UUID itemId);
}
