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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

private static final String EVENT_HANDLER_NAME = "testEventHandler";
private static final String EVENT = "testEvent";
private static final String JSON_EVENT_HANDLER =
        "{\"name\":\"" + EVENT_HANDLER_NAME + "\",\"event\":\"" + EVENT + "\",\"active\":true}";

@Mock private JedisProxy jedisProxy;
@Mock private ObjectMapper objectMapper;
@Mock private ConductorProperties conductorProperties;
@Mock private RedisProperties redisProperties;

@InjectMocks @Conditional(AnyRedisCondition.class) @Component private RedisEventHandlerDAO redisEventHandlerDAO;

private EventHandler eventHandler;
private EventHandler eventHandler2;
private List<EventHandler> allEventHandlers;
private Map<String, String> allEventHandlersMap;
private String nsKey;

@Before
public void setup() {
eventHandler = EventHandler.builder().name(EVENT_HANDLER_NAME).event(EVENT).active(true).build();
eventHandler2 =
EventHandler.builder().name("testEventHandler2").event("testEvent2").active(true).build();
allEventHandlers = new LinkedList<>(Arrays.asList(eventHandler, eventHandler2));
allEventHandlersMap = new HashMap<String, String>() {
{
put(eventHandler.getName(), JSON_EVENT_HANDLER);
put(eventHandler2.getName(), "{\"name\":\"testEventHandler2\",\"event\":\"testEvent2\",\"active\":true}");
}
};
redisEventHandlerDAO = spy(redisEventHandlerDAO);
nsKey = "testNsKey";
}

@Test
public void testAddEventHandler() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(null).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

redisEventHandlerDAO.addEventHandler(eventHandler);

verify(jedisProxy, times(1)).hset(nsKey(nsKey, EVENT_HANDLERS), eventHandler.getName(), JSON_EVENT_HANDLER);
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests("addEventHandler");
}

@Test
public void testAddEventHandlerConflictException() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(eventHandler).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

assertThrows(
ConflictException.class,
() -> redisEventHandlerDAO.addEventHandler(eventHandler),
"EventHandler with name " + eventHandler.getName() + " already exists!");
}

@Test
public void testUpdateEventHandler() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(eventHandler).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

redisEventHandlerDAO.updateEventHandler(eventHandler2);

verify(jedisProxy, times(1)).hset(nsKey(nsKey, EVENT_HANDLERS), eventHandler2.getName(), JSON_EVENT_HANDLER);
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests("updateEventHandler");
}

@Test
public void testUpdateEventHandlerNotFoundException() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(null).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.updateEventHandler(eventHandler2),
"EventHandler with name " + eventHandler.getName() + " not found!");
}

@Test
public void testRemoveEventHandler() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(eventHandler).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

verify(jedisProxy, times(1)).hdel(nsKey(nsKey, EVENT_HANDLERS), eventHandler.getName());
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests("removeEventHandler");
}

@Test
public void testRemoveEventHandlerNotFoundException() throws Exception {
Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

doReturn(null).when(redisEventHandlerDAO).getEventHandler(eventHandler.getName());

assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.removeEventHandler(eventHandler.getName()),
"EventHandler with name " + eventHandler.getName() + " not found!");
}

@Test
public void testGetAllEventHandlers() throws Exception {
List<EventHandler> expectedAllEventHandlers = new LinkedList<>(allEventHandlers);

doReturn(allEventHandlersMap).when(jedisProxy).hgetAll(nsKey(nsKey, EVENT_HANDLERS));

List<EventHandler> actualAllEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

assertEquals(expectedAllEventHandlers, actualAllEventHandlers);
verify(redisEventHandlerDAO, times(1)).recordRedisDaoRequests("getAllEventHandlers");
}

@Test
public void testGetEventHandler() throws Exception {
doReturn(allEventHandlersMap).when(jedisProxy).hgetAll(nsKey(nsKey, EVENT_HANDLERS));

assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
verify(redisEventHandlerDAO, times(1)).recordRedisDaoEventRequests("getEventHandler", EVENT);
}

@Test
public void testGetEventHandlerNotFoundException() throws Exception {
assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.getEventHandler("nonexistentEventHandler"),
"EventHandler with name nonexistentEventHandler not found!");
}

@Test
public void testGetEventHandlersForEvent() throws Exception {
List<EventHandler> expectedEventHandlers = new LinkedList<>(Arrays.asList(eventHandler));

String event = EVENT;
doReturn(allEventHandlersMap).when(jedisProxy).hgetAll(nsKey(nsKey, EVENT_HANDLERS));
doReturn(Arrays.asList(eventHandler.getName())).when(jedisProxy).smembers(nsKey(nsKey, EVENT_HANDLERS_BY_EVENT, event));

List<EventHandler> actualEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

assertEquals(expectedEventHandlers, actualEventHandlers);
verify(redisEventHandlerDAO, times(1)).recordRedisDaoEventRequests("getEventHandler", event);
}

@Test
public void testGetEventHandlersForEventNotFoundException() throws Exception {
String event = "nonexistentEvent";

assertThrows(
NotFoundException.class,
() -> redisEventHandlerDAO.getEventHandlersForEvent(event, true),
"No matching event handler found for event: " + event);
}
}