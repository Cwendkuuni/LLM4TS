import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

public class RedisEventHandlerDAOTest {

@Mock private JedisProxy jedisProxy;
@Mock private ObjectMapper objectMapper;
@Mock private ConductorProperties conductorProperties;
@Mock private RedisProperties redisProperties;
@Mock private Preconditions preconditions;
@Mock private BaseDynoDAO baseDynoDAO;

@InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

private static final String EVENT_HANDLERS_KEY = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT_KEY = "EVENT_HANDLERS_BY_EVENT";
private static final String EVENT = "event";
private static final String NAME = "name";
private static final String JSON = "{}";
private static final String NS_KEY = "nsKey";
private static final EventHandler EVENT_HANDLER = new EventHandler();
private static final List<EventHandler> HANDLERS = new LinkedList<>();
private static final String EXISTING_NAME = "existingName";
private static final String EVENT_HANDLERS_BY_EVENT_KEY_EVENT =
NsKey(EVENT_HANDLERS_BY_EVENT_KEY, EVENT);

@Before
public void initMocks() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testAddEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(preconditions.checkNotNull(eventHandler.getName(), "Missing Name"))
.thenReturn(null);
when(getEventHandler(NAME)).thenReturn(null);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS_KEY), NAME, JSON);
verify(baseDynoDAO, never()).index(eventHandler);
verify(recordRedisDaoRequests, times(1)).addEventHandler();
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(preconditions.checkNotNull(eventHandler.getName(), "Missing Name"))
.thenReturn(null);
when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(preconditions.checkNotNull(eventHandler.getName(), "Missing Name"))
.thenReturn(null);
when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

redisEventHandlerDAO.updateEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS_KEY), NAME, JSON);
verify(baseDynoDAO, times(1)).index(eventHandler);
verify(recordRedisDaoRequests, times(1)).updateEventHandler();
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(preconditions.checkNotNull(eventHandler.getName(), "Missing Name"))
.thenReturn(null);
when(getEventHandler(NAME)).thenReturn(null);

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() {
when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

redisEventHandlerDAO.removeEventHandler(NAME);

verify(jedisProxy, times(1)).hdel(NS_KEY(EVENT_HANDLERS_KEY), NAME);
verify(baseDynoDAO, times(1)).removeIndex(EVENT_HANDLER);
verify(recordRedisDaoRequests, times(1)).removeEventHandler();
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() {
when(getEventHandler(NAME)).thenReturn(null);

redisEventHandlerDAO.removeEventHandler(NAME);
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> all = new HashMap<>();
all.put(NAME, JSON);

when(jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS_KEY))).thenReturn(all);

List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

verify(jedisProxy, times(1)).hgetAll(NS_KEY(EVENT_HANDLERS_KEY));
verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
}

private EventHandler getEventHandler(String name) {
ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
when(jedisProxy.hget(NS_KEY(EVENT_HANDLERS_KEY), argument.capture())).thenReturn(JSON);

verify(jedisProxy, times(1)).hget(NS_KEY(EVENT_HANDLERS_KEY), name);

EventHandler eventHandler = new EventHandler();
eventHandler.setName(name);
eventHandler.setJson(JSON);

return eventHandler;
}

@Test
public void testGetEventHandlersForEvent() {
when(jedisProxy.smembers(EVENT_HANDLERS_BY_EVENT_KEY_EVENT))
.thenReturn(new HashSet<>(List.of(NAME)));
when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

List<EventHandler> handlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

verify(jedisProxy, times(1)).smembers(EVENT_HANDLERS_BY_EVENT_KEY_EVENT);
verify(getEventHandler, times(1)).getName();
verify(recordRedisDaoEventRequests, times(1)).getEventHandler();
}

@Test
public void testGetEventHandlersForEventNoMatch() {
when(jedisProxy.smembers(EVENT_HANDLERS_BY_EVENT_KEY_EVENT))
.thenReturn(new HashSet<>(List.of(NAME)));
when(getEventHandler(NAME))
.thenThrow(new NotFoundException("No matching event handler found for event: " + EVENT));

List<EventHandler> handlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

verify(jedisProxy, times(1)).smembers(EVENT_HANDLERS_BY_EVENT_KEY_EVENT);
verify(getEventHandler, times(1)).getName();
verify(recordRedisDaoEventRequests, never()).getEventHandler();
}

private String nsKey(String... keys) {
return String.join(".", NS_KEY, keys);
}

private class recordRedisDaoRequests {}

private class recordRedisDaoEventRequests {}
}