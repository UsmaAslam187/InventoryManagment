package com.techfoot.stockspree.OutboundAdaptors.Memory.Cache.Configurations;

import com.techfoot.stockspree.OutboundAdaptors.Configurations.TechfootServicesConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class RedisClient {

    private TechfootServicesConfig.CacheService service;
    private JedisPool jedisPool;

    private boolean canSet;

    public RedisClient() {
        service = new TechfootServicesConfig().CacheService();
        this.canSet = false;

        String redisHost = service.getHostName();
        int redisPort = service.getPortNumber();
        String redisPassword = service.getPassword();
        System.out.println("Redis client connecting to " + redisHost + ":" + redisPort + " with password " + redisPassword);
        this.jedisPool = new JedisPool(redisHost, redisPort, null, redisPassword);
        try (Jedis jedis = jedisPool.getResource()) {
            System.out.println("Redis client connected to " + redisHost + ":" + redisPort);
            jedis.ping();
            this.canSet = true;
        } catch (JedisConnectionException e) {
            System.out.println("Redis client connection failed: " + e.getMessage());
        }
    }

    /**
     * Method to get data from cache. The caller must provide the key.
     *
     * @param key Redis key
     * @return Object from cache or null if not found
     */
    public String get(String key) {
        String cachedData = "{ \"workspaces\": ["
                + "{ \"workspace\": \"template\" }"
                + "] }";
        if (key != null && !key.isEmpty()) {
            try (Jedis jedis = jedisPool.getResource()) {
                String result = jedis.get(key);
                return result != null ? result : cachedData;
            } catch (JedisConnectionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Method to set key-value pair in the cache.
     *
     * @param key Redis key
     * @param object Object to cache
     * @return "OK" if the operation was successful, or null if it failed
     */
    public String set(String key, String object) {
        if (key != null && !key.isEmpty()) {
            try (Jedis jedis = jedisPool.getResource()) {
                String result = jedis.set(key, object);
                System.out.println("Redis set function: " + result);
                return result;
            } catch (JedisConnectionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
