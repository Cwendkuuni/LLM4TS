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
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

public class RedisEventHandlerDAOTest {

  @Mock private JedisProxy jedisProxy;

  @Mock private ObjectMapper objectMapper;

  @Mock private ConductorProperties conductorProperties;

  @Mock private RedisProperties redisProperties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  private EventHandler eventHandler1;
  private EventHandler eventHandler2;
  private EventHandler eventHandler3;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    eventHandler1 =
        EventHandler.builder()
            .name("eh1")
            .event("event1")
            .taskDefinition("task1")
            .build();
    eventHandler2 =
        EventHandler.builder()
            .name("eh2")
            .event("event2")
            .taskDefinition("task2")
            .build();
    eventHandler3 =
        EventHandler.builder()
            .name("eh3")
            .event("event3")
            .taskDefinition("task3")
            .build();
  }

  // Add more test methods for other methods in the RedisEventHandlerDAO class

  @Test
  public void testAddEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L)
        .thenReturn(2L)
        .thenReturn(3L);

    redisEventHandlerDAO.addEventHandler(eventHandler1);
    redisEventHandlerDAO.addEventHandler(eventHandler2);
    redisEventHandlerDAO.addEventHandler(eventHandler3);

    verify(jedisProxy, times(3)).hset(anyString(), anyString(), anyString());
    verify(objectMapper, times(3)).convertValue(any(), eq(EventHandler.class));
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandlerConflict() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(0L);

    redisEventHandlerDAO.addEventHandler(eventHandler1);
  }

  @Test(expected = TransientException.class)
  public void testAddEventHandlerTransientException() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new RuntimeException());

    redisEventHandlerDAO.addEventHandler(eventHandler1);
  }

  @Test
  public void testUpdateEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L)
        .thenReturn(2L)
        .thenReturn(3L);

    redisEventHandlerDAO.updateEventHandler(eventHandler1);
    redisEventHandlerDAO.updateEventHandler(eventHandler2);
    redisEventHandlerDAO.updateEventHandler(eventHandler3);

    verify(jedisProxy, times(3)).hset(anyString(), anyString(), anyString());
    verify(objectMapper, times(3)).convertValue(any(), eq(EventHandler.class));
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandlerNotFound() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(0L);

    redisEventHandlerDAO.updateEventHandler(eventHandler1);
  }

  @Test(expected = TransientException.class)
  public void testUpdateEventHandlerTransientException() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new RuntimeException());

    redisEventHandlerDAO.updateEventHandler(eventHandler1);
  }

  @Test
  public void testRemoveEventHandler() {
    when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L).thenReturn(2L).thenReturn(3L);

    redisEventHandlerDAO.removeEventHandler("eh1");
    redisEventHandlerDAO.removeEventHandler("eh2");
    redisEventHandlerDAO.removeEventHandler("eh3");

    verify(jedisProxy, times(3)).hdel(anyString(), anyString());
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandlerNotFound() {
    when(jedisProxy.hdel(anyString(), anyString())).thenReturn(0L);

    redisEventHandlerDAO.removeEventHandler("eh1");
  }

  @Test
  public void testGetAllEventHandlers() {
    Map<String, String> eventHandlersJson = new HashMap<>();
    eventHandlersJson.put("eh1", "{}");
    eventHandlersJson.put("eh2", "{}");
    eventHandlersJson.put("eh3", "{}");

    when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersJson);
    when(objectMapper.convertValue(eq("{}"), eq(EventHandler.class))).thenReturn(eventHandler1);

    List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

    assertEquals(3, eventHandlers.size());
    verify(jedisProxy, times(1)).hgetAll(anyString());
    verify(objectMapper, times(3)).convertValue(eq("{}"), eq(EventHandler.class));
  }

  @Test
  public void testGetEventHandlersForEvent() {
    String key = "EVENT_HANDLERS_BY_EVENT:event1";
    Set<String> eventHandlersNames = new HashSet<>();
    eventHandlersNames.add("eh1");
    eventHandlersNames.add("eh2");

    when(jedisProxy.smembers(eq(key))).thenReturn(eventHandlersNames);
    when(redisEventHandlerDAO.getEventHandler("eh1")).thenReturn(eventHandler1);
    when(redisEventHandlerDAO.getEventHandler("eh2")).thenThrow(new NotFoundException(""));

    List<EventHandler> eventHandlers =
        redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

    assertEquals(1, eventHandlers.size());
    verify(jedisProxy, times(1)).smembers(eq(key));
    verify(redisEventHandlerDAO, times(2)).getEventHandler(anyString());
  }
}