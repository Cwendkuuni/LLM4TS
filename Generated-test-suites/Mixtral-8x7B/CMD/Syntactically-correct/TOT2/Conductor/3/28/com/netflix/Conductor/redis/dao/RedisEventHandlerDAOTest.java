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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

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

    @Before
    public void setUp() {
        when(redisProperties.getNamespace()).thenReturn("test-namespace");
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test-handler"), anyString());
        verify(jedisProxy, times(1)).index(eq(eventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(getEventHandler(eq("test-handler")))
                .thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, TransientException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test-handler");
        existingEventHandler.setEvent("test-event");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("test-handler");
        newEventHandler.setEvent("new-test-event");

        when(getEventHandler(eq("test-handler")))
                .thenReturn(existingEventHandler);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test-handler"), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(eq(existingEventHandler));
        verify(jedisProxy, times(1)).index(eq(newEventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(getEventHandler(eq("test-handler")))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");
        eventHandler.setEvent("test-event");

        when(getEventHandler(eq("test-handler")))
                .thenReturn(eventHandler);

        when(jedisProxy.hdel(anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("test-handler");

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq("test-handler"));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(eq(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        when(getEventHandler(eq("test-handler")))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("test-handler");
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent("test-event-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("test-event-2");

        Map<String, String> all = new HashMap<>();
        all.put("test-handler-1", toJson(eventHandler1));
        all.put("test-handler-2", toJson(eventHandler2));

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("getAllEventHandlers"));
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws TransientException, NotFoundException {
        String event = "test-event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent(event);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("other-event");

        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);

        Set<String> names = new LinkedList<>();
        names.add("test-handler-1");
        names.add("test-handler-2");

        when(jedisProxy.smembers(eq(key)))
                .thenReturn(names);

        when(getEventHandler(eq("test-handler-1")))
                .thenReturn(eventHandler1);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy, times(1)).smembers(eq(key));
        verify(jedisProxy, times(2)).getEventHandler(anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(eq("getEventHandler"), eq(event));
        assertEquals(1, handlers.size());
        assertEquals(event, handlers.get(0).getEvent());
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
            throw new TransientException("Unable to serialize object", e);
        }
    }

    private <T> T readValue(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new TransientException("Unable to deserialize json", e);
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
}