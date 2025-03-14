package com.management.demo.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IItemRepository extends JpaRepository<Item, UUID> {


    @Query("SELECT COALESCE(SUM(sm.quantity), 0) FROM StockMovement sm WHERE sm.item.id = :itemId")
    int getAvailableStock(@Param("itemId") UUID itemId);

    Optional<Item> findByName(String name);
}
