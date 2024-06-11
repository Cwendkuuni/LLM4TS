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
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

public class RedisEventHandlerDAOTest {

  @Mock private JedisProxy jedisProxy;

  @Mock private ObjectMapper objectMapper;

  @Mock private ConductorProperties conductorProperties;

  @Mock private RedisProperties redisProperties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    when(getEventHandler(eventHandler.getName())).thenReturn(null);
    redisEventHandlerDAO.addEventHandler(eventHandler);

    verify(jedisProxy, times(1))
        .hset(
            eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
            eq(eventHandler.getName()),
            anyString());
    verify(index(eventHandler), times(1));
    verify(recordRedisDaoRequests("addEventHandler"), times(1));
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandler_conflictException() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    when(getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
    redisEventHandlerDAO.addEventHandler(eventHandler);
  }

  @Test
  public void testUpdateEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    EventHandler existing = new EventHandler();
    existing.setName("testEventHandler");

    when(getEventHandler(eventHandler.getName())).thenReturn(existing);
    redisEventHandlerDAO.updateEventHandler(eventHandler);

    verify(jedisProxy, times(1))
        .hset(
            eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
            eq(eventHandler.getName()),
            anyString());
    verify(index(eventHandler), times(1));
    verify(removeIndex(existing), times(1));
    verify(recordRedisDaoRequests("updateEventHandler"), times(1));
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandler_notFoundException() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    when(getEventHandler(eventHandler.getName())).thenReturn(null);
    redisEventHandlerDAO.updateEventHandler(eventHandler);
  }

  @Test
  public void testRemoveEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    EventHandler existing = new EventHandler();
    existing.setName("testEventHandler");

    when(getEventHandler(eventHandler.getName())).thenReturn(existing);
    redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

    verify(jedisProxy, times(1)).hdel(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()));
    verify(removeIndex(existing), times(1));
    verify(recordRedisDaoRequests("removeEventHandler"), times(1));
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandler_notFoundException() {
    when(getEventHandler("testEventHandler")).thenReturn(null);
    redisEventHandlerDAO.removeEventHandler("testEventHandler");
  }

  @Test
  public void testGetAllEventHandlers() {
    Map<String, String> all =
        Map.of(
            "testEventHandler1", "{\"name\":\"testEventHandler1\",\"event\":\"testEvent\"}",
            "testEventHandler2", "{\"name\":\"testEventHandler2\",\"event\":\"testEvent\"}");

    when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(EVENT_HANDLERS))).thenReturn(all);

    List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

    assertEquals(2, handlers.size());
    verify(jedisProxy, times(1)).hgetAll(redisEventHandlerDAO.nsKey(EVENT_HANDLERS));
    verify(recordRedisDaoRequests("getAllEventHandlers"), times(1));
  }

  @Test
  public void testGetEventHandlersForEvent() {
    String event = "testEvent";

    when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event)))
        .thenReturn(new HashSet<>(List.of("testEventHandler1", "testEventHandler2")));

    when(getEventHandler("testEventHandler1"))
        .thenReturn(new EventHandler().setName("testEventHandler1").setEvent(event).setActive(true));
    when(getEventHandler("testEventHandler2"))
        .thenReturn(new EventHandler().setName("testEventHandler2").setEvent(event).setActive(false));

    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent(event, true);

    assertEquals(1, handlers.size());
    verify(jedisProxy, times(1))
        .smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event));
    handlers.forEach(
        handler -> {
          verify(getEventHandler(handler.getName()), times(1));
          verify(recordRedisDaoEventRequests("getEventHandler", event), times(1));
        });
  }

  @Test
  public void testGetEventHandlersForEvent_activeOnlyFalse() {
    String event = "testEvent";

    when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event)))
        .thenReturn(new HashSet<>(List.of("testEventHandler1", "testEventHandler2")));

    when(getEventHandler("testEventHandler1"))
        .thenReturn(new EventHandler().setName("testEventHandler1").setEvent(event).setActive(true));
    when(getEventHandler("testEventHandler2"))
        .thenReturn(new EventHandler().setName("testEventHandler2").setEvent(event).setActive(false));

    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent(event, false);

    assertEquals(2, handlers.size());
    verify(jedisProxy, times(1))
        .smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event));
    handlers.forEach(
        handler -> {
          verify(getEventHandler(handler.getName()), times(1));
          verify(recordRedisDaoEventRequests("getEventHandler", event), times(1));
        });
  }

  @Test
  public void testGetEventHandlersForEvent_notFoundException() {
    String event = "testEvent";

    when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event)))
        .thenReturn(new HashSet<>(List.of("testEventHandler1", "testEventHandler2")));

    when(getEventHandler("testEventHandler1"))
        .thenThrow(new NotFoundException("testEventHandler1"));
    when(getEventHandler("testEventHandler2"))
        .thenThrow(new NotFoundException("testEventHandler2"));

    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent(event, true);

    assertEquals(0, handlers.size());
    verify(jedisProxy, times(1))
        .smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event));
    handlers.forEach(
        handler -> {
          verify(getEventHandler(handler.getName()), times(1));
          verify(recordRedisDaoEventRequests("getEventHandler", event), times(1));
        });
  }

  private Preconditions index(EventHandler eventHandler) {
    String event = eventHandler.getEvent();
    String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event);
    redisEventHandlerDAO.jedisProxy.sadd(key, eventHandler.getName());
    return eventHandler;
  }

  private Preconditions removeIndex(EventHandler eventHandler) {
    String event = eventHandler.getEvent();
    String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event);
    redisEventHandlerDAO.jedisProxy.srem(key, eventHandler.getName());
    return eventHandler;
  }

  private EventHandler getEventHandler(String name) {
    String json;
    try {
      json = jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), name);
    } catch (Exception e) {
      throw new TransientException("Unable to get event handler named " + name, e);
    }
    if (json != null) {
      return objectMapper.readValue(json, EventHandler.class);
    }
    return null;
  }
}