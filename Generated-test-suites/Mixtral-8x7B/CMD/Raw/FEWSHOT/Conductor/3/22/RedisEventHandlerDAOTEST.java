import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(conductorProperties.getRedis()).thenReturn(redisProperties);

        redisEventHandlerDAO =
                new RedisEventHandlerDAO(
                        jedisProxy, objectMapper, conductorProperties, redisProperties);

        eventHandler1 = new EventHandler();
        eventHandler1.setName("eh1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("eh2");
        eventHandler2.setEvent("event2");
        eventHandler2.setActive(false);

        eventHandler3 = new EventHandler();
        eventHandler3.setName("eh3");
        eventHandler3.setEvent("event3");
        eventHandler3.setActive(true);
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L); // mock successful operation
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null); // mock that it doesn't exist yet

        redisEventHandlerDAO.addEventHandler(eventHandler1);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(jedisProxy, times(1))
                .hset(
                        keyCaptor.capture(),
                        eq(eventHandler1.getName()),
                        valueCaptor.capture());

        assertEquals(
                RedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
                keyCaptor.getValue()); // check the correct key was used
        assertEquals(
                RedisEventHandlerDAO.toJson(eventHandler1),
                valueCaptor.getValue()); // check the correct json was stored

        // check that the event handler was also indexed
        verify(jedisProxy, times(1))
                .sadd(
                        RedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
                        eventHandler1.getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_whenEventHandlerExists() {
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L); // mock that it already exists
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler1)); // mock the existing json

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler1)); // mock the existing json

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(jedisProxy, times(1))
                .hset(
                        keyCaptor.capture(),
                        eq(eventHandler2.getName()),
                        valueCaptor.capture());

        assertEquals(
                RedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
                keyCaptor.getValue()); // check the correct key was used
        assertEquals(
                RedisEventHandlerDAO.toJson(eventHandler2),
                valueCaptor.getValue()); // check the correct json was stored

        // check that the event handler was re-indexed
        verify(jedisProxy, times(1))
                .sadd(
                        RedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler2.getEvent()),
                        eventHandler2.getName());

        // check that the old index was removed
        verify(jedisProxy, times(1))
                .srem(
                        RedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
                        eventHandler1.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_whenEventHandlerDoesNotExist() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null); // mock that it doesn't exist

        redisEventHandlerDAO.updateEventHandler(eventHandler2);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hdel(anyString(), anyString()))
                .thenReturn(1L); // mock successful operation
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler1)); // mock the existing json

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

        // check that the event handler was removed
        verify(jedisProxy, times(1))
                .hdel(
                        RedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
                        eventHandler1.getName());

        // check that the event handler was also unindexed
        verify(jedisProxy, times(1))
                .srem(
                        RedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
                        eventHandler1.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_whenEventHandlerDoesNotExist() {
        when(jedisProxy.hdel(anyString(), anyString()))
                .thenReturn(0L); // mock that it doesn't exist
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlersJson = new HashMap<>();
        eventHandlersJson.put(eventHandler1.getName(), RedisEventHandlerDAO.toJson(eventHandler1));
        eventHandlersJson.put(eventHandler2.getName(), RedisEventHandlerDAO.toJson(eventHandler2));

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(eventHandlersJson);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "event1";

        // mock the existing json
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler1))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler3));

        // mock the index
        when(jedisProxy.smembers(anyString()))
                .thenReturn(new LinkedList<String>() {{
                    add(eventHandler1.getName());
                    add(eventHandler3.getName());
                }});

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler3));
    }

    @Test
    public void testGetEventHandlersForEvent_whenActiveOnly() {
        String event = "event1";

        // mock the existing json
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler1))
                .thenReturn(RedisEventHandlerDAO.toJson(eventHandler3));

        // mock the index
        when(jedisProxy.smembers(anyString()))
                .thenReturn(new LinkedList<String>() {{
                    add(eventHandler1.getName());
                    add(eventHandler3.getName());
                }});

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }
}