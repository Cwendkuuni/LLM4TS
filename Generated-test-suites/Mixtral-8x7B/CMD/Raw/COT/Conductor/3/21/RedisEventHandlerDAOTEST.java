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
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

    private Jedis jedis;

    @Before
    public void setUp() {
        jedis = new Jedis();
        jedisProxy.setJedis(jedis);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        String json = "{}";
        when(objectMapper.valueToTree(eventHandler)).thenReturn(json);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), "testEventHandler", json);
        verify(jedisProxy, times(1)).hget(nsKey("EVENT_HANDLERS"), "testEventHandler");
        verify(jedisProxy, times(1)).sadd(nsKey("EVENT_HANDLERS_BY_EVENT", eventHandler.getEvent()), "testEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        String json = "{}";
        when(objectMapper.valueToTree(eventHandler)).thenReturn(json);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), "testEventHandler", json);
        verify(jedisProxy, times(1)).hget(nsKey("EVENT_HANDLERS"), "testEventHandler");
        verify(jedisProxy, times(1)).srem(nsKey("EVENT_HANDLERS_BY_EVENT", eventHandler.getEvent()), "testEventHandler");
        verify(jedisProxy, times(1)).sadd(nsKey("EVENT_HANDLERS_BY_EVENT", eventHandler.getEvent()), "testEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        when(getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(nsKey("EVENT_HANDLERS"), "testEventHandler");
        verify(jedisProxy, times(1)).srem(nsKey("EVENT_HANDLERS_BY_EVENT", eventHandler.getEvent()), "testEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFoundException() {
        when(getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlersMap = new HashMap<>();
        eventHandlersMap.put("testEventHandler1", "{}");
        eventHandlersMap.put("testEventHandler2", "{}");
        when(jedisProxy.hgetAll(nsKey("EVENT_HANDLERS"))).thenReturn(eventHandlersMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(nsKey("EVENT_HANDLERS"));
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        Map<String, String> eventHandlersMap = new HashMap<>();
        eventHandlersMap.put("testEventHandler1", "{}");
        eventHandlersMap.put("testEventHandler2", "{}");
        when(jedisProxy.smembers(nsKey("EVENT_HANDLERS_BY_EVENT", anyString()))).thenReturn(set("testEventHandler1", "testEventHandler2"));
        when(jedisProxy.hget(nsKey("EVENT_HANDLERS"), anyString())).thenReturn("{}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        verify(jedisProxy, times(2)).hget(nsKey("EVENT_HANDLERS"), anyString());
        verify(jedisProxy, times(1)).smembers(nsKey("EVENT_HANDLERS_BY_EVENT", "testEvent"));
        assertEquals(2, eventHandlers.size());
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append("conductor:");
        for (String key : keys) {
            sb.append(key).append(":");
        }
        return sb.toString();
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(nsKey("EVENT_HANDLERS"), name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = readValue(json, EventHandler.class);
        }
        return eventHandler;
    }
}