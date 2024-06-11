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

import java.util.Collections;
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

 private EventHandler eventHandler1;
 private EventHandler eventHandler2;
 private EventHandler eventHandler3;
 private EventHandler eventHandler4;
 private String event = "testEvent";
 private String name1 = "testEventHandler1";
 private String name2 = "testEventHandler2";
 private String name3 = "testEventHandler3";
 private String name4 = "testEventHandler4";
 private String json1;
 private String json2;
 private String json3;
 private String json4;
 private Map<String, String> all;
 private List<EventHandler> handlers;
 private Set<String> names;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);

 eventHandler1 = new EventHandler();
 eventHandler1.setName(name1);
 eventHandler1.setEvent(event);
 eventHandler1.setActive(true);

 eventHandler2 = new EventHandler();
 eventHandler2.setName(name2);
 eventHandler2.setEvent(event);
 eventHandler2.setActive(false);

 eventHandler3 = new EventHandler();
 eventHandler3.setName(name3);
 eventHandler3.setEvent("differentEvent");
 eventHandler3.setActive(true);

 eventHandler4 = new EventHandler();
 eventHandler4.setName(name4);
 eventHandler4.setEvent("differentEvent");
 eventHandler4.setActive(false);

 json1 = "{}"; // objectMapper.writeValueAsString(eventHandler1);
 json2 = "{}"; // objectMapper.writeValueAsString(eventHandler2);
 json3 = "{}"; // objectMapper.writeValueAsString(eventHandler3);
 json4 = "{}"; // objectMapper.writeValueAsString(eventHandler4);

 all = new HashMap<>();
 all.put(name1, json1);
 all.put(name2, json2);
 all.put(name3, json3);
 all.put(name4, json4);

 handlers = new LinkedList<>();
 handlers.add(eventHandler1);
 handlers.add(eventHandler2);
 names = Collections.singleton("testEventHandler1");
 }

 @Test
 public void testAddEventHandler() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(null);
 when(jedisProxy.hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1))
 .thenReturn(1L);
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
 .thenReturn(all);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), eventHandler1.getName());
 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1);
 verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(json1);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 }

 @Test
 public void testUpdateEventHandler() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(json1);
 when(jedisProxy.hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1))
 .thenReturn(1L);
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
 .thenReturn(all);
 redisEventHandlerDAO.updateEventHandler(eventHandler1);
 verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), eventHandler1.getName());
 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1);
 verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(null);
 redisEventHandlerDAO.updateEventHandler(eventHandler1);
 }

 @Test
 public void testRemoveEventHandler() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(json1);
 when(jedisProxy.hdel(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(1L);
 redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
 verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), eventHandler1.getName());
 verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler1.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(null);
 redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
 }

 @Test
 public void testGetAllEventHandlers() {
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
 .thenReturn(all);
 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(handlers, result);
 }

 @Test
 public void testIndex() {
 when(jedisProxy.sadd(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandler1.getName()))
 .thenReturn(1L);
 redisEventHandlerDAO.index(eventHandler1);
 verify(jedisProxy, times(1))
 .sadd(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandler1.getName());
 }

 @Test
 public void testRemoveIndex() {
 when(jedisProxy.srem(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandler1.getName()))
 .thenReturn(1L);
 redisEventHandlerDAO.removeIndex(eventHandler1);
 verify(jedisProxy, times(1))
 .srem(nsKey(EVENT_HANDLERS_BY_EVENT, event), eventHandler1.getName());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event)))
 .thenReturn(names);
 when(redisEventHandlerDAO.getEventHandler(name1))
 .thenReturn(eventHandler1);
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
 assertEquals(1, result.size());
 assertEquals(eventHandler1, result.get(0));
 }

 @Test
 public void testGetEventHandler() {
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name1))
 .thenReturn(json1);
 when(objectMapper.readValue(json1, EventHandler.class))
 .thenReturn(eventHandler1);
 EventHandler result = redisEventHandlerDAO.getEventHandler(name1);
 assertEquals(eventHandler1, result);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name1))
 .thenReturn(null);
 redisEventHandlerDAO.getEventHandler(name1);
 }
}