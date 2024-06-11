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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT = "event1";
    private static final String NAME = "name1";
    private static final String JSON = "{}";

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
    public void setup() {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), any())).thenReturn(JSON);
        when(objectMapper.readValue(eq(JSON), eq(EventHandler.class))).thenReturn(new EventHandler());
        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Map.of(NAME, JSON));
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(NAME));
    }

    @Test
    public void testAddEventHandler() throws NotFoundException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
        verify(jedisProxy, times(1))
                .sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws NotFoundException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);

        doThrow(new ConflictException("conflict")).when(redisEventHandlerDAO).getEventHandler(NAME);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler()
            throws NotFoundException, ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
        verify(jedisProxy, times(1))
                .sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound()
            throws NotFoundException, ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonexistent");

        doThrow(new NotFoundException("not found")).when(redisEventHandlerDAO).getEventHandler("nonexistent");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(NAME));
        verify(jedisProxy, times(1))
                .srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        doThrow(new NotFoundException("not found")).when(redisEventHandlerDAO).getEventHandler(NAME);

        redisEventHandlerDAO.removeEventHandler(NAME);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
    }

    private String nsKey(String... keys) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RedisEventHandlerDAO.class.getSimpleName());
        for (String key : keys) {
            stringBuilder.append(":").append(key);
        }
        return stringBuilder.toString();
    }
}