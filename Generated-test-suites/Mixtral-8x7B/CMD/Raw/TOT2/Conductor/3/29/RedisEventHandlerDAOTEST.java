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
import redis.clients.jedis.JedisProxyImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

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

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxyImpl jedisProxyImpl;

    @Before
    public void setUp() {
        jedisProxyImpl = new JedisProxyImpl();
        jedisProxyImpl.setJedis(new Jedis("localhost"));
        ReflectionTestUtils.setField(redisEventHandlerDAO, "jedisProxy", jedisProxyImpl);
    }

    @After
    public void tearDown() {
        jedisProxyImpl.getJedis().flushAll();
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test-event-handler"), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");

        when(getEventHandler(eq("test-event-handler"))).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test-event-handler");
        existingEventHandler.setEvent("test-event");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("test-event-handler");
        newEventHandler.setEvent("test-event-new");

        when(getEventHandler(eq("test-event-handler"))).thenReturn(existingEventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test-event-handler"), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("test-event-handler");
        newEventHandler.setEvent("test-event-new");

        when(getEventHandler(eq("test-event-handler"))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");

        when(getEventHandler(eq("test-event-handler"))).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler("test-event-handler");

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq("test-event-handler"));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
        verify(jedisProxy, times(1)).removeIndex(eq(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(getEventHandler(eq("test-event-handler"))).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("test-event-handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent("test-event-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");

        Map<String, String> data = new HashMap<>();
        data.put("test-event-handler-1", toJson(eventHandler1));
        data.put("test-event-handler-2", toJson(eventHandler2));

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(data);
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenAnswer(invocation -> {
            String json = invocation.getArgument(0);
            if (json.equals(toJson(eventHandler1))) {
                return eventHandler1;
            }
            return eventHandler2;
        });

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "test-event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent(event);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(true);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(
                new HashSet<>(Arrays.asList("test-event-handler-1", "test-event-handler-2")));
        when(getEventHandler(eq("test-event-handler-1"))).thenReturn(eventHandler1);
        when(getEventHandler(eq("test-event-handler-2"))).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler2, eventHandlers.get(0));
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verify(jedisProxy, times(2)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    @Test
    public void testGetEventHandlersForEventActiveFalse() {
        String event = "test-event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent(event);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(
                new HashSet<>(Arrays.asList("test-event-handler-1", "test-event-handler-2")));
        when(getEventHandler(eq("test-event-handler-1"))).thenReturn(eventHandler1);
        when(getEventHandler(eq("test-event-handler-2"))).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verify(jedisProxy, times(2)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    @Test
    public void testGetEventHandlersForEventNoActive() {
        String event = "test-event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent(event);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(
                new HashSet<>(Arrays.asList("test-event-handler-1")));
        when(getEventHandler(eq("test-event-handler-1"))).thenReturn(eventHandler1);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    @Test
    public void testGetEventHandlersForEventNoMatching() {
        String event = "test-event";

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(redisProperties.getNamespace());
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(nsKey(EVENT_HANDLERS), name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = readValue(json, EventHandler.class);
        }
        return eventHandler;
    }

    private <T> T readValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}