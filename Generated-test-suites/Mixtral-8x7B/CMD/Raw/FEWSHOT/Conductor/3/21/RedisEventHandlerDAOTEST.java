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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock private JedisProxy jedisProxy;

    @Mock private ObjectMapper objectMapper;

    @Mock private ConductorProperties conductorProperties;

    @Mock private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NS_KEY = "namespacedKey";
    private static final String NAME = "name";
    private static final String EVENT = "event";
    private static final String JSON = "json";
    private static final EventHandler EVENT_HANDLER = new EventHandler();

    @Before
    public void setUp() {
        when(jedisProxy.hget(NS_KEY(EVENT_HANDLERS), NAME)).thenReturn(JSON);
        when(jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS))).thenReturn(Map.of(NAME, JSON));
        when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(new HashSet<>());
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
        when(redisProperties.getNamespacedKey()).thenReturn(NS_KEY);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
        verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy, times(1)).sadd(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_whenEventHandlerExists() {
        when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
        verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_whenEventHandlerNotFound() {
        when(getEventHandler(NAME)).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(NAME);
        verify(jedisProxy, times(1)).hdel(NS_KEY(EVENT_HANDLERS), NAME);
        verify(jedisProxy, times(1)).srem(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_whenEventHandlerNotFound() {
        when(getEventHandler(NAME)).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, result.size());
        assertEquals(EVENT_HANDLER, result.get(0));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(Set.of(NAME));
        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
        assertEquals(1, result.size());
        assertEquals(EVENT_HANDLER, result.get(0));
    }

    private EventHandler getEventHandler(String name) {
        return redisEventHandlerDAO.getEventHandler(name);
    }

    private String NS_KEY(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(NS_KEY);
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }
}