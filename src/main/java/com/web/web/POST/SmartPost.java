package com.web.web.POST;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by Mitchell on 04/08/2017.
 */
public class SmartPost {

    @NotNull
    private String keyword;

    @NotNull
    private String location;

    @Null
    private int amount;

    public int getAmount(){
        return amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public String getKeyword(){
        return this.keyword;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

}
