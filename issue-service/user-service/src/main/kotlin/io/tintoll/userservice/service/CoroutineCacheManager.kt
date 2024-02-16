package io.tintoll.userservice.service

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class CoroutineCacheManager<T> {
    private val localCache = ConcurrentHashMap<String, CacheWrapper<T>>()

    suspend fun awaitPut(key: String, value: T, ttl: Duration) {
        localCache[key] = CacheWrapper(value, Instant.now().plusMillis(ttl.toMillis()))
    }

    suspend fun awaitEvict(key: String) {
        localCache.remove(key)
    }

    suspend fun awaitGetOrPut(key: String, ttl: Duration? = Duration.ofMinutes(5), supplier: suspend () -> T): T {
        val now: Instant = Instant.now()
        val cacheWrapper = localCache[key]
        // 캐시에 없는 경우 : 신규 생성
        val cache = if (cacheWrapper == null) {
            // also는 자기자긴 객체를 넘겨줌, let는 값을 넘겨는데 아래에서 사용해되됨.
            CacheWrapper(supplier(), now.plusMillis(ttl!!.toMillis())).also {
                localCache[key] = it
            }
        } else if (now.isAfter(cacheWrapper.ttl)) {
            // 캐시에 있는 경우 - TTL이 만료된 경우
            localCache.remove(key)
            CacheWrapper(supplier(), now.plusMillis(ttl!!.toMillis())).also {
                localCache[key] = it
            }
        } else {
            cacheWrapper
        }
        checkNotNull(cache.cached)
        return cache.cached
    }


}

data class CacheWrapper<T>(val cached: T, val ttl: Instant)
