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
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    private Jedis jedis;

    @Before
    public void setup() {
        jedis = new Jedis("localhost");
        ReflectionTestUtils.setField(redisEventHandlerDAO, "jedis", jedis);
    }

    @After
    public void teardown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(objectMapper, times(1)).writeValueAsString(anyObject());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        EventHandler existing = new EventHandler();
        existing.setName("testEventHandler");
        existing.setEvent("testEvent");

        when(getEventHandler(eventHandler.getName())).thenReturn(existing);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(getEventHandler("nonExistent")).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("nonExistent");
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");

        Map<String, String> eventHandlerMap = new LinkedHashMap<>();
        eventHandlerMap.put(eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));
        eventHandlerMap.put(eventHandler2.getName(), objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlerMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
        assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());
    }

    private EventHandler getEventHandler(String name) throws IOException {
        String json = jedis.hget(RedisEventHandlerDAO.EVENT_HANDLERS.getBytes(), name.getBytes());

        if (json == null) {
            return null;
        }

        return objectMapper.readValue(json, new TypeReference<EventHandler>() {});
    }
}