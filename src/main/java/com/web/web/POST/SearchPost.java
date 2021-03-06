package com.web.web.POST;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Mitchell on 02/08/2017.
 */
public class SearchPost {

    @NotNull
    private String keyword;

    @NotNull
    private String location;

    public String getKeyword(){
        return this.keyword.toLowerCase();
    }

    public void setKeyword(String keyword){
        this.keyword = keyword.toLowerCase();
    }

    public String getLocation(){
        return this.location.toLowerCase();
    }

    public void setLocation(String location){
        this.location = location.toLowerCase();
    }
}
