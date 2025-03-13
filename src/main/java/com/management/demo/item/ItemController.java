package com.management.demo.item;

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

    // Criar um novo item e retornar o ItemDTO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO createItem(@RequestBody ItemDTO itemDTO) {
        return itemService.createItem(itemDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItemById(@PathVariable UUID id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO updateItemName(@PathVariable UUID id, @RequestBody ItemDTO itemDTO) {
        return itemService.updateItemName(id, itemDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
    }
}

