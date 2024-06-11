import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.JedisProxy;

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

    private EventHandler eventHandler;
    private List<EventHandler> eventHandlers;
    private String jsonEventHandler;
    private Map<String, String> hgetAllMap;
    private Set<String> smembersSet;

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        eventHandlers = new LinkedList<>();
        eventHandlers.add(eventHandler);

        jsonEventHandler = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";

        hgetAllMap = mock(Map.class);
        when(hgetAllMap.get(anyString())).thenReturn(jsonEventHandler);

        smembersSet = new HashSet<>();
        smembersSet.add("testEventHandler");

        when(jedisProxy.hgetAll(anyString())).thenReturn(hgetAllMap);
        when(jedisProxy.smembers(anyString())).thenReturn(smembersSet);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1))
                .hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), anyString(), anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1))
                .hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), anyString(), anyString());
        verify(jedisProxy, times(1)).index(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(jedisProxy, times(1)).hdel(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), anyString());
        verify(jedisProxy, times(1)).removeIndex(eventHandler);
        verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(hgetAllMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers.size(), 1);
        assertEquals(eventHandlers.get(0).getName(), "testEventHandler");
        verify(jedisProxy, times(1)).hgetAll(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(smembersSet);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        assertEquals(eventHandlers.size(), 1);
        assertEquals(eventHandlers.get(0).getName(), "testEventHandler");
        verify(jedisProxy, times(1)).smembers(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT,
                "testEvent")));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getEventHandlersForEvent");
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        when(jedisProxy.smembers(anyString())).thenReturn(smembersSet);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertEquals(eventHandlers.size(), 1);
        assertEquals(eventHandlers.get(0).getName(), "testEventHandler");
        verify(jedisProxy, times(1)).smembers(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT,
                "testEvent")));
        verify(jedisProxy, times(1)).recordRedisDaoRequests("getEventHandlersForEvent");
    }
}