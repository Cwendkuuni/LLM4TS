import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

@InjectMocks
private RedisEventHandlerDAO redisEventHandlerDAO;

@Mock
private JedisProxy jedisProxy;

@Mock
private ObjectMapper objectMapper;

@Mock
private ConductorProperties conductorProperties;

@Mock
private RedisProperties redisProperties;

private EventHandler eventHandler1;
private EventHandler eventHandler2;
private List<EventHandler> eventHandlersList;

@Before
public void setup() {
eventHandler1 = new EventHandler();
eventHandler1.setName("eh1");
eventHandler1.setEvent("event1");
eventHandler1.setActive(true);

eventHandler2 = new EventHandler();
eventHandler2.setName("eh2");
eventHandler2.setEvent("event2");
eventHandler2.setActive(false);

eventHandlersList = new LinkedList<>();
eventHandlersList.add(eventHandler1);
eventHandlersList.add(eventHandler2);
}

@Test
public void addEventHandlerTest() {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
.thenReturn(null);

when(objectMapper.convertValue(eventHandler1, String.class))
.thenReturn("{}");

doNothing()
.when(jedisProxy)
.hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), "{}");

redisEventHandlerDAO.addEventHandler(eventHandler1);

assertEquals(1, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
}

@Test
public void addEventHandlerConflictExceptionTest() {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
.thenReturn("{}");

assertThrows(
ConflictException.class,
() -> redisEventHandlerDAO.addEventHandler(eventHandler1)
);
}

@Test
public void updateEventHandlerTest() {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
.thenReturn("{}");

when(objectMapper.convertValue(eventHandler1, String.class))
.thenReturn("{}");

doNothing()
.when(jedisProxy)
.hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), "{}");

redisEventHandlerDAO.updateEventHandler(eventHandler1);

assertEquals(1, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
}

@Test
public void updateEventHandlerNotFoundExceptionTest() {
assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.updateEventHandler(eventHandler2)
);
}

@Test
public void removeEventHandlerTest() {
when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
.thenReturn("{}");

redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

assertEquals(0, jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).size());
}

@Test
public void removeEventHandlerNotFoundExceptionTest() {
assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.removeEventHandler(eventHandler2.getName())
);
}

@Test
public void getAllEventHandlersTest() {
Map<String, String> eventHandlersMap = new LinkedHashMap<>();
eventHandlersMap.put(eventHandler1.getName(), "{}");
eventHandlersMap.put(eventHandler2.getName(), "{}");

when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
.thenReturn(eventHandlersMap);

List<EventHandler> eventHandlersList = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(2, eventHandlersList.size());
}

@Test
public void getEventHandlersForEventTest() {
String event = "event1";
String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
Set<String> names = new HashSet<>();
names.add(eventHandler1.getName());

when(jedisProxy.smembers(key))
.thenReturn(names);

when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler1.getName()))
.thenReturn("{}");

List<EventHandler> eventHandlersList = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

assertEquals(1, eventHandlersList.size());
}

@Test
public void getEventHandlersForEventNotFoundExceptionTest() {
String event = "event3";

when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event)))
.thenReturn(new HashSet<>());

List<EventHandler> eventHandlersList = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

assertEquals(0, eventHandlersList.size());
}
}