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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NS_KEY = "conductor";
    private static final String NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}";
    private static final EventHandler EVENT_HANDLER = new EventHandler().setName(NAME).setEvent(EVENT);
    private static final String KEY = NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT;

    @Before
    public void setup() {
        when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, NAME)).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
        when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT, EVENT)).thenReturn(NAME);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
        verify(jedisProxy, times(1)).sadd(KEY, NAME);
        verify(jedisProxy, times(1)).expire(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT, 3600);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
        verify(jedisProxy, times(1)).srem(KEY, NAME);
        verify(jedisProxy, times(1)).sadd(KEY, NAME);
        verify(jedisProxy, times(1)).expire(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT, 3600);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(getEventHandler(NAME)).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy, times(1)).hdel(NS_KEY + ":" + EVENT_HANDLERS, NAME);
        verify(jedisProxy, times(1)).srem(KEY, NAME);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(getEventHandler(NAME)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = new HashMap<>();
        all.put(NAME, JSON);
        when(jedisProxy.hgetAll(NS_KEY + ":" + EVENT_HANDLERS)).thenReturn(all);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, allEventHandlers.size());
        assertEquals(NAME, allEventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> names = new HashSet<>();
        names.add(NAME);
        when(jedisProxy.smembers(KEY)).thenReturn(names);

        List<EventHandler> eventHandlersForEvent =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlersForEvent.size());
        assertEquals(NAME, eventHandlersForEvent.get(0).getName());
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = objectMapper.readValue(json, EventHandler.class);
        }
        return eventHandler;
    }
}