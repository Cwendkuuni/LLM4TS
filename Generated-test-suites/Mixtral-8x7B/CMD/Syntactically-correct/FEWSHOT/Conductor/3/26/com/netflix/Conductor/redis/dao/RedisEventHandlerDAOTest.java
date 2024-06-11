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
  private Map<String, String> eventHandlersMap;
  private Set<String> eventHandlersSet;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    eventHandler1 = new EventHandler();
    eventHandler1.setName("eh1");
    eventHandler1.setEvent("event1");
    eventHandler1.setActive(true);

    eventHandler2 = new EventHandler();
    eventHandler2.setName("eh2");
    eventHandler2.setEvent("event2");
    eventHandler2.setActive(false);

    eventHandler3 = new EventHandler();
    eventHandler3.setName("eh3");
    eventHandler3.setEvent("event3");
    eventHandler3.setActive(true);

    eventHandlersMap = new HashMap<>();
    eventHandlersMap.put("eh1", "{\"name\":\"eh1\",\"event\":\"event1\",\"active\":true}");
    eventHandlersMap.put("eh2", "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}");

    eventHandlersSet = new HashSet<>();
    eventHandlersSet.add("eh1");
    eventHandlersSet.add("eh2");
  }

  @Test
  public void testAddEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L); // simulate success
    redisEventHandlerDAO.addEventHandler(eventHandler1);
    verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), "eh1", anyString());
    verify(objectMapper, times(1)).convertValue(eventHandler1, String.class);
    verify(recordRedisDaoRequests, times(1)).record("addEventHandler");
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandler_conflict() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L); // simulate success
    when(getEventHandler("eh1")).thenReturn(eventHandler1);
    redisEventHandlerDAO.addEventHandler(eventHandler1);
  }

  @Test
  public void testUpdateEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenReturn(1L); // simulate success
    when(getEventHandler("eh2")).thenReturn(eventHandler2);
    redisEventHandlerDAO.updateEventHandler(eventHandler3);
    verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), "eh2", anyString());
    verify(objectMapper, times(1)).convertValue(eventHandler3, String.class);
    verify(recordRedisDaoRequests, times(1)).record("updateEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandler_notFound() {
    when(jedisProxy
            .hset(anyString(), anyString(), anyString()))
        .thenReturn(1L); // simulate success
    when(getEventHandler("eh2")).thenReturn(null);
    redisEventHandlerDAO.updateEventHandler(eventHandler3);
  }

  @Test
  public void testRemoveEventHandler() {
    when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
    redisEventHandlerDAO.removeEventHandler("eh1");
    verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), "eh1");
    verify(recordRedisDaoRequests, times(1)).record("removeEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandler_notFound() {
    when(jedisProxy.hdel(anyString(), anyString())).thenReturn(
        0L); // simulate not found
    redisEventHandlerDAO.removeEventHandler("eh1");
  }

  @Test
  public void testGetAllEventHandlers() {
    when(jedisProxy.hgetAll(anyString()))
        .thenReturn(eventHandlersMap);
    List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
    verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
    assertEquals(2, handlers.size());
    assertEquals(eventHandler1, handlers.get(0));
    assertEquals(eventHandler2, handlers.get(1));
  }

  @Test
  public void testGetEventHandlersForEvent() {
    when(jedisProxy.smembers(anyString()))
        .thenReturn(eventHandlersSet);
    when(getEventHandler("eh1")).thenReturn(eventHandler1);
    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
    verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"));
    assertEquals(1, handlers.size());
    assertEquals(eventHandler1, handlers.get(0));
  }

  @Test
  public void testGetEventHandlersForEvent_inactive() {
    when(jedisProxy.smembers(anyString()))
        .thenReturn(eventHandlersSet);
    when(getEventHandler("eh2")).thenReturn(eventHandler2);
    List<EventHandler> handlers =
        redisEventHandlerDAO.getEventHandlersForEvent("event2", true);
    verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event2"));
    assertEquals(0, handlers.size());
  }

  @Test(expected = NotFoundException.class)
  public void testGetEventHandler_notFound() {
    when(getEventHandler("eh4")).thenReturn(null);
    redisEventHandlerDAO.getEventHandler("eh4");
  }

  private EventHandler getEventHandler(String name) {
    EventHandler eventHandler = null;
    String json = "{\"name\":\"eh2\",\"event\":\"event2\",\"active\":false}";
    if (json != null) {
      eventHandler = objectMapper.convertValue(json, EventHandler.class);
    }
    return eventHandler;
  }

  private String nsKey(String... keys) {
    StringBuilder sb = new StringBuilder();
    sb.append(conductorProperties.getNamespace());
    for (int i = 0; i < keys.length; i++) {
      sb.append(":");
      sb.append(keys[i]);
    }
    return sb.toString();
  }

  private void recordRedisDaoRequests(String operation) {
    // TODO: implement this mock method
  }

  private void recordRedisDaoEventRequests(String operation, String event) {
    // TODO: implement this mock method
  }
}