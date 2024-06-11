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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    private JedisCommands jedis;

    @Before
    public void setUp() {
        jedis = new Jedis("localhost");
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");
        eventHandler.setActive(true);

        String eventHandlerJson = objectMapper.writeValueAsString(eventHandler);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), "test-event-handler", eventHandlerJson);
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");
        eventHandler.setActive(true);

        String eventHandlerJson = objectMapper.writeValueAsString(eventHandler);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException, ConflictException, TransientException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test-event-handler");
        existingEventHandler.setEvent("test-event");
        existingEventHandler.setActive(true);

        String existingEventHandlerJson = objectMapper.writeValueAsString(existingEventHandler);

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(existingEventHandlerJson);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("test-event-handler");
        updatedEventHandler.setEvent("test-event-updated");
        updatedEventHandler.setActive(true);

        String updatedEventHandlerJson = objectMapper.writeValueAsString(updatedEventHandler);

        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), "test-event-handler", updatedEventHandlerJson);
        verify(jedisProxy, times(1)).index(updatedEventHandler);
        verify(jedisProxy, times(1)).removeIndex(existingEventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws IOException, NotFoundException, ConflictException, TransientException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test-event-handler");
        existingEventHandler.setEvent("test-event");
        existingEventHandler.setActive(true);

        String existingEventHandlerJson = objectMapper.writeValueAsString(existingEventHandler);

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("test-event-handler");
        updatedEventHandler.setEvent("test-event-updated");
        updatedEventHandler.setActive(true);

        String updatedEventHandlerJson = objectMapper.writeValueAsString(updatedEventHandler);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");
        eventHandler.setActive(true);

        String eventHandlerJson = objectMapper.writeValueAsString(eventHandler);

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(eventHandlerJson);

        redisEventHandlerDAO.removeEventHandler("test-event-handler");

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), "test-event-handler");
        verify(jedisProxy, times(1)).removeIndex(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("test-event-handler");
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent("test-event-1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(false);

        String eventHandler1Json = objectMapper.writeValueAsString(eventHandler1);
        String eventHandler2Json = objectMapper.writeValueAsString(eventHandler2);

        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(Map.of(
                        "test-event-handler-1", eventHandler1Json,
                        "test-event-handler-2", eventHandler2Json
                ));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));

        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent("test-event");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(false);

        String eventHandler1Json = objectMapper.writeValueAsString(eventHandler1);
        String eventHandler2Json = objectMapper.writeValueAsString(eventHandler2);

        when(jedisProxy.smembers(anyString()))
                .thenReturn(Set.of("test-event-handler-1", "test-event-handler-2"));

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(EventHandlerJson(eventHandler1Json, "test-event-handler-1"))
                .thenReturn(EventHandlerJson(eventHandler2Json, "test-event-handler-2"))
                .thenThrow(new NotFoundException("No matching event handler found for event: test-event"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event", true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "test-event"));
        verify(jedisProxy, times(3)).hget(nsKey(EVENT_HANDLERS), anyString());
    }

    private String EventHandlerJson(String json, String name) {
        return String.format("{\"name\":\"%s\",\"json\":%s}", name, json);
    }

    private String nsKey(String... keys) {
        StringBuilder key = new StringBuilder();
        for (String k : keys) {
            if (key.length() > 0) {
                key.append(':');
            }
            key.append(k);
        }
        return key.toString();
    }
}