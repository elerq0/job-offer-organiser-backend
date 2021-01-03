package com.eleren.jobofferorganiser.dto;

import java.util.ArrayList;

public class ProxyListDto extends ArrayList<ProxyDto> {

    private int index = 0;

    public ProxyDto getCurrent() {
        return this.get(index);
    }

    public String getCurrentString() {
        return this.get(index).getAddress() + ":" + this.get(index).getPort();
    }

    public void next() throws Exception {
        if (this.size() == 0)
            throw new Exception(this.getClass().getSimpleName() + " is empty");

        index++;
        if (index >= this.size())
            throw new Exception("End of proxy");
    }
}
