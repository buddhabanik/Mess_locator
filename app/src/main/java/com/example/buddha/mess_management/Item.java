package com.example.buddha.mess_management;

/**
 * Created by BUDDHA on 2/16/2017.
 */

public class Item {
    private String address,rent,numberofseat,contractnumber,description;

    public Item(String address,String rent,String numberofseat,String contractnumber,String description)
    {
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
}

