import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
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

 private Jedis jedis;

 @Before
 public void setUp() {
 jedis = new Jedis();
 }

 @After
 public void tearDown() {
 jedis.close();
 }

 @Test
 public void testAddEventHandler() throws ConflictException, IOException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");

 when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
 when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), name, "{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");
 verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("addEventHandler");
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() throws ConflictException, IOException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");

 when(getEventHandler(name)).thenReturn(eventHandler);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testUpdateEventHandler() throws NotFoundException, IOException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");
 EventHandler existing = new EventHandler();
 existing.setName(name);
 existing.setEvent("oldTestEvent");

 when(getEventHandler(name)).thenReturn(existing);
 when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
 when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

 redisEventHandlerDAO.updateEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), name, "{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");
 verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("updateEventHandler");
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");

 when(getEventHandler(name)).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(eventHandler);
 }

 @Test
 public void testRemoveEventHandler() throws NotFoundException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);

 when(getEventHandler(name)).thenReturn(eventHandler);
 when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

 redisEventHandlerDAO.removeEventHandler(name);

 verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), name);
 verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("removeEventHandler");
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() throws NotFoundException {
 String name = "testEventHandler";

 when(getEventHandler(name)).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler(name);
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> all = jedis.hgetAll("EVENT_HANDLERS");
 List<EventHandler> handlers = new LinkedList<>();
 all.forEach(
 (key, json) -> {
 EventHandler eventHandler = new EventHandler();
 //parse JSON to set eventHandler properties
 handlers.add(eventHandler);
 });

 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(handlers.size(), result.size());
 verify(recordRedisDaoRequests, times(1)).recordRedisDaoRequests("getAllEventHandlers");
 }

 @Test
 public void testGetEventHandlersForEvent() throws NotFoundException {
 String event = "testEvent";
 Set<String> names = jedis.smembers("EVENT_HANDLERS_BY_EVENT:" + event);
 List<EventHandler> handlers = new LinkedList<>();
 for (String name : names) {
 EventHandler eventHandler = new EventHandler();
 //parse JSON to set eventHandler properties
 handlers.add(eventHandler);
 }

 when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(names);

 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 assertEquals(handlers.size(), result.size());
 verify(recordRedisDaoEventRequests, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
 }

 @Test
 public void testGetEventHandler() throws NotFoundException, IOException {
 String name = "testEventHandler";
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");
 String json = objectMapper.writeValueAsString(eventHandler);

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(json);

 EventHandler result = redisEventHandlerDAO.getEventHandler(name);

 assertEquals(eventHandler, result);
 verify(recordRedisDaoEventRequests, times(1)).recordRedisDaoEventRequests("getEventHandler", "testEvent");
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() throws NotFoundException, IOException {
 String name = "testEventHandler";

 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(null);

 redisEventHandlerDAO.getEventHandler(name);
 }

 private EventHandler getEventHandler(String name) throws IOException {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");
 return eventHandler;
 }

 private String nsKey(String... keys) {
 StringBuilder sb = new StringBuilder();
 sb.append(redisProperties.getNamespace());
 for (String key : keys) {
 sb.append(":").append(key);
 }
 return sb.toString();
 }
}