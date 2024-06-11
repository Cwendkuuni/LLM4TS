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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
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
        eventHandler3.setEvent("event1");
        eventHandler3.setActive(true);
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("json");
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(objectMapper, times(1)).writeValueAsString(any(EventHandler.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflictException() throws ConflictException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler()
            throws ConflictException, NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        when(objectMapper.readValue("json", EventHandler.class)).thenReturn(eventHandler1);
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("json");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(objectMapper, times(1)).readValue(anyString(), any(Class.class));
        verify(objectMapper, times(1)).writeValueAsString(any(EventHandler.class));
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFoundException()
            throws ConflictException, NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler2);

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, never()).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        when(objectMapper.readValue("json", EventHandler.class)).thenReturn(eventHandler1);

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(objectMapper, times(1)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFoundException() throws NotFoundException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("eh1");

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, never()).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        Map<String, String> eventHandlers = mock(Map.class);
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);
        when(eventHandlers.values()).thenReturn(List.of("json1", "json2", "json3"));
        when(objectMapper.readValue("json1", EventHandler.class)).thenReturn(eventHandler1);
        when(objectMapper.readValue("json2", EventHandler.class)).thenReturn(eventHandler2);
        when(objectMapper.readValue("json3", EventHandler.class)).thenReturn(eventHandler3);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(anyString());
        verify(objectMapper, times(3)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");

        assertEquals(3, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
        String event = "event1";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Set<String> eventHandlerNames = mock(Set.class);
        when(jedisProxy.smembers(key)).thenReturn(eventHandlerNames);
        when(eventHandlerNames.contains("eh1")).thenReturn(true);
        when(eventHandlerNames.contains("eh3")).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        when(objectMapper.readValue("json", EventHandler.class)).thenReturn(eventHandler1);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy, times(1)).smembers(key);
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(objectMapper, times(2)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);

        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly()
            throws NotFoundException, TransientException {
        String event = "event1";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Set<String> eventHandlerNames = mock(Set.class);
        when(jedisProxy.smembers(key)).thenReturn(eventHandlerNames);
        when(eventHandlerNames.contains("eh1")).thenReturn(true);
        when(eventHandlerNames.contains("eh3")).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        when(objectMapper.readValue("json", EventHandler.class)).thenReturn(eventHandler1);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy, times(1)).smembers(key);
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(objectMapper, times(2)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);

        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventWithoutActiveOnly()
            throws NotFoundException, TransientException {
        String event = "event1";
        String key = "EVENT_HANDLERS_BY_EVENT:" + event;

        Set<String> eventHandlerNames = mock(Set.class);
        when(jedisProxy.smembers(key)).thenReturn(eventHandlerNames);
        when(eventHandlerNames.contains("eh1")).thenReturn(true);
        when(eventHandlerNames.contains("eh3")).thenReturn(true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("json");
        when(objectMapper.readValue("json", EventHandler.class)).thenReturn(eventHandler2);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy, times(1)).smembers(key);
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(objectMapper, times(2)).readValue(anyString(), any(Class.class));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);

        assertEquals(2, eventHandlers.size());
    }
}