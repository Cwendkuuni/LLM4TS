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

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    @Before
    public void setup() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "nsKey", "test_ns");
    }

    private EventHandler createEventHandler(String name, String event) {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(event);
        eventHandler.setActive(true);
        return eventHandler;
    }

    @Test
    public void testAddEventHandler() throws ConflictException, IOException {
        EventHandler eventHandler = createEventHandler("test_event_handler", "test_event");
        String json = objectMapper.writeValueAsString(eventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                eq(eventHandler.getName()), eq(json));
        verify(jedisProxy, times(1)).index(eq(eventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, IOException {
        EventHandler eventHandler = createEventHandler("test_event_handler", "test_event");
        String json = objectMapper.writeValueAsString(eventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new ConflictException(""));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws ConflictException, NotFoundException, IOException {
        String name = "test_event_handler";
        EventHandler existingEventHandler = createEventHandler(name, "test_event");
        EventHandler updatedEventHandler = createEventHandler(name, "updated_event");
        String json = objectMapper.writeValueAsString(updatedEventHandler);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(json);
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(updatedEventHandler);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
                eq(updatedEventHandler.getName()), eq(json));
        verify(jedisProxy, times(1)).index(eq(updatedEventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws ConflictException, NotFoundException, IOException {
        String name = "test_event_handler";
        EventHandler updatedEventHandler = createEventHandler(name, "updated_event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        String name = "test_event_handler";
        EventHandler existingEventHandler = createEventHandler(name, "test_event");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(objectMapper.writeValueAsString(existingEventHandler));

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(name));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(eq(existingEventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        String name = "test_event_handler";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        String name1 = "test_event_handler_1";
        String name2 = "test_event_handler_2";
        EventHandler eventHandler1 = createEventHandler(name1, "test_event_1");
        EventHandler eventHandler2 = createEventHandler(name2, "test_event_2");
        Map<String, String> eventHandlersJSON = new LinkedHashMap<>();
        eventHandlersJSON.put(name1, objectMapper.writeValueAsString(eventHandler1));
        eventHandlersJSON.put(name2, objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersJSON);
        when(objectMapper.readValue(anyString(), any(Class.class))).thenAnswer(invocation -> {
            String json = invocation.getArgument(0);
            try {
                return objectMapper.readValue(json, EventHandler.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        verify(jedisProxy, times(1)).hgetAll(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(2)).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "test_event";
        String name1 = "test_event_handler_1";
        String name2 = "test_event_handler_2";
        EventHandler eventHandler1 = createEventHandler(name1, event);
        EventHandler eventHandler2 = createEventHandler(name2, event);

        Set<String> names = new HashSet<>();
        names.add(name1);
        names.add(name2);

        String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event);
        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(eq(key));
        verify(jedisProxy, times(2)).recordRedisDaoEventRequests(eq("getEventHandler"), eq(event));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "test_event";
        String name1 = "test_event_handler_1";
        EventHandler eventHandler1 = createEventHandler(name1, event);
        eventHandler1.setActive(false);

        Set<String> names = new HashSet<>();
        names.add(name1);

        String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event);
        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(eq(key));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(eq("getEventHandler"), eq(event));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        String event = "test_event";

        Set<String> names = new HashSet<>();

        String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event);
        when(jedisProxy.smembers(key)).thenReturn(names);

        redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy, times(1)).smembers(eq(key));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(eq("getEventHandler"), eq(event));
    }

    @Test
    public void testGetEventHandler() throws NotFoundException {
        String name = "test_event_handler";
        EventHandler eventHandler = createEventHandler(name, "test_event");
        String json = objectMapper.writeValueAsString(eventHandler);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(json);
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler(name);

        assertNotNull(result);
        assertEquals(eventHandler, result);

        verify(jedisProxy, times(1)).hget(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(name));
        verify(objectMapper, times(1)).readValue(anyString(), eq(EventHandler.class));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() throws NotFoundException {
        String name = "test_event_handler";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.getEventHandler(name);

        verify(jedisProxy, times(1)).hget(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(name));
    }
}