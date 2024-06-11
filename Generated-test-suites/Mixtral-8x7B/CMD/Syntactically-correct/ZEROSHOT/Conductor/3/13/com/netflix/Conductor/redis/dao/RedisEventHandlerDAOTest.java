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

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    RedisProperties redisProperties;

    @InjectMocks
    RedisEventHandlerDAO redisEventHandlerDAO;

    Jedis jedis;

    @Before
    public void setUp() {
        jedis = new Jedis();
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws ConflictException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException());

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        Map<String, String> eventHandlers = Map.of("testEventHandler1", "{}",
                "testEventHandler2", "{}");

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, IOException {
        String event = "testEvent";
        String eventHandlerName1 = "testEventHandler1";
        String eventHandlerName2 = "testEventHandler2";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler1", "{}",
                "testEventHandler2", "{}"
        );

        Set<String> names = new HashSet<>();
        names.add(eventHandlerName1);
        names.add(eventHandlerName2);

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(eventHandlerName1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(eventHandlerName2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(key)).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler1, eventHandler2);

        List<EventHandler> eventHandlersForEvent =
                redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlersForEvent.size());
        assertEquals(eventHandlerName1, eventHandlersForEvent.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() throws NotFoundException, IOException {
        String event = "testEvent";
        String eventHandlerName1 = "testEventHandler1";
        String eventHandlerName2 = "testEventHandler2";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler1", "{}",
                "testEventHandler2", "{}"
        );

        Set<String> names = new HashSet<>();
        names.add(eventHandlerName1);
        names.add(eventHandlerName2);

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(eventHandlerName1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(eventHandlerName2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(key)).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler1, eventHandler2);

        List<EventHandler> eventHandlersForEvent =
                redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlersForEvent.size());
        assertEquals(eventHandlerName1, eventHandlersForEvent.get(0).getName());
        assertEquals(eventHandlerName2, eventHandlersForEvent.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEventNoMatchingEventHandlers() throws NotFoundException, IOException {
        String event = "testEvent";
        String eventHandlerName = "testEventHandler";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler", "{}"
        );

        Set<String> names = new HashSet<>();
        names.add(eventHandlerName);

        when(jedisProxy.smembers(key)).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        List<EventHandler> eventHandlersForEvent =
                redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlersForEvent.size());
    }
}