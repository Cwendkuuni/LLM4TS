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

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties properties;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private EventHandler eventHandler1;
 private EventHandler eventHandler2;
 private EventHandler eventHandler3;
 private String event = "testEvent";

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);

 eventHandler1 = new EventHandler();
 eventHandler1.setName("testHandler1");
 eventHandler1.setEvent(event);
 eventHandler1.setActive(true);

 eventHandler2 = new EventHandler();
 eventHandler2.setName("testHandler2");
 eventHandler2.setEvent(event);
 eventHandler2.setActive(false);

 eventHandler3 = new EventHandler();
 eventHandler3.setName("testHandler3");
 eventHandler3.setEvent("otherEvent");
 eventHandler3.setActive(true);
 }

 @Test
 public void testAddEventHandler() {
 String namespace = "testNamespace";
 when(conductorProperties.getNamespace()).thenReturn(namespace);
 when(properties.getRedisEventHandlerNamespace()).thenReturn(namespace);
 when(objectMapper.convertValue(eventHandler1, Map.class)).thenReturn(null);

 redisEventHandlerDAO.addEventHandler(eventHandler1);

 verify(jedisProxy, times(1)).hset(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS,
 eventHandler1.getName(),
 anyString());
 verify(jedisProxy, times(1)).sadd(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + event,
 eventHandler1.getName());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 }

 @Test
 public void testUpdateEventHandler() {
 String namespace = "testNamespace";
 when(conductorProperties.getNamespace()).thenReturn(namespace);
 when(properties.getRedisEventHandlerNamespace()).thenReturn(namespace);
 when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
 when(objectMapper.convertValue(eventHandler2, Map.class)).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(eventHandler2);

 verify(jedisProxy, times(1)).hset(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS,
 eventHandler2.getName(),
 anyString());
 verify(jedisProxy, times(1)).srem(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + event,
 eventHandler1.getName());
 verify(jedisProxy, times(1)).sadd(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + event,
 eventHandler2.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 when(getEventHandler(eventHandler1.getName())).thenReturn(null);
 redisEventHandlerDAO.updateEventHandler(eventHandler2);
 }

 @Test
 public void testRemoveEventHandler() {
 String namespace = "testNamespace";
 when(conductorProperties.getNamespace()).thenReturn(namespace);
 when(properties.getRedisEventHandlerNamespace()).thenReturn(namespace);
 when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

 redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

 verify(jedisProxy, times(1)).hdel(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS,
 eventHandler1.getName());
 verify(jedisProxy, times(1)).srem(
 namespace + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT + ":" + event,
 eventHandler1.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 when(getEventHandler(eventHandler1.getName())).thenReturn(null);
 redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> eventHandlers = new LinkedHashMap<>();
 eventHandlers.put(eventHandler1.getName(), toJson(eventHandler1));
 eventHandlers.put(eventHandler2.getName(), toJson(eventHandler2));

 when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);
 when(objectMapper.readValue(anyString(), eq(EventHandler.class)))
 .thenReturn(eventHandler1)
 .thenReturn(eventHandler2);

 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, result.size());
 verify(jedisProxy, times(1)).hgetAll(anyString());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 Set<String> handlerNames = new LinkedHashSet<>();
 handlerNames.add(eventHandler1.getName());
 handlerNames.add(eventHandler2.getName());

 when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);
 when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
 when(getEventHandler(eventHandler2.getName())).thenReturn(eventHandler2);

 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 assertEquals(2, result.size());
 verify(jedisProxy, times(1)).smembers(anyString());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 Set<String> handlerNames = new LinkedHashSet<>();
 handlerNames.add(eventHandler1.getName());

 when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);
 when(getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 assertEquals(1, result.size());
 verify(jedisProxy, times(1)).smembers(anyString());
 }

 private EventHandler getEventHandler(String name) {
 EventHandler eventHandler = null;
 String json;
 try {
 json = jedisProxy.hget(nsKey(EVENT_HANDLERS), name);
 } catch (Exception e) {
 throw new TransientException("Unable to get event handler named " + name, e);
 }
 if (json != null) {
 eventHandler = readValue(json, EventHandler.class);
 }
 return eventHandler;
 }

 private String toJson(Object object) {
 try {
 return objectMapper.writeValueAsString(object);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private <T> T readValue(String content, Class<T> valueType) {
 try {
 return objectMapper.readValue(content, valueType);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private String nsKey(String... keys) {
 StringBuilder sb = new StringBuilder();
 sb.append(conductorProperties.getNamespace());
 for (String key : keys) {
 sb.append(":").append(key);
 }
 return sb.toString();
 }
}