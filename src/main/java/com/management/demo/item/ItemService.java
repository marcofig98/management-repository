package com.management.demo.item;

import com.management.demo.item.exception.ItemConflictException;
import com.management.demo.item.exception.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {

    private final IItemRepository itemRepository;

    @Autowired
    public ItemService(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;

    }

    public ItemDTO createItem(ItemDTO itemDTO) {

        Item item = ItemMapper.toEntity(itemDTO);

        Optional<Item> existingItem = itemRepository.findByName(item.getName());

        if (existingItem.isPresent()) {
            throw new ItemConflictException(item.getName());
        }

        Item savedItem = itemRepository.save(item);

        log.info("Item created with id {} and name {}", item.getId(), item.getName());

        return ItemMapper.toDTO(savedItem);
    }

    public ItemDTO getItemById(UUID id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        return ItemMapper.toDTO(item);
    }

    public List<ItemDTO> getAllItems() {

        List<Item> items = itemRepository.findAll();

        return items.stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItemDTO updateItemName(ItemDTO itemDTO) {
        UUID id = itemDTO.getId();
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        Optional<Item> existingItem = itemRepository.findByName(itemDTO.getName());
        if (existingItem.isPresent()) {
            throw new ItemConflictException(itemDTO.getName());
        }

        item.setName(itemDTO.getName());
        Item updatedItem = itemRepository.save(item);

        log.info("Item updated with {} and new name {}", updatedItem.getId(), updatedItem.getName());
        return ItemMapper.toDTO(updatedItem);
    }

    public void deleteItem(UUID id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        itemRepository.delete(item);
        log.info("Item deleted: {}", id);
    }
}
