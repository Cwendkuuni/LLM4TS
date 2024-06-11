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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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

private EventHandler eventHandler1;
private EventHandler eventHandler2;
private List<EventHandler> eventHandlers;

@Before
public void setup() {
eventHandler1 = new EventHandler();
eventHandler1.setName("eh1");
eventHandler1.setEvent("event1");
eventHandler1.setActive(true);

eventHandler2 = new EventHandler();
eventHandler2.setName("eh2");
eventHandler2.setEvent("event2");
eventHandler2.setActive(false);

eventHandlers = new LinkedList<>();
eventHandlers.add(eventHandler1);
eventHandlers.add(eventHandler2);
}

@Test
public void testAddEventHandler() {
String name = "eh3";
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
redisEventHandlerDAO.addEventHandler(name);
assertEquals(name, getEventHandler(name).getName());
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() {
String name = "eh3";
when(getEventHandler(name)).thenReturn(eventHandler1);
redisEventHandlerDAO.addEventHandler(name);
}

@Test
public void testUpdateEventHandler() {
String name = "eh1";
when(getEventHandler(name)).thenReturn(eventHandler1);
redisEventHandlerDAO.updateEventHandler(eventHandler2);
assertEquals(eventHandler2.getEvent(), getEventHandler(name).getEvent());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() {
String name = "eh3";
when(getEventHandler(name)).thenReturn(null);
redisEventHandlerDAO.updateEventHandler(eventHandler2);
}

@Test
public void testRemoveEventHandler() {
String name = "eh1";
EventHandler eventHandler = getEventHandler(name);
redisEventHandlerDAO.removeEventHandler(name);
assertNotNull(eventHandler);
assertEquals(null, getEventHandler(name));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() {
String name = "eh3";
redisEventHandlerDAO.removeEventHandler(name);
}

@Test
public void testGetAllEventHandlers() {
when(jedisProxy.hgetAll(anyString())).thenReturn(toJson(eventHandlers));
List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(eventHandlers.size(), allEventHandlers.size());
}

@Test
public void testGetEventHandlersForEvent() {
String event = "event1";
when(jedisProxy.smembers(anyString())).thenReturn(toSet(eventHandler1.getName()));
List<EventHandler> eventHandlersForEvent =
redisEventHandlerDAO.getEventHandlersForEvent(event, true);
assertEquals(eventHandler1.getEvent(), eventHandlersForEvent.get(0).getEvent());
}

@Test
public void testGetEventHandlersForEventActiveOnlyFalse() {
String event = "event1";
when(jedisProxy.smembers(anyString())).thenReturn(toSet(eventHandler1.getName(), eventHandler2.getName()));
List<EventHandler> eventHandlersForEvent =
redisEventHandlerDAO.getEventHandlersForEvent(event, false);
assertEquals(eventHandler1.getEvent(), eventHandlersForEvent.get(0).getEvent());
assertEquals(eventHandler2.getEvent(), eventHandlersForEvent.get(1).getEvent());
}

private Map<String, String> toJson(List<EventHandler> eventHandlers) {
Map<String, String> jsonMap = new HashMap<>();
for (EventHandler eventHandler : eventHandlers) {
jsonMap.put(eventHandler.getName(), toJson(eventHandler));
}
return jsonMap;
}

private String toJson(EventHandler eventHandler) {
// Implement this method to convert EventHandler object to JSON string
return "";
}

private Set<String> toSet(String... values) {
Set<String> set = new LinkedHashSet<>();
for (String value : values) {
set.add(value);
}
return set;
}

private EventHandler getEventHandler(String name) {
// Implement this method to get EventHandler from Redis using JedisProxy
return null;
}
}