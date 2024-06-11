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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;
    private List<EventHandler> eventHandlers;

    @Before
    public void setup() {
        eventHandler1 = new EventHandler();
        eventHandler1.setName("eh1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("eh2");
        eventHandler2.setEvent("event2");
        eventHandler2.setActive(false);

        eventHandler3 = new EventHandler();
        eventHandler3.setName("eh3");
        eventHandler3.setEvent("event3");
        eventHandler3.setActive(true);

        eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);
        eventHandlers.add(eventHandler3);
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler1);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).index(any(EventHandler.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("oldJson");
        when(objectMapper.readValue("oldJson", EventHandler.class)).thenReturn(eventHandler1);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(objectMapper, times(1)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).index(any(EventHandler.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(0L);

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        Map<String, String> all =
                Map.of("eh1", "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}",
                        "eh2", "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}",
                        "eh3", "{\"name\":\"eh3\",\"event\":\"event3\",\"active\":true}");

        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler1);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers, result);
        verify(jedisProxy, times(1)).hgetAll(anyString());
        verify(objectMapper, times(3)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
        String event = "event1";
        Set<String> names = new HashSet<>();
        names.add("eh1");
        names.add("eh3");

        when(jedisProxy.smembers(anyString())).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"eh1\"," +
                "\"event\":\"event1\",\"active\":true}");
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler1);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, result.size());
        assertEquals(eventHandler1, result.get(0));
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(objectMapper, times(1)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() throws NotFoundException, TransientException {
        String event = "event1";
        Set<String> names = new HashSet<>();
        names.add("eh1");
        names.add("eh3");

        when(jedisProxy.smembers(anyString())).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"eh1\"," +
                "\"event\":\"event1\",\"active\":true}");
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler1);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, result.size());
        assertEquals(eventHandler1, result.get(0));
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(objectMapper, times(1)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEventNoMatch() throws NotFoundException, TransientException {
        String event = "event4";
        Set<String> names = new HashSet<>();
        names.add("eh1");
        names.add("eh3");

        when(jedisProxy.smembers(anyString())).thenReturn(names);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, result.size());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(0)).hget(anyString(), anyString());
        verify(objectMapper, times(0)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() throws NotFoundException, TransientException {
        String event = "event4";
        Set<String> names = new HashSet<>();
        names.add("eh1");
        names.add("eh3");

        when(jedisProxy.smembers(anyString())).thenReturn(names);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"eh1\"," +
                "\"event\":\"event1\",\"active\":true}");

        redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }
}