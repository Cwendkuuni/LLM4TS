import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

@Mock
private JedisProxy jedisProxy;

@Mock
private ObjectMapper objectMapper;

@Mock
private ConductorProperties conductorProperties;

@Mock
private RedisProperties properties;

@InjectMocks
private RedisEventHandlerDAO redisEventHandlerDAO;

private EventHandler eventHandler1;
private EventHandler eventHandler2;
private List<EventHandler> eventHandlers;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

eventHandler1 = new EventHandler();
eventHandler1.setName("eh1");
eventHandler1.setEvent("event1");
eventHandler1.setActive(true);

eventHandler2 = new EventHandler();
eventHandler2.setName("eh2");
eventHandler2.setEvent("event2");
eventHandler2.setActive(false);

eventHandlers = Arrays.asList(eventHandler1, eventHandler2);
}

@Test
public void addEventHandlerTest() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
redisEventHandlerDAO.addEventHandler(eventHandler1);
verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), anyString());
verify(jedisProxy, times(1)).index(eventHandler1);
recordRedisDaoRequests("addEventHandler");
}

@Test
public void addEventHandlerConflictExceptionTest() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
doThrow(new ConflictException("ConflictException Message")).when(redisEventHandlerDAO).getEventHandler(eventHandler1.getName());
assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler1));
}

@Test
public void updateEventHandlerTest() {
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);
redisEventHandlerDAO.updateEventHandler(eventHandler1);
verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler1.getName(), anyString());
verify(jedisProxy, times(1)).index(eventHandler1);
recordRedisDaoRequests("updateEventHandler");
}

@Test
public void updateEventHandlerNotFoundExceptionTest() {
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(null);
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler1));
}

@Test
public void removeEventHandlerTest() {
when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());
verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler1.getName());
recordRedisDaoRequests("removeEventHandler");
}

@Test
public void removeEventHandlerNotFoundExceptionTest() {
when(jedisProxy.hdel(anyString(), anyString())).thenReturn(0L);
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(eventHandler1.getName()));
}

@Test
public void getAllEventHandlersTest() {
Map<String, String> all = new LinkedHashMap<>();
all.put("eh1", toJson(eventHandler1));
all.put("eh2", toJson(eventHandler2));

when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(result.size(), 2);
assertEquals(result.get(0).getName(), eventHandler1.getName());
assertEquals(result.get(1).getName(), eventHandler2.getName());

recordRedisDaoRequests("getAllEventHandlers");
}

@Test
public void getEventHandlersForEventTest() {
String event = "event1";
String nsKey = nsKey(EVENT_HANDLERS_BY_EVENT, event);

Set<String> names = new HashSet<>(Arrays.asList(eventHandler1.getName(), eventHandler2.getName()));

when(jedisProxy.smembers(nsKey)).thenReturn(names);
when(redisEventHandlerDAO.getEventHandler(eventHandler1.getName())).thenReturn(eventHandler1);

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

assertEquals(result.size(), 1);
assertEquals(result.get(0).getName(), eventHandler1.getName());

recordRedisDaoEventRequests("getEventHandler", event);
}

@Test
public void getEventHandlersForEventNotFoundExceptionTest() {
String event = "unknown_event";

List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

assertEquals(result.size(), 0);
}

private String nsKey(String... keys) {
String namespace = "my-namespace";
return String.join(":", namespace, keys);
}

private String toJson(Object object) throws Exception {
return objectMapper.writeValueAsString(object);
}

private <T> T readValue(String json, Class<T> valueType) throws Exception {
return objectMapper.readValue(json, valueType);
}
}