package hulio13.articlesApi.domain.entity.common

import hulio13.articlesApi.domain.entity.Article
import java.io.ObjectInputStream
import java.util.Objects

data class Result<T>(val isSuccess: Boolean, val data: T?, val error: Error?) {
    companion object {
        fun <T> ok(entity: T): Result<T>  = Result(true, entity, null)

        fun <T> error(errCode: Int, msg: String?): Result<T> =
            Result(false, null, Error(errCode, msg!!))
    }
}
