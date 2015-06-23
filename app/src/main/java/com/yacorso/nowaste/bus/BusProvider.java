package com.yacorso.nowaste.bus;

import com.squareup.otto.Bus;

/**
 * Created by quentin on 21/06/15.
 */
public class BusProvider {

    private static final Bus BUS = new Bus();


    public static Bus getInstance(){
        return BUS;
    }

    private BusProvider(){}
}
