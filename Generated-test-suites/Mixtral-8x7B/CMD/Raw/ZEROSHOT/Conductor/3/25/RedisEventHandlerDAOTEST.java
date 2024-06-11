import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
    @SuppressWarnings("unused")
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = spy(new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties));
    }

    @Test
    public void testAddEventHandler() throws ConflictException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Verify the method was called
        assertThat(redisEventHandlerDAO.getEventHandler(eventHandler.getName()), notNullValue());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // Verify the method was called
        assertThat(redisEventHandlerDAO.getEventHandler(eventHandler.getName()), notNullValue());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
        doNothing().when(jedisProxy).hdel(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        // Verify the method was called
        assertThat(redisEventHandlerDAO.getEventHandler(eventHandler.getName()), nullValue());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");

        Map<String, String> handlerMap = mock(Map.class);
        when(handlerMap.get("test-handler-1")).thenReturn(objectMapper.writeValueAsString(eventHandler1));
        when(handlerMap.get("test-handler-2")).thenReturn(objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(handlerMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertThat(eventHandlers.size(), equalTo(2));
        assertThat(eventHandlers.get(0).getName(), equalTo("test-handler-1"));
        assertThat(eventHandlers.get(1).getName(), equalTo("test-handler-2"));
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent("test-event-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("test-event-2");

        Map<String, String> handlerMap = mock(Map.class);
        when(handlerMap.get("test-handler-1")).thenReturn(objectMapper.writeValueAsString(eventHandler1));
        when(handlerMap.get("test-handler-2")).thenReturn(objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(handlerMap);

        Set<String> handlerNames = spy(new LinkedList<String>() {{
            add("test-handler-1");
            add("test-handler-2");
        }});

        when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event-1", true);

        assertThat(eventHandlers.size(), equalTo(1));
        assertThat(eventHandlers.get(0).getName(), equalTo("test-handler-1"));
    }

    @Test
    public void testGetEventHandlersForEventNoActive() throws NotFoundException, IOException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-handler-1");
        eventHandler1.setEvent("test-event-1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-handler-2");
        eventHandler2.setEvent("test-event-2");

        Map<String, String> handlerMap = mock(Map.class);
        when(handlerMap.get("test-handler-1")).thenReturn(objectMapper.writeValueAsString(eventHandler1));
        when(handlerMap.get("test-handler-2")).thenReturn(objectMapper.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(handlerMap);

        Set<String> handlerNames = spy(new LinkedList<String>() {{
            add("test-handler-1");
            add("test-handler-2");
        }});

        when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event-1", false);

        assertThat(eventHandlers.size(), equalTo(2));
        assertThat(eventHandlers.get(0).getName(), equalTo("test-handler-1"));
        assertThat(eventHandlers.get(1).getName(), equalTo("test-handler-2"));
    }

    @Test
    public void testGetEventHandlersForEventNotFound() throws NotFoundException, IOException {
        Set<String> handlerNames = spy(new LinkedList<String>());

        when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event-1", true);

        assertThat(eventHandlers.size(), equalTo(0));
    }
}