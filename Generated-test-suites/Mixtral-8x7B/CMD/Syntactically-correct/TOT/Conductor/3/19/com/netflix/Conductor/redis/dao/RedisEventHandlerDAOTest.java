import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashMap;
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
    private static final String EVENT_NAME = "event-1";
    private static final String EVENT_HANDLER_NAME = "event-handler-1";
    private static final String EVENT_HANDLER_JSON =
            "{\"name\":\"event-handler-1\",\"event\":\"event-1\",\"taskDefinition\":\"my-task\",\"queue\":\"my-queue\"," +
            "\"priority\":2,\"headers\":{},\"retryCount\":3,\"retryDelaySeconds\":10,\"timeoutSeconds\":60," +
            "\"maxExecutions\":10,\"executionMode\":\"PARALLEL\",\"isActive\":true}";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "namespace", NAMESPACE);

        when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(new EventHandler());
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn(EVENT_HANDLER_JSON);
        when(objectMapper.convertValue(anyString(), any(TypeReference.class))).thenReturn(new LinkedList<>());

        Jedis jedis = new Jedis();
        when(jedisProxy.getJedis()).thenReturn(jedis);
    }

    @Test
    public void testAddEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
        verify(jedisProxy, times(1)).publish(anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

        EventHandler existing = new EventHandler();
        existing.setName(EVENT_HANDLER_NAME);
        existing.setEvent(EVENT_NAME);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(EVENT_HANDLER_JSON);
        when(getEventHandler(anyString())).thenReturn(existing);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
        verify(jedisProxy, times(1)).publish(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        when(getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        when(getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).publish(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws IOException {
        when(getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(new EventHandler());

        when(objectMapper.convertValue(anyString(), any(TypeReference.class))).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers.size(), result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);

        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler);

        when(jedisProxy.smembers(anyString())).thenReturn(eventHandler.getName().split(","));
        when(getEventHandler(anyString())).thenReturn(eventHandler);

        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        assertEquals(eventHandlers.size(), result.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);
        eventHandler.setActive(false);

        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler);

        when(jedisProxy.smembers(anyString())).thenReturn(eventHandler.getName().split(","));
        when(getEventHandler(anyString())).thenReturn(eventHandler);

        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() throws IOException {
        when(jedisProxy.smembers(anyString())).thenReturn(new String[]{});

        List<EventHandler> result =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        assertEquals(0, result.size());
    }

    private EventHandler getEventHandler(String name) throws IOException {
        EventHandler eventHandler = null;
        String json = "{\"name\":\"event-handler-1\",\"event\":\"event-1\",\"taskDefinition\":\"my-task\",\"queue\":\"my-queue\"," +
                "\"priority\":2,\"headers\":{},\"retryCount\":3,\"retryDelaySeconds\":10,\"timeoutSeconds\":60," +
                "\"maxExecutions\":10,\"executionMode\":\"PARALLEL\",\"isActive\":true}";

        if (name != null && !name.isEmpty()) {
            eventHandler = objectMapper.readValue(json, EventHandler.class);
        }
        return eventHandler;
    }
}