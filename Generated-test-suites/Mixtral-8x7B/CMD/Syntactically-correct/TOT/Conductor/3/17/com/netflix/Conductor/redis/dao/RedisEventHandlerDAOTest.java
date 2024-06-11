import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
private static final String NAMESPACE = "my-namespace";
private static final String EVENT = "my-event";
private static final String EVENT_HANDLER_NAME = "my-event-handler";
private static final String JSON_EVENT_HANDLER = "{\"name\": \"my-event-handler\", \"event\": \"my-event\", \"workflow\": \"my-workflow\", \"timeoutSeconds\": 300, \"active\": true, \"retryCount\": 3, \"retryDelaySeconds\": 10}";

private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;
private EventHandlerDAO redisEventHandlerDAO;

@Before
public void setUp() {
jedisProxy = mock(JedisProxy.class);
objectMapper = new ObjectMapper();
conductorProperties = new ConductorProperties();
redisProperties = new RedisProperties();
redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
}

@Test
public void testAddEventHandler() throws IOException {
// Arrange
when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
.thenReturn(1L);

EventHandler eventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

// Act
redisEventHandlerDAO.addEventHandler(eventHandler);

// Assert
Mockito.verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), JSON_EVENT_HANDLER);
}

@Test
public void testAddEventHandler_conflictException() {
// Arrange
when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
.thenReturn("{\"name\": \"my-event-handler\", \"event\": \"my-event\", \"workflow\": \"my-workflow\", \"timeoutSeconds\": 300, \"active\": true, \"retryCount\": 3, \"retryDelaySeconds\": 10}");

EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT);

// Act & Assert
assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
}

@Test
public void testUpdateEventHandler() throws IOException {
// Arrange
when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
.thenReturn(JSON_EVENT_HANDLER);
when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
.thenReturn(1L);

EventHandler eventHandler = objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class);

// Act
redisEventHandlerDAO.updateEventHandler(eventHandler);

// Assert
Mockito.verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);
}

@Test
public void testUpdateEventHandler_notFoundException() {
// Arrange
when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
.thenReturn(null);

EventHandler eventHandler = new EventHandler();
eventHandler.setName(EVENT_HANDLER_NAME);
eventHandler.setEvent(EVENT);

// Act & Assert
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
}

@Test
public void testRemoveEventHandler() {
// Arrange
when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
.thenReturn(JSON_EVENT_HANDLER);
when(jedisProxy.hdel(Mockito.anyString(), Mockito.anyString()))
.thenReturn(1L);

// Act
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

// Assert
Mockito.verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME);
}

@Test
public void testRemoveEventHandler_notFoundException() {
// Arrange
when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
.thenReturn(null);

// Act & Assert
assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME));
}

@Test
public void testGetAllEventHandlers() throws IOException {
// Arrange
Map<String, String> all = new HashMap<>();
all.put(EVENT_HANDLER_NAME, JSON_EVENT_HANDLER);
when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)))
.thenReturn(all);

List<EventHandler> expected = new LinkedList<>();
expected.add(objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class));

// Act
List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

// Assert
assertEquals(expected, result);
}

private String nsKey(String... keys) {
return String.format("namespace:%s:%s", NAMESPACE, String.join(":", keys));
}
}