package com.example.xkcd.repository.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface XkcdSearchApi {

    /**
     * Returns a string containing the xkcd matching the search
     *
     * @param query The term to search for
     * @return List of items matched. (from 3rd line)
     */
    @GET("process?action=xkcd")
    Call<String> search(@Query("query") String query);
}
