import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

private static final String EVENT_NAME = "testEventHandler";
private static final String EVENT = "testEvent";
private static final String JSON = "{}";
private static final String NS_KEY = "testNsKey";
private static final String NS_KEY_EVENT_HANDLERS = NS_KEY + ":" + RedisEventHandlerDAO.EVENT_HANDLERS;
private static final String NS_KEY_EVENT_HANDLERS_BY_EVENT = NS_KEY + ":" + RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT;
private static final ObjectMapper OBJECT_MAPPER = mock(ObjectMapper.class);
private static final ConductorProperties CONDUCTOR_PROPERTIES = mock(ConductorProperties.class);
private static final RedisProperties REDIS_PROPERTIES = mock(RedisProperties.class);
private static final JedisProxy JEDIS_PROXY = mock(JedisProxy.class);
private RedisEventHandlerDAO redisEventHandlerDAO;

@Mock
private EventHandler eventHandler;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
redisEventHandlerDAO =
spy(
new RedisEventHandlerDAO(
JEDIS_PROXY, OBJECT_MAPPER, CONDUCTOR_PROPERTIES, REDIS_PROPERTIES) {
@Override
protected String nsKey(String... keys) {
return NS_KEY;
}
});
}

@Test
public void testAddEventHandler() throws ConflictException, TransientException {
redisEventHandlerDAO.addEventHandler(eventHandler);
verify(JEDIS_PROXY, times(1)).hset(NS_KEY_EVENT_HANDLERS, EVENT_NAME, JSON);
verify(JEDIS_PROXY, times(1)).index(eventHandler);
assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(EVENT_NAME));
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, TransientException {
when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);
redisEventHandlerDAO.addEventHandler(eventHandler);
}

@Test
public void testUpdateEventHandler()
throws ConflictException, NotFoundException, TransientException {
when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);
redisEventHandlerDAO.updateEventHandler(eventHandler);
verify(JEDIS_PROXY, times(1)).hset(NS_KEY_EVENT_HANDLERS, EVENT_NAME, JSON);
verify(JEDIS_PROXY, times(1)).index(eventHandler);
assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(EVENT_NAME));
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws ConflictException, NotFoundException, TransientException {
redisEventHandlerDAO.updateEventHandler(eventHandler);
}

@Test
public void testRemoveEventHandler() throws NotFoundException, TransientException {
redisEventHandlerDAO.addEventHandler(eventHandler);
redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
verify(JEDIS_PROXY, times(1)).hdel(NS_KEY_EVENT_HANDLERS, EVENT_NAME);
verify(JEDIS_PROXY, times(1)).removeIndex(eventHandler);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException, TransientException {
redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> map = new LinkedHashMap<>();
map.put(EVENT_NAME, JSON);
when(JEDIS_PROXY.hgetAll(NS_KEY_EVENT_HANDLERS)).thenReturn(map);
List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertEquals(1, eventHandlers.size());
assertEquals(eventHandler, eventHandlers.get(0));
}

@Test
public void testGetEventHandlersForEvent() throws NotFoundException {
Set<String> names = new HashSet<>();
names.add(EVENT_NAME);
when(JEDIS_PROXY.smembers(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT)).thenReturn(names);
when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);
List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
assertEquals(1, eventHandlers.size());
assertEquals(eventHandler, eventHandlers.get(0));
}

@Test
public void testGetEventHandlersForEventActiveOnlyFalse() throws NotFoundException {
Set<String> names = new HashSet<>();
names.add(EVENT_NAME);
when(JEDIS_PROXY.smembers(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT)).thenReturn(names);
when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);
List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
assertEquals(1, eventHandlers.size());
assertEquals(eventHandler, eventHandlers.get(0));
}

@Test
public void testGetEventHandlersForEventActiveOnlyTrue() throws NotFoundException {
EventHandler eventHandler2 = mock(EventHandler.class);
String eventHandler2Name = "testEventHandler2";
String eventHandler2Json = "{}";
when(eventHandler2.isActive()).thenReturn(false);
when(JEDIS_PROXY.hget(NS_KEY_EVENT_HANDLERS, eventHandler2Name)).thenReturn(eventHandler2Json);
when(OBJECT_MAPPER.readValue(eventHandler2Json, EventHandler.class)).thenReturn(eventHandler2);
Set<String> names = new HashSet<>();
names.add(eventHandler2Name);
names.add(EVENT_NAME);
when(JEDIS_PROXY.smembers(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT)).thenReturn(names);
when(redisEventHandlerDAO.getEventHandler(EVENT_NAME)).thenReturn(eventHandler);
List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
assertEquals(1, eventHandlers.size());
assertEquals(eventHandler, eventHandlers.get(0));
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlersForEventNotFound() throws NotFoundException {
Set<String> names = new HashSet<>();
when(JEDIS_PROXY.smembers(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT)).thenReturn(names);
List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
}

@Test
public void testGetEventHandler() throws NotFoundException {
when(JEDIS_PROXY.hget(NS_KEY_EVENT_HANDLERS, EVENT_NAME)).thenReturn(JSON);
when(OBJECT_MAPPER.readValue(JSON, EventHandler.class)).thenReturn(eventHandler);
assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(EVENT_NAME));
}

@Test(expected = NotFoundException.class)
public void testGetEventHandlerNotFound() throws NotFoundException {
when(JEDIS_PROXY.hget(NS_KEY_EVENT_HANDLERS, EVENT_NAME)).thenReturn(null);
redisEventHandlerDAO.getEventHandler(EVENT_NAME);
}

@Test
public void testIndex() {
redisEventHandlerDAO.index(eventHandler);
verify(JEDIS_PROXY, times(1)).sadd(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT, EVENT_NAME);
}

@Test
public void testRemoveIndex() {
redisEventHandlerDAO.removeIndex(eventHandler);
verify(JEDIS_PROXY, times(1)).srem(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":" + EVENT, EVENT_NAME);
}

@Test
public void testRecordRedisDaoRequests() {
redisEventHandlerDAO.recordRedisDaoRequests("test");
verify(CONDUCTOR_PROPERTIES, times(1))
.incrementCounter(RedisEventHandlerDAO.class.getSimpleName() + ".redisDaoRequests");
}

@Test
public void testRecordRedisDaoEventRequests() {
redisEventHandlerDAO.recordRedisDaoEventRequests("test", EVENT);
verify(CONDUCTOR_PROPERTIES, times(1))
.incrementCounter(
RedisEventHandlerDAO.class.getSimpleName()
+ ".redisDaoEventRequests."
+ EVENT);
}

@Test
public void testHandleException() {
doThrow(new TransientException("test")).when(JEDIS_PROXY).hset(anyString(), anyString(), anyString());
redisEventHandlerDAO.addEventHandler(eventHandler);
verify(CONDUCTOR_PROPERTIES, times(1))
.incrementCounter(RedisEventHandlerDAO.class.getSimpleName() + ".transientExceptions");
}
}