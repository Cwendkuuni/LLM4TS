import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NAMESPACE = "my-namespace";
    private static final String EVENT = "my-event";
    private static final String EVENT_HANDLER_NAME = "my-event-handler";
    private static final String EVENT_HANDLER_JSON =
            "{\"name\":\"my-event-handler\",\"event\":\"my-event\",\"topic\":\"my-topic\",\"activity\":\"my-activity\",\"workflow\":\"my-workflow\",\"active\":true,\"heartbeatTimeoutSeconds\":300}";

    @Mock private JedisProxy jedisProxy;
    @Mock private ObjectMapper objectMapper;
    @Mock private ConductorProperties conductorProperties;
    @Mock private RedisProperties redisProperties;

    @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisPool jedisPool;
    private Jedis jedis;

    @Before
    public void setup() {
        initMocks(this);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        jedis = jedisPool.getResource();

        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "namespace", NAMESPACE, String.class);

        when(jedisProxy.getJedis()).thenReturn(jedis);
        when(objectMapper.readValue(anyString(), any(Class.class)))
                .thenReturn(new EventHandler());
        when(objectMapper.writeValueAsString(any(EventHandler.class)))
                .thenReturn(EVENT_HANDLER_JSON);
        when(
                        jedisProxy.hget(
                                eq(NAMESPACE + ":" + EVENT_HANDLERS), anyString()))
                .thenReturn(EVENT_HANDLER_JSON);
    }

    @After
    public void teardown() {
        jedis.close();
        jedisPool.close();
    }

    @Test
    public void testAddEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), anyString());
        verify(jedisProxy).sadd(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT), eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        doThrow(new ConflictException("EventHandler already exists"))
                .when(jedisProxy)
                .hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), anyString());

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("Expected ConflictException");
        } catch (ConflictException e) {
            verify(jedisProxy).hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), anyString());
            verify(jedisProxy, never()).sadd(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT), eq(EVENT_HANDLER_NAME));
        }
    }

    @Test
    public void testUpdateEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), anyString());
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonexistent-event-handler");
        eventHandler.setEvent(EVENT);

        doThrow(new NotFoundException("EventHandler not found"))
                .when(jedisProxy)
                .hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq("nonexistent-event-handler"), anyString());

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            verify(jedisProxy).hset(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq("nonexistent-event-handler"), anyString());
        }
    }

    @Test
    public void testRemoveEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy).hdel(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME));
        verify(jedisProxy).srem(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT), eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        doThrow(new NotFoundException("EventHandler not found"))
                .when(jedisProxy)
                .hdel(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq("nonexistent-event-handler"));

        try {
            redisEventHandlerDAO.removeEventHandler("nonexistent-event-handler");
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            verify(jedisProxy).hdel(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq("nonexistent-event-handler"));
            verify(jedisProxy, never()).srem(anyString(), anyString());
        }
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("eventHandler1", EVENT_HANDLER_JSON);
        map.put("eventHandler2", EVENT_HANDLER_JSON);

        when(jedisProxy.hgetAll(eq(NAMESPACE + ":" + EVENT_HANDLERS)))
                .thenReturn(map);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertThat(eventHandlers.size(), equalTo(2));
        verify(jedisProxy).hgetAll(eq(NAMESPACE + ":" + EVENT_HANDLERS));
    }

    @Test
    public void testGetEventHandlersForEvent() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("eventHandler1", EVENT_HANDLER_JSON);
        map.put("eventHandler2", EVENT_HANDLER_JSON);

        when(jedisProxy.smembers(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT)))
                .thenReturn(
                        new LinkedList<>(
                                List.of(map.keySet().toArray(new String[0]))));

        when(jedisProxy.hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), anyString()))
                .thenReturn(EVENT_HANDLER_JSON);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertThat(eventHandlers.size(), equalTo(2));
        verify(jedisProxy).smembers(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT));
        verify(jedisProxy, times(2)).hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), anyString());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("eventHandler1", EVENT_HANDLER_JSON);
        map.put("eventHandler2", EVENT_HANDLER_JSON);

        when(jedisProxy.smembers(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT)))
                .thenReturn(
                        new LinkedList<>(
                                List.of(map.keySet().toArray(new String[0]))));

        when(jedisProxy.hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), anyString()))
                .thenReturn(EVENT_HANDLER_JSON.replace("active\":true", "active\":false"));

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertThat(eventHandlers.size(), equalTo(0));
        verify(jedisProxy).smembers(eq(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT));
        verify(jedisProxy, times(2)).hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), anyString());
    }

    @Test
    public void testGetEventHandler() throws IOException {
        when(jedisProxy.hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME)))
                .thenReturn(EVENT_HANDLER_JSON);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        assertThat(eventHandler, notNullValue());
        verify(jedisProxy).hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        when(jedisProxy.hget(eq(NAMESPACE + ":" + EVENT_HANDLERS), eq(EVENT_HANDLER_NAME)))
                .thenReturn(null);

        redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
    }
}