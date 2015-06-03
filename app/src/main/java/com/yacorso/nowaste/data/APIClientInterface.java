package com.yacorso.nowaste.data;

import com.yacorso.nowaste.model.FoodList;
import com.yacorso.nowaste.model.Fridge;
import com.yacorso.nowaste.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.*;

public interface APIClientInterface {

    @GET("foods")
    void getFridge(Callback<Fridge> fridge);

    /*@GET("/users/{username}")
    void getUser(@Path("username") String username, Callback<User> cb);

    @GET("/group/{id}/users")
    void groupList(@Path("id") int groupId, @Query("sort") String sort, Callback<List<User>> cb);

    @POST("/users/new")
    void createUser(@Body User user, Callback<User> cb);
    */

}
