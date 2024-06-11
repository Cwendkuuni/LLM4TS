import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private static final String NAME = "testName";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{}";

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

    @Before
    public void setup() {
        ReflectionTestUtils.setField(redisEventHandlerDAO, "prefix", "unitTestPrefix_");
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
        verify(jedisProxy, times(1)).index(eq(eventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(redisProxy.hget(anyString(), anyString())).thenReturn(JSON);
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.index(any())).thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(NAME), eq(JSON));
        verify(jedisProxy, times(1)).index(eq(eventHandler));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);

        when(redisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String name = "testName";

        when(redisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), eq(name));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("removeEventHandler"));
        verify(jedisProxy, times(1)).removeIndex(any(EventHandler.class));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws Exception {
        String name = "testName";

        when(redisProxy.hdel(anyString(), anyString())).thenReturn(0L);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        Map<String, String> all = mock(Map.class);
        List<EventHandler> handlers = new LinkedList<>();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);
        handlers.add(eventHandler);

        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        when(all.containsKey(anyString())).thenReturn(true);
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(result, handlers);
        verify(jedisProxy, times(1)).hgetAll(eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)));
        verify(jedisProxy, times(1)).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    private void verifyNoMoreInteractions() {
        verifyNoMoreInteractions(jedisProxy);
    }

    private void verifyNoInteractions() {
        verifyNoInteractions(jedisProxy);
    }
}