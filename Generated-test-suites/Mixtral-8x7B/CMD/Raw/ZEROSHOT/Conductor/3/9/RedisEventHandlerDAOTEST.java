import com.fasterxml.jackson.databind.ObjectMapper;
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    private JedisCommands jedis;

    @Before
    public void setUp() {
        jedis = new Jedis("localhost");
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException, JedisConnectionException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
        verify(jedisProxy, times(1)).expire(anyString(), anyLong());
        verify(jedisProxy, times(1)).expire(anyString(), anyLong());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException, JedisConnectionException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException, JedisConnectionException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}");
        when(objectMapper.readValue("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}", EventHandler.class))
                .thenReturn(eventHandler);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
        verify(jedisProxy, times(1)).expire(anyString(), anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws IOException, NotFoundException, JedisConnectionException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, JedisConnectionException {
        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}");

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, JedisConnectionException {
        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() throws JedisConnectionException, IOException {
        String json1 = "{\"name\":\"testEventHandler1\",\"event\":\"testEvent1\",\"active\":true}";
        String json2 = "{\"name\":\"testEventHandler2\",\"event\":\"testEvent2\",\"active\":false}";

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("testEventHandler1", json1, "testEventHandler2", json2));
        when(objectMapper.readValue(json1, EventHandler.class)).thenReturn(new EventHandler());
        when(objectMapper.readValue(json2, EventHandler.class)).thenReturn(new EventHandler());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() throws JedisConnectionException, IOException, NotFoundException {
        String json1 = "{\"name\":\"testEventHandler1\",\"event\":\"testEvent\",\"active\":true}";
        String json2 = "{\"name\":\"testEventHandler2\",\"event\":\"testEvent\",\"active\":false}";

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>(List.of("testEventHandler1", "testEventHandler2")));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(json1, json2, null);
        when(objectMapper.readValue(json1, EventHandler.class)).thenReturn(new EventHandler());
        when(objectMapper.readValue(json2, EventHandler.class)).thenReturn(new EventHandler());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws JedisConnectionException, IOException, NotFoundException {
        String json1 = "{\"name\":\"testEventHandler1\",\"event\":\"testEvent\",\"active\":true}";

        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>(List.of("testEventHandler1")));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(json1);
        when(objectMapper.readValue(json1, EventHandler.class)).thenReturn(new EventHandler());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertEquals(1, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() throws JedisConnectionException, IOException, NotFoundException {
        when(conductorProperties.isEventHandlersEnabled()).thenReturn(true);
        when(redisProperties.isEventHandlersEnabled()).thenReturn(true);
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(anyString());
    }
}