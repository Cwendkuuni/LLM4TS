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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

  @Mock private JedisProxy jedisProxy;

  @Mock private ObjectMapper objectMapper;

  @Mock private ConductorProperties conductorProperties;

  @Mock private RedisProperties redisProperties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RedisEventHandlerDAO.class);

  private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
  private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L)
        .thenThrow(new TransientException("test exception"));
    when(objectMapper.convertValue(any(), eq(String.class))).thenReturn("{}");
    when(conductorProperties.isRecordRedisDaoRequests()).thenReturn(false);

    redisEventHandlerDAO.addEventHandler(eventHandler);
    verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), anyString(), anyString());
    verify(objectMapper, times(1)).convertValue(any(), eq(String.class));
    verify(conductorProperties, times(1)).isRecordRedisDaoRequests();

    try {
      redisEventHandlerDAO.addEventHandler(eventHandler);
      fail("Should have thrown TransientException");
    } catch (TransientException e) {
      // expected
    }
  }

  @Test
  public void testUpdateEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");
    EventHandler existing = new EventHandler();
    existing.setName("testEventHandler");
    when(jedisProxy.hget(anyString(), anyString()))
        .thenReturn("{}")
        .thenReturn(null)
        .thenThrow(new TransientException("test exception"));
    when(objectMapper.convertValue(any(), eq(String.class))).thenReturn("{}");
    when(conductorProperties.isRecordRedisDaoRequests())
        .thenReturn(false)
        .thenThrow(new TransientException("test exception"));

    redisEventHandlerDAO.updateEventHandler(eventHandler);
    verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), anyString());
    verify(objectMapper, times(1)).convertValue(any(), eq(String.class));
    verify(conductorProperties, times(1)).isRecordRedisDaoRequests();

    try {
      redisEventHandlerDAO.updateEventHandler(eventHandler);
      fail("Should have thrown TransientException");
    } catch (TransientException e) {
      // expected
    }
  }

  @Test
  public void testRemoveEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");
    when(jedisProxy.hget(anyString(), anyString()))
        .thenReturn("{}")
        .thenReturn(null)
        .thenThrow(new TransientException("test exception"));
    when(conductorProperties.isRecordRedisDaoRequests())
        .thenReturn(false)
        .thenThrow(new TransientException("test exception"));

    redisEventHandlerDAO.removeEventHandler("testEventHandler");
    verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), anyString());
    verify(conductorProperties, times(1)).isRecordRedisDaoRequests();

    try {
      redisEventHandlerDAO.removeEventHandler("testEventHandler");
      fail("Should have thrown TransientException");
    } catch (TransientException e) {
      // expected
    }
  }

  @Test
  public void testGetAllEventHandlers() {
    when(jedisProxy.hgetAll(anyString()))
        .thenReturn(null)
        .thenReturn(getMockMap())
        .thenThrow(new TransientException("test exception"));
    when(conductorProperties.isRecordRedisDaoRequests())
        .thenReturn(false)
        .thenThrow(new TransientException("test exception"));

    List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
    assertEquals(0, handlers.size());

    handlers = redisEventHandlerDAO.getAllEventHandlers();
    assertEquals(2, handlers.size());

    try {
      redisEventHandlerDAO.getAllEventHandlers();
      fail("Should have thrown TransientException");
    } catch (TransientException e) {
      // expected
    }
  }

  private Map<String, String> getMockMap() {
    Map<String, String> map = mock(Map.class);
    when(map.get("key1")).thenReturn("value1");
    when(map.get("key2")).thenReturn("value2");
    return map;
  }

  private String nsKey(String... keys) {
    StringBuilder sb = new StringBuilder();
    sb.append(LOGGER.getName()).append(":");
    for (String key : keys) {
      sb.append(key).append(":");
    }
    return sb.substring(0, sb.length() - 1);
  }

  @Test
  public void testIndex() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");
    String event = "testEvent";
    String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
    when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1L);

    redisEventHandlerDAO.index(eventHandler);
    verify(jedisProxy, times(1)).sadd(key, anyString());
  }

  @Test
  public void testRemoveIndex() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");
    String event = "testEvent";
    String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
    when(jedisProxy.srem(anyString(), anyString())).thenReturn(1L);

    redisEventHandlerDAO.removeIndex(eventHandler);
    verify(jedisProxy, times(1)).srem(key, anyString());
  }

  @Test
  public void testGetEventHandlersForEvent() {
    String event = "testEvent";
    String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
    Set<String> names = new HashSet<>();
    names.add("testEventHandler1");
    names.add("testEventHandler2");
    when(jedisProxy.smembers(anyString())).thenReturn(names);
    when(getEventHandler("testEventHandler1")).thenReturn(new EventHandler());
    when(getEventHandler("testEventHandler2"))
        .thenThrow(new NotFoundException("test exception"))
        .thenReturn(new EventHandler());
    when(conductorProperties.isRecordRedisDaoEventRequests())
        .thenReturn(false)
        .thenThrow(new NotFoundException("test exception"));

    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent(event, false);
    assertEquals(2, handlers.size());

    try {
      redisEventHandlerDAO.getEventHandlersForEvent(event, false);
      fail("Should have thrown NotFoundException");
    } catch (NotFoundException e) {
      // expected
    }
  }

  private EventHandler getEventHandler(String name) {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(name);
    return eventHandler;
  }
}