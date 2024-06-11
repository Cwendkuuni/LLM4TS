import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock private JedisProxy jedisProxy;

    @Mock private ObjectMapper objectMapper;

    @Mock private ConductorProperties conductorProperties;

    @Mock private RedisProperties redisProperties;

    @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{}";
    private static final EventHandler EVENT_HANDLER = new EventHandler();
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    @Before
    public void setup() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_NAME)).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
    }

    @Test
    public void testAddEventHandler() {
        when(getEventHandler(EVENT_NAME)).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), EVENT_NAME, JSON);
        verify(jedisProxy, times(1)).index(EVENT_HANDLER);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExisting() {
        when(getEventHandler(EVENT_NAME)).thenReturn(EVENT_HANDLER);

        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testUpdateEventHandler() {
        when(getEventHandler(EVENT_NAME)).thenReturn(EVENT_HANDLER);

        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), EVENT_NAME, JSON);
        verify(jedisProxy, times(1)).index(EVENT_HANDLER);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(getEventHandler(EVENT_NAME)).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testRemoveEventHandler() {
        when(getEventHandler(EVENT_NAME)).thenReturn(EVENT_HANDLER);

        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), EVENT_NAME);
        verify(jedisProxy, times(1)).removeIndex(EVENT_HANDLER);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(getEventHandler(EVENT_NAME)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = new LinkedList<String, String>() {{
            put(EVENT_NAME, JSON);
        }};

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);
        when(objectMapper.readValue(JSON, new TypeReference<List<EventHandler>>() {})).thenReturn(new LinkedList<>() {{
            add(EVENT_HANDLER);
        }});

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testIndex() {
        Set<String> names = new HashSet<String>() {{
            add(EVENT_NAME);
        }};

        redisEventHandlerDAO.index(EVENT_HANDLER);

        verify(jedisProxy, times(1))
                .sadd(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), EVENT_NAME);
    }

    @Test
    public void testRemoveIndex() {
        redisEventHandlerDAO.removeIndex(EVENT_HANDLER);

        verify(jedisProxy, times(1))
                .srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), EVENT_NAME);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> names = new HashSet<String>() {{
            add(EVENT_NAME);
        }};

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        Set<String> names = new HashSet<String>() {{
            add(EVENT_NAME);
        }};

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(LOGGER.getName()).append(":");
        for (String key : keys) {
            sb.append(key).append(":");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
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
            eventHandler = objectMapper.readValue(json, EventHandler.class);
        }
        return eventHandler;
    }
}