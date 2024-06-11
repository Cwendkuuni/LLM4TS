import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
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
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private static final String EVENT_HANDLERS_NAMESPACE = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT_NAMESPACE = "EVENT_HANDLERS_BY_EVENT";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "nsKey", "test:");
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");
        eventHandler.setEvent("test-event");

        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), any(), any()))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), any(), any());
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("test-event-handler");
        existingEventHandler.setEvent("test-event");

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("test-event-handler");
        updatedEventHandler.setEvent("updated-test-event");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(existingEventHandler.getName())))
                .thenReturn("{\"name\":\"test-event-handler\", \"event\":\"test-event\"}");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class)))
                .thenReturn(existingEventHandler);
        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), any(), any()))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_throwsNotFoundException() throws IOException, NotFoundException {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("test-event-handler");
        updatedEventHandler.setEvent("updated-test-event");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(updatedEventHandler.getName())))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq(updatedEventHandler.getName()));
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        String eventHandlerName = "test-event-handler";

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandlerName)))
                .thenReturn("{\"name\":\"test-event-handler\", \"event\":\"test-event\"}");

        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(eventHandlerName));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_throwsNotFoundException() throws NotFoundException {
        String eventHandlerName = "nonexistent-event-handler";

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandlerName)))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandlerName));
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        List<EventHandler> expectedEventHandlers = new LinkedList<>();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test-event-handler-1");
        eventHandler1.setEvent("test-event-1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("test-event-handler-2");
        eventHandler2.setEvent("test-event-2");

        expectedEventHandlers.add(eventHandler1);
        expectedEventHandlers.add(eventHandler2);

        Map<String, String> eventHandlersMap = new LinkedHashMap<>();
        eventHandlersMap.put(eventHandler1.getName(), toJson(eventHandler1));
        eventHandlersMap.put(eventHandler2.getName(), toJson(eventHandler2));

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS))))
                .thenReturn(eventHandlersMap);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers.size(), expectedEventHandlers.size());
        assertEquals(eventHandlers.get(0).getName(), eventHandler1.getName());
        assertEquals(eventHandlers.get(0).getEvent(), eventHandler1.getEvent());
        assertEquals(eventHandlers.get(1).getName(), eventHandler2.getName());
        assertEquals(eventHandlers.get(1).getEvent(), eventHandler2.getEvent());
    }

    private String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder(EVENT_HANDLERS_NAMESPACE);
        for (String key : keys) {
            sb.append(':').append(key);
        }
        return sb.toString();
    }
}