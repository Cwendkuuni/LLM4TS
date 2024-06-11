import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String JSON_EVENT_HANDLER = "{}";
    private static final String JSON_EVENT_HANDLERS = "{}";

    @Before
    public void setUp() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON_EVENT_HANDLER);
        when(jedisProxy.hgetAll(anyString())).thenReturn(JSON_EVENT_HANDLERS);
        when(objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class)).thenReturn(new EventHandler());
        when(objectMapper.readValue(JSON_EVENT_HANDLERS, Map.class)).thenReturn(Map.of());
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(new EventHandler());

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).index(any());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testAddEventHandlerConflictException() {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(new EventHandler());

        Throwable throwable = catchThrowable(() -> redisEventHandlerDAO.addEventHandler(new EventHandler()));

        assertThat(throwable)
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("EventHandler with name %s already exists!", EVENT_HANDLER_NAME);
        verify(jedisProxy, never()).index(any());
        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).index(any());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistentName");

        Throwable throwable = catchThrowable(() -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("EventHandler with name %s not found!", "nonExistentName");
        verify(jedisProxy, never()).index(any());
        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
        verify(jedisProxy, times(1)).removeIndex(any());
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        Throwable throwable = catchThrowable(() -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));

        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("EventHandler with name %s not found!", EVENT_HANDLER_NAME);
        verify(jedisProxy, never()).recordRedisDaoRequests(anyString());
        verify(jedisProxy, never()).removeIndex(any());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertThat(eventHandlers).hasSize(0);
        verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(EVENT_HANDLER_NAME));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);

        assertThat(eventHandlers).hasSize(1);
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of());

        Throwable throwable = catchThrowable(() -> redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false));

        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No matching event handler found for event: %s", EVENT_NAME);
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test
    public void testGetEventHandler() throws NotFoundException {
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        assertThat(eventHandler).isNotNull();
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlerNotFoundException() {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        Throwable throwable = catchThrowable(() -> redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));

        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("EventHandler with name %s not found!", EVENT_HANDLER_NAME);
        verify(jedisProxy, never()).recordRedisDaoEventRequests(anyString(), anyString());
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(nsKey(EVENT_HANDLERS), name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = readValue(json, EventHandler.class);
        }
        return eventHandler;
    }

    private <T> T readValue(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(redisProperties.getNamespace());
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }
}