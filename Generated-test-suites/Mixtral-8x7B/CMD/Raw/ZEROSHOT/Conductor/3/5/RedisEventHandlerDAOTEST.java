import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    private static final String EVENT = "test";

    @Before
    public void setUp() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON_EVENT_HANDLER);
        when(objectMapper.readValue(anyString(), any())).thenReturn(new EventHandler());
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        redisEventHandlerDAO.addEventHandler(createEventHandler());

        verify(jedisProxy, times(1))
                .hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1))
                .index(any(EventHandler.class));
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException {
        redisEventHandlerDAO.updateEventHandler(createEventHandler());

        verify(jedisProxy, times(1))
                .hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1))
                .index(any(EventHandler.class));
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1))
                .hdel(anyString(), anyString());
        verify(removeIndex, times(1)).removeIndex(any(EventHandler.class));
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(anyString());
        verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyTrue() throws NotFoundException {
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(0, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(getEventHandler, times(1)).getEventHandler(anyString());
        verify(recordRedisDaoEventRequests, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyFalse() throws NotFoundException {
        EventHandler eventHandler = createEventHandler();

        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());
        when(getEventHandler.getEventHandler(anyString())).thenReturn(eventHandler);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlers.size());
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(getEventHandler, times(1)).getEventHandler(anyString());
        verify(recordRedisDaoEventRequests, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT);
    }

    private EventHandler createEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);
        return eventHandler;
    }
}