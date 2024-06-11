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
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
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

  @Mock private RedisProperties redisProperties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
  private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
  private static final String EVENT = "event";
  private static final String NAME = "name";
  private static final String JSON = "json";
  private static final String NS_KEY = "nsKey";

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(NAME);

    when(jedisProxy.hset(NS_KEY(EVENT_HANDLERS), NAME, JSON)).thenReturn(true);

    redisEventHandlerDAO.addEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandlerConflict() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(NAME);

    when(getEventHandler(NAME)).thenReturn(eventHandler);

    redisEventHandlerDAO.addEventHandler(eventHandler);

    verify(jedisProxy, never()).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
  }

  @Test
  public void testUpdateEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(NAME);

    when(getEventHandler(NAME)).thenReturn(eventHandler);

    redisEventHandlerDAO.updateEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandlerNotFound() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(NAME);

    when(getEventHandler(NAME)).thenReturn(null);

    redisEventHandlerDAO.updateEventHandler(eventHandler);

    verify(jedisProxy, never()).hset(NS_KEY(EVENT_HANDLERS), NAME, JSON);
  }

  @Test
  public void testRemoveEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(NAME);

    when(getEventHandler(NAME)).thenReturn(eventHandler);

    redisEventHandlerDAO.removeEventHandler(NAME);

    verify(jedisProxy, times(1)).hdel(NS_KEY(EVENT_HANDLERS), NAME);
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandlerNotFound() {
    when(getEventHandler(NAME)).thenReturn(null);

    redisEventHandlerDAO.removeEventHandler(NAME);

    verify(jedisProxy, never()).hdel(NS_KEY(EVENT_HANDLERS), NAME);
  }

  @Test
  public void testGetAllEventHandlers() {
    Map<String, String> all =
        Map.of(NAME, "{\"name\":\"name\", \"event\":\"event\"}", "NAME1", "{\"name\":\"name1\"}");
    when(jedisProxy.hgetAll(NS_KEY(EVENT_HANDLERS))).thenReturn(all);

    List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

    assertEquals(all.size(), eventHandlers.size());
  }

  @Test
  public void testGetEventHandlersForEvent() {
    Set<String> names = new HashSet<>();
    names.add(NAME);

    when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT)))
        .thenReturn(names);
    when(getEventHandler(NAME)).thenReturn(new EventHandler());

    List<EventHandler> eventHandlers =
        redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

    assertEquals(names.size(), eventHandlers.size());
  }

  @Test
  public void testGetEventHandlersForEventNotFound() {
    when(jedisProxy.smembers(NS_KEY(EVENT_HANDLERS_BY_EVENT, EVENT))).thenReturn(new HashSet<>());

    List<EventHandler> eventHandlers =
        redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

    assertTrue(eventHandlers.isEmpty());
  }

  private EventHandler getEventHandler(String name) {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(name);
    eventHandler.setEvent("event");
    return eventHandler;
  }

  private String NS_KEY(String... keys) {
    StringBuilder sb = new StringBuilder(NS_KEY);
    for (String key : keys) {
      sb.append(":").append(key);
    }
    return sb.toString();
  }
}