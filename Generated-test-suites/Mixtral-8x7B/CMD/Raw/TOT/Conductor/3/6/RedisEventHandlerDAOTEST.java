import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
private RedisEventHandlerDAO redisEventHandlerDAO;

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);
private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String NAMESPACE = "my_namespace";
private static final String EVENT = "my_event";
private static final String EVENT_HANDLER_NAME = "my_event_handler";
private static final String JSON = "{}";
private static final EventHandler EVENT_HANDLER = new EventHandler();
private static final List<EventHandler> EVENT_HANDLERS_LIST = new LinkedList<>();

@Before
public void setUp() {
EVENT_HANDLER.setName(EVENT_HANDLER_NAME);
EVENT_HANDLERS_LIST.add(EVENT_HANDLER);
}

@Test
public void testAddEventHandler() {
redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(jedisProxy, times(1)).index(any(EventHandler.class));
recordRedisDaoRequests("addEventHandler");
}

@Test
public void testAddEventHandlerConflictException() {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON);
assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(EVENT_HANDLER));
}

@Test
public void testUpdateEventHandler() {
EventHandler updatedEventHandler = new EventHandler();
updatedEventHandler.setName(EVENT_HANDLER_NAME);
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(JSON);
redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
verify(jedisProxy, times(1)).index(any(EventHandler.class));
verify(jedisProxy, times(1)).removeIndex(any(EventHandler.class));
recordRedisDaoRequests("updateEventHandler");
}

@Test
public void testUpdateEventHandlerNotFoundException() {
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER));
}

@Test
public void testRemoveEventHandler() {
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).removeIndex(any(EventHandler.class));
recordRedisDaoRequests("removeEventHandler");
}

@Test
public void testRemoveEventHandlerNotFoundException() {
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("non_existent_name"));
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> all = new LinkedHashMap<>();
all.put(EVENT_HANDLER_NAME, JSON);
when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);
List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(1, handlers.size());
assertEquals(EVENT_HANDLER, handlers.get(0));
recordRedisDaoRequests("getAllEventHandlers");
}

@Test
public void testGetEventHandlersForEvent() {
when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)))
.thenReturn(new HashSet<>(Arrays.asList(EVENT_HANDLER_NAME)));
when(getEventHandler(EVENT_HANDLER_NAME)).thenReturn(EVENT_HANDLER);
List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
assertEquals(1, handlers.size());
assertEquals(EVENT_HANDLER, handlers.get(0));
recordRedisDaoEventRequests("getEventHandler", EVENT);
}

@Test
public void testGetEventHandlersForEventNotFoundException() {
when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(new HashSet<>());
List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
assertEquals(0, handlers.size());
}

private String nsKey(String... keys) {
StringBuilder sb = new StringBuilder(NAMESPACE);
for (int i = 0; i < keys.length; i++) {
sb.append(':').append(keys[i]);
}
return sb.toString();
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

private void recordRedisDaoRequests(String methodName) {
LOGGER.info("Record Redis DAO request for method {}", methodName);
}

private void recordRedisDaoEventRequests(String methodName, String event) {
LOGGER.info("Record Redis DAO event request for method {} and event {}", methodName, event);
}
}