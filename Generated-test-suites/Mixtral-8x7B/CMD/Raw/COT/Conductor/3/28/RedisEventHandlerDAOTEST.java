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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

@Component
@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String JSON_DATA = "{}";
    private static final String NS_KEY = "testNsKey";

    @Mock private JedisProxy jedisProxy;
    @Mock private ObjectMapper objectMapper;
    @Mock private ConductorProperties conductorProperties;
    @Mock private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Captor private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(redisProperties.getEventsNs()).thenReturn(NS_KEY);

        redisEventHandlerDAO =
                new RedisEventHandlerDAO(
                        jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        when(objectMapper.createObjectNode()).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(objectMapper.convertValue(any(EventHandler.class), any(Class.class))).thenReturn(JSON_DATA);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(NS_KEY + EventRedisDAO.EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON_DATA);
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        when(objectMapper.createObjectNode()).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(objectMapper.convertValue(any(EventHandler.class), any(Class.class))).thenReturn(JSON_DATA);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);

        assertThrows(
                ConflictException.class,
                () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy, never()).index(eventHandler);
        verify(jedisProxy, never()).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT_NAME);

        when(objectMapper.createObjectNode()).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(objectMapper.convertValue(any(EventHandler.class), any(Class.class))).thenReturn(JSON_DATA);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(existingEventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(NS_KEY + EventRedisDAO.EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON_DATA);
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        when(objectMapper.createObjectNode()).thenReturn(mock(com.fasterxml.jackson.databind.JsonNode.class));
        when(objectMapper.convertValue(any(EventHandler.class), any(Class.class))).thenReturn(JSON_DATA);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        verify(jedisProxy, never()).index(eventHandler);
        verify(jedisProxy, never()).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(NS_KEY + EventRedisDAO.EVENT_HANDLERS, EVENT_HANDLER_NAME);
        verify(jedisProxy, times(1)).removeIndex(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));

        verify(jedisProxy, never()).removeIndex(any(EventHandler.class));
        verify(jedisProxy, never()).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put(UUID.randomUUID().toString(), JSON_DATA);

        when(jedisProxy.hgetAll(NS_KEY + EventRedisDAO.EVENT_HANDLERS)).thenReturn(eventHandlers);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, allEventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(NS_KEY + EventRedisDAO.EVENT_HANDLERS);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "testEvent";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(UUID.randomUUID().toString());
        eventHandler1.setEvent(eventName);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(UUID.randomUUID().toString());
        eventHandler2.setEvent(eventName);

        when(jedisProxy.smembers(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + eventName))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));
        when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName()))
                .thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler(eventHandler2.getName()))
                .thenReturn(eventHandler2);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + eventName);
        verify(jedisProxy, times(2)).getEventHandler(anyString());
        verify(jedisProxy, times(2)).recordRedisDaoEventRequests("getEventHandler", eventName);
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        String eventName = "testEvent";

        when(jedisProxy.smembers(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + eventName))
                .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + eventName);
        verify(jedisProxy, never()).getEventHandler(anyString());
        verify(jedisProxy, never()).recordRedisDaoEventRequests("getEventHandler", eventName);
    }

    @Test
    public void testGetEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(EVENT_NAME);

        when(jedisProxy.hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name)).thenReturn(JSON_DATA);
        when(objectMapper.convertValue(JSON_DATA, EventHandler.class)).thenReturn(eventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler(name);

        assertEquals(eventHandler, result);
        verify(jedisProxy, times(1)).hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name);
        verify(objectMapper, times(1)).convertValue(JSON_DATA, EventHandler.class);
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT_NAME);
    }

    @Test
    public void testGetEventHandlerNotFoundException() {
        String name = UUID.randomUUID().toString();

        when(jedisProxy.hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name)).thenReturn(null);

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.getEventHandler(name));

        verify(jedisProxy, times(1)).hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name);
        verify(objectMapper, never()).convertValue(anyString(), any(Class.class));
        verify(jedisProxy, never()).recordRedisDaoEventRequests("getEventHandler", anyString());
    }

    @Test
    public void testGetEventHandlerTransientException() {
        String name = UUID.randomUUID().toString();

        when(jedisProxy.hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name)).thenThrow(new RuntimeException());

        assertThrows(
                TransientException.class,
                () -> redisEventHandlerDAO.getEventHandler(name));

        verify(jedisProxy, times(1)).hget(NS_KEY + EventRedisDAO.EVENT_HANDLERS, name);
        verify(objectMapper, never()).convertValue(anyString(), any(Class.class));
        verify(jedisProxy, never()).recordRedisDaoEventRequests("getEventHandler", anyString());
    }

    @Test
    public void testIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(UUID.randomUUID().toString());
        eventHandler.setEvent(EVENT_NAME);

        redisEventHandlerDAO.index(eventHandler);

        verify(jedisProxy, times(1))
                .sadd(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + EVENT_NAME, eventHandler.getName());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("index");
    }

    @Test
    public void testRemoveIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(UUID.randomUUID().toString());
        eventHandler.setEvent(EVENT_NAME);

        redisEventHandlerDAO.removeIndex(eventHandler);

        verify(jedisProxy, times(1))
                .srem(NS_KEY + EventRedisDAO.EVENT_HANDLERS_BY_EVENT + EVENT_NAME, eventHandler.getName());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeIndex");
    }
}