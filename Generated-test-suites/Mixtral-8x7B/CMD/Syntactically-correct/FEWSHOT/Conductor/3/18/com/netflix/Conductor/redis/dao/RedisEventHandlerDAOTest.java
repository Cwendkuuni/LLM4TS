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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
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
@Mock private RedisProperties properties;
@InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String NAMESPACE = "my-namespace";
private static final String EVENT = "my-event";
private static final String NAME = "my-handler";
private static final String JSON = "{}";
private static final EventHandler EVENT_HANDLER = new EventHandler();
private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

@Before
public void init() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testAddEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(getEventHandler(NAME)).thenReturn(null);
redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
verify(index(eventHandler), times(1));
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
redisEventHandlerDAO.updateEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
verify(index(eventHandler), times(1));
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(getEventHandler(NAME)).thenReturn(null);
redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
redisEventHandlerDAO.removeEventHandler(NAME);

verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), NAME);
verify(removeIndex(eventHandler), times(1));
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() {
redisEventHandlerDAO.removeEventHandler(NAME);

verifyNoMoreInteractions(jedisProxy);
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> all = new HashMap<>();
all.put(NAME, JSON);

when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
assertEquals(1, handlers.size());
}

private String nsKey(String key) {
return nsKey(key, NAMESPACE);
}

private String nsKey(String key, String namespace) {
return String.format("%s:%s:%s", namespace, key, NAMESPACE);
}

private JedisProxy index(EventHandler eventHandler) {
String event = eventHandler.getEvent();
String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
jedisProxy.sadd(key, eventHandler.getName());
return jedisProxy;
}

private JedisProxy removeIndex(EventHandler eventHandler) {
String event = eventHandler.getEvent();
String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
jedisProxy.srem(key, eventHandler.getName());
return jedisProxy;
}

private EventHandler getEventHandler(String name) {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(JSON);
return readValue(JSON, EventHandler.class);
}

private <T> T readValue(String content, Class<T> valueType) {
try {
return objectMapper.readValue(content, valueType);
} catch (Exception e) {
throw new RuntimeException(e);
}
}

}