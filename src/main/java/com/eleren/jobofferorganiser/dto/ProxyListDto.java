package com.eleren.jobofferorganiser.dto;

import java.util.ArrayList;

public class ProxyListDto extends ArrayList<ProxyDto> {

    private int index = 0;

    public ProxyDto getCurrent(){
        return this.get(index);
    }

    public boolean next() throws Exception {
        if(this.size() == 0)
            throw new Exception(this.getClass().getSimpleName() + " is empty");

        index++;
        if(index >= this.size()) {
            index = 0;
            return true;
        }
        return false;
    }
}
