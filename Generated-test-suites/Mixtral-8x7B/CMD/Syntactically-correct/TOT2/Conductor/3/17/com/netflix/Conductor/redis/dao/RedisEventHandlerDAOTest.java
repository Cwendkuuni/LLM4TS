import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
private List<EventHandler> eventHandlers;

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

eventHandlers = new LinkedList<>();
eventHandlers.add(eventHandler1);
eventHandlers.add(eventHandler2);

ReflectionTestUtils.setField(redisEventHandlerDAO, "jedisProxy", jedisProxy);
ReflectionTestUtils.setField(redisEventHandlerDAO, "objectMapper", objectMapper);
ReflectionTestUtils.setField(redisEventHandlerDAO, "conductorProperties", conductorProperties);
ReflectionTestUtils.setField(redisEventHandlerDAO, "redisProperties", redisProperties);
}

@Test
public void testAddEventHandler() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

redisEventHandlerDAO.addEventHandler(eventHandler1);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), anyString());
verify(jedisProxy, times(1)).index(eventHandler1);
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

redisEventHandlerDAO.addEventHandler(eventHandler1);
}

@Test
public void testUpdateEventHandler() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

redisEventHandlerDAO.updateEventHandler(eventHandler1);

verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), anyString());
verify(jedisProxy, times(1)).index(eventHandler1);
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(null);

redisEventHandlerDAO.updateEventHandler(eventHandler1);
}

@Test
public void testRemoveEventHandler() {
when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler1.getName());
verify(jedisProxy, times(1)).removeIndex(eventHandler1);
verifyNoMoreInteractions(jedisProxy);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() {
when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(null);

redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
}

@Test
public void testGetAllEventHandlers() {
Map<String, String> eventHandlersMap = new LinkedHashMap<>();
eventHandlersMap.put("eh1", "{}");
eventHandlersMap.put("eh2", "{}");

when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlersMap);
when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler1, eventHandler2);

List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(eventHandlers, result);
}

@Test
public void testGetEventHandlersForEventActiveOnly() {
when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"))).thenReturn(Set.of("eh1"));
when(redisEventHandlerDAO.getEventHandler("eh1")).thenReturn(eventHandler1);

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

assertEquals(1, result.size());
assertEquals(eventHandler1, result.get(0));
}

@Test
public void testGetEventHandlersForEventInActiveOnly() {
when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event2"))).thenReturn(Set.of("eh2"));
when(redisEventHandlerDAO.getEventHandler("eh2")).thenReturn(eventHandler2);

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event2", false);

assertEquals(1, result.size());
assertEquals(eventHandler2, result.get(0));
}

@Test
public void testGetEventHandlersForEventNoMatching() {
when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event3"))).thenReturn(Set.of());

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event3", true);

assertEquals(0, result.size());
}

private String nsKey(String... keys) {
StringBuilder sb = new StringBuilder();
sb.append("conductor:redis:daos:eventHandlers:");
for (String key : keys) {
sb.append(key).append(':');
}
return sb.toString();
}
}