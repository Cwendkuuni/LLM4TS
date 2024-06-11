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
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

private static final String NAMESPACE = "test-namespace";
private static final String EVENT = "test-event";
private static final String EVENT_HANDLER_NAME = "test-event-handler";
private static final String JSON_EVENT_HANDLER =
        "{\"name\":\"test-event-handler\",\"event\":\"test-event\",\"workflow\":\"test-workflow\",\"activity\":\"test-activity\",\"priority\":5,\"active\":true,\"createdAt\":123456789,\"updatedAt\":123456789}";
private static final String JSON_EVENT_HANDLERS = "[" + JSON_EVENT_HANDLER + "]";
private static final String NS_KEY_EVENT_HANDLERS = "EVENT_HANDLERS:test-namespace";
private static final String NS_KEY_EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT:test-event";

private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;
private EventHandlerDAO redisEventHandlerDAO;

@Before
public void setUp() {
jedisProxy = mock(JedisProxy.class);
objectMapper = mock(ObjectMapper.class);
conductorProperties = mock(ConductorProperties.class);
redisProperties = mock(RedisProperties.class);

redisEventHandlerDAO = spy(
new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties));

// Initialize the mocks
when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
when(objectMapper.writeValueAsString(any(EventHandler.class)))
.thenReturn(JSON_EVENT_HANDLER);
when(objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class))
.thenReturn(createEventHandler());
when(objectMapper.readValue(JSON_EVENT_HANDLERS,
new TypeReference<List<EventHandler>>() {}))
.thenReturn(Arrays.asList(createEventHandler()));
when(jedisProxy.hget(anyString(), anyString()))
.thenReturn(JSON_EVENT_HANDLER);
when(jedisProxy.hgetAll(anyString()))
.thenReturn(Map.of(EVENT_HANDLER_NAME, JSON_EVENT_HANDLER));
when(jedisProxy.smembers(anyString()))
.thenReturn(Set.of(EVENT_HANDLER_NAME));

// Configure namespace for DAO
ReflectionTestUtils.setField(redisEventHandlerDAO, "namespace", NAMESPACE);
}

@After
public void tearDown() {
// Clear any state from the mocks
Mockito.reset(jedisProxy, objectMapper, conductorProperties, redisProperties);
}

@Test
public void testAddEventHandler() throws ConflictException, IOException {
redisEventHandlerDAO.addEventHandler(createEventHandler());

ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
verify(jedisProxy, times(1)).hset(argument.capture(), anyString(), anyString());
assertThat(argument.getValue(), equalTo(NS_KEY_EVENT_HANDLERS));

argument = ArgumentCaptor.forClass(String.class);
verify(jedisProxy, times(1)).hset(argument.capture(), anyString(), anyString());
assertThat(argument.getValue(), equalTo(NS_KEY_EVENT_HANDLERS_BY_EVENT));
}

@Test(expected = ConflictException.class)
public void testAddEventHandlerConflict() throws ConflictException, IOException {
when(jedisProxy.hget(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME)).thenReturn(JSON_EVENT_HANDLER);

redisEventHandlerDAO.addEventHandler(createEventHandler());
}

@Test
public void testUpdateEventHandler() throws NotFoundException, IOException {
redisEventHandlerDAO.updateEventHandler(createEventHandler());
redisEventHandlerDAO.updateEventHandler(createUpdatedEventHandler());

ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
verify(jedisProxy, times(2)).hset(argument.capture(), anyString(), anyString());

List<String> values = argument.getAllValues().stream().distinct().collect(Collectors.toList());
assertThat(values.size(), equalTo(1));
assertThat(values.get(0), equalTo(NS_KEY_EVENT_HANDLERS));
}

@Test(expected = NotFoundException.class)
public void testUpdateEventHandlerNotFound() throws NotFoundException, IOException {
when(jedisProxy.hget(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME)).thenReturn(null);

redisEventHandlerDAO.updateEventHandler(createEventHandler());
}

@Test
public void testRemoveEventHandler() throws NotFoundException {
redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

verify(jedisProxy, times(1)).hdel(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME);
verify(jedisProxy, times(1)).srem(NS_KEY_EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_NAME);
}

@Test(expected = NotFoundException.class)
public void testRemoveEventHandlerNotFound() throws NotFoundException {
when(jedisProxy.hget(NS_KEY_EVENT_HANDLERS, EVENT_HANDLER_NAME)).thenReturn(null);

redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
}

@Test
public void testGetAllEventHandlers() {
List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
assertThat(eventHandlers, notNullValue());
assertThat(eventHandlers.size(), equalTo(1));
}

@Test
public void testGetEventHandlersForEvent() {
List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
assertThat(eventHandlers, notNullValue());
assertThat(eventHandlers.size(), equalTo(1));
}

private EventHandler createEventHandler() {
return EventHandler.builder()
.name(EVENT_HANDLER_NAME)
.event(EVENT)
.workflow("test-workflow")
.activity("test-activity")
.priority(5)
.active(true)
.createdAt(123456789L)
.updatedAt(123456789L)
.build();
}

private EventHandler createUpdatedEventHandler() {
return EventHandler.builder()
.name(EVENT_HANDLER_NAME)
.event(EVENT + "updated")
.workflow("test-workflow")
.activity("test-activity")
.priority(5)
.active(true)
.createdAt(123456789L)
.updatedAt(123456789L)
.build();
}
}