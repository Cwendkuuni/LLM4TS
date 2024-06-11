import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
}

@Test(expected = ConflictException.class)
public void testAddEventHandler_whenEventHandlerExists_shouldThrowConflictException()
throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
when(objectMapper.readValue(anyString(), any())).thenReturn(eventHandler);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hget(anyString(), anyString());
verify(objectMapper, times(1)).readValue(anyString(), any());
verify(jedisProxy, never()).hset(anyString(), anyString(), anyString());

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testAddEventHandler_whenEventHandlerDoesNotExist_shouldAddEventHandler()
throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, never()).hget(anyString(), anyString());
verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandler_whenEventHandlerDoesNotExist_shouldThrowNotFoundException()
throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler_whenEventHandlerExists_shouldUpdateEventHandler()
throws Exception {
EventHandler existingEventHandler = new EventHandler();
existingEventHandler.setName("test-event-handler");

EventHandler newEventHandler = new EventHandler();
newEventHandler.setName("test-event-handler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));
when(objectMapper.readValue(anyString(), any())).thenReturn(newEventHandler);

redisEventHandlerDAO.updateEventHandler(newEventHandler);

verify(jedisProxy, times(1)).hget(anyString(), anyString());
verify(objectMapper, times(1)).readValue(anyString(), any());
verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandler_whenEventHandlerDoesNotExist_shouldThrowNotFoundException()
throws Exception {
redisEventHandlerDAO.removeEventHandler("test-event-handler");

verify(jedisProxy, never()).hget(anyString(), anyString());
verify(jedisProxy, never()).hdel(anyString(), anyString());
}

@Test
public void testRemoveEventHandler_whenEventHandlerExists_shouldRemoveEventHandler()
throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));

redisEventHandlerDAO.removeEventHandler("test-event-handler");

verify(jedisProxy, times(1)).hget(anyString(), anyString());
verify(jedisProxy, times(1)).hdel(anyString(), anyString());
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> eventHandlersMap = new LinkedHashMap<>();
eventHandlersMap.put("eventHandler1", toJson(new EventHandler()));
eventHandlersMap.put("eventHandler2", toJson(new EventHandler()));

when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersMap);
when(objectMapper.readValue(anyString(), any())).thenReturn(new EventHandler());

List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

verify(jedisProxy, times(1)).hgetAll(anyString());
verify(objectMapper, times(2)).readValue(anyString(), any());
assertEquals(2, eventHandlers.size());
}

@Test
public void testGetEventHandlersForEvent_whenNoEventHandlersExist_shouldReturnEmptyList() {
String event = "test-event";

List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

verify(jedisProxy, times(1)).smembers(anyString());
verify(jedisProxy, never()).hget(anyString(), anyString());
verify(objectMapper, never()).readValue(anyString(), any());
assertEquals(0, eventHandlers.size());
}

@Test
public void testGetEventHandlersForEvent_whenEventHandlersExist_shouldReturnEventHandlers()
throws Exception {
String event = "test-event";

EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("eventHandler1");
eventHandler1.setEvent(event);
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("eventHandler2");
eventHandler2.setEvent(event);
eventHandler2.setActive(false);

when(jedisProxy.smembers(anyString()))
.thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));

when(jedisProxy.hget(anyString(), anyString()))
.thenReturn(toJson(eventHandler1), toJson(eventHandler2));

when(objectMapper.readValue(anyString(), any()))
.thenReturn(eventHandler1, eventHandler2);

List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

verify(jedisProxy, times(1)).smembers(anyString());
verify(jedisProxy, times(2)).hget(anyString(), anyString());
verify(objectMapper, times(2)).readValue(anyString(), any());
assertEquals(2, eventHandlers.size());
assertTrue(eventHandlers.contains(eventHandler1));
assertTrue(eventHandlers.contains(eventHandler2));
}

@Test
public void testGetEventHandlersForEvent_whenActiveOnlyIsTrue_shouldReturnActiveEventHandlers()
throws Exception {
String event = "test-event";

EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("eventHandler1");
eventHandler1.setEvent(event);
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("eventHandler2");
eventHandler2.setEvent(event);
eventHandler2.setActive(false);

when(jedisProxy.smembers(anyString()))
.thenReturn(new HashSet<>(List.of(eventHandler1.getName(), eventHandler2.getName())));

when(jedisProxy.hget(anyString(), anyString()))
.thenReturn(toJson(eventHandler1), toJson(eventHandler2));

when(objectMapper.readValue(anyString(), any()))
.thenReturn(eventHandler1, eventHandler2);

List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

verify(jedisProxy, times(1)).smembers(anyString());
verify(jedisProxy, times(2)).hget(anyString(), anyString());
verify(objectMapper, times(2)).readValue(anyString(), any());
assertEquals(1, eventHandlers.size());
assertTrue(eventHandlers.contains(eventHandler1));
}

private String toJson(Object obj) throws Exception {
return objectMapper.writeValueAsString(obj);
}

private <T> T readValue(String json, Class<T> valueType) throws Exception {
return objectMapper.readValue(json, valueType);
}
}