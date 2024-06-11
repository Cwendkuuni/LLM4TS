import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hset(any(String.class), any(String.class), any(String.class)))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"), any(String.class));
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(getEventHandler(eq("testEventHandler")))
                .thenReturn(new EventHandler());

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);

        when(getEventHandler(eq(name)))
                .thenReturn(new EventHandler());

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), any(String.class));
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);

        when(getEventHandler(eq(name)))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String name = "testEventHandler";

        when(getEventHandler(eq(name)))
                .thenReturn(new EventHandler());

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))
                , eq(name));
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        String name = "testEventHandler";

        when(getEventHandler(eq(name)))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        Map<String, String> all = getEventHandlerMap();

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS))))
                .thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(all.size(), eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws Exception {
        List<EventHandler> eventHandlers = new LinkedList<>();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setActive(true);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setActive(false);

        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))))
                .thenReturn(new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler2.getName())));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event", true);

        assertEquals(1, result.size());
        assertEquals(eventHandler1.getName(), result.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventNotActiveOnly() throws Exception {
        List<EventHandler> eventHandlers = new LinkedList<>();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setActive(true);
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setActive(false);

        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))))
                .thenReturn(new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler2.getName())));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event", false);

        assertEquals(2, result.size());
        assertEquals(eventHandler1.getName(), result.get(0).getName());
        assertEquals(eventHandler2.getName(), result.get(1).getName());
    }

    private Map<String, String> getEventHandlerMap() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");

        return Map.of(
                eventHandler1.getName(), toJson(eventHandler1),
                eventHandler2.getName(), toJson(eventHandler2)
        );
    }

    private String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    private EventHandler getEventHandler(String name) throws NotFoundException {
        return redisEventHandlerDAO.getEventHandler(name);
    }

    private String nsKey(String... keys) {
        String namespace = "conductor:eventHandlers:";

        StringBuilder sb = new StringBuilder(namespace);
        for (String key : keys) {
            sb.append(key).append(':');
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}