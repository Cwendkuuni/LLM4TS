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
import org.junit.After;
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

    private String namespace = "myNamespace";

    @Before
    public void setUp() {
        when(conductorProperties.getNamespace()).thenReturn(namespace);
        when(redisProperties.getEventsNamespace()).thenReturn(namespace + ":events:");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(namespace + ":events:" + EVENT_HANDLERS, "testEventHandler", anyString());
        verify(jedisProxy).sadd(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + eventHandler.getEvent(), "testEventHandler");
        verify(jedisProxy).record(anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(namespace + ":events:" + EVENT_HANDLERS, "testEventHandler", anyString());
        verify(jedisProxy).record(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        String eventHandlerName = "testEventHandler";

        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        verify(jedisProxy).hdel(namespace + ":events:" + EVENT_HANDLERS, eventHandlerName);
        verify(jedisProxy).record(anyString());
        verify(jedisProxy).srem(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + "testEvent", eventHandlerName);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        String eventHandlerName = "testEventHandler";

        when(redisEventHandlerDAO.getEventHandler(eventHandlerName)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandlerName);
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");

        Map<String, String> eventHandlersJson = Map.of(
                eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1),
                eventHandler2.getName(), objectMapper.writeValueAsString(eventHandler2)
        );

        when(jedisProxy.hgetAll(namespace + ":events:" + EVENT_HANDLERS)).thenReturn(eventHandlersJson);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
        assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());

        verify(jedisProxy).hgetAll(namespace + ":events:" + EVENT_HANDLERS);
        verify(jedisProxy).record(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent(event);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("anotherEvent");

        when(jedisProxy.smembers(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + event))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());

        verify(jedisProxy).smembers(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + event);
        verify(redisEventHandlerDAO, times(2)).getEventHandler(eventHandler1.getName());
        verifyNoMoreInteractions(redisEventHandlerDAO);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() {
        String event = "testEvent";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent(event);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("anotherEvent");

        when(jedisProxy.smembers(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + event))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
        assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());

        verify(jedisProxy).smembers(namespace + ":events:" + EVENT_HANDLERS_BY_EVENT + ":" + event);
        verify(redisEventHandlerDAO, times(2)).getEventHandler(eventHandler1.getName());
        verify(redisEventHandlerDAO, times(1)).getEventHandler(eventHandler2.getName());
        verifyNoMoreInteractions(redisEventHandlerDAO);
    }
}