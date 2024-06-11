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
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;

    @Before
    public void setup() {
        when(conductorProperties.getLockExpiry()).thenReturn(1000L);
        when(redisProperties.getEventHandlersNs()).thenReturn("EVENT_HANDLERS_NAMESPACE");

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
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null)
                .thenThrow(new RuntimeException());

        redisEventHandlerDAO.addEventHandler(eventHandler1);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

        assertThrows(
                ConflictException.class,
                () -> redisEventHandlerDAO.addEventHandler(eventHandler1));
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn("{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}")
                .thenThrow(new RuntimeException());

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.updateEventHandler(eventHandler3));
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn("{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}")
                .thenThrow(new RuntimeException());

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.removeEventHandler("eh4"));
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlersJson = new HashMap<>();
        eventHandlersJson.put("eh1", "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}");
        eventHandlersJson.put("eh2", "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}");

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(eventHandlersJson)
                .thenThrow(new RuntimeException());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

        assertEquals(2, eventHandlers.size());

        assertThrows(TransientException.class, () -> redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Map<String, String> eventHandlersJson = new HashMap<>();
        eventHandlersJson.put("eh1", "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}");
        eventHandlersJson.put("eh2", "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}");

        String eventKey = "EVENT_HANDLERS_NAMESPACE_EVENT_EVENT1";
        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(eventHandlersJson);
        when(jedisProxy.smembers(anyString()))
                .thenReturn(Arrays.asList("eh1", "eh3"));
        when(jedisProxy.hget(eventKey, "eh1"))
                .thenReturn("{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}");

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        verify(jedisProxy, times(1)).hgetAll(anyString());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(eventKey, "eh1");
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }
}