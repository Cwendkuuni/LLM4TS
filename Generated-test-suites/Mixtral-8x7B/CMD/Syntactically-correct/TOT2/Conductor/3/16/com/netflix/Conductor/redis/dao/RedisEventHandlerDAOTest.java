import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.redis.RedisEventHandlerDAO;
import com.netflix.conductor.dao.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
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

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NAME = "testName";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{}";
    private static final EventHandler EVENT_HANDLER = new EventHandler();
    private static final List<EventHandler> HANDLERS = new LinkedList<>();

    @Before
    public void setUp() {
        EVENT_HANDLER.setName(NAME);
        EVENT_HANDLER.setEvent(EVENT);
        HANDLERS.add(EVENT_HANDLER);

        when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
        when(objectMapper.readValue(JSON, new TypeReference<List<EventHandler>>() {}))
                .thenReturn(HANDLERS);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
        when(jedisProxy.hgetAll(anyString())).thenReturn(Collections.singletonMap(NAME, JSON));
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton(NAME));
    }

    @Test
    public void testAddEventHandler() throws ConflictException {
        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), NAME);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException {
        doThrow(new ConflictException("")).when(jedisProxy).hset(anyString(), anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testUpdateEventHandler()
            throws NotFoundException, ConflictException {
        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
        verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
        verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME)).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), NAME);
        verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> allHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(HANDLERS, allHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> handlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(HANDLERS, handlers);
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append("conductor:redis:");
        for (String key : keys) {
            sb.append(key).append(":");
        }
        return sb.toString();
    }
}