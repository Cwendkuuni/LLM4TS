import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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

    private final String NAME = "testEventHandler";
    private final String EVENT = "testEvent";
    private final String JSON_EVENT_HANDLER =
            "{\"name\":\"testEventHandler\", \"event\":\"testEvent\", \"workflow\":\"testWorkflow\", \"activity\":\"testActivity\", \"priority\":1, \"maxPolls\":1, \"status\":\"CREATED\", \"createdAt\":1234567890000, \"updatedAt\":1234567890000, \"active\":true}";

    @Before
    public void setup() {
        when(redisProperties.getNamespace()).thenReturn("testNamespace");
    }

    @Test
    public void testAddEventHandler() throws IOException {
        EventHandler eventHandler =
                objectMapper.readValue(JSON_EVENT_HANDLER, new TypeReference<EventHandler>() {});

        Jedis jedis = mock(Jedis.class);
        when(jedisProxy.getJedis()).thenReturn(jedis);

        when(objectMapper.writeValueAsString(any())).thenReturn(JSON_EVENT_HANDLER);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(
                JSON_EVENT_HANDLER,
                jedis.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), NAME));
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler =
                objectMapper.readValue(JSON_EVENT_HANDLER, new TypeReference<EventHandler>() {});

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), NAME))
                .thenReturn(JSON_EVENT_HANDLER);

        assertThrows(
                ConflictException.class,
                () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() throws IOException {
        EventHandler existingEventHandler =
                objectMapper.readValue(JSON_EVENT_HANDLER, new TypeReference<EventHandler>() {});
        EventHandler updatedEventHandler =
                objectMapper.readValue(
                        "{\"name\":\"testEventHandler\", \"event\":\"testEvent2\", \"priority\":2}",
                        new TypeReference<EventHandler>() {});

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), NAME))
                .thenReturn(redisEventHandlerDAO.toJson(existingEventHandler));

        when(objectMapper.writeValueAsString(any())).thenReturn(JSON_EVENT_HANDLER);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertEquals(
                JSON_EVENT_HANDLER,
                jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), NAME));
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler =
                objectMapper.readValue(
                        "{\"name\":\"testEventHandler\", \"event\":\"testEvent2\", \"priority\":2}",
                        new TypeReference<EventHandler>() {});

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), NAME))
                .thenReturn(null);

        assertThrows(
                NotFoundException.class,
                () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        Jedis jedis = mock(Jedis.class);
        when(jedisProxy.getJedis()).thenReturn(jedis);

        redisEventHandlerDAO.removeEventHandler(NAME);

        assertEquals(0, jedis.hlen(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        Jedis jedis = mock(Jedis.class);
        when(jedisProxy.getJedis()).thenReturn(jedis);
        when(jedis.hlen(redisEventHandlerDAO.nsKey(EVENT_HANDLERS))).thenReturn(0L);

        assertThrows(
                NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(NAME));
    }

    @Test
    public void testGetAllEventHandlers() {
        List<String> jsonEventHandlers = new LinkedList<>();
        jsonEventHandlers.add(JSON_EVENT_HANDLER);

        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)))
                .thenReturn(mapFromJsonEventHandlers(jsonEventHandlers));

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(jsonEventHandlers.size(), allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 =
                objectMapper.readValue(JSON_EVENT_HANDLER, new TypeReference<EventHandler>() {});
        EventHandler eventHandler2 =
                objectMapper.readValue(
                        "{\"name\":\"testEventHandler2\", \"event\":\"testEvent\", \"priority\":1}",
                        new TypeReference<EventHandler>() {});

        List<String> jsonEventHandlers = new LinkedList<>();
        jsonEventHandlers.add(JSON_EVENT_HANDLER);
        jsonEventHandlers.add(
                "{\"name\":\"testEventHandler2\", \"event\":\"testEvent\", \"priority\":1}");

        String key = redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, EVENT);
        when(jedisProxy.smembers(key)).thenReturn(mapFromJsonEventHandlers(jsonEventHandlers));

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler"))
                .thenReturn(JSON_EVENT_HANDLER);
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler2"))
                .thenReturn(
                        "{\"name\":\"testEventHandler2\", \"event\":\"testEvent\", \"priority\":1}");

        List<EventHandler> eventHandlers =
                redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(2, eventHandlers.size());
    }

    private Map<String, String> mapFromJsonEventHandlers(List<String> jsonEventHandlers)
            throws IOException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < jsonEventHandlers.size(); i++) {
            map.put(String.valueOf(i), jsonEventHandlers.get(i));
        }
        return map;
    }
}