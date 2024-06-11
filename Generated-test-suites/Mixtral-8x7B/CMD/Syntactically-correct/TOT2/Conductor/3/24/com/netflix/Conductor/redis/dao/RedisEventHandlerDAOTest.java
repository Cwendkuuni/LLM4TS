import com.fasterxml.jackson.core.type.TypeReference;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
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
 @Component
 @Conditional(AnyRedisCondition.class)
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private static final String NAME = "test-name";
 private static final String EVENT = "test-event";
 private static final String JSON = "{}";
 private static final String ANOTHER_EVENT = "another-event";

 @Before
 public void setUp() {
 Mockito.reset(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @After
 public void tearDown() {
 redisEventHandlerDAO = null;
 }

 @Test
 public void testAddEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(EVENT);

 doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn(JSON);
 when(conductorProperties.getRedisDaoRequestsSampleRate()).thenReturn(0);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(EVENT);

 when(getEventHandler(NAME)).thenReturn(eventHandler);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(NAME);
 existingEventHandler.setEvent(EVENT);

 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(ANOTHER_EVENT);

 when(getEventHandler(NAME)).thenReturn(existingEventHandler);
 doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn(JSON);
 when(conductorProperties.getRedisDaoRequestsSampleRate()).thenReturn(0);

 redisEventHandlerDAO.updateEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(ANOTHER_EVENT);

 when(getEventHandler(NAME)).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(eventHandler);
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(EVENT);

 when(getEventHandler(NAME)).thenReturn(eventHandler);
 doNothing().when(jedisProxy).hdel(anyString(), anyString());

 redisEventHandlerDAO.removeEventHandler(NAME);

 verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), NAME);
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() throws Exception {
 when(getEventHandler(NAME)).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler(NAME);
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 Map<String, String> map = new HashMap<>();
 map.put(NAME, JSON);

 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(map);
 when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(new EventHandler());

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() throws Exception {
 String key = nsKey(EVENT_HANDLERS_BY_EVENT, EVENT);
 Set<String> names = new LinkedList<>();
 names.add(NAME);

 when(jedisProxy.smembers(key)).thenReturn(names);
 when(getEventHandler(NAME)).thenReturn(new EventHandler());

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() throws Exception {
 String key = nsKey(EVENT_HANDLERS_BY_EVENT, EVENT);
 Set<String> names = new LinkedList<>();
 names.add(NAME);

 when(jedisProxy.smembers(key)).thenReturn(names);
 when(getEventHandler(NAME)).thenReturn(new EventHandler());

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

 assertEquals(0, eventHandlers.size());
 }

 private EventHandler getEventHandler(String name) throws NotFoundException, TransientException {
 Preconditions.checkNotNull(name, "Missing Name");
 String json;
 try {
 json = jedisProxy.hget(nsKey(EVENT_HANDLERS), name);
 } catch (Exception e) {
 throw new TransientException("Unable to get event handler named " + name, e);
 }
 if (json == null) {
 throw new NotFoundException("EventHandler with name " + name + " not found!");
 }
 return objectMapper.readValue(json, EventHandler.class);
 }

 private String nsKey(String... keys) {
 StringBuilder sb = new StringBuilder();
 sb.append(redisProperties.getNamespace()).append(":");
 for (String key : keys) {
 sb.append(key).append(":");
 }
 return sb.substring(0, sb.length() - 1);
 }
}