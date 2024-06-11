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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    private RedisProperties properties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String JSON_EVENT_HANDLER = "{}";
    private static final EventHandler EVENT_HANDLER = new EventHandler();
    private static final String NON_EXISTENT_EVENT_HANDLER_NAME = "nonExistentEventHandler";
    private static final String EVENT = "test";

    @Before
    public void setUp() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON_EVENT_HANDLER);
        when(objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class)).thenReturn(EVENT_HANDLER);
        when(jedisProxy.hgetAll(anyString())).thenReturn(new HashMap<>());
        when(jedisProxy.smembers(anyString())).thenReturn(new LinkedList<>());
        when(jedisProxy.srem(anyString(), anyString())).thenReturn(1L);
    }

    @Test
    public void testAddEventHandler_existingEventHandler() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON_EVENT_HANDLER);

        try {
            redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
            assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
        } catch (ConflictException e) {
            assertEquals("EventHandler with name " + EVENT_HANDLER_NAME + " already exists!", e.getMessage());
        }
    }

    @Test
    public void testAddEventHandler_notExistingEventHandler() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);
        verify(jedisProxy, times(1)).index(EVENT_HANDLER);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);
        verify(jedisProxy, times(1)).index(EVENT_HANDLER);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
        verify(jedisProxy, times(1)).removeIndex(EVENT_HANDLER);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT);
    }

    @Test
    public void testGetEventHandler() {
        redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME);
        verify(objectMapper, times(1)).readValue(JSON_EVENT_HANDLER, EventHandler.class);
        verify(jedisProxy, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT_NAME);
    }
}