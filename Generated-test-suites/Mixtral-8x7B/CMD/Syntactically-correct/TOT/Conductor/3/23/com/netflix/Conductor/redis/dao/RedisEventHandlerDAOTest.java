import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    @Conditional(AnyRedisCondition.class)
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String NS_KEY = "conductor";
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT = "event1";
    private static final String NAME = "handler1";
    private static final String JSON = "{\"name\":\"handler1\",\"event\":\"event1\",\"active\":true}";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = spy(redisEventHandlerDAO);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "nsKey", NS_KEY);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    @Test
    public void testAddEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        doReturn(true).when(jedisProxy).hexists(eq(NS_KEY + ":" + EVENT_HANDLERS), any());
        doReturn(null).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME), eq(JSON));
        verify(jedisProxy, times(1)).sadd(eq(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT),
                eq(NAME));
    }

    @Test
    public void testAddEventHandlerConflict() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        doReturn(true).when(jedisProxy).hexists(eq(NS_KEY + ":" + EVENT_HANDLERS), any());
        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME), eq(JSON));
    }

    @Test
    public void testUpdateEventHandlerNotFound() throws IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        doReturn(null).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy, times(1)).hdel(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));
        verify(jedisProxy, times(1)).srem(eq(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT),
                eq(NAME));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        doReturn(null).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(NAME));
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        Map<String, String> map = new LinkedList<Map.Entry<String, String>>() {
            {
                add(Map.entry(NAME, JSON));
            }
        }.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        doReturn(map).when(jedisProxy).hgetAll(eq(NS_KEY + ":" + EVENT_HANDLERS));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, eventHandlers.size());
        assertEquals(NAME, eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventActive() throws IOException {
        Set<String> names = new HashSet<String>() {
            {
                add(NAME);
            }
        };

        doReturn(names).when(jedisProxy).smembers(eq(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT));
        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(NAME, eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventInactive() throws IOException {
        Set<String> names = new HashSet<String>() {
            {
                add(NAME);
            }
        };

        doReturn(names).when(jedisProxy).smembers(eq(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT));
        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(NAME, eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        doReturn(new HashSet<>()).when(jedisProxy).smembers(eq(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() throws IOException {
        doReturn(JSON).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(NAME);

        assertNotNull(eventHandler);
        assertEquals(NAME, eventHandler.getName());
    }

    @Test
    public void testGetEventHandlerNotFound() {
        doReturn(null).when(jedisProxy).hget(eq(NS_KEY + ":" + EVENT_HANDLERS), eq(NAME));

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(NAME));
    }
}