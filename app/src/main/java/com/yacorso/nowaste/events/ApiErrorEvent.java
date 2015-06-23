package com.yacorso.nowaste.events;

import retrofit.RetrofitError;

/**
 * Created by quentin on 21/06/15.
 */
public class ApiErrorEvent {

    RetrofitError mMessage;

    public ApiErrorEvent(RetrofitError message){
        mMessage = message;
    }

    public String getErrorMessage(){
        return mMessage.getMessage();
    }
}
