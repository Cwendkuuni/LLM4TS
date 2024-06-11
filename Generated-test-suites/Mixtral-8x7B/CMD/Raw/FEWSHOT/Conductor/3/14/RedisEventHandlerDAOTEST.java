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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName()))
                .thenReturn(new EventHandler());

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(anyString(), eq(name));
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(redisEventHandlerDAO, times(1)).removeIndex(any(EventHandler.class));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "testEventHandler";

        when(redisEventHandlerDAO.getEventHandler(name)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        String name1 = "testEventHandler1";
        String name2 = "testEventHandler2";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);

        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put(name1, toJson(eventHandler1));
        eventHandlers.put(name2, toJson(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.contains(eventHandler1));
        assertTrue(allEventHandlers.contains(eventHandler2));
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        String name1 = "testEventHandler1";
        String name2 = "testEventHandler2";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        String key = "EVENT_HANDLERS_BY_EVENT:" + event;
        Set<String> names = new HashSet<>();
        names.add(name1);
        names.add(name2);

        when(jedisProxy.smembers(eq(key))).thenReturn(names);
        when(redisEventHandlerDAO.getEventHandler(name1)).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler(name2)).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "testEvent";
        String name1 = "testEventHandler1";
        String name2 = "testEventHandler2";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        String key = "EVENT_HANDLERS_BY_EVENT:" + event;
        Set<String> names = new HashSet<>();
        names.add(name1);
        names.add(name2);

        when(jedisProxy.smembers(eq(key))).thenReturn(names);
        when(redisEventHandlerDAO.getEventHandler(name1)).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler(name2)).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventInactiveOnly() {
        String event = "testEvent";
        String name1 = "testEventHandler1";
        String name2 = "testEventHandler2";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(false);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(true);

        String key = "EVENT_HANDLERS_BY_EVENT:" + event;
        Set<String> names = new HashSet<>();
        names.add(name1);
        names.add(name2);

        when(jedisProxy.smembers(eq(key))).thenReturn(names);
        when(redisEventHandlerDAO.getEventHandler(name1)).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler(name2)).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventNoMatchingEventHandlers() {
        String event = "testEvent";
        String name1 = "testEventHandler1";
        String name2 = "testEventHandler2";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent("differentEvent");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent("differentEvent");

        String key = "EVENT_HANDLERS_BY_EVENT:" + event;
        Set<String> names = new HashSet<>();
        names.add(name1);
        names.add(name2);

        when(jedisProxy.smembers(eq(key))).thenReturn(names);
        when(redisEventHandlerDAO.getEventHandler(name1)).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler(name2)).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandler() {
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);

        when(jedisProxy.hget(anyString(), eq(name))).thenReturn(toJson(eventHandler));

        EventHandler result = redisEventHandlerDAO.getEventHandler(name);

        assertEquals(eventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String name = "testEventHandler";

        when(jedisProxy.hget(anyString(), eq(name))).thenReturn(null);

        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test
    public void testRemoveIndex() {
        String event = "testEvent";
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(event);

        redisEventHandlerDAO.removeIndex(eventHandler);

        verify(jedisProxy, times(1)).srem(anyString(), eq(name));
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}