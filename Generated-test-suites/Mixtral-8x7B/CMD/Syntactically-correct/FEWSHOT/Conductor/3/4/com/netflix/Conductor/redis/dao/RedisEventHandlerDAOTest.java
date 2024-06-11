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
import org.springframework.context.annotation.Conditional;
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
    private static final String EVENT = "event1";
    private static final String NAME = "handler1";
    private static final String JSON = "{}";
    private static final EventHandler EVENT_HANDLER = new EventHandler();

    @Before
    public void setUp() {
        when(redisProperties.getPrefix()).thenReturn("prefix");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(EVENT_HANDLER);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
        verify(jedisProxy, times(1))
                .hset(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                        eq(NAME),
                        eq(JSON));
        verify(jedisProxy, times(1))
                .sadd(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)),
                        eq(NAME));
        verify(jedisProxy, times(1))
                .publish(
                        eq(redisEventHandlerDAO.redisChannels.getRedisDaoRequestsChannel()),
                        eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(redisEventHandlerDAO.getEventHandler(NAME))
                .thenReturn(EVENT_HANDLER);
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testUpdateEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(NAME))
                .thenReturn(EVENT_HANDLER);
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
        verify(jedisProxy, times(1))
                .hset(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                        eq(NAME),
                        eq(JSON));
        verify(jedisProxy, times(1))
                .srem(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)),
                        eq(NAME));
        verify(jedisProxy, times(1))
                .sadd(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)),
                        eq(NAME));
        verify(jedisProxy, times(1))
                .publish(
                        eq(redisEventHandlerDAO.redisChannels.getRedisDaoRequestsChannel()),
                        eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(NAME))
                .thenReturn(EVENT_HANDLER);
        redisEventHandlerDAO.removeEventHandler(NAME);
        verify(jedisProxy, times(1))
                .hdel(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                        eq(NAME));
        verify(jedisProxy, times(1))
                .srem(
                        eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)),
                        eq(NAME));
        verify(jedisProxy, times(1))
                .publish(
                        eq(redisEventHandlerDAO.redisChannels.getRedisDaoRequestsChannel()),
                        eq("removeEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler(NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> map = new HashMap<>();
        map.put(NAME, JSON);
        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(map);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> names = new HashSet<>();
        names.add(NAME);
        when(jedisProxy.smembers(anyString()))
                .thenReturn(names);
        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(JSON);
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(NAME);
        assertEquals(EVENT_HANDLER, eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        doThrow(new RuntimeException())
                .when(jedisProxy)
                .hget(anyString(), anyString());
        redisEventHandlerDAO.getEventHandler(NAME);
    }
}