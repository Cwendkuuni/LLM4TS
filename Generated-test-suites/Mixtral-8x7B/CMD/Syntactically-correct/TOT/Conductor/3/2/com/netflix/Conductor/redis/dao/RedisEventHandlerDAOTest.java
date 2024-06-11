import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisProxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO =
                new RedisEventHandlerDAO(
                        jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_handler");
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L); // Return 1 as acknowledgement for successful write

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), "test_handler", anyString());
        verify(jedisProxy, times(1))
                .hset(anyString(), anyString(), anyString()); // Verify the indexing
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws IOException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_handler");
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L); // Return 0 if key already exists

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler()
            throws IOException, NotFoundException, ConflictException {
        String oldName = "old_test_handler";
        String newName = "new_test_handler";
        EventHandler existing = new EventHandler();
        existing.setName(oldName);
        when(jedisProxy.hget(anyString(), eq(oldName)))
                .thenReturn("old_json".getBytes())
                .thenReturn(null); // Simulate not found in second call
        when(objectMapper.readValue("old_json".getBytes(), EventHandler.class))
                .thenReturn(existing);
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(1L)
                .thenReturn(0L); // Return 0 if key already exists

        EventHandler updated = new EventHandler();
        updated.setName(newName);
        redisEventHandlerDAO.updateEventHandler(updated);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), oldName, "old_json");
        verify(jedisProxy, times(1))
                .hset(nsKey(EVENT_HANDLERS), newName, "old_json"); // Update with old data
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString()); // Verify the indexing
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound()
            throws IOException, NotFoundException, ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_handler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(0)).hset(nsKey(EVENT_HANDLERS), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testUpdateEventHandlerConflict()
            throws IOException, NotFoundException, ConflictException {
        String oldName = "old_test_handler";
        String newName = "new_test_handler";
        EventHandler existing = new EventHandler();
        existing.setName(oldName);
        when(jedisProxy.hget(anyString(), eq(oldName)))
                .thenReturn("old_json".getBytes());
        when(objectMapper.readValue("old_json".getBytes(), EventHandler.class))
                .thenReturn(existing);
        when(jedisProxy.hset(anyString(), anyString(), anyString()))
                .thenReturn(0L); // Return 0 if key already exists

        EventHandler updated = new EventHandler();
        updated.setName(newName);
        redisEventHandlerDAO.updateEventHandler(updated);

        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), oldName, "old_json");
    }

    @Test
    public void testRemoveEventHandler()
            throws NotFoundException, IOException, ConflictException {
        String name = "test_handler";
        EventHandler existing = new EventHandler();
        existing.setName(name);
        when(jedisProxy.hget(anyString(), eq(name)))
                .thenReturn("json".getBytes());
        when(objectMapper.readValue("json".getBytes(), EventHandler.class))
                .thenReturn(existing);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), name);
        verify(jedisProxy, times(1)).srem(anyString(), eq(name));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound()
            throws NotFoundException, IOException, ConflictException {
        String name = "test_handler";

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(0)).hdel(nsKey(EVENT_HANDLERS), anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws IOException {
        List<Map.Entry<String, String>> entries = new LinkedList<>();
        entries.add(Map.entry("test_handler_1", "{\"name\": \"test_handler_1\"}"));
        entries.add(Map.entry("test_handler_2", "{\"name\": \"test_handler_2\"}"));
        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(getMapFromEntries(entries));
        when(objectMapper.readValue(anyString(), EventHandler.class))
                .thenReturn(new EventHandler());

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, handlers.size());
    }

    private Map<String, String> getMapFromEntries(
            List<Map.Entry<String, String>> entries) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    // Add more test cases for other methods like index, removeIndex, getEventHandlersForEvent
    // ...
}