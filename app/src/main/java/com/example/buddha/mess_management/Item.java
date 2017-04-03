package com.example.buddha.mess_management;

/**
 * Created by BUDDHA on 2/16/2017.
 */

public class Item {
    private String id;
    private String address;
    private String rent;
    private String numberofseat;
    private String contractnumber;
    private String description;

    public Item(String id,String address,String rent,String numberofseat,String contractnumber,String description)
    {
        this.setId(id);
        this.setAddress(address);
        this.setRent(rent);
        this.setNumberoseat(numberofseat);
        this.setContractnumber(contractnumber);
        this.setDescription(description);
    }

    public void setNumberoseat(String numberofseat) {
        this.numberofseat = numberofseat;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public void setContractnumber(String contractnumber) {
        this.contractnumber = contractnumber;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getRent() {
        return rent;
    }

    public String getNumberofseat() {
        return numberofseat;
    }

    public String getContractnumber() {

        return contractnumber;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {

        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

