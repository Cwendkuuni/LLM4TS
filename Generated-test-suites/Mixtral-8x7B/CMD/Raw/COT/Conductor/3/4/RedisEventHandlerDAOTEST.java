import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.events.EventType;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(redisProperties.getPrefix()).thenReturn("conductor:redis:");

        redisEventHandlerDAO =
                new RedisEventHandlerDAO(
                        jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null)
                .thenThrow(new TransientException("Test exception"))
                .thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), anyString(), anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");

        String name = stringArgumentCaptor.getValue();
        assertTrue(name.startsWith("conductor:redis:EVENT_HANDLERS"));

        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("test-handler"));

        // Test for conflict exception
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existing-handler");
        existingEventHandler.setEvent("test-event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            assertTrue(false);
        } catch (ConflictException e) {
            assertEquals(
                    "EventHandler with name test-handler already exists!", e.getMessage());
        }

        // Test for transient exception
        doThrow(new TransientException("Test exception")).when(jedisProxy).index(eventHandler);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            assertTrue(false);
        } catch (TransientException e) {
            assertEquals("Unable to add event handler named test-handler", e.getMessage());
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, never()).index(eventHandler);
        verify(jedisProxy, never()).recordRedisDaoRequests("index");

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existing-handler");
        existingEventHandler.setEvent("test-event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("index");

        verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), anyString(), anyString());
        String name = stringArgumentCaptor.getValue();
        assertTrue(name.startsWith("conductor:redis:EVENT_HANDLERS"));

        // Test for not found exception
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            assertTrue(false);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name test-handler not found!", e.getMessage());
        }
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler("test-handler");

        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
        verify(jedisProxy, times(1)).hdel(stringArgumentCaptor.capture(), anyString());
        verify(jedisProxy, times(1)).removeIndex(any(EventHandler.class));

        String name = stringArgumentCaptor.getValue();
        assertTrue(name.startsWith("conductor:redis:EVENT_HANDLERS"));

        // Test for not found exception
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler("test-handler");
            assertTrue(false);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name test-handler not found!", e.getMessage());
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent("test-event-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("test-event-2");

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(Map.of("test-handler-1", toJson(eventHandler1)))
                .thenReturn(Map.of("test-handler-2", toJson(eventHandler2)));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(2)).recordRedisDaoRequests("getAllEventHandlers");

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent("test-event-1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString()))
                .thenReturn(new HashSet<>(Arrays.asList("test-handler-1", "test-handler-2")));

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(toJson(eventHandler1))
                .thenReturn(toJson(eventHandler2));

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("test-event-1", true);

        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", "test-event-1");

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event-1", false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("nonexistent-event", true);

        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", "nonexistent-event");

        assertTrue(eventHandlers.isEmpty());
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}