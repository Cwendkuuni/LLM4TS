import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

// Add test cases for addEventHandler
@Test
public void testAddEventHandler_happyPath() {
String eventHandlerJson = "{}";
String eventHandlerName = "testEventHandler";
EventHandler eventHandler = new EventHandler();
eventHandler.setName(eventHandlerName);

when(objectMapper.convertValue(eventHandler, String.class)).thenReturn(eventHandlerJson);
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandlerName, eventHandlerJson);
}

@Test(expected = ConflictException.class)
public void testAddEventHandler_conflict() {
String eventHandlerJson = "{}";
String eventHandlerName = "testEventHandler";
EventHandler eventHandler = new EventHandler();
eventHandler.setName(eventHandlerName);

when(objectMapper.convertValue(eventHandler, String.class)).thenReturn(eventHandlerJson);
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(0L);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

// Add test cases for updateEventHandler
@Test
public void testUpdateEventHandler_happyPath() {
String updatedEventHandlerJson = "{}";
String eventHandlerName = "testEventHandler";
EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName(eventHandlerName);

when(objectMapper.convertValue(updatedEventHandler, String.class)).thenReturn(updatedEventHandlerJson);
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandlerName, updatedEventHandlerJson);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandler_notFound() {
String updatedEventHandlerJson = "{}";
String eventHandlerName = "testEventHandler";
EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName(eventHandlerName);

when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(0L);
redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
}

// Add test cases for removeEventHandler
@Test
public void testRemoveEventHandler_happyPath() {
String eventHandlerName = "testEventHandler";
redisEventHandlerDAO.removeEventHandler(eventHandlerName);

verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandlerName);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandler_notFound() {
String eventHandlerName = "testEventHandler";

when(jedisProxy.hdel(anyString(), anyString())).thenReturn(0L);
redisEventHandlerDAO.removeEventHandler(eventHandlerName);
}

// Add test cases for getAllEventHandlers
@Test
public void testGetAllEventHandlers_happyPath() {
Map<String, String> eventHandlersMap = new HashMap<>();
eventHandlersMap.put("testEventHandler1", "{}");
eventHandlersMap.put("testEventHandler2", "{}");

when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersMap);

List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(2, eventHandlers.size());
}

// Add test cases for index
@Test
public void testIndex_happyPath() {
String event = "testEvent";
String eventHandlerName = "testEventHandler";

redisEventHandlerDAO.index(new EventHandler().setEvent(event).setName(eventHandlerName));

Set<String> names = new HashSet<>();
names.add(eventHandlerName);
verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
}

// Add test cases for removeIndex
@Test
public void testRemoveIndex_happyPath() {
String event = "testEvent";
String eventHandlerName = "testEventHandler";

redisEventHandlerDAO.removeIndex(new EventHandler().setEvent(event).setName(eventHandlerName));

verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandlerName);
}

// Add test cases for getEventHandlersForEvent
@Test
public void testGetEventHandlersForEvent_happyPath() {
String event = "testEvent";
String eventHandlerName1 = "testEventHandler1";
String eventHandlerName2 = "testEventHandler2";

when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(new HashSet<>(List.of(eventHandlerName1, eventHandlerName2)));

List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
assertEquals(2, eventHandlers.size());
}

// Add test cases for getEventHandler
@Test
public void testGetEventHandler_happyPath() {
String eventHandlerName = "testEventHandler";
String eventHandlerJson = "{}";

when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName)).thenReturn(eventHandlerJson);

EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventHandlerName);
assertEquals(eventHandlerJson, objectMapper.convertValue(eventHandler, String.class));
}

@Test(expected = NotFoundException.class)
public void testGetEventHandler_notFound() {
String eventHandlerName = "testEventHandler";

when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName)).thenReturn(null);

redisEventHandlerDAO.getEventHandler(eventHandlerName);
}

private String nsKey(String... keys) {
StringBuilder sb = new StringBuilder();
for (String key : keys) {
sb.append(key).append(":");
}
return sb.substring(0, sb.length() - 1);
}
}