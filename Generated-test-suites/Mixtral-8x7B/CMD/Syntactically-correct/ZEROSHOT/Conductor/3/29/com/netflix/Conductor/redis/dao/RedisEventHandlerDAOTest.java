import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class RedisEventHandlerDAOTest {

private RedisEventHandlerDAO redisEventHandlerDAO;
private JedisProxy jedisProxy;

@Before
public void setup() {
jedisProxy = Mockito.mock(JedisProxy.class);
redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
}

@Test
public void testAddEventHandler() throws ConflictException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler);

Mockito.verify(jedisProxy, Mockito.times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler1);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler2);
}

@Test
public void testUpdateEventHandler() throws NotFoundException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler1);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler");

redisEventHandlerDAO.updateEventHandler(eventHandler2);

Mockito.verify(jedisProxy, Mockito.times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler);

redisEventHandlerDAO.removeEventHandler("testEventHandler");

Mockito.verify(jedisProxy, Mockito.times(1)).hdel(anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException, IOException {
redisEventHandlerDAO.removeEventHandler("testEventHandler");
}

@Test
public void testGetAllEventHandlers() throws IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");

doAnswer(
(invocation) -> {
Map<String, String> map = new ConcurrentHashMap<>();
map.put(eventHandler1.getName(), toJson(eventHandler1));
map.put(eventHandler2.getName(), toJson(eventHandler2));
return map;
})
.when(jedisProxy)
.hgetAll(anyString());

List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(2, eventHandlers.size());
assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());
}

@Test
public void testGetEventHandlersForEvent() throws NotFoundException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");
eventHandler1.setEvent("testEvent");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");
eventHandler2.setEvent("testEvent2");
eventHandler2.setActive(false);

doAnswer(
(invocation) -> {
Set<String> set = new HashSet<>();
set.add(eventHandler1.getName());
return set;
})
.when(jedisProxy)
.smembers(anyString());

doReturn(toJson(eventHandler1))
.when(jedisProxy)
.hget(anyString(), anyString());

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

assertEquals(1, eventHandlers.size());
assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
}

@Test
public void testGetEventHandlersForEventActiveOnly() throws NotFoundException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");
eventHandler1.setEvent("testEvent");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");
eventHandler2.setEvent("testEvent2");
eventHandler2.setActive(false);

doAnswer(
(invocation) -> {
Set<String> set = new HashSet<>();
set.add(eventHandler1.getName());
return set;
})
.when(jedisProxy)
.smembers(anyString());

doReturn(toJson(eventHandler1))
.when(jedisProxy)
.hget(anyString(), anyString());

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

assertEquals(1, eventHandlers.size());
assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
}

@Test
public void testGetEventHandlersForEventInactiveOnly() throws NotFoundException, IOException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");
eventHandler1.setEvent("testEvent");
eventHandler1.setActive(false);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");
eventHandler2.setEvent("testEvent2");
eventHandler2.setActive(true);

doAnswer(
(invocation) -> {
Set<String> set = new HashSet<>();
set.add(eventHandler1.getName());
return set;
})
.when(jedisProxy)
.smembers(anyString());

doReturn(toJson(eventHandler1))
.when(jedisProxy)
.hget(anyString(), anyString());

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

assertEquals(1, eventHandlers.size());
assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
}

@Test
public void testGetEventHandlersForEventNoMatchingEventHandlers()
throws NotFoundException, IOException {
doAnswer(
(invocation) -> {
Set<String> set = new HashSet<>();
return set;
})
.when(jedisProxy)
.smembers(anyString());

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

assertEquals(0, eventHandlers.size());
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlersForEventException() throws NotFoundException, IOException {
doThrow(new TransientException("test"))
.when(jedisProxy)
.hget(anyString(), anyString());

redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
}
}