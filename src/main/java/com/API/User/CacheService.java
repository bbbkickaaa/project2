package com.API.User;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	
	@Cacheable(value = "mailCache", key = "#email + '_' + #authNumber")
	public Map<String, String> cacheAuthNumber(String authNumber, String email) {
	    Map<String, String> registerMail = new HashMap<>();
	    registerMail.put("authNumber", authNumber);
	    registerMail.put("email", email);
	    return registerMail;
	}
	
	@CacheEvict(value = "mailCache", key = "#email + '_' + #authNumber")
	public void evictCache(String authNumber, String email) {
	}

}
