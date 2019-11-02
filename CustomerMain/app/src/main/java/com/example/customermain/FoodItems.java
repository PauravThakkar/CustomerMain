package com.example.customermain;


public class FoodItems
{

    String item_name;
    int item_price;
    boolean item_availability;
    String item_id;
    public FoodItems() {

    }

    public FoodItems(String item_id, String item_name, int item_price, boolean item_availability){
        this.item_name=item_name;
        this.item_price=item_price;
        this.item_availability=item_availability;
        this.item_id=item_id;
    }


    public String getKey(){ return this.item_id; }

    public String getName()
    {
        return this.item_name;
    }

    public boolean getAvailability()
    {
        return this.item_availability;
    }

    public int getPrice()
    {
        return this.item_price;
    }

    public void setKey(String item_id) { this.item_id = item_id; }

    public  void setName(String item_name)
    {
        this.item_name=item_name;
    }

    public void setPrice(int item_price)
    {
        this.item_price=item_price;
    }

    public void setAvailability(boolean item_availability)
    {
        this.item_availability = item_availability;
    }


}
