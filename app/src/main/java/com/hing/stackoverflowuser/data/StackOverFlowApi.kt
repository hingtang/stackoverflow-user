package com.hing.stackoverflowuser.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by HingTang on 2019-05-23.
 */
interface StackOverFlowApi {
    @Headers(
        "Accept: application/json;charset=utf-t",
        "Accept-Language: en"
    )
    @GET("2.2/users")
    fun getUseList(
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("site") site: String
    ): Observable<UserItems>

    @Headers(
        "Accept: application/json;charset=utf-t",
        "Accept-Language: en"
    )
    @GET("2.2/users/{ids}/reputation-history")
    fun getUserReputation(
        @Path("ids") userId: Int,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int,
        @Query("site") site: String
    ): Observable<UserReputationItems>
}
