package com.hing.stackoverflowuser.data.gateways

import com.hing.stackoverflowuser.data.StackOverFlowApi
import com.hing.stackoverflowuser.data.UserItems
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by HingTang on 2019-05-23.
 */
interface ApiGateway {
    fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems>
}

class ApiGatewayImpl @Inject constructor(
    private val stackOverFlowApi: StackOverFlowApi
) : ApiGateway {
    override fun getUserList(page: Int, pageSize: Int, site: String): Single<UserItems> {
        return stackOverFlowApi.getUseList(page, pageSize, site).firstOrError()
    }
}
