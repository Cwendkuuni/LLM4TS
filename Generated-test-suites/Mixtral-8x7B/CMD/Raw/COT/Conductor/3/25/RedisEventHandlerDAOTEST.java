import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.QueueDao;
import com.netflix.conductor.dao.SchemaDao;
import com.netflix.conductor.dao.SchemaRecord;
import com.netflix.conductor.redis.RedisTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

    @Mock
    private QueueDao queueDao;

    @Mock
    private SchemaDao schemaDao;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT = "some_event";
    private static final String NAME = "some_name";
    private static final String JSON = "{}";

    @Before
    public void setUp() {
        RedisTestUtils.setupJedisMock(jedisProxy);
    }

    @After
    public void tearDown() {
        RedisTestUtils.cleanJedisMock(jedisProxy);
    }

    @Test
    public void testAddEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(recordRedisDaoRequests).accept("addEventHandler");
    }

    @Test
    public void testAddEventHandlerConflict() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(NAME);
        existingEventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy, never()).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy, never()).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(recordRedisDaoRequests, never()).accept("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(NAME);
        existingEventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(existingEventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()), NAME);
        verify(recordRedisDaoRequests).accept("updateEventHandler");
    }

    @Test
    public void testUpdateEventHandlerNotFound() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        verify(jedisProxy, never()).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy, never()).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(jedisProxy, never()).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()), NAME);
        verify(recordRedisDaoRequests, never()).accept("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), NAME);
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(recordRedisDaoRequests).accept("removeEventHandler");
    }

    @Test
    public void testRemoveEventHandlerNotFound() throws IOException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(NAME));

        verify(jedisProxy, never()).hdel(nsKey(EVENT_HANDLERS), NAME);
        verify(jedisProxy, never()).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(recordRedisDaoRequests, never()).accept("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("name1");
        eventHandler1.setEvent(EVENT);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("name2");
        eventHandler2.setEvent(EVENT);

        Map<String, String> all = new LinkedHashMap<>();
        all.put("name1", JSON);
        all.put("name2", JSON);

        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler1, eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        verify(recordRedisDaoRequests).accept("getAllEventHandlers");
    }

    @Test
    public void testGetAllEventHandlersNoEventHandlers() throws IOException {
        Map<String, String> all = new LinkedHashMap<>();

        when(jedisProxy.hgetAll(anyString())).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertTrue(eventHandlers.isEmpty());
        verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        verify(recordRedisDaoRequests).accept("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() throws IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("name1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("name2");
        eventHandler2.setEvent("different_event");
        eventHandler2.setActive(true);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("name3");
        eventHandler3.setEvent(EVENT);
        eventHandler3.setActive(false);

        Set<String> names = new HashSet<>();
        names.add("name1");
        names.add("name3");

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(names)
                .thenThrow(new RuntimeException())
                .thenReturn(names);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class))
                .thenReturn(eventHandler1, eventHandler2, eventHandler3);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler3));

        verify(jedisProxy, times(3)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(jedisProxy, times(3)).hget(nsKey(EVENT_HANDLERS), anyString());
        verify(recordRedisDaoEventRequests, times(2)).accept("getEventHandler");
    }

    @Test
    public void testGetEventHandlersForEventNoEventHandlers() throws IOException {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertTrue(eventHandlers.isEmpty());
        verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(recordRedisDaoEventRequests).accept("getEventHandler");
    }

    @Test
    public void testGetEventHandlersForEventException() throws IOException {
        Set<String> names = new HashSet<>();
        names.add("name1");

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(names)
                .thenThrow(new RuntimeException());
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(new EventHandler());

        assertThrows(RuntimeException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true));

        verify(jedisProxy, times(2)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(jedisProxy, times(2)).hget(nsKey(EVENT_HANDLERS), anyString());
        verify(recordRedisDaoEventRequests, times(2)).accept("getEventHandler");
    }

    @Test
    public void testGetEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME)).thenReturn(JSON);
        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler(NAME);

        assertEquals(eventHandler, result);
        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), NAME);
        verify(recordRedisDaoRequests).accept("getEventHandler");
    }

    @Test
    public void testGetEventHandlerNotFound() throws IOException {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(NAME));

        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), NAME);
        verify(recordRedisDaoRequests).accept("getEventHandler");
    }

    @Test
    public void testNsKey() {
        String namespace = "some_namespace";
        String key = "some_key";

        String result = RedisEventHandlerDAO.nsKey(key, namespace);

        assertEquals(namespace + ":" + key, result);
    }

    private void recordRedisDaoRequests(String operation) {
        // Add your custom logic here for testing recordRedisDaoRequests method
    }

    private void recordRedisDaoEventRequests(String operation, String event) {
        // Add your custom logic here for testing recordRedisDaoEventRequests method
    }

    private <T> T readValue(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    private <T> T readValue(String json, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(json, valueTypeRef);
    }
}