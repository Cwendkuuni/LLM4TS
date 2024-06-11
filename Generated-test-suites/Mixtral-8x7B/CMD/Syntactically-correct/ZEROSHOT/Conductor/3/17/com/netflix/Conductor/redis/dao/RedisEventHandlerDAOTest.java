import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@Before
public void init() {
MockitoAnnotations.initMocks(this);
redisEventHandlerDAO = spy(redisEventHandlerDAO);
}

@Test
public void testAddEventHandler() throws ConflictException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests(anyString());
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingHandlerJson");

redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler() throws NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingHandlerJson");

redisEventHandlerDAO.updateEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests(anyString());
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws NotFoundException {
EventHandler eventHandler = new EventHandler();
eventHandler.setName("testEventHandler");

when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingHandlerJson");

redisEventHandlerDAO.removeEventHandler("testEventHandler");

verify(jedisProxy, times(1)).hdel(anyString(), anyString());
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests(anyString());
verify(redisEventHandlerDAO, times(1)).removeIndex(any(EventHandler.class));
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException {
redisEventHandlerDAO.removeEventHandler("testEventHandler");
}

@Test
public void testGetAllEventHandlers() throws IOException {
Map<String, String> handlersJsonMap = new HashMap<>();
handlersJsonMap.put("testEventHandler", "{\"name\":\"testEventHandler\"}");

when(jedisProxy.hgetAll(anyString())).thenReturn(handlersJsonMap);

List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(1, handlers.size());
verify(jedisProxy, times(1)).hgetAll(anyString());
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests(anyString());
}

@Test
public void testGetEventHandlersForEvent() throws NotFoundException, IOException {
String event = "testEvent";

Map<String, String> handlersJsonMap = new HashMap<>();
handlersJsonMap.put("testEventHandler1", "{\"name\":\"testEventHandler1\", \"event\":\"" + event + "\"}");
handlersJsonMap.put("testEventHandler2", "{\"name\":\"testEventHandler2\", \"event\":\"" + event + "\"}");

when(jedisProxy.smembers(anyString())).thenReturn(new LinkedList<>(handlersJsonMap.keySet()));
when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingHandlerJson");

List<EventHandler> handlers =
redisEventHandlerDAO.getEventHandlersForEvent(event, false);

assertEquals(2, handlers.size());
verify(jedisProxy, times(1)).smembers(anyString());
verify(jedisProxy, times(2)).hget(anyString(), anyString());
verify(redisEventHandlerDAO, times(2)).recordRedisDaoEventRequests(anyString(), anyString());
}

@Test
public void testGetEventHandlersForEventActiveOnly() throws NotFoundException, IOException {
String event = "testEvent";

Map<String, String> handlersJsonMap = new HashMap<>();
handlersJsonMap.put("testEventHandler1", "{\"name\":\"testEventHandler1\", \"event\":\"" + event + "\", \"active\":true}");
handlersJsonMap.put("testEventHandler2", "{\"name\":\"testEventHandler2\", \"event\":\"" + event + "\", \"active\":false}");

when(jedisProxy.smembers(anyString())).thenReturn(new LinkedList<>(handlersJsonMap.keySet()));
when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingHandlerJson");

List<EventHandler> handlers =
redisEventHandlerDAO.getEventHandlersForEvent(event, true);

assertEquals(1, handlers.size());
verify(jedisProxy, times(1)).smembers(anyString());
verify(jedisProxy, times(2)).hget(anyString(), anyString());
verify(redisEventHandlerDAO, times(2)).recordRedisDaoEventRequests(anyString(), anyString());
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlersForEventNotFound() throws NotFoundException, IOException {
String event = "testEvent";

when(jedisProxy.smembers(anyString())).thenReturn(new LinkedList<>());

List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

assertEquals(0, handlers.size());
verify(jedisProxy, times(1)).smembers(anyString());
}

private JedisProxy createJedisProxy() {
Jedis jedis = mock(Jedis.class);
return new JedisProxy(jedis);
}

private String nsKey(String... keys) {
String namespace = "test-namespace";
StringBuilder sb = new StringBuilder();
sb.append(namespace);
for (String key : keys) {
sb.append(":").append(key);
}
return sb.toString();
}

private <T> T readValue(String json, Class<T> valueType) throws IOException {
return objectMapper.readValue(json, valueType);
}
}