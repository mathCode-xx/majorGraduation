package cn.scut.xx.majorgraduation.config.redis;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 徐鑫
 */
@Configuration(value = "rBloomFilterConfigurationByAdmin")
public class RedisBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> phoneNumberCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("phoneNumberCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(2000000L, 0.01);
        return cachePenetrationBloomFilter;
    }

    @Bean
    public RBloomFilter<String> idCardCachePenetrationBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("idCardCachePenetrationBloomFilter");
        cachePenetrationBloomFilter.tryInit(2000000L, 0.01);
        return cachePenetrationBloomFilter;
    }
}
