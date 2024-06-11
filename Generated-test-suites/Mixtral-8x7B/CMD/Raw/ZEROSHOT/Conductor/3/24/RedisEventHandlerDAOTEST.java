package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisEventHandlerDAOTest {

    private static final String EVENT_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{}";
    private static final String NS_KEY = "testNsKey";

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Map<String, String>> mapArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(redisProperties.getNamespace()).thenReturn(NS_KEY);
        when(conductorProperties.getRedisProperties()).thenReturn(redisProperties);

        redisEventHandlerDAO =
                new RedisEventHandlerDAO(
                        jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L)
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), anyString(), anyString());

        Map<String, String> args = mapArgumentCaptor.getValue();
        assertEquals(2, args.size());
        assertEquals(EVENT_NAME, args.get("name"));
        assertEquals(JSON, args.get("json"));

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(2))
                .hset(stringArgumentCaptor.capture(), anyString(), anyString());
        assertEquals(NS_KEY + "EVENT_HANDLERS:" + EVENT_NAME, stringArgumentCaptor.getAllValues().get(1));
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L)
                .thenReturn(0L);

        assertThrows(
                ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
        verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(JSON)
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), anyString(), anyString());

        Map<String, String> args = mapArgumentCaptor.getValue();
        assertEquals(2, args.size());
        assertEquals(EVENT_NAME, args.get("name"));
        assertEquals(JSON, args.get("json"));

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
        verify(jedisProxy, times(2)).hset(stringArgumentCaptor.capture(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
        verify(jedisProxy, times(1)).hdel(stringArgumentCaptor.capture(), anyString());

        assertEquals(NS_KEY + "EVENT_HANDLERS:" + EVENT_NAME, stringArgumentCaptor.getValue());
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_NAME));
        verify(jedisProxy, never()).hdel(stringArgumentCaptor.capture(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = Mockito.mock(Map.class);
        when(all.values()).thenReturn(new LinkedList<String>() {{add(JSON);}});

        when(jedisProxy.hgetAll(anyString())).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());

        EventHandler eventHandler = eventHandlers.get(0);
        assertEquals(EVENT_NAME, eventHandler.getName());
        assertEquals(JSON, eventHandler.getJson());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

        EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_NAME);
        assertNotNull(result);
        assertEquals(EVENT_NAME, result.getName());
        assertEquals(JSON, result.getJson());
    }

    @Test
    public void testGetEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(EVENT_NAME));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> names = new HashSet<>();
        names.add(EVENT_NAME);

        when(jedisProxy.smembers(anyString())).thenReturn(names);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());

        EventHandler eventHandler = eventHandlers.get(0);
        assertEquals(EVENT_NAME, eventHandler.getName());
        assertEquals(JSON, eventHandler.getJson());
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventTransientException() {
        String key = NS_KEY + "EVENT_HANDLERS_BY_EVENT:" + EVENT;

        doThrow(new RuntimeException()).when(jedisProxy).smembers(key);

        assertThrows(
                TransientException.class,
                () -> redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() {
        Set<String> names = new HashSet<>();
        names.add(EVENT_NAME);

        when(jedisProxy.smembers(anyString())).thenReturn(names);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);
        eventHandler.setActive(false);

        when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        assertEquals(0, eventHandlers.size());
    }
}