package com.example.offlinemaps;

public class ShopItem {

    private String itemName;
    private int itemCost;

    public ShopItem() {
    }

    public ShopItem(String itemName, int itemCost) {
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemCost() {
        return itemCost;
    }

    public void setItemCost(int itemCost) {
        this.itemCost = itemCost;
    }
}
