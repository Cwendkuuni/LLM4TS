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
import java.util.HashMap;
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

    private static final String EVENT_HANDLER_NAME = "eventHandlerName";
    private static final String EVENT = "event";
    private static final String JSON_EVENT_HANDLER = "{}";
    private static final String JSON_EVENT_HANDLERS = "[]";
    private static final String NAMESPACE = "namespace";

    @Before
    public void setUp() {
        when(conductorProperties.getAppName()).thenReturn("conductor");
        when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy)
                .hset(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS),
                        eq(EVENT_HANDLER_NAME),
                        eq(JSON_EVENT_HANDLER));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent("newEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy)
                .hset(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS),
                        eq(EVENT_HANDLER_NAME),
                        eq(JSON_EVENT_HANDLER));

        verify(jedisProxy)
                .srem(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + EVENT),
                        eq(EVENT_HANDLER_NAME));

        verify(jedisProxy)
                .sadd(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + "newEvent"),
                        eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT);

        doNothing().when(jedisProxy).hdel(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy)
                .hdel(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS),
                        eq(EVENT_HANDLER_NAME));

        verify(jedisProxy)
                .srem(eq(NAMESPACE + ":" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + EVENT),
                        eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws Exception {
        doThrow(new NotFoundException("EventHandler with name " + EVENT_HANDLER_NAME + " not found!"))
                .when(jedisProxy)
                .hdel(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(new EventHandler());
        eventHandlers.add(new EventHandler());

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(mapFromJson(JSON_EVENT_HANDLERS));

        when(objectMapper.readValue(anyString(), eq(EventHandler.class)))
                .thenAnswer(invocationOnMock -> {
                    String json = invocationOnMock.getArgument(0);
                    return fromJson(json, EventHandler.class);
                });

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers.size(), result.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyTrue() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(EVENT);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString()))
                .thenReturn(Arrays.asList("eventHandler1", "eventHandler2"));

        when(redisEventHandlerDAO.getEventHandler(anyString()))
                .thenReturn(eventHandler1)
                .thenThrow(new NotFoundException("No matching event handler found for event: " + EVENT));

        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, result.size());
        assertEquals(eventHandler1, result.get(0));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(EVENT);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString()))
                .thenReturn(Arrays.asList("eventHandler1", "eventHandler2"));

        when(redisEventHandlerDAO.getEventHandler(anyString()))
                .thenReturn(eventHandler1)
                .thenReturn(eventHandler2);

        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(2, result.size());
        assertEquals(eventHandler1, result.get(0));
        assertEquals(eventHandler2, result.get(1));
    }

    // Helper methods

    private Map<String, String> mapFromJson(String json) throws Exception {
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> maps = objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class,
                        Map.class));
        for (Map<String, String> m : maps) {
            for (Map.Entry<String, String> entry : m.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    private <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }
}