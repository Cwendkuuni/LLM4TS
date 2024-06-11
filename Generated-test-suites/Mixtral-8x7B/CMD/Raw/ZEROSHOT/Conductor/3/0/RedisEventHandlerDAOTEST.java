import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

    private JedisCommands jedisCommands;

    @Before
    public void setUp() {
        initMocks(this);
        jedisCommands = mock(JedisCommands.class);
        when(jedisProxy.getJedis()).thenReturn(jedisCommands);
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisCommands.hget(anyString(), anyString()))
                .thenReturn(null)
                .thenThrow(new JedisConnectionException("Test exception"));

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisCommands, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisCommands, times(1)).sadd(anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisCommands, times(2)).hget(anyString(), anyString());
        verify(conductorProperties, times(1)).isRedisEnabled();
        verifyNoMoreInteractions(conductorProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisCommands.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler()
            throws IOException, NotFoundException, TransientException {
        String name = "testEventHandler";
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(name);
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(name);

        when(jedisCommands.hget(anyString(), anyString())).thenReturn("{}");
        when(jedisCommands.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisCommands, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisCommands, times(1)).srem(anyString(), anyString());
        verify(jedisCommands, times(1)).sadd(anyString(), anyString());

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisCommands, times(2)).hget(anyString(), anyString());
        verifyNoMoreInteractions(conductorProperties);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound()
            throws IOException, NotFoundException, TransientException {
        String name = "testEventHandler";
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(name);

        when(jedisCommands.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        String name = "testEventHandler";

        when(jedisCommands.hget(anyString(), anyString()))
                .thenReturn("{}")
                .thenReturn(null)
                .thenThrow(new JedisConnectionException("Test exception"));

        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisCommands, times(1)).hdel(anyString(), anyString());
        verify(jedisCommands, times(1)).srem(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(name);

        redisEventHandlerDAO.removeEventHandler(name);
        verify(conductorProperties, times(1)).isRedisEnabled();
        verifyNoMoreInteractions(conductorProperties);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        String name = "testEventHandler";

        when(jedisCommands.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(new EventHandler());

        when(jedisCommands.hgetAll(anyString()))
                .thenReturn(Map.of())
                .thenReturn(mapToJson(eventHandlers));

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(eventHandlers, allEventHandlers);

        allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        verify(conductorProperties, times(1)).isRedisEnabled();
        verifyNoMoreInteractions(conductorProperties);
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException {
        String event = "testEvent";
        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(new EventHandler());

        when(jedisCommands.smembers(anyString()))
                .thenReturn(Set.of("handler1", "handler2"))
                .thenThrow(new JedisConnectionException("Test exception"));

        when(jedisCommands.hget(anyString(), anyString()))
                .thenReturn(toJson(eventHandlers.get(0)))
                .thenThrow(new NotFoundException("Test exception"));

        List<EventHandler> handlers =
                redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(eventHandlers, handlers);

        handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        verify(jedisCommands, times(2)).hget(anyString(), anyString());
        verify(conductorProperties, times(1)).isRedisEnabled();
        verifyNoMoreInteractions(conductorProperties);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() throws NotFoundException {
        String event = "testEvent";

        when(jedisCommands.smembers(anyString())).thenReturn(Set.of());

        redisEventHandlerDAO.getEventHandlersForEvent(event, true);
    }

    private String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    private String mapToJson(List<EventHandler> eventHandlers) {
        Map<String, String> map = eventHandlers.stream().map(eh -> Map.entry(eh.getName(), toJson(eh)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ObjectMapper().writeValueAsString(map);
    }
}