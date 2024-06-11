import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

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

    private static final String EVENT_NAME = "testEvent";
    private static final String HANDLER_NAME = "testHandler";
    private static final String JSON_HANDLER =
            "{\"name\":\"testHandler\",\"event\":\"testEvent\",\"taskDefinitionName\":\"testTask\","
            + "\"queue\":\"testQueue\",\"priority\":1,\"maxPollAttempts\":3,\"timeout\":5,\"maxPollInterval\":10,"
            + "\"headers\":{},\"status\":\"CREATED\",\"createdAt\":1623545600000,\"updatedAt\":1623545600000}";

    @Before
    public void setup() {
        initMocks(this);
        jedis = new Jedis();
        when(jedisProxy.getJedis()).thenReturn(jedis);
    }

    @After
    public void teardown() {
        jedis.flushDB();
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = objectMapper.readValue(JSON_HANDLER, EventHandler.class);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Map<String, String> all = jedis.hgetAll("EVENT_HANDLERS");
        assertEquals(1, all.size());
        assertEquals(JSON_HANDLER, all.get(HANDLER_NAME));

        verify(jedisProxy).hset("EVENT_HANDLERS", HANDLER_NAME, JSON_HANDLER);
        verify(jedisProxy).sadd("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testUpdateEventHandler()
            throws IOException, NotFoundException, ConflictException {
        EventHandler updatedEventHandler =
                new EventHandler()
                        .setName(HANDLER_NAME)
                        .setEvent(EVENT_NAME)
                        .setTaskDefinitionName("newTask")
                        .setActive(false);

        jedis.hset("EVENT_HANDLERS", HANDLER_NAME, JSON_HANDLER);
        jedis.sadd("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Map<String, String> all = jedis.hgetAll("EVENT_HANDLERS");
        assertEquals(1, all.size());
        assertEquals(
                JSON_HANDLER.replace("\"testTask\"", "\"newTask\""), all.get(HANDLER_NAME));

        verify(jedisProxy).hset("EVENT_HANDLERS", HANDLER_NAME, anyString());
        verify(jedisProxy).srem("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);
        verify(jedisProxy).sadd("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        jedis.hset("EVENT_HANDLERS", HANDLER_NAME, JSON_HANDLER);
        jedis.sadd("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);

        redisEventHandlerDAO.removeEventHandler(HANDLER_NAME);

        Map<String, String> all = jedis.hgetAll("EVENT_HANDLERS");
        assertEquals(0, all.size());

        Set<String> members = jedis.smembers("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME);
        assertEquals(0, members.size());

        verify(jedisProxy).hdel("EVENT_HANDLERS", HANDLER_NAME);
        verify(jedisProxy).srem("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<String> jsonHandlers = new LinkedList<>();
        jsonHandlers.add(JSON_HANDLER);
        jsonHandlers.add(
                "{\"name\":\"testHandler2\",\"event\":\"testEvent\",\"taskDefinitionName\":\"testTask2\","
                        + "\"queue\":\"testQueue\",\"priority\":1,\"maxPollAttempts\":3,\"timeout\":5,\"maxPollInterval\":10,"
                        + "\"headers\":{},\"status\":\"CREATED\",\"createdAt\":1623545600000,\"updatedAt\":1623545600000}");

        when(jedisProxy.hgetAll("EVENT_HANDLERS")).thenReturn(jsonHandlers.stream().collect(Collectors.toMap(
                s -> s.split(":")[1].replace("\"", ""), s -> s)));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(HANDLER_NAME, eventHandlers.get(0).getName());
        assertEquals(EVENT_NAME, eventHandlers.get(0).getEvent());

        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException {
        jedis.hset("EVENT_HANDLERS", HANDLER_NAME, JSON_HANDLER);
        jedis.sadd("EVENT_HANDLERS_BY_EVENT:" + EVENT_NAME, HANDLER_NAME);

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(HANDLER_NAME, eventHandlers.get(0).getName());
        assertEquals(EVENT_NAME, eventHandlers.get(0).getEvent());

        verifyNoMoreInteractions(jedisProxy);
    }

    private String nsKey(String... keys) {
        StringBuilder sb = new StringBuilder();
        sb.append("Conductor:EventHandlers:");
        for (String key : keys) {
            sb.append(key).append(":");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
}