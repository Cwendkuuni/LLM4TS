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

  private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
  private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
  private static final String EVENT = "event1";
  private static final String NAME = "name1";
  private static final String JSON = "{}";
  private static final EventHandler EVENT_HANDLER = new EventHandler();
  private static final Logger LOGGER =
      LoggerFactory.getLogger(RedisEventHandlerDAO.class);

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    EVENT_HANDLER.setName(NAME);
    EVENT_HANDLER.setEvent(EVENT);
    when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
    when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
    when(jedisProxy.hgetAll(anyString())).thenReturn(new HashMap<>());
    when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());
  }

  @Test
  public void testAddEventHandler() {
    redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    verify(jedisProxy, times(1))
        .hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
    verify(jedisProxy, times(1)).index(eq(EVENT_HANDLER));
    verify(jedisProxy, times(1))
        .sadd(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
    verify(LOGGER, times(1)).info(anyString(), anyString(), anyString(), anyString());
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandlerConflict() {
    when(redisEventHandlerDAO.getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
    redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
  }

  @Test
  public void testUpdateEventHandler() {
    when(redisEventHandlerDAO.getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
    redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    verify(jedisProxy, times(1))
        .hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
    verify(jedisProxy, times(1)).index(eq(EVENT_HANDLER));
    verify(jedisProxy, times(1))
        .srem(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
    verify(jedisProxy, times(1))
        .sadd(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
    verify(LOGGER, times(1)).info(anyString(), anyString(), anyString(), anyString());
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandlerNotFound() {
    redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
  }

  @Test
  public void testRemoveEventHandler() {
    when(redisEventHandlerDAO.getEventHandler(NAME)).thenReturn(EVENT_HANDLER);
    redisEventHandlerDAO.removeEventHandler(NAME);
    verify(jedisProxy, times(1)).hdel(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME));
    verify(jedisProxy, times(1))
        .srem(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)), eq(NAME));
    verify(LOGGER, times(1)).info(anyString(), anyString(), anyString(), anyString());
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandlerNotFound() {
    redisEventHandlerDAO.removeEventHandler(NAME);
  }

  @Test
  public void testGetAllEventHandlers() {
    List<EventHandler> eventHandlers = new LinkedList<>();
    eventHandlers.add(EVENT_HANDLER);
    when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of(NAME, JSON));
    when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
    assertEquals(eventHandlers, redisEventHandlerDAO.getAllEventHandlers());
    verify(jedisProxy, times(1)).hgetAll(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
  }

  @Test
  public void testGetEventHandlersForEvent() {
    List<EventHandler> eventHandlers = new LinkedList<>();
    eventHandlers.add(EVENT_HANDLER);
    when(jedisProxy.smembers(anyString())).thenReturn(Set.of(NAME));
    assertEquals(eventHandlers, redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true));
    verify(jedisProxy, times(1))
        .smembers(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT)));
    verify(redisEventHandlerDAO, times(1)).getEventHandler(NAME);
  }

  @Test
  public void testGetEventHandler() {
    when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
    assertEquals(EVENT_HANDLER, redisEventHandlerDAO.getEventHandler(NAME));
    verify(jedisProxy, times(1)).hget(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME));
  }

  @Test(expected = TransientException.class)
  public void testGetEventHandlerTransientException() {
    when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
    redisEventHandlerDAO.getEventHandler(NAME);
  }
}