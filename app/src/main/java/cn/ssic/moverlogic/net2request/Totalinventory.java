package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/10/20.
 */

public class Totalinventory {
    String furnitureName;
    int  quantity;
    double  furnitureVolume;
    double volume;
    String inventoryName;

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public void setFurnitureName(String furnitureName) {
        this.furnitureName = furnitureName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getFurnitureVolume() {
        return furnitureVolume;
    }

    public void setFurnitureVolume(double furnitureVolume) {
        this.furnitureVolume = furnitureVolume;
    }
}
