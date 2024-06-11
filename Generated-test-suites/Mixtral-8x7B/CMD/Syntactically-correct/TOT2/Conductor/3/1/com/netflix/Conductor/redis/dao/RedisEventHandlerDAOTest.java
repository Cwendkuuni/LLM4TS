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
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);
private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String EVENT = "event1";
private static final String NAME = "handler1";
private static final String JSON = "{\"name\":\"handler1\",\"event\":\"event1\",\"active\":true}";

@Before
public void setup() {
ReflectionTestUtils.setField(redisEventHandlerDAO, "nsKey", "testNsKey");
}

@After
public void tearDown() {
// Clean up any test data added to Redis
jedisProxy.flushDB();
}

@Test
public void testAddEventHandler() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);
eventHandler.setEvent(EVENT);

when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);

redisEventHandlerDAO.addEventHandler(eventHandler);

Map<String, String> all = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
Assert.assertEquals(1, all.size());
Assert.assertTrue(all.containsKey(NAME));

EventHandler retrievedEventHandler =
readValue(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME), EventHandler.class);
Assert.assertEquals(eventHandler, retrievedEventHandler);
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);
eventHandler.setEvent(EVENT);

when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() throws Exception {
EventHandler existingEventHandler = new EventHandler();
existingEventHandler.setName(NAME);
existingEventHandler.setEvent(EVENT);
existingEventHandler.setActive(true);

EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName(NAME);
updatedEventHandler.setEvent(EVENT);
updatedEventHandler.setActive(false);

when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);

redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

EventHandler retrievedEventHandler =
readValue(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME), EventHandler.class);
Assert.assertEquals(updatedEventHandler, retrievedEventHandler);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws Exception {
EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName("non-existent");
updatedEventHandler.setEvent(EVENT);

redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
}

@Test
public void testRemoveEventHandler() throws Exception {
EventHandler existingEventHandler = new EventHandler();
existingEventHandler.setName(NAME);
existingEventHandler.setEvent(EVENT);

when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));
when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1);

redisEventHandlerDAO.removeEventHandler(NAME);

Assert.assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), NAME));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws Exception {
redisEventHandlerDAO.removeEventHandler("non-existent");
}

@Test
public void testGetAllEventHandlers() throws Exception {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("handler1");
eventHandler1.setEvent("event1");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("handler2");
eventHandler2.setEvent("event2");
eventHandler2.setActive(false);

when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of(
"handler1", toJson(eventHandler1),
"handler2", toJson(eventHandler2)
));

List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
Assert.assertEquals(2, allEventHandlers.size());
Assert.assertTrue(allEventHandlers.contains(eventHandler1));
Assert.assertTrue(allEventHandlers.contains(eventHandler2));
}

@Test
public void testGetEventHandlersForEvent() throws Exception {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("handler1");
eventHandler1.setEvent("event1");
eventHandler1.setActive(true);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("handler2");
eventHandler2.setEvent("event2");
eventHandler2.setActive(false);

when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
.thenReturn(Set.of("handler1"));

when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler1"))
.thenReturn(toJson(eventHandler1));

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
Assert.assertEquals(1, eventHandlers.size());
Assert.assertEquals(eventHandler1, eventHandlers.get(0));
}

private <T> String toJson(T obj) throws Exception {
return objectMapper.writeValueAsString(obj);
}

private <T> T readValue(String json, Class<T> valueType) throws Exception {
return objectMapper.readValue(json, valueType);
}
}