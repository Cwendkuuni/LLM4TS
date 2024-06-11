import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NS_KEY_EVENT_HANDLERS = "test:namespaced:key:event:handlers";
    private static final String NS_KEY_EVENT_HANDLERS_BY_EVENT = "test:namespaced:key:event:handlers:by:event";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{\"name\":\"testEventHandler\", \"event\":\"testEvent\"}";

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "eventHandlersNamespace", NS_KEY_EVENT_HANDLERS);
        ReflectionTestUtils.setField(redisEventHandlerDAO, "eventHandlersByEventNamespace", NS_KEY_EVENT_HANDLERS_BY_EVENT);

        when(conductorProperties.getEventHandlersTTL()).thenReturn(60);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(objectMapper.valueToTree(any(EventHandler.class))).thenReturn(eventHandler);
        when(objectMapper.convertValue(any(), any())).thenReturn(eventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON);
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(objectMapper.valueToTree(any(EventHandler.class))).thenReturn(eventHandler);
        when(objectMapper.convertValue(any(), any())).thenReturn(eventHandler);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            Assert.fail("ConflictException was expected but not thrown");
        } catch (ConflictException e) {
            Assert.assertEquals("EventHandler with name testEventHandler already exists!", e.getMessage());
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(EVENT_HANDLER_NAME);
        updatedEventHandler.setEvent("updatedEvent");

        when(objectMapper.valueToTree(any(EventHandler.class))).thenReturn(existingEventHandler)
                .thenReturn(updatedEventHandler);
        when(objectMapper.convertValue(any(), any())).thenReturn(existingEventHandler, updatedEventHandler);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON);
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("differentName");
        existingEventHandler.setEvent(EVENT);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(EVENT_HANDLER_NAME);
        updatedEventHandler.setEvent("updatedEvent");

        when(objectMapper.valueToTree(any(EventHandler.class))).thenReturn(existingEventHandler)
                .thenReturn(updatedEventHandler);
        when(objectMapper.convertValue(any(), any())).thenReturn(existingEventHandler, updatedEventHandler);

        try {
            redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
            Assert.fail("NotFoundException was expected but not thrown");
        } catch (NotFoundException e) {
            Assert.assertEquals("EventHandler with name testEventHandler not found!", e.getMessage());
        }
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT);

        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(eventHandler);
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME);
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
            Assert.fail("NotFoundException was expected but not thrown");
        } catch (NotFoundException e) {
            Assert.assertEquals("EventHandler with name testEventHandler not found!", e.getMessage());
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent(EVENT);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("differentEvent");

        Map<String, String> all = Mockito.mock(Map.class);
        when(all.containsKey(EVENT_HANDLER_NAME)).thenReturn(true).thenReturn(true).thenReturn(false);
        when(all.get(EVENT_HANDLER_NAME)).thenReturn(JSON, null);

        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS)).thenReturn(all);
        when(objectMapper.convertValue(JSON, EventHandler.class)).thenReturn(eventHandler1);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(2, eventHandlers.size());
        Assert.assertEquals(eventHandler1, eventHandlers.get(0));
    }

    private EventHandler getEventHandler(String name) {
        EventHandler eventHandler = null;
        String json;
        try {
            json = jedisProxy.hget(NS_KEY_EVENT_HANDLERS, name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
        if (json != null) {
            eventHandler = readValue(json, EventHandler.class);
        }
        return eventHandler;
    }

    private <T> T readValue(String content, Class<T> valueType) {
        return objectMapper.readValue(content, valueType);
    }
}