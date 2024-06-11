import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private Jedis jedis;

    @Before
    public void setUp() {
        jedis = mock(Jedis.class);
        ReflectionTestUtils.setField(jedisProxy, "jedis", jedis);
        when(jedisProxy.hget(anyString(), anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            String field = invocation.getArgument(1);
            if (key.equals("EVENT_HANDLERS") && field.equals("eventHandler1")) {
                return "{\"name\":\"eventHandler1\",\"event\":\"testEvent\",\"resourceId\":\"testResourceId\"," +
                        "\"queue\":\"testQueue\",\"parallel\":false,\"maxPollCount\":0,\"active\":true}";
            }
            return null;
        });
    }

    @After
    public void tearDown() {
        jedis.close();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException, TransientException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        eventHandler.setEvent("testEvent");
        eventHandler.setResourceId("testResourceId");
        eventHandler.setQueue("testQueue");
        eventHandler.setParallel(false);
        eventHandler.setMaxPollCount(0);
        eventHandler.setActive(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(eventHandler);

        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler);
        when(jedis.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedis, Mockito.times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() throws IOException, NotFoundException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        eventHandler.setEvent("testEvent");
        eventHandler.setResourceId("testResourceId");
        eventHandler.setQueue("testQueue");
        eventHandler.setParallel(false);
        eventHandler.setMaxPollCount(0);
        eventHandler.setActive(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(eventHandler);

        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(eventHandler);
        when(jedis.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedis, Mockito.times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        when(jedis.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("eventHandler1");

        Mockito.verify(jedis, Mockito.times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        Map<String, String> eventHandlers = new LinkedList<Map.Entry<String, String>>() {{
            add(new LinkedList.SimpleEntry<>("eventHandler1",
                    "{\"name\":\"eventHandler1\",\"event\":\"testEvent\",\"resourceId\":\"testResourceId\"," +
                            "\"queue\":\"testQueue\",\"parallel\":false,\"maxPollCount\":0,\"active\":true}"));
        }}.stream().distinct().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        when(jedis.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> eventHandlerList = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertEquals(eventHandlerList.size(), 1);

        Mockito.verify(jedis, Mockito.times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException {
        String event = "testEvent";
        Set<String> handlerNames = new HashSet<String>() {{
            add("eventHandler1");
        }};

        when(jedis.smembers(anyString())).thenReturn(handlerNames);
        when(jedis.hget(anyString(), anyString())).thenReturn("{\"name\":\"eventHandler1\",\"event\":\"testEvent\"," +
                "\"resourceId\":\"testResourceId\",\"queue\":\"testQueue\",\"parallel\":false,\"maxPollCount\":0," +
                "\"active\":true}");

        List<EventHandler> eventHandlerList = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        Assert.assertEquals(eventHandlerList.size(), 1);

        Mockito.verify(jedis, Mockito.times(1)).smembers(anyString());
        Mockito.verify(jedis, Mockito.times(1)).hget(anyString(), anyString());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() throws NotFoundException {
        String event = "testEvent";

        when(jedis.smembers(anyString())).thenReturn(new HashSet<String>() {{
            add("eventHandler1");
        }});

        when(jedis.hget(anyString(), anyString())).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        Mockito.verify(jedis, Mockito.times(1)).smembers(anyString());
        Mockito.verify(jedis, Mockito.times(1)).hget(anyString(), anyString());
    }

    @Test
    public void testGetEventHandler() throws NotFoundException {
        String eventHandlerName = "eventHandler1";

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);
        eventHandler.setEvent("testEvent");
        eventHandler.setResourceId("testResourceId");
        eventHandler.setQueue("testQueue");
        eventHandler.setParallel(false);
        eventHandler.setMaxPollCount(0);
        eventHandler.setActive(true);

        when(objectMapper.readValue("{\"name\":\"eventHandler1\"," +
                "\"event\":\"testEvent\",\"resourceId\":\"testResourceId\"," +
                "\"queue\":\"testQueue\",\"parallel\":false,\"maxPollCount\":0,\"active\":true}",
                new TypeReference<EventHandler>() {}))
                .thenReturn(eventHandler);

        EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandlerName);

        Assert.assertEquals(returnedEventHandler, eventHandler);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(anyString(), any(TypeReference.class));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() throws NotFoundException {
        String eventHandlerName = "eventHandler1";

        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler(eventHandlerName);

        Mockito.verify(objectMapper, Mockito.times(1))
                .readValue(anyString(), any(TypeReference.class));
    }
}