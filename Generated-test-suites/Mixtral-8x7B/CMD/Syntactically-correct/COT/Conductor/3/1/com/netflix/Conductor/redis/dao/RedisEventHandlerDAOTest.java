import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_NAME = "test_event_name";
    private static final String EVENT_HANDLER_NAME = "test_event_handler_name";
    private static final String EVENT = "test_event";

    @Before
    public void setUp() {
        when(conductorProperties.getLockTimeout()).thenReturn(3000);
        when(redisProperties.getPrefix()).thenReturn("conductor_test");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("{}");
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME, "{}");
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME), anyString());
        verify(jedisProxy).publish(anyString(), anyString());
    }

    @Test
    public void testAddEventHandler_ConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (ConflictException e) {
            assertEquals("EventHandler with name " + EVENT_HANDLER_NAME + " already exists!",
                    e.getMessage());
            verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), anyString());
            verifyNoMoreInteractions(jedisProxy);
            return;
        }

        // ConflictException not thrown
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), anyString());
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(EVENT_HANDLER_NAME);
        newEventHandler.setEvent("new_event");

        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(existingEventHandler);
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("{}");
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME, "{}");
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "new_event")), eq(EVENT_HANDLER_NAME), anyString());
        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testUpdateEventHandler_NotFoundException() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(EVENT_HANDLER_NAME);
        newEventHandler.setEvent("new_event");

        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        try {
            redisEventHandlerDAO.updateEventHandler(newEventHandler);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name " + EVENT_HANDLER_NAME + " not found!",
                    e.getMessage());
            verifyNoMoreInteractions(jedisProxy);
            return;
        }

        // NotFoundException not thrown
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);
        when(jedisProxy.hdel(anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME);
        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void testRemoveEventHandler_NotFoundException() {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name " + EVENT_HANDLER_NAME + " not found!",
                    e.getMessage());
            verifyNoMoreInteractions(jedisProxy);
            return;
        }

        // NotFoundException not thrown
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("event_handler_1");
        eventHandler1.setEvent("event_1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("event_handler_2");
        eventHandler2.setEvent("event_2");

        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put(eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));
        eventHandlers.put(eventHandler2.getName(), objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(eventHandlers);
        when(objectMapper.readValue(anyString(),
                eq(new TypeReference<EventHandler>() {})))
                .thenAnswer(invocation -> {
                    String json = invocation.getArgument(0);
                    if (json.equals(eventHandler1.toString())) {
                        return eventHandler1;
                    } else {
                        return eventHandler2;
                    }
                });

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(2, allEventHandlers.size());
        assertEquals(eventHandler1, allEventHandlers.get(0));
        assertEquals(eventHandler2, allEventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("event_handler_1");
        eventHandler1.setEvent(EVENT);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("event_handler_2");
        eventHandler2.setEvent("event_2");

        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(Set.of(eventHandler1.getName(), eventHandler2.getName()));
        when(getEventHandler(eventHandler1.getName()))
                .thenReturn(eventHandler1);
        when(getEventHandler(eventHandler2.getName()))
                .thenReturn(eventHandler2);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(getEventHandler, times(2)).apply(anyString());
        assertEquals(2, result.size());
        assertEquals(eventHandler1, result.get(0));
        assertEquals(eventHandler2, result.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent_NotFoundException() {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(Set.of());

        try {
            redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        } catch (NotFoundException e) {
            assertEquals("No matching event handler found for event: " + EVENT, e.getMessage());
            verify(getEventHandler).apply(anyString());
            return;
        }

        // NotFoundException not thrown
        verify(getEventHandler).apply(anyString());
    }

    private EventHandler getEventHandler(String name) throws NotFoundException, TransientException {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(nsKey(EVENT_HANDLERS), name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = objectMapper.readValue(json, EventHandler.class);
        }
        assertNotNull(eventHandler);
        return eventHandler;
    }

    private String nsKey(String... keys) {
        return String.join("_", "conductor_test", keys);
    }
}