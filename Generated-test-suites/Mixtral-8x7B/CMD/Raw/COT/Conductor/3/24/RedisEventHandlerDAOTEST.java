import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String JSON_EVENT_HANDLER = "{\"name\":\"testEventHandler\","
            + "\"event\":\"testEvent\",\"workflow\":\"testWorkflow\","
            + "\"active\":true,\"decision\":false}";

    @Before
    public void init() {
        when(conductorProperties.getRedisNamespace()).thenReturn("testNamespace");
        when(objectMapper.convertValue(any(EventHandler.class), eq(String.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(objectMapper.convertValue(any(String.class), eq(new TypeReference<EventHandler>() {})))
                .thenAnswer(invocationOnMock -> objectMapper.readValue(invocationOnMock.getArguments()[0],
                new TypeReference<EventHandler>() {}));
    }

    @Test
    public void testAddEventHandler() throws NotFoundException, ConflictException {
        EventHandler eventHandler = new EventHandler().setName(EVENT_HANDLER_NAME).setEvent(EVENT_NAME);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS),
                eq(EVENT_HANDLER_NAME), eq(JSON_EVENT_HANDLER));
        verify(jedisProxy, times(1)).sadd(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + EVENT_NAME),
                eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler().setName(EVENT_HANDLER_NAME).setEvent(EVENT_NAME);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy, never()).sadd(any(String.class), any(String.class));
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException {
        EventHandler oldEventHandler = new EventHandler().setName(EVENT_HANDLER_NAME).setEvent(EVENT_NAME);
        EventHandler newEventHandler = new EventHandler().setName(EVENT_HANDLER_NAME)
                .setEvent("newTestEvent")
                .setWorkflow("newTestWorkflow")
                .setActive(false)
                .setDecision(true);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(oldEventHandler);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS),
                eq(EVENT_HANDLER_NAME), eq(objectMapper.convertValue(newEventHandler, String.class)));
        verify(jedisProxy, times(1)).srem(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + EVENT_NAME),
                eq(EVENT_HANDLER_NAME));
        verify(jedisProxy, times(1)).sadd(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + "newTestEvent"),
                eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler newEventHandler = new EventHandler().setName(EVENT_HANDLER_NAME)
                .setEvent("newTestEvent")
                .setWorkflow("newTestWorkflow")
                .setActive(false)
                .setDecision(true);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(newEventHandler));

        verify(jedisProxy, never()).srem(any(String.class), any(String.class));
        verify(jedisProxy, never()).sadd(any(String.class), any(String.class));
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler().setName(EVENT_HANDLER_NAME).setEvent(EVENT_NAME);
        when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS),
                eq(EVENT_HANDLER_NAME));
        verify(jedisProxy, times(1)).srem(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + EVENT_NAME),
                eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));

        verify(jedisProxy, never()).hdel(any(String.class), any(String.class));
        verify(jedisProxy, never()).srem(any(String.class), any(String.class));
    }

    @Test
    public void testGetAllEventHandlers() throws NotFoundException {
        EventHandler eventHandler1 = new EventHandler().setName("eventHandler1").setEvent("event1");
        EventHandler eventHandler2 = new EventHandler().setName("eventHandler2").setEvent("event2");
        EventHandler eventHandler3 = new EventHandler().setName("eventHandler3").setEvent("event3");
        Map<String, String> eventHandlersJsonMap = new HashMap<>();
        eventHandlersJsonMap.put(eventHandler1.getName(), objectMapper.convertValue(eventHandler1, String.class));
        eventHandlersJsonMap.put(eventHandler2.getName(), objectMapper.convertValue(eventHandler2, String.class));
        eventHandlersJsonMap.put(eventHandler3.getName(), objectMapper.convertValue(eventHandler3, String.class));
        when(jedisProxy.hgetAll(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS)))
                .thenReturn(eventHandlersJsonMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(3, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() throws NotFoundException {
        when(jedisProxy.hget(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME)))
                .thenReturn(JSON_EVENT_HANDLER);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        assertNotNull(eventHandler);
        assertEquals(EVENT_NAME, eventHandler.getEvent());
        assertEquals(EVENT_HANDLER_NAME, eventHandler.getName());
    }

    @Test
    public void testGetEventHandlerNotFound() {
        when(jedisProxy.hget(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME)))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException {
        String event = EVENT_NAME;
        String jsonEventHandler1 = "{\"name\":\"eventHandler1\",\"event\":\"" + event + "\","
                + "\"workflow\":\"testWorkflow\","
                + "\"active\":true,\"decision\":false}";
        String jsonEventHandler2 = "{\"name\":\"eventHandler2\",\"event\":\"" + event + "\","
                + "\"workflow\":\"testWorkflow\","
                + "\"active\":false,\"decision\":false}";
        Set<String> eventNamesSet = new HashSet<>();
        eventNamesSet.add("eventHandler1");
        eventNamesSet.add("eventHandler2");

        when(jedisProxy.smembers(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + event)))
                .thenReturn(eventNamesSet);
        when(jedisProxy.hget(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS), eq("eventHandler1")))
                .thenReturn(jsonEventHandler1);
        when(jedisProxy.hget(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS), eq("eventHandler2")))
                .thenReturn(jsonEventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() throws NotFoundException {
        String event = EVENT_NAME;
        Set<String> eventNamesSet = new HashSet<>();

        when(jedisProxy.smembers(eq("testNamespace" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + event)))
                .thenReturn(eventNamesSet);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(0, eventHandlers.size());
    }
}