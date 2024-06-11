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
 private List<EventHandler> eventHandlers;
 private Map<String, String> eventHandlerMap;
 private String json1;
 private String json2;
 private String json3;
 private String nsKey;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 eventHandler1 = new EventHandler();
 eventHandler1.setName("eh1");
 eventHandler1.setEvent("event1");
 eventHandler1.setActive(true);

 eventHandler2 = new EventHandler();
 eventHandler2.setName("eh2");
 eventHandler2.setEvent("event2");
 eventHandler2.setActive(false);

 eventHandler3 = new EventHandler();
 eventHandler3.setName("eh3");
 eventHandler3.setEvent("event3");
 eventHandler3.setActive(true);

 eventHandlers = new LinkedList<>();
 eventHandlers.add(eventHandler1);
 eventHandlers.add(eventHandler2);
 eventHandlers.add(eventHandler3);

 eventHandlerMap = new HashMap<>();
 eventHandlerMap.put("eh1", json1);
 eventHandlerMap.put("eh2", json2);
 eventHandlerMap.put("eh3", json3);

 json1 = "{}";
 json2 = "{}";
 json3 = "{}";

 nsKey = "myNsKey";
 }

 @Test
 public void testAddEventHandler() {
 Preconditions.checkNotNull(eventHandler1.getName(), "Missing Name");

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
 .thenReturn(null);

 when(objectMapper.convertValue(eventHandler1, String.class)).thenReturn(json1);

 redisEventHandlerDAO.addEventHandler(eventHandler1);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1);
 verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
 eventHandler1.getName());
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

 when(objectMapper.convertValue(eventHandler1, String.class)).thenReturn(json1);

 redisEventHandlerDAO.updateEventHandler(eventHandler1);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), json1);
 verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
 eventHandler1.getName());
 verify(jedisProxy, times(1)).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
 eventHandler1.getName());
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

 redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

 verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler1.getName());
 verify(jedisProxy, times(1)).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler1.getEvent()),
 eventHandler1.getName());
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
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlerMap);

 when(objectMapper.convertValue(json1, EventHandler.class)).thenReturn(eventHandler1);
 when(objectMapper.convertValue(json2, EventHandler.class)).thenReturn(eventHandler2);
 when(objectMapper.convertValue(json3, EventHandler.class)).thenReturn(eventHandler3);

 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(eventHandlers, result);
 }

 private String nsKey(String... keys) {
 StringBuilder sb = new StringBuilder(nsKey);
 for (String key : keys) {
 sb.append(":").append(key);
 }
 return sb.toString();
 }
}