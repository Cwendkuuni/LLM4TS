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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
private RedisEventHandlerDAO redisEventHandlerDAO = spy(new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties));

private Jedis jedis;

@Before
public void setUp() {
jedis = mock(Jedis.class);
jedisProxy.setJedis(jedis);
}

@After
public void tearDown() {
jedis.close();
}

// Helper method to set up a basic EventHandler
private EventHandler getBasicEventHandler() {
return EventHandler.builder().name("testEventHandler").event("testEvent").build();
}

// Helper method to set up a basic Map for hgetAll
private Map<String, String> getBasicMap() {
Map<String, String> map = mock(Map.class);
doReturn(getBasicJson()).when(map).get(anyString());
return map;
}

// Helper method to set up a basic JSON string
private String getBasicJson() {
return "{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}";
}

@Test
public void testAddEventHandler_basicCase() throws ConflictException {
EventHandler eventHandler = getBasicEventHandler();
redisEventHandlerDAO.addEventHandler(eventHandler);
verify(jedis, times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = ConflictException.class)
public void testAddEventHandler_conflictCase() throws ConflictException {
EventHandler eventHandler = getBasicEventHandler();
doReturn(getBasicJson()).when(jedis).hget(anyString(), anyString());
redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler_basicCase() throws ConflictException, NotFoundException {
EventHandler eventHandler = getBasicEventHandler();
EventHandler existing = EventHandler.builder().name("testEventHandler").event("differentTestEvent").build();
doReturn(getBasicJson()).when(jedis).hget(anyString(), anyString());
redisEventHandlerDAO.updateEventHandler(eventHandler);
verify(jedis, times(1)).hset(anyString(), anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandler_notFoundCase() throws ConflictException, NotFoundException {
EventHandler eventHandler = getBasicEventHandler();
redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler_basicCase() throws NotFoundException {
EventHandler eventHandler = getBasicEventHandler();
doReturn(getBasicJson()).when(jedis).hget(anyString(), anyString());
redisEventHandlerDAO.removeEventHandler("testEventHandler");
verify(jedis, times(1)).hdel(anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandler_notFoundCase() throws NotFoundException {
redisEventHandlerDAO.removeEventHandler("nonexistentEventHandler");
}

@Test
public void testGetAllEventHandlers_basicCase() {
Map<String, String> map = getBasicMap();
doReturn(map).when(jedis).hgetAll(anyString());
List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(eventHandlers.size(), 1);
}

@Test
public void testGetEventHandlersForEvent_basicCase() throws NotFoundException {
String event = "testEvent";
String name1 = "testEventHandler1";
String name2 = "testEventHandler2";
EventHandler eventHandler1 = EventHandler.builder().name(name1).event(event).build();
EventHandler eventHandler2 = EventHandler.builder().name(name2).event(event).build();
List<String> names = new LinkedList<>();
names.add(name1);
names.add(name2);
doReturn(getBasicJson()).doReturn(getBasicJson()).when(jedis).hget(anyString(), anyString());
doReturn(names).when(jedis).smembers(anyString());
List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
assertEquals(eventHandlers.size(), 2);
}

@Test
public void testGetEventHandlersForEvent_activeOnlyCase() throws NotFoundException {
String event = "testEvent";
String name1 = "testEventHandler1";
String name2 = "testEventHandler2";
EventHandler eventHandler1 = EventHandler.builder().name(name1).event(event).active(true).build();
EventHandler eventHandler2 = EventHandler.builder().name(name2).event(event).active(false).build();
List<String> names = new LinkedList<>();
names.add(name1);
names.add(name2);
doReturn(getBasicJson()).doReturn(getBasicJson()).when(jedis).hget(anyString(), anyString());
doReturn(names).when(jedis).smembers(anyString());
List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
assertEquals(eventHandlers.size(), 1);
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlersForEvent_notFoundCase() throws NotFoundException {
String event = "nonexistentEvent";
redisEventHandlerDAO.getEventHandlersForEvent(event, false);
}

private class RedisEventHandlerDAOWithJedis extends RedisEventHandlerDAO {

public RedisEventHandlerDAOWithJedis(
JedisProxy jedisProxy,
ObjectMapper objectMapper,
ConductorProperties conductorProperties,
RedisProperties properties) {
super(jedisProxy, objectMapper, conductorProperties, properties);
}

@Override
protected Jedis getJedis() {
return jedis;
}
}
}