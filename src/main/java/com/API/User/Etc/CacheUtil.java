

package com.API.User.Etc;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;

@Service
@RequiredArgsConstructor
public class CacheUtil {

    private final CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();

    public String getData(String key) {
        Cache<String, String> cache = cacheManager.getCache("mailCache", String.class, String.class);
        return cache.get(key);
    }

    public void setData(String key, String value) {
        Cache<String, String> cache = cacheManager.getCache("mailCache", String.class, String.class);
        cache.put(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        MutableConfiguration<String, String> config = new MutableConfiguration<>();
        config.setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(new Duration(java.util.concurrent.TimeUnit.SECONDS, duration)));
        Cache<String, String> cache = cacheManager.createCache("mailCache", config);
        cache.put(key, value);
    }

    public void deleteData(String key) {
        Cache<String, String> cache = cacheManager.getCache("mailCache", String.class, String.class);
        cache.remove(key);
    }
}
