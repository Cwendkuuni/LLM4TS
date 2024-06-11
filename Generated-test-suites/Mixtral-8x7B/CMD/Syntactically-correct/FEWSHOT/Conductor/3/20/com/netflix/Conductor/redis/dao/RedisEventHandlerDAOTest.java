import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

  private RedisEventHandlerDAO redisEventHandlerDAO;

  private JedisProxy jedisProxy;

  private ObjectMapper objectMapper;

  private ConductorProperties conductorProperties;

  private RedisProperties redisProperties;

  private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
  private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

  @Before
  public void setup() {
    jedisProxy = mock(JedisProxy.class);
    objectMapper = mock(ObjectMapper.class);
    conductorProperties = mock(ConductorProperties.class);
    redisProperties = mock(RedisProperties.class);

    redisEventHandlerDAO =
        new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties) {
          @Override
          protected Jedis getJedis() {
            return mock(Jedis.class);
          }
        };
  }

  // Add test cases for addEventHandler method
  @Test
  public void testAddEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(null)
        .thenThrow(new TransientException("Unable to get event handler named testEventHandler"));

    redisEventHandlerDAO.addEventHandler(eventHandler);

    Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests(Mockito.anyString());
  }

  // Add test cases for updateEventHandler method
  @Test
  public void testUpdateEventHandler() {
    EventHandler existingEventHandler = new EventHandler();
    existingEventHandler.setName("existingEventHandler");

    EventHandler newEventHandler = new EventHandler();
    newEventHandler.setName("newEventHandler");

    when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(objectMapper.convertValue(existingEventHandler, String.class))
        .thenThrow(new NotFoundException("EventHandler with name newEventHandler not found!"));

    redisEventHandlerDAO.updateEventHandler(newEventHandler);

    Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests(Mockito.anyString());
  }

  // Add test cases for removeEventHandler method
  @Test
  public void testRemoveEventHandler() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(objectMapper.convertValue(eventHandler, String.class))
        .thenThrow(new NotFoundException("EventHandler with name testEventHandler not found!"));

    redisEventHandlerDAO.removeEventHandler("testEventHandler");

    Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests(Mockito.anyString());
    Mockito.verify(jedisProxy, Mockito.times(1)).removeIndex(Mockito.any(EventHandler.class));
  }

  // Add test cases for getAllEventHandlers method
  @Test
  public void testGetAllEventHandlers() {
    EventHandler eventHandler1 = new EventHandler();
    eventHandler1.setName("eventHandler1");

    EventHandler eventHandler2 = new EventHandler();
    eventHandler2.setName("eventHandler2");

    Map<String, String> eventHandlersMap =
        Map.of("eventHandler1", objectMapper.convertValue(eventHandler1, String.class), "eventHandler2", objectMapper.convertValue(eventHandler2, String.class));

    when(jedisProxy.hgetAll(Mockito.anyString()))
        .thenReturn(eventHandlersMap);

    List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

    assertEquals(2, eventHandlers.size());
    Mockito.verify(jedisProxy, Mockito.times(1)).hgetAll(Mockito.anyString());
    Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests(Mockito.anyString());
  }

  // Add test cases for index method
  @Test
  public void testIndex() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    redisEventHandlerDAO.index(eventHandler);

    Mockito.verify(jedisProxy, Mockito.times(1)).sadd(Mockito.anyString(), Mockito.anyString());
  }

  // Add test cases for removeIndex method
  @Test
  public void testRemoveIndex() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testEventHandler");

    redisEventHandlerDAO.removeIndex(eventHandler);

    Mockito.verify(jedisProxy, Mockito.times(1)).srem(Mockito.anyString(), Mockito.anyString());
  }

  // Add test cases for getEventHandlersForEvent method
  @Test
  public void testGetEventHandlersForEvent() {
    String event = "testEvent";

    EventHandler eventHandler1 = new EventHandler();
    eventHandler1.setName("eventHandler1");
    eventHandler1.setEvent(event);
    eventHandler1.setActive(true);

    EventHandler eventHandler2 = new EventHandler();
    eventHandler2.setName("eventHandler2");
    eventHandler2.setEvent(event);
    eventHandler2.setActive(false);

    Set<String> eventHandlerNames = new HashSet<>();
    eventHandlerNames.add(eventHandler1.getName());
    eventHandlerNames.add(eventHandler2.getName());

    when(jedisProxy.smembers(Mockito.anyString()))
        .thenReturn(eventHandlerNames);

    List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

    assertEquals(1, eventHandlers.size());
    assertEquals(eventHandler1, eventHandlers.get(0));
  }

  // Add test cases for getEventHandler method
  @Test
  public void testGetEventHandler() {
    String name = "testEventHandler";

    EventHandler eventHandler = new EventHandler();
    eventHandler.setName(name);

    when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(objectMapper.convertValue(eventHandler, String.class));

    EventHandler result = redisEventHandlerDAO.getEventHandler(name);

    assertEquals(eventHandler, result);
  }
}