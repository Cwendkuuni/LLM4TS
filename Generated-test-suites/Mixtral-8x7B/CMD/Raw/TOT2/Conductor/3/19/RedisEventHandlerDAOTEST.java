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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

    private String namespace = "myNamespace";

    @Before
    public void setUp() {
        initMocks(this);
        when(conductorProperties.getNamespace()).thenReturn(namespace);
        when(redisProperties.getEventsPrefix()).thenReturn("EVENT_HANDLERS");
        when(redisProperties.getEventsByEventPrefix()).thenReturn("EVENT_HANDLERS_BY_EVENT");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eh1");
        eventHandler.setEvent("event1");

        when(jedisProxy.hset(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"), any())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"), any());
        verify(objectMapper).writeValueAsString(eventHandler);
        verify(jedisProxy).recordCommand("addEventHandler");
    }

    @Test
    public void testAddEventHandlerConflictException() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eh1");
        eventHandler.setEvent("event1");

        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1")))
                .thenReturn("{\"name\":\"eh1\",\"event\":\"event1\"}");

        assertThrows(
                ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        String originalJson = "{\"name\":\"eh1\",\"event\":\"event1\"}";
        EventHandler originalEventHandler = objectMapper.readValue(originalJson, EventHandler.class);
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("eh1");
        updatedEventHandler.setEvent("event2");

        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"))).thenReturn(originalJson);
        when(jedisProxy.hset(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"), any())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
        verify(jedisProxy).hset(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"), any());
        verify(objectMapper).writeValueAsString(updatedEventHandler);
        verify(jedisProxy).recordCommand("updateEventHandler");
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() throws Exception {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("eh1");
        updatedEventHandler.setEvent("event2");

        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1")))
                .thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));

        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String json = "{\"name\":\"eh1\",\"event\":\"event1\"}";
        EventHandler eventHandler = objectMapper.readValue(json, EventHandler.class);

        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"))).thenReturn(json);

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
        verify(jedisProxy).hdel(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
        verify(jedisProxy).recordCommand("removeEventHandler");
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() throws Exception {

        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1")))
                .thenReturn(null);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("eh1"));

        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        String json1 = "{\"name\":\"eh1\",\"event\":\"event1\"}";
        String json2 = "{\"name\":\"eh2\",\"event\":\"event2\"}";
        EventHandler eventHandler1 = objectMapper.readValue(json1, EventHandler.class);
        EventHandler eventHandler2 = objectMapper.readValue(json2, EventHandler.class);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("eh1", json1);
        map.put("eh2", json2);

        when(jedisProxy.hgetAll(eq(namespace + ":EVENT_HANDLERS"))).thenReturn(map);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertEquals(eventHandler1, allEventHandlers.get(0));
        assertEquals(eventHandler2, allEventHandlers.get(1));
        verify(jedisProxy).hgetAll(eq(namespace + ":EVENT_HANDLERS"));
        verify(jedisProxy).recordCommand("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String json1 = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";
        String json2 = "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}";
        EventHandler eventHandler1 = objectMapper.readValue(json1, EventHandler.class);
        EventHandler eventHandler2 = objectMapper.readValue(json2, EventHandler.class);

        String key1 = namespace + ":EVENT_HANDLERS_BY_EVENT:event1";
        String key2 = namespace + ":EVENT_HANDLERS_BY_EVENT:event2";

        when(jedisProxy.smembers(eq(key1))).thenReturn(Arrays.asList("eh1"));
        when(jedisProxy.smembers(eq(key2))).thenReturn(Arrays.asList("eh2"));
        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"))).thenReturn(json1);
        when(jedisProxy.hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh2"))).thenReturn(json2);

        List<EventHandler> eventHandlersForEvent =
                redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(1, eventHandlersForEvent.size());
        assertEquals(eventHandler1, eventHandlersForEvent.get(0));

        verify(jedisProxy).smembers(eq(key1));
        verify(jedisProxy).hget(eq(namespace + ":EVENT_HANDLERS"), eq("eh1"));
        verify(jedisProxy).recordCommand("getEventHandler");
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() throws Exception {
        when(jedisProxy.smembers(eq(namespace + ":EVENT_HANDLERS_BY_EVENT:event1")))
                .thenReturn(new LinkedList<>());

        assertThrows(
                NotFoundException.class,
                () ->
                        redisEventHandlerDAO.getEventHandlersForEvent("event1", false));

        verify(jedisProxy).smembers(eq(namespace + ":EVENT_HANDLERS_BY_EVENT:event1"));
    }
}