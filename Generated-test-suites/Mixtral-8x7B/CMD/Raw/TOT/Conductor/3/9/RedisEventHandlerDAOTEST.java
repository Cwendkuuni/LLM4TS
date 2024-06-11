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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

private static final String EVENT_HANDLER_NAME = "testEventHandler";
private static final String EVENT = "testEvent";
private static final String JSON = "{}";

private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;
private RedisEventHandlerDAO redisEventHandlerDAO;

@Before
public void setup() {
jedisProxy = mock(JedisProxy.class);
objectMapper = mock(ObjectMapper.class);
conductorProperties = mock(ConductorProperties.class);
redisProperties = mock(RedisProperties.class);

redisEventHandlerDAO = new RedisEventHandlerDAO(
jedisProxy, objectMapper, conductorProperties, redisProperties) {
@Override
public String nsKey(String... keys) {
// Mock namespace key
return "test_namespace_key";
}
};
}

@After
public void tearDown() {
// Clear any mocked static behavior
Mockito.validateMockitoUsage();
}

@Test
public void testAddEventHandler() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1))
.hset(eq("test_namespace_key" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), eq(JSON));
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT);

when(jedisProxy.hget(eq("test_namespace_key" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME)))
.thenReturn(JSON);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT);

redisEventHandlerDAO.updateEventHandler(eventHandler);

verify(jedisProxy, times(1))
.hset(eq("test_namespace_key" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME), eq(JSON));
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("nonexistent");
eventHandler.setEvent(EVENT);

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws Exception {
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

verify(jedisProxy, times(1))
.hdel(eq("test_namespace_key" + RedisEventHandlerDAO.EVENT_HANDLERS), eq(EVENT_HANDLER_NAME));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws Exception {
redisEventHandlerDAO.removeEventHandler("nonexistent");
}

@Test
public void testGetAllEventHandlers() throws Exception {
Map<String, String> all = jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));

when(objectMapper.readValue(all.get(EVENT_HANDLER_NAME), EventHandler.class))
.thenReturn(new EventHandler());

List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(1, result.size());
}

@Test
public void testGetEventHandlersForEvent() throws Exception {
when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, EVENT)))
.thenReturn(new LinkedList<>(Arrays.asList(EVENT_HANDLER_NAME)));

when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), EVENT_HANDLER_NAME))
.thenReturn(JSON);

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

assertEquals(1, result.size());
}

@Test
public void testGetEventHandlersForEventActiveOnlyFalse() throws Exception {
when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, EVENT)))
.thenReturn(new LinkedList<>(Arrays.asList(EVENT_HANDLER_NAME)));

when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), EVENT_HANDLER_NAME))
.thenReturn(JSON);

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

assertEquals(1, result.size());
}

@Test
public void testGetEventHandlersForEventNoMatchingEventHandlers() throws Exception {
when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, EVENT)))
.thenReturn(new LinkedList<>());

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

assertEquals(0, result.size());
}
}