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
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

@Component
@Conditional(AnyRedisCondition.class)
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
 private String eventHandlerJson1;
 private String eventHandlerJson2;
 private String eventHandlerJson3;

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
 eventHandlerMap.put("eh1", eventHandlerJson1);
 eventHandlerMap.put("eh2", eventHandlerJson2);
 eventHandlerMap.put("eh3", eventHandlerJson3);

 eventHandlerJson1 = "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}";
 eventHandlerJson2 = "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}";
 eventHandlerJson3 = "{\"name\":\"eh3\",\"event\":\"event3\",\"active\":true}";
 }

 @Test
 public void testAddEventHandler() {
 String name = "eh4";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("event4");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(name), anyString());
 verify(jedisProxy, times(1)).sadd(anyString(), eq(name));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 String name = "eh1";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("event1");
 eventHandler.setActive(true);

 when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson1);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetEventHandler() {
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson1);

 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("eh1");

 verify(jedisProxy, times(1)).hget(anyString(), anyString());
 assertEquals(eventHandler1, eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

 redisEventHandlerDAO.getEventHandler("eh4");
 }

 @Test
 public void testGetAllEventHandlers() {
 when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlerMap);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 verify(jedisProxy, times(1)).hgetAll(anyString());
 assertEquals(this.eventHandlers, eventHandlers);
 }

 @Test
 public void testUpdateEventHandler() {
 String name = "eh1";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("event1");
 eventHandler.setActive(false);

 when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson1);

 redisEventHandlerDAO.updateEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(name), anyString());
 verify(jedisProxy, times(1)).srem(anyString(), eq(name));
 verify(jedisProxy, times(1)).sadd(anyString(), eq(name));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 String name = "eh4";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("event4");
 eventHandler.setActive(true);

 redisEventHandlerDAO.updateEventHandler(eventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 String name = "eh1";

 redisEventHandlerDAO.removeEventHandler(name);

 verify(jedisProxy, times(1)).hdel(anyString(), eq(name));
 verify(jedisProxy, times(1)).srem(anyString(), eq(name));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 String name = "eh4";

 redisEventHandlerDAO.removeEventHandler(name);
 }

 @Test
 public void testGetEventHandlersForEvent() {
 String event = "event1";
 Set<String> names = new HashSet<>();
 names.add("eh1");

 when(jedisProxy.smembers(anyString())).thenReturn(names);
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson1);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 verify(jedisProxy, times(1)).smembers(anyString());
 verify(jedisProxy, times(1)).hget(anyString(), anyString());
 assertEquals(1, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventActiveOnlyFalse() {
 String event = "event1";
 Set<String> names = new HashSet<>();
 names.add("eh1");

 when(jedisProxy.smembers(anyString())).thenReturn(names);
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson1);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 verify(jedisProxy, times(1)).smembers(anyString());
 verify(jedisProxy, times(1)).hget(anyString(), anyString());
 assertEquals(1, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventNoMatchingEventHandlers() {
 String event = "event4";
 Set<String> names = new HashSet<>();

 when(jedisProxy.smembers(anyString())).thenReturn(names);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 verify(jedisProxy, times(1)).smembers(anyString());
 assertEquals(0, eventHandlers.size());
 }
}