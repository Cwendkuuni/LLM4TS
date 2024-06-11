import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = getDummyEventHandler();
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test
    public void testAddEventHandlerConflictException() throws Exception {
        EventHandler eventHandler = getDummyEventHandler();
        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = getDummyEventHandler();
        EventHandler updatedEventHandler = getDummyEventHandler();
        updatedEventHandler.setCron("0 0 * * * ?");

        when(getEventHandler(existingEventHandler.getName())).thenReturn(existingEventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), existingEventHandler.getName(), anyString());
        verify(jedisProxy, times(1)).index(updatedEventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() throws Exception {
        EventHandler eventHandler = getDummyEventHandler();

        when(getEventHandler(eventHandler.getName())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = getDummyEventHandler();

        when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler.getName());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(eventHandler);
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() throws Exception {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));

        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = getDummyEventHandler();
        EventHandler eventHandler2 = getDummyEventHandler();
        eventHandler2.setName("eventHandler2");

        Map<String, String> hgetAll = new HashMap<>();
        hgetAll.put(objectMapper.writeValueAsString(eventHandler1), objectMapper.writeValueAsString(eventHandler1));
        hgetAll.put(objectMapper.writeValueAsString(eventHandler2), objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(hgetAll);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = getDummyEventHandler();
        EventHandler eventHandler2 = getDummyEventHandler();
        eventHandler2.setName("eventHandler2");

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
                .thenReturn(Set.of(eventHandler1.getName(), eventHandler2.getName()));
        when(getEventHandler(eventHandler1.getName()))
                .thenReturn(eventHandler1);
        when(getEventHandler(eventHandler2.getName()))
                .thenReturn(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(2, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(jedisProxy, times(2)).getEventHandler(anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(eq("getEventHandler"), eq(EVENT));
    }

    @Test
    public void testGetEventHandlersForEventNoMatchingEventHandlers() throws Exception {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(Set.of());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(jedisProxy, never()).getEventHandler(anyString());
        verify(jedisProxy, never()).recordRedisDaoEventRequests(anyString(), anyString());
    }

    private EventHandler getDummyEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);
        return eventHandler;
    }

    private String nsKey(String... keys) {
        return String.join(".", "conductor", "events", "handlers", objectMapper.writeValueAsString(keys));
    }

    private <T> T readValue(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }

    private <T> T readValue(String json, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(json, typeReference);
    }
}