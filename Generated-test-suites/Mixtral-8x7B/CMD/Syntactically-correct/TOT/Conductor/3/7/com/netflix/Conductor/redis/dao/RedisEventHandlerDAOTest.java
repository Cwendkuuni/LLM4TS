import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event");
        eventHandler.setEvent("test-event");
        eventHandler.setActive(true);

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event");
        eventHandler.setEvent("test-event");
        eventHandler.setActive(true);

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new ConflictException(""));

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), anyString());
        verify(jedisProxy, never()).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() {
        String name = "test-event";
        EventHandler existing = new EventHandler();
        existing.setName(name);
        existing.setEvent("old-event");
        existing.setActive(true);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent("new-event");
        eventHandler.setActive(true);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), name, anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        String name = "test-event";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent("new-event");
        eventHandler.setActive(true);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        verify(jedisProxy, times(0)).hset(nsKey(EVENT_HANDLERS), anyString(), anyString());
        verify(jedisProxy, never()).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "test-event";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), name);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        String name = "test-event";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(name));

        verify(jedisProxy, times(0)).hdel(nsKey(EVENT_HANDLERS), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        String name1 = "test-event-1";
        String name2 = "test-event-2";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent("test-event-1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent("test-event-2");
        eventHandler2.setActive(false);

        Map<String, String> eventHandlersJson = Map.of(name1, toJson(eventHandler1), name2, toJson(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersJson);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(name1, eventHandlers.get(0).getName());
        assertEquals(name2, eventHandlers.get(1).getName());

        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "test-event";
        String name1 = "test-event-1";
        String name2 = "test-event-2";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(name1, name2));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(name1, eventHandlers.get(0).getName());

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        verify(jedisProxy, times(2)).hget(nsKey(EVENT_HANDLERS), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    @Test
    public void testGetEventHandlersForEventNotActiveOnly() {
        String event = "test-event";
        String name1 = "test-event-1";
        String name2 = "test-event-2";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(name1, name2));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(name1, eventHandlers.get(0).getName());
        assertEquals(name2, eventHandlers.get(1).getName());

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        verify(jedisProxy, times(2)).hget(nsKey(EVENT_HANDLERS), anyString());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(RedisEventHandlerDAO.EVENT_HANDLERS);
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T readValue(String json, Class<T> valueType) throws Exception {
        return objectMapper.readValue(json, valueType);
    }
}