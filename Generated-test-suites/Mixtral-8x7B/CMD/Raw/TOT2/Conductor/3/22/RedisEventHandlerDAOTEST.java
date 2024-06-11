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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
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

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String EVENT = "event";
private static final String NAME = "name";
private static final String JSON = "json";
private static final String NS_KEY = "nsKey";
private static final String HSET = "hset";
private static final String HDEL = "hdel";
private static final String SADD = "sadd";
private static final String SMEMBERS = "smembers";
private static final String HGET = "hget";
private static final String HGETALL = "hgetAll";

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void addEventHandlerTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");

when(jedisProxy.hset(NS_KEY(EVENT_HANDLERS), any(), any())).thenReturn("1");
when(objectMapper.valueToTree(any())).thenReturn(new HashMap<>());

redisEventHandlerDAO.addEventHandler(eventHandler);

Mockito.verify(jedisProxy).hset(NS_KEY(EVENT_HANDLERS), eventHandler.getName(), JSON);
Mockito.verify(recordRedisDaoRequests).call("addEventHandler");
}

@Test(expected = ConflictException.class)
public void addEventHandlerConflictTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");

when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void updateEventHandlerTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");

when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
when(jedisProxy.hset(NS_KEY(EVENT_HANDLERS), any(), any())).thenReturn("1");
when(objectMapper.valueToTree(any())).thenReturn(new HashMap<>());

redisEventHandlerDAO.updateEventHandler(eventHandler);

Mockito.verify(jedisProxy).hset(NS_KEY(EVENT_HANDLERS), eventHandler.getName(), JSON);
Mockito.verify(recordRedisDaoRequests).call("updateEventHandler");
}

@Test(expected = NotFoundException.class)
public void updateEventHandlerNotFoundTest() {
redisEventHandlerDAO.updateEventHandler(new EventHandler());
}

@Test
public void removeEventHandlerTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");

when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
when(jedisProxy.hdel(NS_KEY(EVENT_HANDLERS), any())).thenReturn(1L);

redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

Mockito.verify(jedisProxy).hdel(NS_KEY(EVENT_HANDLERS), NAME);
Mockito.verify(recordRedisDaoRequests).call("removeEventHandler");
}

@Test(expected = NotFoundException.class)
public void removeEventHandlerNotFoundTest() {
redisEventHandlerDAO.removeEventHandler("nonexistentName");
}

@Test
public void getAllEventHandlersTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName1");

Map<String, String> all = new HashMap<>();
all.put(eventHandler.getName(), JSON);

when(jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS))).thenReturn(all);
when(objectMapper.readValue(any(), any())).thenReturn(eventHandler);

List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

Assert.assertEquals(result.size(), 1);
Assert.assertEquals(result.get(0).getName(), eventHandler.getName());
Mockito.verify(recordRedisDaoRequests).call("getAllEventHandlers");
}

@Test
public void indexTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");
eventHandler.setEvent(EVENT);

when(jedisProxy.sadd(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), any())).thenReturn(1L);

redisEventHandlerDAO.index(eventHandler);

Mockito.verify(jedisProxy).sadd(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
}

@Test
public void removeIndexTest() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testName");
eventHandler.setEvent(EVENT);

when(jedisProxy.srem(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), any())).thenReturn(1L);

redisEventHandlerDAO.removeIndex(eventHandler);

Mockito.verify(jedisProxy).srem(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT), NAME);
}

@Test
public void getEventHandlersForEventTest() {
List<EventHandler> eventHandlers = new LinkedList<>();
eventHandlers.add(new EventHandler());
eventHandlers.add(new EventHandler());

when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(Set.of("name1", "name2"));
when(getEventHandler("name1")).thenReturn(eventHandlers.get(0));
when(getEventHandler("name2")).thenReturn(eventHandlers.get(1));

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

Assert.assertEquals(result, eventHandlers);
Mockito.verify(recordRedisDaoEventRequests).call("getEventHandler", EVENT);
}

@Test
public void getEventHandlersForEventNoMatchTest() {
when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(Set.of());

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

Assert.assertEquals(result.size(), 0);
Mockito.verify(recordRedisDaoEventRequests).call("getEventHandler", EVENT);
}

private <T> T readValue(String json, Class<T> clazz) {
try {
return objectMapper.readValue(json, clazz);
} catch (Exception e) {
throw new RuntimeException(e);
}
}

private String NS_KEY(String... keys) {
StringBuilder sb = new StringBuilder();
sb.append(NS_KEY);
for (String key : keys) {
sb.append(":").append(key);
}
return sb.toString();
}

private EventHandler getEventHandler(String name) {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(name);
return eventHandler;
}

private void recordRedisDaoRequests(String methodName) {
Mockito.doNothing().when(recordRedisDaoRequests).call(methodName);
}

private void recordRedisDaoEventRequests(String methodName, String event) {
Mockito.doNothing().when(recordRedisDaoEventRequests).call(methodName, event);
}
}