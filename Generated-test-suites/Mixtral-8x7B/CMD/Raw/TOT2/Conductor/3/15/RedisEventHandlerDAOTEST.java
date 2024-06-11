import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.dao.util.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

 @Mock private JedisProxy jedisProxy;
 @Mock private ObjectMapper objectMapper;
 @Mock private ConductorProperties conductorProperties;
 @Mock private RedisProperties redisProperties;
 @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

 private EventHandler eventHandler1;
 private EventHandler eventHandler2;
 private List<EventHandler> eventHandlers;
 private EventHandler nonExistentEventHandler;
 private String eventName;
 private String eventHandlerName1;
 private String eventHandlerName2;
 private Map<String, String> eventHandlerMap;
 private Task task;

 @Before
 public void setup() {
 eventName = "testEvent";
 eventHandlerName1 = "testEventHandler1";
 eventHandlerName2 = "testEventHandler2";

 eventHandler1 = new EventHandler();
 eventHandler1.setName(eventHandlerName1);
 eventHandler1.setEvent(eventName);
 eventHandler1.setActive(true);

 eventHandler2 = new EventHandler();
 eventHandler2.setName(eventHandlerName2);
 eventHandler2.setEvent(eventName);
 eventHandler2.setActive(true);

 eventHandlers = new LinkedList<>();
 eventHandlers.add(eventHandler1);
 eventHandlers.add(eventHandler2);

 nonExistentEventHandler = new EventHandler();
 nonExistentEventHandler.setName("nonExistentEventHandler");

 eventHandlerMap = new HashMap<>();
 eventHandlerMap.put(eventHandlerName1, toJson(eventHandler1));
 eventHandlerMap.put(eventHandlerName2, toJson(eventHandler2));

 task = new Task();
 task.setName("testTask");
 }

 private String toJson(EventHandler eventHandler) {
 try {
 return objectMapper.writeValueAsString(eventHandler);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testAddEventHandler() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hset(anyString(), anyString(), anyString()))
 .thenReturn(1L);

 redisEventHandlerDAO.addEventHandler(eventHandler1);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandlerName1, toJson(eventHandler1));
 verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests("addEventHandler");

 // Test duplication error
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 doThrow(new ConflictException("EventHandler with name " + eventHandlerName1 + " already exists!"))
 .when(redisEventHandlerDAO)
 .addEventHandler(eventHandler1);

 redisEventHandlerDAO.addEventHandler(eventHandler1);

 verify(redisEventHandlerDAO, times(1))
 .addEventHandler(eventHandler1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("addEventHandler");
 }

 @Test
 public void testUpdateEventHandler() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 when(jedisProxy.hset(anyString(), anyString(), anyString()))
 .thenReturn(1L);

 redisEventHandlerDAO.updateEventHandler(eventHandler1);

 verify(jedisProxy, times(1))
 .hset(nsKey(EVENT_HANDLERS), eventHandlerName1, toJson(eventHandler1));
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("updateEventHandler");

 // Test event change
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 eventHandler1.setEvent("newEvent");
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 when(jedisProxy.hdel(nsKey(EVENT_HANDLERS_BY_EVENT, eventName), eventHandlerName1))
 .thenReturn(1L);

 when(jedisProxy.sadd(nsKey(EVENT_HANDLERS_BY_EVENT, "newEvent"), eventHandlerName1))
 .thenReturn(1L);

 redisEventHandlerDAO.updateEventHandler(eventHandler1);

 verify(jedisProxy, times(1))
 .hget(nsKey(EVENT_HANDLERS), eventHandlerName1);
 verify(jedisProxy, times(1))
 .hset(nsKey(EVENT_HANDLERS), eventHandlerName1, toJson(eventHandler1));
 verify(jedisProxy, times(1))
 .hdel(nsKey(EVENT_HANDLERS_BY_EVENT, eventName), eventHandlerName1);
 verify(jedisProxy, times(1))
 .sadd(nsKey(EVENT_HANDLERS_BY_EVENT, "newEvent"), eventHandlerName1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("updateEventHandler");

 // Test non-existent error
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 doThrow(new NotFoundException("EventHandler with name " + eventHandlerName1 + " not found!"))
 .when(redisEventHandlerDAO)
 .getEventHandler(eventHandlerName1);

 redisEventHandlerDAO.updateEventHandler(eventHandler1);

 verify(redisEventHandlerDAO, times(1))
 .updateEventHandler(eventHandler1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("updateEventHandler");
 }

 @Test
 public void testRemoveEventHandler() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 when(jedisProxy.hdel(anyString(), anyString()))
 .thenReturn(1L);

 redisEventHandlerDAO.removeEventHandler(eventHandlerName1);

 verify(jedisProxy, times(1))
 .hdel(nsKey(EVENT_HANDLERS), eventHandlerName1);
 verify(jedisProxy, times(1))
 .hdel(nsKey(EVENT_HANDLERS_BY_EVENT, eventName), eventHandlerName1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("removeEventHandler");

 // Test non-existent error
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 doThrow(new NotFoundException("EventHandler with name " + eventHandlerName1 + " not found!"))
 .when(redisEventHandlerDAO)
 .getEventHandler(eventHandlerName1);

 redisEventHandlerDAO.removeEventHandler(eventHandlerName1);

 verify(redisEventHandlerDAO, times(1))
 .removeEventHandler(eventHandlerName1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("removeEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
 .thenReturn(eventHandlerMap);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, eventHandlers.size());
 verify(jedisProxy, times(1))
 .hgetAll(nsKey(EVENT_HANDLERS));
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("getAllEventHandlers");
 }

 @Test
 public void testGetEventHandlersForEvent() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 String key = nsKey(EVENT_HANDLERS_BY_EVENT, eventName);

 when(jedisProxy.smembers(key))
 .thenReturn(set(eventHandlerName1, eventHandlerName2));

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName2))
 .thenReturn(toJson(eventHandler2));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

 assertEquals(2, eventHandlers.size());
 verify(jedisProxy, times(1))
 .smembers(key);
 verify(jedisProxy, times(2))
 .hget(nsKey(EVENT_HANDLERS), anyString());
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoEventRequests("getEventHandler", eventName);

 // Test active-only
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 String keyActive = nsKey(EVENT_HANDLERS_BY_EVENT, eventName + ":active");

 when(jedisProxy.smembers(keyActive))
 .thenReturn(set(eventHandlerName1));

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 eventHandler1.setActive(false);
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 List<EventHandler> eventHandlersActiveOnly = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

 assertEquals(0, eventHandlersActiveOnly.size());
 verify(jedisProxy, times(1))
 .smembers(keyActive);
 verify(jedisProxy, times(1))
 .hget(nsKey(EVENT_HANDLERS), eventHandlerName1);
 verify(jedisProxy, never())
 .hget(nsKey(EVENT_HANDLERS), eventHandlerName2);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoEventRequests("getEventHandler", eventName);

 // Test non-existent error
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 doThrow(new NotFoundException("EventHandler with name " + eventHandlerName1 + " not found!"))
 .when(redisEventHandlerDAO)
 .getEventHandler(eventHandlerName1);

 redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

 verify(redisEventHandlerDAO, times(1))
 .getEventHandlersForEvent(eventName, false);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoEventRequests("getEventHandler", eventName);
 }

 @Test
 public void testGetEventHandler() {
 // Test basic functionality
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandlerName1))
 .thenReturn(toJson(eventHandler1));

 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventHandlerName1);

 assertEquals(eventHandler, eventHandler1);
 verify(jedisProxy, times(1))
 .hget(nsKey(EVENT_HANDLERS), eventHandlerName1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("getEventHandler");

 // Test non-existent error
 redisEventHandlerDAO = spy(redisEventHandlerDAO);

 doThrow(new NotFoundException("EventHandler with name " + eventHandlerName1 + " not found!"))
 .when(redisEventHandlerDAO)
 .getEventHandler(eventHandlerName1);

 redisEventHandlerDAO.getEventHandler(eventHandlerName1);

 verify(redisEventHandlerDAO, times(1))
 .getEventHandler(eventHandlerName1);
 verify(redisEventHandlerDAO, times(1))
 .recordRedisDaoRequests("getEventHandler");
 }
}