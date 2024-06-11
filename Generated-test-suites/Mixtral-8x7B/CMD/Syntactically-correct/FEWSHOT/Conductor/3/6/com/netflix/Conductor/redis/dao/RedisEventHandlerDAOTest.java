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
import com.netflix.conductor.redis.util.RedisDaoRequestsUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import redis.clients.jedis.Jedis;

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

 @Mock
 private RedisDaoRequestsUtil redisDaoRequestsUtil;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private static final String EVENT_HANDLER_NAME = "eventHandlerName";
 private static final String EVENT = "event";
 private static final String JSON = "{}";
 private static final String NS_KEY_EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String NS_KEY_EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
 private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);
 private EventHandler eventHandler;
 private Jedis jedis;

 @Before
 public void setUp() {
 eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT);

 jedis = new Jedis();
 }

 @Test
 public void testAddEventHandler() {
 String name = eventHandler.getName();

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(null);
 when(objectMapper.valueToTree(eventHandler)).thenReturn(JSON);
 when(jedisProxy.hset(nsKey(NS_KEY_EVENT_HANDLERS), name, JSON)).thenReturn(1L);
 when(redisDaoRequestsUtil.recordRedisDaoRequests(anyString())).thenReturn(null);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verify(objectMapper, times(1)).valueToTree(eventHandler);
 verify(jedisProxy, times(1)).hset(nsKey(NS_KEY_EVENT_HANDLERS), name, JSON);
 verify(redisDaoRequestsUtil, times(1)).recordRedisDaoRequests(anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 String name = eventHandler.getName();

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(JSON);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 verifyNoMoreInteractions(redisDaoRequestsUtil);
 }

 @Test
 public void testUpdateEventHandler() {
 String name = eventHandler.getName();
 String newEvent = "newEvent";
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(name);
 updatedEventHandler.setEvent(newEvent);

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(JSON);
 when(objectMapper.valueToTree(eventHandler)).thenReturn(JSON);
 when(objectMapper.valueToTree(updatedEventHandler)).thenReturn(JSON);
 when(jedisProxy.hset(nsKey(NS_KEY_EVENT_HANDLERS), name, JSON)).thenReturn(1L);
 when(redisDaoRequestsUtil.recordRedisDaoRequests(anyString())).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verify(objectMapper, times(1)).valueToTree(eventHandler);
 verify(objectMapper, times(1)).valueToTree(updatedEventHandler);
 verify(jedisProxy, times(1)).hset(nsKey(NS_KEY_EVENT_HANDLERS), name, JSON);
 verify(redisDaoRequestsUtil, times(1)).recordRedisDaoRequests(anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 String name = eventHandler.getName();

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 verifyNoMoreInteractions(redisDaoRequestsUtil);
 }

 @Test
 public void testRemoveEventHandler() {
 String name = eventHandler.getName();

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(JSON);
 when(jedisProxy.hdel(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(1L);
 when(redisDaoRequestsUtil.recordRedisDaoRequests(anyString())).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler(name);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verify(jedisProxy, times(1)).hdel(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verify(redisDaoRequestsUtil, times(1)).recordRedisDaoRequests(anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 String name = eventHandler.getName();

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler(name);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verifyNoMoreInteractions(jedisProxy);
 verifyNoMoreInteractions(redisDaoRequestsUtil);
 }

 @Test
 public void testGetAllEventHandlers() {
 String name1 = "name1";
 String name2 = "name2";
 String event1 = "event1";
 String event2 = "event2";
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(name1);
 eventHandler1.setEvent(event1);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(name2);
 eventHandler2.setEvent(event2);
 Map<String, String> all = new LinkedHashMap<>();
 all.put(name1, JSON);
 all.put(name2, JSON);

 when(jedisProxy.hgetAll(nsKey(NS_KEY_EVENT_HANDLERS))).thenReturn(all);
 when(objectMapper.readTree(JSON)).thenReturn(null);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler1);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler2);
 when(redisDaoRequestsUtil.recordRedisDaoRequests(anyString())).thenReturn(null);

 List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

 verify(jedisProxy, times(1)).hgetAll(nsKey(NS_KEY_EVENT_HANDLERS));
 verify(objectMapper, times(2)).readTree(JSON);
 verify(objectMapper, times(2)).readValue(JSON, EventHandler.class);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 verify(redisDaoRequestsUtil, times(1)).recordRedisDaoRequests(anyString());
 assertEquals(Arrays.asList(eventHandler1, eventHandler2), handlers);
 }

 @Test
 public void testIndex() {
 String event = eventHandler.getEvent();
 String key = nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event);

 redisEventHandlerDAO.index(eventHandler);

 verify(jedisProxy, times(1)).sadd(key, eventHandler.getName());
 }

 @Test
 public void testRemoveIndex() {
 String event = eventHandler.getEvent();
 String key = nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event);

 redisEventHandlerDAO.removeIndex(eventHandler);

 verify(jedisProxy, times(1)).srem(key, eventHandler.getName());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 String event = "event";
 String name1 = "name1";
 String name2 = "name2";
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(name1);
 eventHandler1.setEvent(event);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(name2);
 eventHandler2.setEvent("otherEvent");
 Set<String> names = new HashSet<>();
 names.add(name1);
 names.add(name2);

 when(jedisProxy.smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event))).thenReturn(names);
 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name1)).thenReturn(JSON);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler1);
 when(redisDaoRequestsUtil.recordRedisDaoEventRequests(anyString(), anyString())).thenReturn(null);

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 verify(jedisProxy, times(1)).smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event));
 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name1);
 verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 verify(redisDaoRequestsUtil, times(1)).recordRedisDaoEventRequests(anyString(), anyString());
 assertEquals(Arrays.asList(eventHandler1), handlers);
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 String event = "event";
 String name1 = "name1";
 String name2 = "name2";
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(name1);
 eventHandler1.setEvent(event);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(name2);
 eventHandler2.setEvent("otherEvent");
 eventHandler2.setActive(false);
 Set<String> names = new HashSet<>();
 names.add(name1);
 names.add(name2);

 when(jedisProxy.smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event))).thenReturn(names);
 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name1)).thenReturn(JSON);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(eventHandler1);

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 verify(jedisProxy, times(1)).smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event));
 verify(jedisProxy, times(0)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name1);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 assertEquals(Arrays.asList(), handlers);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForEventNotFound() {
 String event = "event";

 when(jedisProxy.smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event))).thenReturn(new HashSet<>());

 redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 verify(jedisProxy, times(1)).smembers(nsKey(NS_KEY_EVENT_HANDLERS_BY_EVENT, event));
 verifyNoMoreInteractions(jedisProxy);
 }

 @Test
 public void testGetEventHandler() {
 String name = "name";
 String json = "{}";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(json);
 when(objectMapper.readValue(json, EventHandler.class)).thenReturn(eventHandler);

 EventHandler result = redisEventHandlerDAO.getEventHandler(name);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verify(objectMapper, times(1)).readValue(json, EventHandler.class);
 verifyNoMoreInteractions(objectMapper);
 verifyNoMoreInteractions(jedisProxy);
 assertEquals(eventHandler, result);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 String name = "name";

 when(jedisProxy.hget(nsKey(NS_KEY_EVENT_HANDLERS), name)).thenReturn(null);

 redisEventHandlerDAO.getEventHandler(name);

 verify(jedisProxy, times(1)).hget(nsKey(NS_KEY_EVENT_HANDLERS), name);
 verifyNoMoreInteractions(jedisProxy);
 }

 private String nsKey(String... keys) {
 StringBuilder sb = new StringBuilder();
 sb.append(NS_KEY_EVENT_HANDLERS).append(":");
 for (String key : keys) {
 sb.append(key).append(":");
 }
 return sb.substring(0, sb.length() - 1);
 }
}