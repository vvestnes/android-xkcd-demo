package com.example.xkcd.repository.api;


import com.example.xkcd.repository.info.Info;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface XkcdInfoApi {

    /**
     * Returns Info on xkcd
     *
     * @param num The xkcd unique num/id
     * @return Info object
     */
    @GET("{id}/info.0.json")
    Call<Info> getInfo(@Path("id") int num);

    /**
     * Returns the current/latest available xkcd.
     *
     * @return Info object of current xkcd.
     */
    @GET("info.0.json")
    Call<Info> getCurrentInfo();

}
