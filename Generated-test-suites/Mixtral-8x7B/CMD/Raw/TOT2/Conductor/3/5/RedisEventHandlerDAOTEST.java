import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

@Mock
private JedisProxy jedisProxy;

private RedisEventHandlerDAO redisEventHandlerDAO;

private ObjectMapper objectMapper = new ObjectMapper();

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
redisEventHandlerDAO =
new RedisEventHandlerDAO(
jedisProxy, objectMapper, new ConductorProperties(), new RedisProperties());
}

@Test
public void testAddEventHandler() throws ConflictException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");
eventHandler.setEvent("test-event");

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(1, allEventHandlers.size());
assertEquals("test-event-handler", allEventHandlers.get(0).getName());
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");
eventHandler.setEvent("test-event");

EventHandler existingEventHandler = new EventHandler();
existingEventHandler.setName("test-event-handler");
existingEventHandler.setEvent("test-event");

when(jedisProxy.hget(anyString(), anyString()))
.thenReturn(objectMapper.writeValueAsString(existingEventHandler));

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler()
throws ConflictException, NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");
eventHandler.setEvent("test-event");

redisEventHandlerDAO.addEventHandler(eventHandler);

EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName("test-event-handler");
updatedEventHandler.setEvent("updated-test-event");

redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(1, allEventHandlers.size());
assertEquals("updated-test-event", allEventHandlers.get(0).getEvent());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound()
throws ConflictException, NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");
eventHandler.setEvent("test-event");

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("test-event-handler");
eventHandler.setEvent("test-event");

redisEventHandlerDAO.addEventHandler(eventHandler);

redisEventHandlerDAO.removeEventHandler("test-event-handler");

verify(jedisProxy, times(1)).hdel(anyString(), anyString());
verify(jedisProxy, times(1)).recordRedisDaoRequests(anyString());

verify(jedisProxy, times(1)).srem(anyString(), anyString());

List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(0, allEventHandlers.size());
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException, IOException {
redisEventHandlerDAO.removeEventHandler("test-event-handler");
}

@Test
public void testGetAllEventHandlers() throws IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("test-event-handler-1");
eventHandler1.setEvent("test-event-1");

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("test-event-handler-2");
eventHandler2.setEvent("test-event-2");

when(jedisProxy.hgetAll(anyString()))
.thenReturn(
new LinkedList<Map.Entry<String, String>>() {{
add(mapEntry("test-event-handler-1", objectMapper.writeValueAsString(eventHandler1)));
add(mapEntry("test-event-handler-2", objectMapper.writeValueAsString(eventHandler2)));
}});

List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(2, allEventHandlers.size());
}

@Test
public void testGetEventHandlersForEvent() throws NotFoundException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("test-event-handler-1");
eventHandler1.setEvent("test-event");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("test-event-handler-2");
eventHandler2.setEvent("test-event");
eventHandler2.setActive(false);

when(jedisProxy.smembers(anyString()))
.thenReturn(
new HashSet<String>() {{
add("test-event-handler-1");
add("test-event-handler-2");
}});

when(jedisProxy.hget(anyString(), anyString()))
.thenAnswer(
(invocation) -> {
String name = invocation.getArgument(1);
if (name.equals("test-event-handler-1")) {
return objectMapper.writeValueAsString(eventHandler1);
} else if (name.equals("test-event-handler-2")) {
return objectMapper.writeValueAsString(eventHandler2);
}
return null;
});

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("test-event", false);
assertEquals(2, eventHandlers.size());

eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test-event", true);
assertEquals(1, eventHandlers.size());
assertEquals("test-event-handler-1", eventHandlers.get(0).getName());
}

private Map.Entry<String, String> mapEntry(String key, String value) {
return new Object() {
public String getKey() {
return key;
}

public String getValue() {
return value;
}
};
}

private String toJson(Object object) throws IOException {
return objectMapper.writeValueAsString(object);
}

private <T> T readValue(String json, Class<T> valueType) throws IOException {
return objectMapper.readValue(json, valueType);
}

private <T> T readValue(String json, TypeReference<T> valueTypeRef)
throws IOException {
return objectMapper.readValue(json, valueTypeRef);
}
}