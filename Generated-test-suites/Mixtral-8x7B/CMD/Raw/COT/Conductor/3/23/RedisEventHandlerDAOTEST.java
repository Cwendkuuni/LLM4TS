import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private static final String EVENT_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{}";
    private static final String NS_KEY = "testNsKey";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "eventHandlersKey", EVENT_HANDLERS);
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "eventHandlersByEventKeyPrefix", EVENT_HANDLERS_BY_EVENT);
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setField(
                redisEventHandlerDAO, "redisProperties", redisProperties);

        Jedis jedis = mock(Jedis.class);
        when(jedisProxy.getJedis()).thenReturn(jedis);
        when(jedis.hget(anyString(), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(anyString(), any())).thenReturn(new EventHandler());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        doNothing().when(jedis).hset(anyString(), anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(1, jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS)).size());
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        when(getEventHandler(EVENT_NAME)).thenReturn(eventHandler);

        assertThrows(
                ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        when(getEventHandler(EVENT_NAME)).thenReturn(eventHandler);

        doNothing().when(jedis).hset(anyString(), anyString(), anyString());

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertEquals(1, jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS)).size());
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        when(getEventHandler(EVENT_NAME)).thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        when(getEventHandler(EVENT_NAME)).thenReturn(eventHandler);

        doNothing().when(jedis).hdel(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);

        assertEquals(0, jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS)).size());
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(getEventHandler(EVENT_NAME)).thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_NAME));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        when(jedis.hgetAll(NS_KEY(EVENT_HANDLERS))).thenReturn(Map.of(EVENT_NAME, JSON));
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent(EVENT);

        when(jedis.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(Set.of(EVENT_NAME));
        when(getEventHandler(EVENT_NAME)).thenReturn(eventHandler);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        when(jedis.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(new HashSet<>());

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false));
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(NS_KEY(EVENT_HANDLERS), name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = objectMapper.readValue(json, EventHandler.class);
        }
        return eventHandler;
    }

    private String NS_KEY(String... keys) {
        StringBuilder sb = new StringBuilder(NS_KEY);
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }
}