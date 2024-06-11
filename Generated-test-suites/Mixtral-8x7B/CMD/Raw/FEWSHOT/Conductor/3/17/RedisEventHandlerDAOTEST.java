import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

public class RedisEventHandlerDAOTest {

  @Mock private JedisProxy jedisProxy;

  @Mock private ObjectMapper objectMapper;

  @Mock private ConductorProperties conductorProperties;

  @Mock private RedisProperties properties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  private static final String NS_KEY = "conductor:eventHandlers";

  private static final String EVENT = "event1";

  private static final String EVENT_HANDLER_NAME = "eventHandler1";

  private static final String JSON = "{}";

  private EventHandler eventHandler;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    eventHandler = new EventHandler();
    eventHandler.setName(EVENT_HANDLER_NAME);
    eventHandler.setEvent(EVENT);
    eventHandler.setActive(true);
  }

  @Test
  public void testAddEventHandler() {
    when(jedisProxy.hset(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON))
        .thenReturn(1L);

    redisEventHandlerDAO.addEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON);
    verify(jedisProxy, times(1))
        .hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
    verify(conductorProperties, times(1)).getRedisIndexTTL();
    verify(jedisProxy, times(1))
        .expire(NS_KEY + ":" + EVENT_HANDLERS + ":" + EVENT_HANDLER_NAME, anyLong());
    verify(recordRedisDaoRequests, times(1)).accept("addEventHandler");
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandlerConflict() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(JSON);

    redisEventHandlerDAO.addEventHandler(eventHandler);
  }

  @Test
  public void testUpdateEventHandler() {
    EventHandler existingEventHandler = new EventHandler();
    existingEventHandler.setName(EVENT_HANDLER_NAME);
    existingEventHandler.setEvent(EVENT);
    existingEventHandler.setActive(false);

    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(objectMapper.writeValueAsString(existingEventHandler));
    when(jedisProxy.hset(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON))
        .thenReturn(1L);

    redisEventHandlerDAO.updateEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(jedisProxy, times(1)).hset(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME, JSON);
    verify(jedisProxy, times(1))
        .hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
    verify(conductorProperties, times(1)).getRedisIndexTTL();
    verify(jedisProxy, times(1))
        .expire(NS_KEY + ":" + EVENT_HANDLERS + ":" + EVENT_HANDLER_NAME, anyLong());
    verify(recordRedisDaoRequests, times(1)).accept("updateEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandlerNotFound() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(null);

    redisEventHandlerDAO.updateEventHandler(eventHandler);
  }

  @Test
  public void testRemoveEventHandler() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(JSON);

    redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

    verify(jedisProxy, times(1)).hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(jedisProxy, times(1)).hdel(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(recordRedisDaoRequests, times(1)).accept("removeEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandlerNotFound() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(null);

    redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
  }

  @Test
  public void testGetAllEventHandlers() {
    Map<String, String> eventHandlers = new HashMap<>();
    eventHandlers.put(EVENT_HANDLER_NAME, JSON);

    when(jedisProxy.hgetAll(NS_KEY + ":" + EVENT_HANDLERS))
        .thenReturn(eventHandlers);
    when(objectMapper.readValue(JSON, EventHandler.class))
        .thenReturn(eventHandler);

    List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

    verify(jedisProxy, times(1)).hgetAll(NS_KEY + ":" + EVENT_HANDLERS);
    verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
    verify(recordRedisDaoRequests, times(1)).accept("getAllEventHandlers");
    assertEquals(1, result.size());
  }

  @Test
  public void testGetEventHandlersForEvent() {
    Set<String> eventHandlerNames = new HashSet<>();
    eventHandlerNames.add(EVENT_HANDLER_NAME);

    when(jedisProxy.smembers(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT))
        .thenReturn(eventHandlerNames);
    when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME))
        .thenReturn(eventHandler);

    List<EventHandler> result =
        redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

    verify(jedisProxy, times(1)).smembers(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT);
    verify(redisEventHandlerDAO, times(1)).getEventHandler(EVENT_HANDLER_NAME);
    verify(recordRedisDaoEventRequests, times(1)).accept("getEventHandler", EVENT);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetEventHandlersForEventNoActive() {
    Set<String> eventHandlerNames = new HashSet<>();
    eventHandlerNames.add(EVENT_HANDLER_NAME);

    when(jedisProxy.smembers(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT))
        .thenReturn(eventHandlerNames);
    when(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME))
        .thenReturn(eventHandler);

    List<EventHandler> result =
        redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

    verify(jedisProxy, times(1)).smembers(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT);
    verify(redisEventHandlerDAO, times(1)).getEventHandler(EVENT_HANDLER_NAME);
    verify(recordRedisDaoEventRequests, times(1)).accept("getEventHandler", EVENT);
    assertEquals(1, result.size());
  }

  @Test(expected = NotFoundException.class)
  public void testGetEventHandlersForEventNotFound() {
    when(jedisProxy.smembers(NS_KEY + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT))
        .thenReturn(new HashSet<>());

    redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
  }

  @Test
  public void testGetEventHandler() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(JSON);
    when(objectMapper.readValue(JSON, EventHandler.class))
        .thenReturn(eventHandler);

    EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

    verify(jedisProxy, times(1)).hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
    verify(recordRedisDaoRequests, times(1)).accept("getEventHandler");
    assertEquals(eventHandler, result);
  }

  @Test
  public void testGetEventHandlerNull() {
    when(jedisProxy.hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME))
        .thenReturn(null);

    EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);

    verify(jedisProxy, times(1)).hget(NS_KEY + ":" + EVENT_HANDLERS, EVENT_HANDLER_NAME);
    verify(recordRedisDaoRequests, times(1)).accept("getEventHandler");
    assertNull(result);
  }
}