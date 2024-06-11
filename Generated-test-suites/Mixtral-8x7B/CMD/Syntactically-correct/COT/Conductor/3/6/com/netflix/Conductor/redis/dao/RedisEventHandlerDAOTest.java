import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
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

    private EventHandler eventHandler1, eventHandler2;
    private List<EventHandler> eventHandlers;

    @Before
    public void setup() {
        eventHandler1 = new EventHandler();
        eventHandler1.setName("eh1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("eh2");
        eventHandler2.setEvent("event2");
        eventHandler2.setActive(false);

        eventHandlers = Arrays.asList(eventHandler1, eventHandler2);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"eh1\", \"event\":\"event1\", \"active\":true}");
        when(jedisProxy.hgetAll(anyString())).thenReturn(getHgetAllMap());
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler1);
    }

    private Map<String, String> getHgetAllMap() {
        Map<String, String> map = new HashMap<>();
        map.put("eh1", "{\"name\":\"eh1\", \"event\":\"event1\", \"active\":true}");
        map.put("eh2", "{\"name\":\"eh2\", \"event\":\"event2\", \"active\":false}");
        return map;
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName()), anyString());
        verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())), eq(eventHandler1.getName()));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName()), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test(expected = TransientException.class)
    public void testAddEventHandlerTransient() throws ConflictException, TransientException {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new TransientException("test exception"));

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

        redisEventHandlerDAO.updateEventHandler(eventHandler1);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName()), anyString());
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())), eq(eventHandler1.getName()));
        verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())), eq(eventHandler1.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler1);
    }

    @Test(expected = TransientException.class)
    public void testUpdateEventHandlerTransient() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new TransientException("test exception"));

        redisEventHandlerDAO.updateEventHandler(eventHandler1);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName()));
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent())), eq(eventHandler1.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
    }

    @Test(expected = TransientException.class)
    public void testRemoveEventHandlerTransient() throws NotFoundException, TransientException {
        when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
        when(jedisProxy.hdel(anyString(), anyString())).thenThrow(new TransientException("test exception"));

        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        List<EventHandler> eventHandlersFromDao = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers, eventHandlersFromDao);
    }

    @Test(expected = TransientException.class)
    public void testGetAllEventHandlersTransient() throws TransientException {
        when(jedisProxy.hgetAll(anyString())).thenThrow(new TransientException("test exception"));

        redisEventHandlerDAO.getAllEventHandlers();
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()))).thenReturn(new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler2.getName())));

        List<EventHandler> eventHandlersFromDao = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);

        assertEquals(Arrays.asList(eventHandler1), eventHandlersFromDao);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() throws NotFoundException, TransientException {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()))).thenReturn(new HashSet<>());

        redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlersForEventTransient() throws NotFoundException, TransientException {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()))).thenThrow(new TransientException("test exception"));

        redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);
    }

    private EventHandler getEventHandler(String name) {
        return eventHandlers.stream()
                .filter(eh -> eh.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}