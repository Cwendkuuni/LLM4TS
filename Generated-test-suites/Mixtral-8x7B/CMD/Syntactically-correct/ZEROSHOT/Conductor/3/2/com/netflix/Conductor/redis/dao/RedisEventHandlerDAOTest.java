import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

private Jedis jedis;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
jedis = mock(Jedis.class);
jedisProxy.setJedis(jedis);
}

@Test
public void testAddEventHandler() throws ConflictException, TransientException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler);

String json = jedis.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler");
assertEquals(redisEventHandlerDAO.toJson(eventHandler), json);
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, TransientException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() throws NotFoundException, TransientException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

redisEventHandlerDAO.updateEventHandler(eventHandler);

String json = jedis.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler");
assertEquals(redisEventHandlerDAO.toJson(eventHandler), json);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws NotFoundException, TransientException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws NotFoundException, TransientException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler);

redisEventHandlerDAO.removeEventHandler("testEventHandler");

assertEquals(0, jedis.hlen(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
redisEventHandlerDAO.removeEventHandler("testEventHandler");
}

@Test
public void testGetAllEventHandlers() throws TransientException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");

when(jedis.hgetAll(redisEventHandlerDAO.nsKey(EVENT_HANDLERS))).thenReturn(new HashMap<String, String>() {{
put("testEventHandler1", redisEventHandlerDAO.toJson(eventHandler1));
put("testEventHandler2", redisEventHandlerDAO.toJson(eventHandler2));
}});

List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(2, eventHandlers.size());
assertEquals(eventHandler1, eventHandlers.get(0));
assertEquals(eventHandler2, eventHandlers.get(1));
}

@Test
public void testGetEventHandlersForEvent() throws NotFoundException, TransientException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");
eventHandler1.setEvent("testEvent");

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");
eventHandler2.setEvent("testEvent2");

when(jedis.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
.thenReturn(new HashSet<String>() {{
add("testEventHandler1");
add("testEventHandler2");
}});

when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler1, eventHandler2, null);

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

assertEquals(2, eventHandlers.size());
assertEquals(eventHandler1, eventHandlers.get(0));
assertEquals(eventHandler2, eventHandlers.get(1));
}

@Test
public void testGetEventHandlersForEventActiveOnly() throws NotFoundException, TransientException {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("testEventHandler1");
eventHandler1.setEvent("testEvent");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("testEventHandler2");
eventHandler2.setEvent("testEvent2");

when(jedis.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
.thenReturn(new HashSet<String>() {{
add("testEventHandler1");
add("testEventHandler2");
}});

when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler1, eventHandler2, null);

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

assertEquals(1, eventHandlers.size());
assertEquals(eventHandler1, eventHandlers.get(0));
}
}