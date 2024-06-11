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
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import static org.junit.Assert.assertThrows;
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

    private static final String NAMESPACE = "my-namespace";
    private static final String EVENT = "test-event";
    private static final String EVENT_HANDLER_NAME = "test-event-handler";
    private static final String JSON_EVENT_HANDLER = "{\"name\":\"test-event-handler\"," +
            "\"event\":\"test-event\",\"workflowInstanceId\":\"workflow-1\"," +
            "\"activityName\":\"test-activity\",\"priority\":1," +
            "\"decisionCron\":\"0 0 * * * ?\",\"runbookUrl\":\"http://test.com\"," +
            "\"retryCount\":3,\"retryDelay\":1000,\"timeout\":30000,\"status\":\"CREATED\"," +
            "\"createdAt\":123456789,\"updatedAt\":123456789}";

    @Before
    public void setup() {
        when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
        when(jedisProxy.getJedis()).thenReturn(new Jedis());
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(NAMESPACE + ":" + "EVENT_HANDLERS",
                eventHandler.getName(), JSON_EVENT_HANDLER);
        verify(jedisProxy, times(1)).sadd(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + EVENT,
                eventHandler.getName());
    }

    @Test
    public void testAddEventHandlerConflictException() throws IOException {
        EventHandler eventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", eventHandler.getName()))
                .thenReturn(JSON_EVENT_HANDLER);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException {
        EventHandler existingEventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);
        EventHandler updatedEventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", updatedEventHandler.getName()))
                .thenReturn(JSON_EVENT_HANDLER);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(NAMESPACE + ":" + "EVENT_HANDLERS",
                updatedEventHandler.getName(), JSON_EVENT_HANDLER);
        verify(jedisProxy, times(1)).srem(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + existingEventHandler.getEvent(),
                existingEventHandler.getName());
        verify(jedisProxy, times(1)).sadd(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + updatedEventHandler.getEvent(),
                updatedEventHandler.getName());
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() throws IOException {
        EventHandler existingEventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);
        EventHandler updatedEventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", updatedEventHandler.getName()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
    }

    @Test
    public void testRemoveEventHandler() throws IOException, NotFoundException {
        EventHandler eventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", eventHandler.getName()))
                .thenReturn(JSON_EVENT_HANDLER);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(NAMESPACE + ":" + "EVENT_HANDLERS",
                eventHandler.getName());
        verify(jedisProxy, times(1)).srem(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + eventHandler.getEvent(),
                eventHandler.getName());
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() throws IOException {
        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", EVENT_HANDLER_NAME))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        List<String> jsonEventHandlers = Arrays.asList(JSON_EVENT_HANDLER, JSON_EVENT_HANDLER);
        Map<String, String> hgetAllMap = new HashMap<>();
        hgetAllMap.put(EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);
        hgetAllMap.put(EVENT_HANDLER_NAME + "-2", JSON_EVENT_HANDLER);

        when(jedisProxy.hgetAll(NAMESPACE + ":" + "EVENT_HANDLERS"))
                .thenReturn(hgetAllMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(EVENT_HANDLER_NAME, eventHandlers.get(0).getName());
        assertEquals(EVENT_HANDLER_NAME + "-2", eventHandlers.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() throws IOException, NotFoundException {
        EventHandler eventHandler1 = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);
        EventHandler eventHandler2 = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);
        eventHandler2.setName(eventHandler2.getName() + "-2");

        when(jedisProxy.smembers(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + EVENT))
                .thenReturn(new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler2.getName())));
        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", eventHandler1.getName()))
                .thenReturn(JSON_EVENT_HANDLER);
        when(jedisProxy.hget(NAMESPACE + ":" + "EVENT_HANDLERS", eventHandler2.getName()))
                .thenReturn(JSON_EVENT_HANDLER);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
        assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() throws IOException, NotFoundException {
        when(jedisProxy.smembers(NAMESPACE + ":" + "EVENT_HANDLERS_BY_EVENT:" + EVENT))
                .thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(0, eventHandlers.size());
    }
}