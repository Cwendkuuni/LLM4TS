package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
import com.netflix.conductor.redis.config.RedisProperties;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @Mock private JedisProxy jedisProxy;
    @Mock private ObjectMapper objectMapper;
    @Mock private ConductorProperties conductorProperties;
    @Mock private RedisProperties redisProperties;
    @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String JSON_EVENT_HANDLER = "{}";
    private static final String JSON_EVENT_HANDLER_2 = "{}";
    private static final String NAMESPACE = "testNamespace";

    private EventHandler eventHandler;
    private EventHandler eventHandler2;
    private Map<String, String> all;
    private List<EventHandler> handlers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent(EVENT_NAME);

        all = new java.util.HashMap<>();
        all.put(EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);

        handlers = new LinkedList<>();
        handlers.add(eventHandler);
        handlers.add(eventHandler2);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON_EVENT_HANDLER);
        when(objectMapper.readValue(anyString(), any())).thenReturn(eventHandler);
        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>(List.of(EVENT_HANDLER_NAME)));
        when(conductorProperties.getGeneral().getNamespace()).thenReturn(NAMESPACE);
        when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<String> argCaptorName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argCaptorJson = ArgumentCaptor.forClass(String.class);

        verify(jedisProxy, times(1)).hset(argCaptorName.capture(), argCaptorJson.capture());
        assertEquals(NAMESPACE + EVENT_HANDLERS, argCaptorName.getValue());
        assertEquals(JSON_EVENT_HANDLER, argCaptorJson.getValue());
    }

    @Test
    public void testAddEventHandlerConflictException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON_EVENT_HANDLER);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        ArgumentCaptor<String> argCaptorName = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argCaptorJson = ArgumentCaptor.forClass(String.class);

        verify(jedisProxy, times(1)).hset(argCaptorName.capture(), argCaptorJson.capture());
        assertEquals(NAMESPACE + EVENT_HANDLERS, argCaptorName.getValue());
        assertEquals(JSON_EVENT_HANDLER, argCaptorJson.getValue());
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(NAMESPACE + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(handlers.size(), result.size());
        assertEquals(eventHandler.getName(), result.get(0).getName());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        assertNotNull(result);
        assertEquals(eventHandler.getName(), result.getName());
    }

    @Test
    public void testGetEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        assertEquals(handlers.size(), result.size());
        assertEquals(eventHandler.getName(), result.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() {
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);

        assertEquals(handlers.size(), result.size());
        assertEquals(eventHandler.getName(), result.get(0).getName());
    }

    @Test
    public void testExceptionInGetEventHandler() {
        doThrow(new TransientException("test")).when(jedisProxy).hget(anyString(), anyString());

        assertThrows(TransientException.class,
                () -> redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testExceptionInGetEventHandlersForEvent() {
        doThrow(new TransientException("test")).when(jedisProxy).smembers(anyString());

        assertThrows(TransientException.class,
                () -> redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true));
    }
}