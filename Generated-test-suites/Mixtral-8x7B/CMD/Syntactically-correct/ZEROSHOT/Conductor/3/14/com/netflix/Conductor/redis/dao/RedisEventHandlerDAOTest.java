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
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

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
    public void setUp() {
        // Initialize mocks here if needed
    }

    @Test
    public void testAddEventHandler() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).index(eq(eventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, TransientException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("newTestEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), newEventHandler.getName()))
                .thenReturn(null);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), existingEventHandler.getName()))
                .thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(newEventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).index(eq(newEventHandler));
        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(existingEventHandler.getName
        ()));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("newTestEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), newEventHandler.getName()))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(eq(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() throws TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");

        Map<String, String> all = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        all.put(eventHandler1.getName(), toJson(eventHandler1));
        all.put(eventHandler2.getName(), toJson(eventHandler2));

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, handlers.size());
        assertNotNull(handlers.get(0));
        assertNotNull(handlers.get(1));
        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");

        String key = nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent());
        jedisProxy.sadd(key, eventHandler1.getName());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);

        assertEquals(1, handlers.size());
        assertEquals(eventHandler1.getName(), handlers.get(0).getName());
        verify(jedisProxy, times(1)).smembers(eq(key));
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append(redisProperties.getNamespace());
        for (String key : keys) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }

    private String toJson(Object object) throws TransientException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new TransientException("Unable to serialize object", e);
        }
    }

    private <T> T readValue(String json, Class<T> valueType) throws TransientException {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new TransientException("Unable to deserialize JSON", e);
        }
    }
}