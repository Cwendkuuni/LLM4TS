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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

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

    private Jedis jedis;

    @Before
    public void setUp() {
        jedis = new Jedis();
        ReflectionTestUtils.setField(redisEventHandlerDAO, "jedisProxy", jedisProxy);
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws ConflictException, NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");
        eventHandler.setEvent("test_event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        when(objectMapper.canSerialize(any())).thenReturn(true);
        when(objectMapper.convertValue(any(), any())).thenReturn(new HashMap<>());
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"));
        verify(objectMapper, times(1)).canSerialize(eq(EventHandler.class));
        verify(objectMapper, times(1)).convertValue(eq(eventHandler), eq(Map.class));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");
        eventHandler.setEvent("test_event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws ConflictException, NotFoundException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test_event_handler");
        existingEventHandler.setEvent("test_event_old");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("test_event_handler");
        newEventHandler.setEvent("test_event_new");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler)).thenReturn(null);
        when(objectMapper.canSerialize(any())).thenReturn(true);
        when(objectMapper.convertValue(any(), any())).thenReturn(new HashMap<>());
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"));
        verify(objectMapper, times(1)).canSerialize(eq(EventHandler.class));
        verify(objectMapper, times(1)).convertValue(eq(newEventHandler), eq(Map.class));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws ConflictException, NotFoundException {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("test_event_handler");
        newEventHandler.setEvent("test_event_new");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test_event_handler");
        existingEventHandler.setEvent("test_event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.removeEventHandler("test_event_handler");

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"));
        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq("test_event_handler"));
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "test_event")), eq("test_event_handler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("test_event_handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test_event_handler_1");
        eventHandler1.setEvent("test_event_1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test_event_handler_2");
        eventHandler2.setEvent("test_event_2");

        Map<String, String> eventHandlersJson = new HashMap<>();
        eventHandlersJson.put("test_event_handler_1", toJson(eventHandler1));
        eventHandlersJson.put("test_event_handler_2", toJson(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersJson);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test_event_handler_1");
        eventHandler1.setEvent("test_event_1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test_event_handler_2");
        eventHandler2.setEvent("test_event_2");
        eventHandler2.setActive(false);

        Map<String, String> eventHandlersJson = new HashMap<>();
        eventHandlersJson.put("test_event_handler_1", toJson(eventHandler1));
        eventHandlersJson.put("test_event_handler_2", toJson(eventHandler2));

        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>(List.of("test_event_handler_1", "test_event_handler_2")));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler1), toJson(eventHandler2));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event_1", true);

        assertEquals(1, eventHandlers.size());
        assertEquals("test_event_handler_1", eventHandlers.get(0).getName());
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error("Failed to serialize object", e);
            return "";
        }
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(redisProperties.getNamespace());
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }
}