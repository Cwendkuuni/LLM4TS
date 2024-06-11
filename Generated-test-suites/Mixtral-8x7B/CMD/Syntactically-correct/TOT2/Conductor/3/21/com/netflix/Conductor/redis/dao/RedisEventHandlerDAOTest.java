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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
 private String namespace;

 @Before
 public void setup() {
 jedis = new Jedis();
 namespace = "test_namespace";
 redisEventHandlerDAO.setJedisProxy(jedisProxy);
 redisEventHandlerDAO.setObjectMapper(objectMapper);
 redisEventHandlerDAO.setConductorProperties(conductorProperties);
 redisEventHandlerDAO.setRedisProperties(redisProperties);
 redisEventHandlerDAO.setNamespace(namespace);
 }

 @After
 public void teardown() {
 jedis.flushDB();
 }

 @Test
 public void testAddEventHandler() throws ConflictException, IOException {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("test_handler");
 eventHandler.setEvent("test_event");
 eventHandler.setActive(true);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler.getName(), objectMapper.writeValueAsString(eventHandler));

 redisEventHandlerDAO.addEventHandler(eventHandler);

 Map<String, String> all = jedis.hgetAll(namespace + ":EVENT_HANDLERS");
 Assert.assertEquals(1, all.size());
 EventHandler retrievedEventHandler = objectMapper.readValue(all.get(eventHandler.getName()), EventHandler.class);
 Assert.assertEquals(eventHandler, retrievedEventHandler);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() throws ConflictException, IOException {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("test_handler");
 eventHandler1.setEvent("test_event");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("test_handler");
 eventHandler2.setEvent("test_event_2");
 eventHandler2.setActive(true);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));

 redisEventHandlerDAO.addEventHandler(eventHandler2);
 }

 @Test
 public void testUpdateEventHandler() throws ConflictException, IOException, NotFoundException {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("test_handler");
 eventHandler1.setEvent("test_event");
 eventHandler1.setActive(true);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("test_handler");
 eventHandler2.setEvent("test_event_2");
 eventHandler2.setActive(false);

 redisEventHandlerDAO.updateEventHandler(eventHandler2);

 Map<String, String> all = jedis.hgetAll(namespace + ":EVENT_HANDLERS");
 Assert.assertEquals(1, all.size());
 EventHandler retrievedEventHandler = objectMapper.readValue(all.get(eventHandler2.getName()), EventHandler.class);
 Assert.assertEquals(eventHandler2, retrievedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() throws ConflictException, IOException, NotFoundException {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("test_handler");
 eventHandler1.setEvent("test_event");
 eventHandler1.setActive(true);

 redisEventHandlerDAO.updateEventHandler(eventHandler1);
 }

 @Test
 public void testRemoveEventHandler() throws NotFoundException, IOException {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("test_handler");
 eventHandler.setEvent("test_event");
 eventHandler.setActive(true);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler.getName(), objectMapper.writeValueAsString(eventHandler));

 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

 Map<String, String> all = jedis.hgetAll(namespace + ":EVENT_HANDLERS");
 Assert.assertEquals(0, all.size());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() throws NotFoundException, IOException {
 redisEventHandlerDAO.removeEventHandler("non_existing_handler");
 }

 @Test
 public void testGetAllEventHandlers() throws IOException {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("test_handler_1");
 eventHandler1.setEvent("test_event_1");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("test_handler_2");
 eventHandler2.setEvent("test_event_2");
 eventHandler2.setActive(false);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));
 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler2.getName(), objectMapper.writeValueAsString(eventHandler2));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 Assert.assertEquals(2, eventHandlers.size());
 Assert.assertTrue(eventHandlers.contains(eventHandler1));
 Assert.assertTrue(eventHandlers.contains(eventHandler2));
 }

 @Test
 public void testGetEventHandlersForEvent() throws IOException, NotFoundException {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("test_handler_1");
 eventHandler1.setEvent("test_event");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("test_handler_2");
 eventHandler2.setEvent("test_event");
 eventHandler2.setActive(false);

 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler1.getName(), objectMapper.writeValueAsString(eventHandler1));
 jedis.hset(namespace + ":EVENT_HANDLERS", eventHandler2.getName(), objectMapper.writeValueAsString(eventHandler2));

 Set<String> handlerNames = new HashSet<>();
 handlerNames.add(eventHandler1.getName());
 handlerNames.add(eventHandler2.getName());

 when(jedisProxy.smembers(namespace + ":EVENT_HANDLERS_BY_EVENT:test_event")).thenReturn(handlerNames);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event", true);
 Assert.assertEquals(1, eventHandlers.size());
 Assert.assertTrue(eventHandlers.contains(eventHandler1));

 List<EventHandler> eventHandlersInactive = redisEventHandlerDAO.getEventHandlersForEvent("test_event", false);
 Assert.assertEquals(2, eventHandlersInactive.size());
 Assert.assertTrue(eventHandlersInactive.contains(eventHandler1));
 Assert.assertTrue(eventHandlersInactive.contains(eventHandler2));
 }

 @Test
 public void testGetEventHandlersForEventNoMatching() throws IOException, NotFoundException {
 when(jedisProxy.smembers(namespace + ":EVENT_HANDLERS_BY_EVENT:test_event")).thenReturn(new HashSet<>());

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event", true);
 Assert.assertEquals(0, eventHandlers.size());
 }
}