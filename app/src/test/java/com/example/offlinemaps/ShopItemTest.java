package com.example.offlinemaps;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShopItemTest {

    private ShopItem shopItem = new ShopItem();

    @Test
    public void testGetAndSetShopItemName() {
        shopItem.setItemName("name");
        assertEquals("name", shopItem.getItemName());
    }

    @Test
    public void testGetAndSetShopItem() {
        shopItem.setItemCost(20);
        assertEquals(20, shopItem.getItemCost());
    }

    @Test
    public void testShopItemToString() {
        assertEquals("ShopItem{itemName='null', itemCost=0}", shopItem.toString());
    }
}