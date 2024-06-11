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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String NAMESPACE = "test";
private static final String EVENT = "testEvent";
private static final String EVENT_HANDLER_NAME = "testEventHandler";
private static final String JSON = "{}";
private static final EventHandler EVENT_HANDLER = new EventHandler();
private static final EventHandler EVENT_HANDLER_WITH_NAME = new EventHandler();
private static final EventHandler EVENT_HANDLER_WITH_EVENT = new EventHandler();
private static final EventHandler EVENT_HANDLER_WITH_NAME_AND_EVENT = new EventHandler();
private static final EventHandler EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE = new EventHandler();
private static final EventHandler EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_INACTIVE = new EventHandler();

@Before
public void setup() {
EVENT_HANDLER.setName(null);
EVENT_HANDLER_WITH_NAME.setName(EVENT_HANDLER_NAME);
EVENT_HANDLER_WITH_EVENT.setEvent(EVENT);
EVENT_HANDLER_WITH_NAME_AND_EVENT.setName(EVENT_HANDLER_NAME).setEvent(EVENT);
EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE.setName(EVENT_HANDLER_NAME).setEvent(EVENT).setActive(true);
EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_INACTIVE.setName(EVENT_HANDLER_NAME).setEvent(EVENT).setActive(false);

ReflectionTestUtils.setField(redisEventHandlerDAO, "namespace", NAMESPACE);
}

@Test
public void testAddEventHandler() throws ConflictException, NotFoundException, TransientException {
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(null);

redisEventHandlerDAO.addEventHandler(EVENT_HANDLER_WITH_NAME);

verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), eq(JSON));
verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, NotFoundException, TransientException {
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(JSON);

redisEventHandlerDAO.addEventHandler(EVENT_HANDLER_WITH_NAME);
}

@Test
public void testUpdateEventHandler() throws ConflictException, NotFoundException, TransientException {
EventHandler existing = EVENT_HANDLER_WITH_NAME_AND_EVENT;
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(toJson(existing));

redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE);

verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), eq(toJson(EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE)));
verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
verify(jedisProxy, times(1)).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws ConflictException, NotFoundException, TransientException {
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(null);

redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE);
}

@Test
public void testRemoveEventHandler() throws NotFoundException {
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(toJson(EVENT_HANDLER_WITH_NAME_AND_EVENT));

redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME));
verify(jedisProxy, times(1)).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(EVENT_HANDLER_NAME));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException {
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(null);

redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> all = new HashMap<>();
all.put(EVENT_HANDLER_NAME, JSON);
when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(all);

List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(1, handlers.size());
assertEquals(EVENT_HANDLER_NAME, handlers.get(0).getName());
}

@Test
public void testGetEventHandlersForEvent() {
String key = nsKey(EVENT_HANDLERS_BY_EVENT, EVENT);
Set<String> names = mock(Set.class);
when(names.contains(EVENT_HANDLER_NAME)).thenReturn(true);
when(jedisProxy.smembers(eq(key))).thenReturn(names);
when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME))).thenReturn(toJson(EVENT_HANDLER_WITH_NAME_AND_EVENT_AND_ACTIVE));

List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

assertEquals(1, handlers.size());
assertEquals(EVENT_HANDLER_NAME, handlers.get(0).getName());
}

@Test
public void testGetEventHandlersForEventEmpty() {
String key = nsKey(EVENT_HANDLERS_BY_EVENT, EVENT);
Set<String> names = mock(Set.class);
when(names.contains(EVENT_HANDLER_NAME)).thenReturn(false);
when(jedisProxy.smembers(eq(key))).thenReturn(names);

List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

assertEquals(0, handlers.size());
}

private String nsKey(String... keys) {
return String.join(":", NAMESPACE, keys);
}

private String toJson(Object object) throws TransientException {
try {
return objectMapper.writeValueAsString(object);
} catch (Exception e) {
throw new TransientException("Unable to convert object to JSON", e);
}
}
}