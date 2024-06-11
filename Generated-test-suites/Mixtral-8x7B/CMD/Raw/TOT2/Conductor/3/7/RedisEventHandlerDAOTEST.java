import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

private static final String EVENT_HANDLER_NAME = "testEventHandler";
private static final String EVENT_NAME = "testEvent";
private static final String JSON = "{}";
private static final String NS_KEY = "testNsKey";

private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;
private RedisEventHandlerDAO redisEventHandlerDAO;

@Before
public void setUp() {
jedisProxy = mock(JedisProxy.class);
objectMapper = mock(ObjectMapper.class);
conductorProperties = mock(ConductorProperties.class);
redisProperties = mock(RedisProperties.class);

redisEventHandlerDAO = new RedisEventHandlerDAO(
jedisProxy, objectMapper, conductorProperties, redisProperties) {
@Override
protected String nsKey(String... keys) {
if (keys.length == 0) {
return NS_KEY;
}
return String.join("_", keys);
}
};
}

@After
public void tearDown() {
redisEventHandlerDAO = null;
}

@Test
public void testAddEventHandler() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY + "_EVENT_HANDLERS", EVENT_HANDLER_NAME, JSON);
verify(jedisProxy, times(1)).sadd(NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + EVENT_NAME, EVENT_HANDLER_NAME);
assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);

redisEventHandlerDAO.addEventHandler(eventHandler);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() throws Exception {
EventHandler existingEventHandler = new EventHandler();
existingEventHandler.setName(EVENT_HANDLER_NAME);
existingEventHandler.setEvent(EVENT_NAME);

redisEventHandlerDAO.addEventHandler(existingEventHandler);

EventHandler newEventHandler = new EventHandler();
newEventHandler.setName(EVENT_HANDLER_NAME);
newEventHandler.setEvent(EVENT_NAME + "_new");

redisEventHandlerDAO.updateEventHandler(newEventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY + "_EVENT_HANDLERS", EVENT_HANDLER_NAME, JSON);
verify(jedisProxy, times(1)).srem(NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + EVENT_NAME, EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).sadd(NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + newEventHandler.getEvent(), EVENT_HANDLER_NAME);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws Exception {
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
}

@Test
public void testRemoveEventHandler() throws Exception {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);

redisEventHandlerDAO.addEventHandler(eventHandler);

redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

verify(jedisProxy, times(1)).hdel(NS_KEY + "_EVENT_HANDLERS", EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).srem(NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + EVENT_NAME, EVENT_HANDLER_NAME);
}

@Test
public void testGetAllEventHandlers() throws Exception {
List<EventHandler> eventHandlers = new LinkedList<>();
eventHandlers.add(new EventHandler());
eventHandlers.add(new EventHandler());

when(jedisProxy.hgetAll(NS_KEY + "_EVENT_HANDLERS")).thenReturn(Collections.singletonMap(EVENT_HANDLER_NAME, JSON));
when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(new EventHandler());

assertEquals(eventHandlers, redisEventHandlerDAO.getAllEventHandlers());
}

@Test
public void testGetEventHandlersForEvent() throws Exception {
List<EventHandler> eventHandlers = new LinkedList<>();
eventHandlers.add(new EventHandler());
eventHandlers.add(new EventHandler());

String key = NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + EVENT_NAME;
when(jedisProxy.smembers(key)).thenReturn(Collections.singleton(EVENT_HANDLER_NAME));
when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(new EventHandler());

assertEquals(eventHandlers, redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true));
}

@Test
public void testGetEventHandlersForEventActiveOnlyFalse() throws Exception {
List<EventHandler> eventHandlers = new LinkedList<>();
eventHandlers.add(new EventHandler());
eventHandlers.add(new EventHandler());

String key = NS_KEY + "_EVENT_HANDLERS_BY_EVENT_" + EVENT_NAME;
when(jedisProxy.smembers(key)).thenReturn(Collections.singleton(EVENT_HANDLER_NAME));
when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)).thenReturn(new EventHandler());

assertEquals(eventHandlers, redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false));
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlersForEventNotFound() throws Exception {
redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
}
}