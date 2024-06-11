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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
@Conditional(AnyRedisCondition.class)
private RedisEventHandlerDAO redisEventHandlerDAO;

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String NAME = "name";
private static final String EVENT = "event";
private static final String JSON = "json";
private static final String NS_KEY = "nsKey";
private static final String KEY = "key";
private static final String EXCEPTION_MSG = "Exception message";
private static final EventHandler EVENT_HANDLER = new EventHandler();
private static final String EVENT_HANDLER_NAME = "eventHandlerName";
private static final String EVENT_NAME = "eventName";
private static final List<EventHandler> HANDLERS = new LinkedList<>();

@Before
public void setup() {
EVENT_HANDLER.setName(EVENT_HANDLER_NAME);
EVENT_HANDLER.setEvent(EVENT_NAME);
HANDLERS.add(EVENT_HANDLER);
LOGGER.info("Setting up RedisEventHandlerDAOTest");
}

@Test
public void testAddEventHandler() throws Exception {
LOGGER.info("Testing addEventHandler");
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), EVENT_HANDLER_NAME, JSON);
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws Exception {
LOGGER.info("Testing addEventHandlerConflict");
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(EVENT_HANDLER);
redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
}

@Test
public void testUpdateEventHandler() throws Exception {
LOGGER.info("Testing updateEventHandler");
EventHandler existing = spy(EVENT_HANDLER);
when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON).thenReturn(null);
when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(existing);
redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), EVENT_HANDLER_NAME, JSON);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws Exception {
LOGGER.info("Testing updateEventHandlerNotFound");
when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
}

@Test
public void testRemoveEventHandler() throws Exception {
LOGGER.info("Testing removeEventHandler");
EventHandler existing = spy(EVENT_HANDLER);
doNothing().when(existing).getName();
when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(existing);
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).hdel(NS_KEY(EVENT_HANDLERS), EVENT_HANDLER_NAME);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFoundException() throws Exception {
LOGGER.info("Testing removeEventHandlerNotFoundException");
when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(null);
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
}

@Test
public void testGetAllEventHandlers() throws Exception {
LOGGER.info("Testing getAllEventHandlers");
Map<String, String> all = mock(Map.class);
when(jedisProxy.hgetAll(anyString())).thenReturn(all);
all.put(EVENT_HANDLER_NAME, JSON);
List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(HANDLERS, handlers);
}

@Test
public void testGetEventHandlersForEvent() throws Exception {
LOGGER.info("Testing getEventHandlersForEvent");
Set<String> names = mock(Set.class);
when(jedisProxy.smembers(anyString())).thenReturn(names);
for (String name : names) {
when(getEventHandler(name)).thenReturn(EVENT_HANDLER);
}
List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);
assertEquals(HANDLERS, handlers);
}

@Test
public void testGetEventHandlersForEventActiveOnly() throws Exception {
LOGGER.info("Testing getEventHandlersForEventActiveOnly");
Set<String> names = mock(Set.class);
when(jedisProxy.smembers(anyString())).thenReturn(names);
for (String name : names) {
when(getEventHandler(name)).thenReturn(EVENT_HANDLER);
}
EVENT_HANDLER.setActive(false);
List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
assertEquals(0, handlers.size());
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlerNotFoundException() throws Exception {
LOGGER.info("Testing getEventHandlerNotFoundException");
when(getEventHandler(anyString())).thenReturn(null);
redisEventHandlerDAO.getEventHandler(anyString());
}

private EventHandler getEventHandler(String name) throws NotFoundException, TransientException {
// Implement this method to return EventHandler based on the name from Redis
throw new NotFoundException(EXCEPTION_MSG);
}

private String NS_KEY(String key) {
return NS_KEY + "(" + key + ")";
}
}