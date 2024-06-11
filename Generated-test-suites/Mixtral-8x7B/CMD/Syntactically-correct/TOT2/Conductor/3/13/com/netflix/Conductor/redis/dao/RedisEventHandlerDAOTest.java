import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.TestConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.TestRedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final String EVENT_NAME = "test-event";
    private static final String EVENT_HANDLER_NAME = "test-event-handler";
    private static final String JSON = "{\"name\": \"%s\", \"event\": \"%s\", \"activity\": \"%s\", \"workflow\": \"%s\", \"active\": true}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "conductorProperties", new TestConductorProperties());
        ReflectionTestUtils.setField(redisEventHandlerDAO, "redisProperties", new TestRedisProperties());
    }

    @After
    public void teardown() {
        Mockito.reset(jedisProxy);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), any());
        verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws Exception {
        EventHandler eventHandler = createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(String.format(JSON, EVENT_HANDLER_NAME, EVENT_NAME, "", "", true));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME);
        EventHandler newEventHandler = createEventHandler(EVENT_HANDLER_NAME, "new-event");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(String.format(JSON, EVENT_HANDLER_NAME, EVENT_NAME, "", "", true));

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), any());
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)), eq(EVENT_HANDLER_NAME));
        verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "new-event")), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws Exception {
        EventHandler newEventHandler = createEventHandler(EVENT_HANDLER_NAME, "new-event");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(String.format(JSON, EVENT_HANDLER_NAME, EVENT_NAME, "", "", true));

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME));
        verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws Exception {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        List<EventHandler> eventHandlers = new LinkedList<>();
        eventHandlers.add(createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME));

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS))))
                .thenReturn(Map.of(EVENT_HANDLER_NAME, String.format(JSON, EVENT_HANDLER_NAME, EVENT_NAME, "", "", true)));

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(eventHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = createEventHandler("event-handler-1", EVENT_NAME);
        EventHandler eventHandler2 = createEventHandler("event-handler-2", EVENT_NAME);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME))))
                .thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler1.getName())))
                .thenReturn(String.format(JSON, eventHandler1.getName(), eventHandler1.getEvent(), "", "", true));
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler2.getName())))
                .thenReturn(String.format(JSON, eventHandler2.getName(), eventHandler2.getEvent(), "", "", false));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(eventHandler1, result.get(0));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws Exception {
        EventHandler eventHandler = createEventHandler("event-handler", EVENT_NAME);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME))))
                .thenReturn(new HashSet<>(List.of(eventHandler.getName())));
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName())))
                .thenReturn(String.format(JSON, eventHandler.getName(), eventHandler.getEvent(), "", "", true));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(eventHandler, result.get(0));
    }

    @Test
    public void testGetEventHandlersForEventNotFound() throws Exception {
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME))))
                .thenReturn(new HashSet<>());

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);

        Assert.assertTrue(result.isEmpty());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlersForEventTransientException() throws Exception {
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME))))
                .thenThrow(new RuntimeException("test exception"));

        redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);
    }

    @Test
    public void testGetEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler(EVENT_HANDLER_NAME, EVENT_NAME);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(String.format(JSON, EVENT_HANDLER_NAME, EVENT_NAME, "", "", true));

        EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

        Assert.assertEquals(eventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() throws Exception {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)))
                .thenReturn(null);

        redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
    }

    @Test
    public void testRecordRedisDaoRequests() {
        redisEventHandlerDAO.recordRedisDaoRequests("test-request");

        verify(jedisProxy, times(1)).recordRequest("test-request");
    }

    @Test
    public void testRecordRedisDaoEventRequests() {
        redisEventHandlerDAO.recordRedisDaoEventRequests("test-event-request", EVENT_NAME);

        verify(jedisProxy, times(1)).recordEventRequest("test-event-request", EVENT_NAME);
    }

    private EventHandler createEventHandler(String name, String event) {
        return OBJECT_MAPPER.readValue(String.format(JSON, name, event, "", "", true), new TypeReference<EventHandler>() {});
    }

}