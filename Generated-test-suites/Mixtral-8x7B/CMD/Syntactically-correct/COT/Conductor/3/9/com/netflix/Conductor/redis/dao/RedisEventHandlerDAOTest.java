import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

private static final String EVENT_HANDLER_NAME = "eventHandlerName";
private static final String EVENT_HANDLER_EVENT = "eventHandlerEvent";
private static final String EVENT_HANDLERS_NS_KEY = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT_NS_KEY = "EVENT_HANDLERS_BY_EVENT";
private static final String EVENT_KEY = "eventKey";
private static final String JSON = "{\"name\":\"eventHandlerName\",\"event\":\"eventHandlerEvent\",\"active\":true}";
private static final String DIFFERENT_JSON = "{\"name\":\"differentEventHandlerName\",\"event\":\"eventHandlerEvent\",\"active\":true}";
private EventHandler eventHandler;
private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private RedisEventHandlerDAO redisEventHandlerDAO;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;

@Before
public void setup() {
eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT_HANDLER_EVENT);
eventHandler.setActive(true);

jedisProxy = mock(JedisProxy.class);
objectMapper = mock(ObjectMapper.class);
conductorProperties = mock(ConductorProperties.class);
redisProperties = mock(RedisProperties.class);

redisEventHandlerDAO =
new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
}

@Test
public void testAddEventHandler() {
when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(null);

redisEventHandlerDAO.addEventHandler(eventHandler);

Map<String, String> all = new HashMap<>();
all.put(EVENT_HANDLER_NAME, JSON);
when(getJedisProxy().hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
assertEquals(EVENT_HANDLER_NAME, result.getName());
assertEquals(EVENT_HANDLER_EVENT, result.getEvent());
assertEquals(true, result.isActive());
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() {
when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() {
when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON);

EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName(EVENT_HANDLER_NAME);
updatedEventHandler.setEvent("differentEvent");
updatedEventHandler.setActive(true);

redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

Map<String, String> all = new HashMap<>();
all.put(EVENT_HANDLER_NAME, DIFFERENT_JSON);
when(getJedisProxy().hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
assertEquals(EVENT_HANDLER_NAME, result.getName());
assertEquals("differentEvent", result.getEvent());
assertEquals(true, result.isActive());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() {
when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(null);

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() {
when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON);

redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(null);

assertNull(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() {
redisEventHandlerDAO.removeEventHandler("nonexistentEventHandler");
}

@Test
public void testGetAllEventHandlers() {
when(getJedisProxy().hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(getAllEventHandlersJson());

List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(2, result.size());
}

private Map<String, String> getAllEventHandlersJson() {
Map<String, String> all = new HashMap<>();
all.put("eventHandler1", JSON);
all.put("eventHandler2", JSON);
return all;
}

@Test
public void testGetEventHandlersForEvent() {
when(getJedisProxy().smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_EVENT)))
.thenReturn(getEventHandlerNames());

when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME))
.thenReturn(JSON);

List<EventHandler> result =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT_HANDLER_EVENT, true);

assertEquals(1, result.size());
assertEquals(EVENT_HANDLER_NAME, result.get(0).getName());
assertEquals(EVENT_HANDLER_EVENT, result.get(0).getEvent());
assertEquals(true, result.get(0).isActive());
}

@Test
public void testGetEventHandlersForEventActiveOnlyFalse() {
when(getJedisProxy().smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_EVENT)))
.thenReturn(getEventHandlerNames());

when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME))
.thenReturn(JSON);

when(getJedisProxy().hget(nsKey(EVENT_HANDLERS), "inactiveEventHandler"))
.thenReturn("{\"name\":\"inactiveEventHandler\",\"event\":\"eventHandlerEvent\",\"active\":false}");

List<EventHandler> result =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT_HANDLER_EVENT, false);

assertEquals(2, result.size());
}

private Set<String> getEventHandlerNames() {
Set<String> names = new HashSet<>();
names.add(EVENT_HANDLER_NAME);
return names;
}

private String nsKey(String... keys) {
StringBuilder sb = new StringBuilder(EVENT_HANDLERS_NS_KEY);
for (int i = 0; i < keys.length; i++) {
sb.append(':').append(keys[i]);
}
return sb.toString();
}

private JedisProxy getJedisProxy() {
return jedisProxy;
}

private ObjectMapper getObjectMapper() {
return objectMapper;
}

private ConductorProperties getConductorProperties() {
return conductorProperties;
}

private RedisProperties getRedisProperties() {
return redisProperties;
}
}