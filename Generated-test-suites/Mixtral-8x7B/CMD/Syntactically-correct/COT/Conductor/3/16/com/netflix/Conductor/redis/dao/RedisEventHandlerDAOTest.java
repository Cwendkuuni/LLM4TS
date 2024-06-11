import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.netflix.conductor.redis.jedis.JedisProxy;

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

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "nsKey", "test:");
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy, times(1)).hset(argCaptor.capture(), valueCaptor.capture());
        assertEquals(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS),
                argCaptor.getValue());
        assertEquals(redisEventHandlerDAO.toJson(eventHandler), valueCaptor.getValue());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName()))
                .thenReturn(new EventHandler());

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
        verify(jedisProxy, never()).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy, times(1)).hset(argCaptor.capture(), valueCaptor.capture());
        assertEquals(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS),
                argCaptor.getValue());
        assertEquals(redisEventHandlerDAO.toJson(eventHandler), valueCaptor.getValue());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
        verify(jedisProxy, never()).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy, times(1)).hdel(argCaptor.capture(), eventHandler.getName());
        assertEquals(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS),
                argCaptor.getValue());
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("testEventHandler"));
        verify(jedisProxy, never()).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put("testEventHandler", "{}");

        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)))
                .thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, result.size());
        verify(jedisProxy, times(1)).hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new ArrayList<String>() {{ add("testEventHandler"); }});

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName()))
                .thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(1, result.size());
        verify(jedisProxy, times(1)).smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"));
        verify(jedisProxy, times(1)).getEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", "testEvent");
    }

    @Test
    public void testGetEventHandlersForEventNotFoundException() {
        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new ArrayList<String>() {{ add("testEventHandler"); }});

        when(redisEventHandlerDAO.getEventHandler("testEventHandler"))
                .thenThrow(new NotFoundException("test"));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(0, result.size());
        verify(jedisProxy, times(1)).smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"));
        verify(redisEventHandlerDAO, times(1)).getEventHandler("testEventHandler");
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))
                .thenReturn(new ArrayList<String>() {{ add("testEventHandler"); }});

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(false);
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName()))
                .thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(0, result.size());
        verify(jedisProxy, times(1)).smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"));
        verify(redisEventHandlerDAO, times(1)).getEventHandler("testEventHandler");
    }

}