package com.management.demo.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/items")
@Tag(name = "Items", description = "Item Management controller")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new item")
    public ItemDTO createItem(@RequestBody ItemDTO itemDTO) {
        return itemService.createItem(itemDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get item by id")
    public ItemDTO getItemById(@PathVariable UUID id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all items")
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Item name")
    public ItemDTO updateItemName(@PathVariable UUID id, @RequestBody ItemDTO itemDTO) {
        itemDTO.setId(id);
        return itemService.updateItemName(itemDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "This endpoint will not work if the item has any associated orders, " +
            "as deleting the item would result in the loss of important information. " +
            "Removing this information would require additional specifications and steps.")
    public void deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
    }
}

