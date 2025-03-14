package com.management.demo.item;

public class ItemMapper {

    public static ItemDTO toDTO(Item item) {
        if (item == null) {
            return null;
        }
        return new ItemDTO(item.getId(), item.getName());
    }

    public static Item toEntity(ItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }
        return new Item(itemDTO.getName());
    }
}
