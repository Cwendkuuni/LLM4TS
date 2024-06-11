import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
@Conditional(AnyRedisCondition.class)
private RedisEventHandlerDAO redisEventHandlerDAO;

private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String EVENT = "event1";
private static final String NAME = "name1";
private static final String JSON = "{}";
private static final String NS_KEY = "conductor:namespaces:test";

@Before
public void setup() {
redisEventHandlerDAO = spy(redisEventHandlerDAO);
ReflectionTestUtils.setField(redisEventHandlerDAO, "namespace", "test");
}

@Test
public void testAddEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testAddEventHandlerConflict() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

doReturn(JSON).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

try {
redisEventHandlerDAO.addEventHandler(eventHandler);
} catch (ConflictException e) {
assertEquals("EventHandler with name " + NAME + " already exists!", e.getMessage());
}

verify(jedisProxy, times(1)).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);
verify(jedisProxy, times(0)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testUpdateEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

redisEventHandlerDAO.updateEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testUpdateEventHandlerNotFound() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

doReturn(null).when(redisEventHandlerDAO).getEventHandler(NAME);

try {
redisEventHandlerDAO.updateEventHandler(eventHandler);
} catch (NotFoundException e) {
assertEquals("EventHandler with name " + NAME + " not found!", e.getMessage());
}

verify(redisEventHandlerDAO, times(1)).getEventHandler(NAME);
verify(jedisProxy, times(0)).hset(NS_KEY + ":" + EVENT_HANDLERS, NAME, JSON);
assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testRemoveEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

redisEventHandlerDAO.removeEventHandler(NAME);

verify(jedisProxy, times(1)).hdel(NS_KEY + ":" + EVENT_HANDLERS, NAME);
assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testRemoveEventHandlerNotFound() {

doReturn(null).when(redisEventHandlerDAO).getEventHandler(NAME);

try {
redisEventHandlerDAO.removeEventHandler(NAME);
} catch (NotFoundException e) {
assertEquals("EventHandler with name " + NAME + " not found!", e.getMessage());
}

verify(redisEventHandlerDAO, times(1)).getEventHandler(NAME);
verify(jedisProxy, times(0)).hdel(NS_KEY + ":" + EVENT_HANDLERS, NAME);
assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
}

@Test
public void testGetAllEventHandlers() {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("name1");

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("name2");

doReturn(JSON).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

redisEventHandlerDAO.addEventHandler(eventHandler1);
redisEventHandlerDAO.addEventHandler(eventHandler2);

List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(2, eventHandlers.size());
assertEquals(eventHandler1, eventHandlers.get(0));
assertEquals(eventHandler2, eventHandlers.get(1));
}

@Test
public void testGetEventHandlersForEvent() {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("name1");
eventHandler1.setEvent(EVENT);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("name2");
eventHandler2.setEvent(EVENT);

doReturn(JSON).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

redisEventHandlerDAO.addEventHandler(eventHandler1);
redisEventHandlerDAO.addEventHandler(eventHandler2);

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

assertEquals(2, eventHandlers.size());
assertEquals(eventHandler1, eventHandlers.get(0));
assertEquals(eventHandler2, eventHandlers.get(1));
}

@Test
public void testGetEventHandlersForEventActiveOnly() {
EventHandler eventHandler1 = new EventHandler();
eventHandler1.setName("name1");
eventHandler1.setEvent(EVENT);
eventHandler1.setActive(false);

EventHandler eventHandler2 = new EventHandler();
eventHandler2.setName("name2");
eventHandler2.setEvent(EVENT);
eventHandler2.setActive(true);

doReturn(JSON).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

redisEventHandlerDAO.addEventHandler(eventHandler1);
redisEventHandlerDAO.addEventHandler(eventHandler2);

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

assertEquals(1, eventHandlers.size());
assertEquals(eventHandler2, eventHandlers.get(0));
}

@Test
public void testGetEventHandlersForEventNotFound() {

List<EventHandler> eventHandlers =
redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

assertEquals(0, eventHandlers.size());
}

@Test
public void testGetEventHandler() {
EventHandler eventHandler = new EventHandler();
eventHandler.setName(NAME);

doReturn(JSON).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

EventHandler result = redisEventHandlerDAO.getEventHandler(NAME);

assertNotNull(result);
assertEquals(NAME, result.getName());
}

@Test
public void testGetEventHandlerNotFound() {

doReturn(null).when(jedisProxy).hget(NS_KEY + ":" + EVENT_HANDLERS, NAME);

EventHandler result = redisEventHandlerDAO.getEventHandler(NAME);

assertNotNull(result);
assertEquals(null, result.getName());
}
}