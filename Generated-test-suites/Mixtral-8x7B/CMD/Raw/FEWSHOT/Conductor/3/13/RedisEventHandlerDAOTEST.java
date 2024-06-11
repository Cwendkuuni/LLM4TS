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
import redis.clients.jedis.Jedis;

public class RedisEventHandlerDAOTest {

  @Mock private JedisProxy jedisProxy;

  @Mock private ObjectMapper objectMapper;

  @Mock private ConductorProperties conductorProperties;

  @Mock private RedisProperties redisProperties;

  @InjectMocks private RedisEventHandlerDAO redisEventHandlerDAO;

  private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
  private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
  private static final String NAMESPACE = "conductor";
  private static final String EVENT = "testEvent";
  private static final String NAME = "testEventHandler";
  private static final String JSON = "{}";
  private static final EventHandler EVENT_HANDLER = new EventHandler();
  private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    when(redisProperties.getNamespace()).thenReturn(NAMESPACE);
    when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
    when(objectMapper.readValue(JSON, EventHandler.class)).thenReturn(EVENT_HANDLER);
  }

  @Test
  public void testAddEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");
    redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
    verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
    verify(jedisProxy, times(1)).index(EVENT_HANDLER);
    verify(jedisProxy, times(1)).recordRedisDaoRequests("addEventHandler");
  }

  @Test(expected = ConflictException.class)
  public void testAddEventHandlerConflict() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new ConflictException(""));
    redisEventHandlerDAO.addEventHandler(EVENT_HANDLER);
  }

  @Test
  public void testUpdateEventHandler() {
    when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn("OK");
    redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
    verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), NAME, JSON);
    verify(jedisProxy, times(1)).index(EVENT_HANDLER);
    verify(jedisProxy, times(1)).recordRedisDaoRequests("updateEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testUpdateEventHandlerNotFound() {
    when(jedisProxy.hset(anyString(), anyString(), anyString()))
        .thenThrow(new NotFoundException("EventHandler with name " + NAME + " not found!"));
    redisEventHandlerDAO.updateEventHandler(EVENT_HANDLER);
  }

  @Test
  public void testRemoveEventHandler() {
    when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
    redisEventHandlerDAO.removeEventHandler(NAME);
    verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), NAME);
    verify(jedisProxy, times(1)).recordRedisDaoRequests("removeEventHandler");
  }

  @Test(expected = NotFoundException.class)
  public void testRemoveEventHandlerNotFound() {
    when(jedisProxy.hdel(anyString(), anyString()))
        .thenReturn(0L);
    redisEventHandlerDAO.removeEventHandler(NAME);
  }

  @Test
  public void testGetAllEventHandlers() {
    Map<String, String> all = new HashMap<>();
    all.put(NAME, JSON);
    when(jedisProxy.hgetAll(anyString())).thenReturn(all);
    List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
    assertEquals(1, handlers.size());
    verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
    verify(jedisProxy, times(1)).recordRedisDaoRequests("getAllEventHandlers");
  }

  @Test
  public void testGetEventHandler() {
    EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(NAME);
    assertEquals(EVENT_HANDLER, eventHandler);
    verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), NAME);
    verify(objectMapper, times(1)).readValue(JSON, EventHandler.class);
  }

  @Test(expected = NotFoundException.class)
  public void testGetEventHandlerNotFound() {
    when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
    redisEventHandlerDAO.getEventHandler(NAME);
  }

  @Test
  public void testGetEventHandlersForEvent() {
    Set<String> names = new LinkedList<>();
    names.add(NAME);
    when(jedisProxy.smembers(anyString())).thenReturn(names);
    List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
    assertEquals(1, handlers.size());
    verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
    verify(jedisProxy, times(1)).getEventHandler(NAME);
  }

  @Test
  public void testGetEventHandlersForEventActiveOnly() {
    Set<String> names = new LinkedList<>();
    names.add(NAME);
    when(jedisProxy.smembers(anyString())).thenReturn(names);
    List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
    assertEquals(1, handlers.size());
    verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT));
    verify(jedisProxy, times(1)).getEventHandler(NAME);
  }

  private String nsKey(String... keys) {
    StringBuilder nsKeyBuilder = new StringBuilder();
    nsKeyBuilder.append(NAMESPACE).append(":");
    for (String key : keys) {
      nsKeyBuilder.append(key).append(":");
    }
    return nsKeyBuilder.substring(0, nsKeyBuilder.length() - 1);
  }
}