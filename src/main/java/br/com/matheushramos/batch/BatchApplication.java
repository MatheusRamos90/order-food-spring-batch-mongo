package br.com.matheushramos.batch;

import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class BatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

	@Bean
	public CacheManager springCacheManager() {
		SpringCache2kCacheManager cacheManager = new SpringCache2kCacheManager("spring-" + hashCode());
		cacheManager.addCaches(b -> b.name("cache"));
		return cacheManager;
	}

}
