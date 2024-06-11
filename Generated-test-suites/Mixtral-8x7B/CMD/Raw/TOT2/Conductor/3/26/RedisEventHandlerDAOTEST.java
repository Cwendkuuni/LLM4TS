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
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    private String eventHandlerName = "eventHandlerName";
    private String event = "event";
    private EventHandler eventHandler;
    private List<EventHandler> eventHandlers;
    private Map<String, String> eventHandlersJsonMap;

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);
        eventHandler.setEvent(event);

        eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler);

        eventHandlersJsonMap = new HashMap<>();
        eventHandlersJsonMap.put(eventHandlerName, toJson(eventHandler));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() throws ConflictException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eventHandlerName, toJson(eventHandler));
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eventHandlerName, toJson(eventHandler));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, ConflictException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eventHandlerName, toJson(eventHandler));
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, ConflictException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eventHandlerName);
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventHandlerName);
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersJsonMap);

        List<EventHandler> eventHandlersFound = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(eventHandlers.size(), eventHandlersFound.size());
        assertEquals(eventHandler.getName(), eventHandlersFound.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(eventHandlerName));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));

        List<EventHandler> eventHandlersFound =
                redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(eventHandlers.size(), eventHandlersFound.size());
        assertEquals(eventHandler.getName(), eventHandlersFound.get(0).getName());
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T readValue(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(redisProperties.getNamespace()).append(":");
        for (String key : keys) {
            sb.append(key).append(":");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}