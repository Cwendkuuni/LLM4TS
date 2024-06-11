import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

public class RedisEventHandlerDAOTest {

    @Mock private JedisProxy jedisProxy;

    @Mock private ObjectMapper objectMapper;

    @Mock private ConductorProperties conductorProperties;

    @Mock private RedisProperties redisProperties;

    @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        eventHandler1 =
            EventHandler.builder()
                .name("eh1")
                .event("event1")
                .active(true)
                .build();

        eventHandler2 =
            EventHandler.builder()
                .name("eh2")
                .event("event1")
                .active(false)
                .build();

        eventHandler3 =
            EventHandler.builder()
                .name("eh3")
                .event("event2")
                .active(true)
                .build();

        when(objectMapper.convertValue(ArgumentMatchers.any(), ArgumentMatchers.eq(EventHandler.class)))
            .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(null);

        when(jedisProxy.hset(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler1);

        Mockito.verify(jedisProxy, Mockito.times(1))
            .hget(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"));

        Mockito.verify(jedisProxy, Mockito.times(1))
            .hset(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"), ArgumentMatchers.anyString());

        Mockito.verify(jedisProxy, Mockito.times(1))
            .sadd(
                ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")),
                ArgumentMatchers.eq("eh1"));

        Mockito.verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_conflict() {
        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn("{\"name\":\"eh1\",\"event\":\"event1\"}");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() {
        String eh1Json = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";

        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(eh1Json);

        when(jedisProxy.hset(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        Mockito.verify(jedisProxy, Mockito.times(1))
            .hget(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"));

        Mockito.verify(
            jedisProxy,
            Mockito.times(1))
            .hset(
                ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                ArgumentMatchers.eq("eh1"),
                ArgumentMatchers.eq("{\"name\":\"eh1\",\"event\":\"event1\",\"active\":false}"));

        Mockito.verify(jedisProxy, Mockito.times(1))
            .srem(
                ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")),
                ArgumentMatchers.eq("eh1"));

        Mockito.verify(jedisProxy, Mockito.times(1))
            .sadd(
                ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")),
                ArgumentMatchers.eq("eh1"));

        Mockito.verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_notFound() {
        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler2);
    }

    @Test
    public void testRemoveEventHandler() {
        String eh1Json = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";

        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(eh1Json);

        redisEventHandlerDAO.removeEventHandler("eh1");

        Mockito.verify(jedisProxy, Mockito.times(1))
            .hget(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"));

        Mockito.verify(jedisProxy, Mockito.times(1))
            .hdel(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"));

        Mockito.verify(jedisProxy, Mockito.times(1))
            .srem(
                ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")),
                ArgumentMatchers.eq("eh1"));

        Mockito.verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_notFound() {
        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("non-existing-eh");
    }

    @Test
    public void testGetAllEventHandlers() {
        String eh1Json = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";
        String eh2Json = "{\"name\":\"eh2\",\"event\":\"event1\",\"active\":false}";
        String eh3Json = "{\"name\":\"eh3\",\"event\":\"event2\",\"active\":true}";

        when(jedisProxy.hgetAll(ArgumentMatchers.anyString()))
            .thenReturn(
                new HashMap<String, String>() {
                  {
                    put("eh1", eh1Json);
                    put("eh2", eh2Json);
                    put("eh3", eh3Json);
                  }
                });

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eh1Json = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";
        String eh3Json = "{\"name\":\"eh3\",\"event\":\"event2\",\"active\":true}";

        when(jedisProxy.smembers(ArgumentMatchers.anyString()))
            .thenReturn(new HashSet<>(Arrays.asList("eh1")));

        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(eh1Json);

        List<EventHandler> eventHandlers =
            redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        // Verify the method was called with the correct key
        Mockito.verify(jedisProxy, Mockito.times(1))
            .smembers(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")));

        // Verify getEventHandler was called once with the correct name
        Mockito.verify(jedisProxy, Mockito.times(1))
            .hget(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq("eh1"));

        // Verify getEventHandler was not called with the wrong name
        Mockito.verifyNoMoreInteractions(jedisProxy);

        // Test with the other event
        when(jedisProxy.smembers(ArgumentMatchers.anyString()))
            .thenReturn(new HashSet<>(Arrays.asList("eh3")));

        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(eh3Json);

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event2", true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler3, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEvent_empty() {
        when(jedisProxy.smembers(ArgumentMatchers.anyString()))
            .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent_inactive() {
        String eh1Json = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":false}";

        when(jedisProxy.smembers(ArgumentMatchers.anyString()))
            .thenReturn(new HashSet<>(Arrays.asList("eh1")));

        when(jedisProxy.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(eh1Json);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(0, eventHandlers.size());

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    // Add more test methods for other edge cases, if necessary
}