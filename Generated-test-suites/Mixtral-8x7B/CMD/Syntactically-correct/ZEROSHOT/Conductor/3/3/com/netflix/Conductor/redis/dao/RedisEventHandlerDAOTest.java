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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

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
        jedis.flushDB();
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), anyString());
        verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn("{}");
        when(jedisProxy.sismember(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName()))
                .thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), anyString());
        verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName());
        verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws IOException, NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn("{}");
        when(jedisProxy.sismember(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName()))
                .thenReturn(true);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler.getName());
        verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()),
                eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
                .thenReturn(Map.of(eventHandler1.getName(), "{}", eventHandler2.getName(), "{}"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(2, eventHandlers.size());
        Assert.assertTrue(eventHandlers.contains(eventHandler1));
        Assert.assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEventActive() throws NotFoundException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setActive(true);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
                .thenReturn("{}");
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler2.getName()))
                .thenReturn("{}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(
                "testEvent", true);

        Assert.assertEquals(2, eventHandlers.size());
        Assert.assertTrue(eventHandlers.contains(eventHandler1));
        Assert.assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEventInActive() throws NotFoundException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setActive(false);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setActive(true);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
                .thenReturn("{}");
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler2.getName()))
                .thenReturn("{}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(
                "testEvent", false);

        Assert.assertEquals(1, eventHandlers.size());
        Assert.assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEventNoMatching() throws NotFoundException {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(
                "testEvent", true);

        Assert.assertTrue(eventHandlers.isEmpty());
    }
}