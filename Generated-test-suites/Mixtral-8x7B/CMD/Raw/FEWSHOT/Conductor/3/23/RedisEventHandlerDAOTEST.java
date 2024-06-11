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

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
 private static final String NAME_SPACE = "conductor";
 private static final String EVENT = "event1";
 private static final String NAME = "handler1";
 private static final String JSON = "{\"name\":\"handler1\",\"event\":\"event1\",\"active\":true}";
 private static final EventHandler EVENT_HANDLER = new EventHandler(NAME, EVENT, true);
 private static final List<EventHandler> HANDLERS = new LinkedList<EventHandler>() {{
 add(EVENT_HANDLER);
 }};
 private static final String KEY = NAME_SPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT;
 private static final String NAME_SPACE_KEY = NAME_SPACE + ":" + EVENT_HANDLERS;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddEventHandler() {
 Preconditions.checkNotNull(EVENT_HANDLER.getName(), "Missing Name");
 when(getEventHandler(EVENT_HANDLER.getName())).thenReturn(null);
 when(objectMapper.canSerialize(EventHandler.class)).thenReturn(true);
 when(objectMapper.convertValue(EVENT_HANDLER, String.class)).thenReturn(JSON);
 when(jedisProxy.hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON)).thenReturn(1L);

 redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

 verify(jedisProxy, times(1)).hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 Preconditions.checkNotNull(EVENT_HANDLER.getName(), "Missing Name");
 when(getEventHandler(EVENT_HANDLER.getName())).thenReturn(EVENT_HANDLER);

 redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);

 verify(jedisProxy, never()).hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON);
 }

 @Test
 public void testUpdateEventHandler() {
 Preconditions.checkNotNull(EVENT_HANDLER.getName(), "Missing Name");
 EventHandler existing = new EventHandler(NAME, EVENT, true);
 when(getEventHandler(EVENT_HANDLER.getName())).thenReturn(existing);
 when(objectMapper.canSerialize(EventHandler.class)).thenReturn(true);
 when(objectMapper.convertValue(EVENT_HANDLER, String.class)).thenReturn(JSON);
 when(jedisProxy.hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON)).thenReturn(1L);

 redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

 verify(jedisProxy, times(1)).hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 Preconditions.checkNotNull(EVENT_HANDLER.getName(), "Missing Name");
 when(getEventHandler(EVENT_HANDLER.getName())).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);

 verify(jedisProxy, never()).hset(NAME_SPACE_KEY, EVENT_HANDLER.getName(), JSON);
 }

 @Test
 public void testRemoveEventHandler() {
 Preconditions.checkNotNull(NAME, "Missing Name");
 EventHandler existing = new EventHandler(NAME, EVENT, true);
 when(getEventHandler(NAME)).thenReturn(existing);

 redisEventHandlerDAO.removeEventHandler(NAME);

 verify(jedisProxy, times(1)).hdel(NAME_SPACE_KEY, NAME);
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 Preconditions.checkNotNull(NAME, "Missing Name");
 when(getEventHandler(NAME)).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler(NAME);

 verify(jedisProxy, never()).hdel(NAME_SPACE_KEY, NAME);
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> all = new HashMap<>();
 all.put(NAME, JSON);
 when(jedisProxy.hgetAll(NAME_SPACE_KEY)).thenReturn(all);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);

 List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(HANDLERS, handlers);
 }

 @Test
 public void testGetEventHandlersForEvent() {
 Set<String> names = new LinkedHashSet<>();
 names.add(NAME);
 when(jedisProxy.smembers(KEY)).thenReturn(names);
 when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

 assertEquals(HANDLERS, handlers);
 }

 @Test
 public void testGetEventHandlersForEvent_isEmpty() {
 when(jedisProxy.smembers(KEY)).thenReturn(new LinkedHashSet<>());

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

 assertTrue(handlers.isEmpty());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandler() {
 when(getEventHandler(NAME)).thenReturn(null);

 redisEventHandlerDAO.getEventHandler(NAME);
 }

 @Test
 public void testGetEventHandler() {
 when(getEventHandler(NAME)).thenReturn(EVENT_HANDLER);

 EventHandler handler = redisEventHandlerDAO.getEventHandler(NAME);

 assertEquals(EVENT_HANDLER, handler);
 }

 private EventHandler getEventHandler(String name) {
 EventHandler eventHandler = null;
 String json;
 try {
 json = jedisProxy.hget(NAME_SPACE_KEY, name);
 } catch (Exception e) {
 throw new TransientException("Unable to get event handler named " + name, e);
 }
 if (json != null) {
 eventHandler = objectMapper.readValue(json, EventHandler.class);
 }
 return eventHandler;
 }
}