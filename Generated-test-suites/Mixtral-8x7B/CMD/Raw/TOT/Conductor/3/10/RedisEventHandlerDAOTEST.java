import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
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

        eventHandlers = Arrays.asList(eventHandler1, eventHandler2, eventHandler3);

        when(jedisProxy.hget(any(String.class), any(String.class))).thenReturn("{}");
        when(jedisProxy.hgetAll(any(String.class))).thenReturn(new HashMap<>());
        when(redisProperties.getEventHandlersNamespaces()).thenReturn("eh");
    }

    @Test
    public void testAddEventHandler() throws ConflictException, IOException {
        when(objectMapper.readValue("{}", EventHandler.class)).thenReturn(eventHandler1);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler1);

        assertEquals(1, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
        assertEquals(1, jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())).size());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, IOException {
        when(objectMapper.readValue("{}", EventHandler.class)).thenReturn(eventHandler1);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(toJson(eventHandler1));

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, ConflictException, IOException {
        when(objectMapper.readValue("{}", EventHandler.class)).thenReturn(eventHandler1);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(toJson(eventHandler2));

        redisEventHandlerDAO.updateEventHandler(eventHandler1);

        assertEquals(1, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
        assertEquals(1, jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())).size());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, ConflictException, IOException {
        when(objectMapper.readValue("{}", EventHandler.class)).thenReturn(eventHandler1);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler1);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(toJson(eventHandler1));

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

        assertEquals(0, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
        assertEquals(0, jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())).size());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        when(objectMapper.readValue(any(String.class), eq(EventHandler.class)))
                .thenReturn(eventHandler1, eventHandler2);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(getMockMap(eventHandler1.getName()));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventActive() {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())))
                .thenReturn(new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler3.getName())));

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
                .thenReturn(toJson(eventHandler1));

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler3.getName()))
                .thenReturn(toJson(eventHandler3));

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler3, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventInactive() {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler2.getEvent())))
                .thenReturn(new HashSet<>(Arrays.asList(eventHandler2.getName())));

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler2.getName()))
                .thenReturn(toJson(eventHandler2));

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(eventHandler2.getEvent(), true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler2, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler3.getEvent())))
                .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(eventHandler3.getEvent(), true);

        assertEquals(0, eventHandlers.size());
    }

    private String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    private Map<String, String> getMockMap(String... entries) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < entries.length; i += 2) {
            map.put(entries[i], entries[i + 1]);
        }
        return map;
    }

    private String nsKey(String... keys) {
        StringBuilder nsKey = new StringBuilder();
        nsKey.append(redisProperties.getEventHandlersNamespaces());
        for (String key : keys) {
            nsKey.append(":").append(key);
        }
        return nsKey.toString();
    }
}