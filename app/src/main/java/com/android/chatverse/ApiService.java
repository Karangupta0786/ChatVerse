package com.android.chatverse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("random/{gender}/{userId}")
    Call<RandomUserResponse> randomSearch(@Path("gender") String gender,@Path("userId")String userId);
}
