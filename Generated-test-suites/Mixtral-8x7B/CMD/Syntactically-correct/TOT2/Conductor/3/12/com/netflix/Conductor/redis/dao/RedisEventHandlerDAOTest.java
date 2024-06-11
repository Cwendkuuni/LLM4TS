import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.dao.RedisDAOTestBase;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest extends RedisDAOTestBase {

	@Mock
	JedisProxy jedisProxy;

	@Mock
	ConductorProperties conductorProperties;

	@Mock
	RedisProperties redisProperties;

	@Mock
	ObjectMapper objectMapper;

	@Captor
	ArgumentCaptor<String> stringArgumentCaptor;

	@Captor
	ArgumentCaptor<Object> objectArgumentCaptor;

	@InjectMocks
	RedisEventHandlerDAO redisEventHandlerDAO;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddEventHandler() {
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName("testEventHandler");
		eventHandler.setEvent("testEvent");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
		when(objectMapper.convertValue(any(), eq(new TypeReference<EventHandler>() {}))).thenReturn(eventHandler);

		redisEventHandlerDAO.addEventHandler(eventHandler);

		verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), stringArgumentCaptor.capture(),
				objectArgumentCaptor.capture());
		assertEquals(EVENT_HANDLERS, stringArgumentCaptor.getAllValues().get(0));
		assertEquals("testEventHandler", stringArgumentCaptor.getAllValues().get(1));
		assertEquals(toJson(eventHandler), objectArgumentCaptor.getAllValues().get(2));
	}

	@Test
	public void testAddEventHandlerConflict() {
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName("testEventHandler");
		eventHandler.setEvent("testEvent");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));

		assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
		verify(jedisProxy, never()).hset(anyString(), anyString(), any());
	}

	@Test
	public void testUpdateEventHandler() {
		EventHandler existingEventHandler = new EventHandler();
		existingEventHandler.setName("existingEventHandler");
		existingEventHandler.setEvent("existingEvent");

		EventHandler updatedEventHandler = new EventHandler();
		updatedEventHandler.setName("updatedEventHandler");
		updatedEventHandler.setEvent("updatedEvent");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));
		when(objectMapper.convertValue(any(), eq(new TypeReference<EventHandler>() {}))).thenReturn(updatedEventHandler);

		redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

		verify(jedisProxy, times(1)).hset(stringArgumentCaptor.capture(), stringArgumentCaptor.capture(),
				objectArgumentCaptor.capture());
		assertEquals(EVENT_HANDLERS, stringArgumentCaptor.getAllValues().get(0));
		assertEquals("updatedEventHandler", stringArgumentCaptor.getAllValues().get(1));
		assertEquals(toJson(updatedEventHandler), objectArgumentCaptor.getAllValues().get(2));
	}

	@Test
	public void testUpdateEventHandlerNotFound() {
		EventHandler updatedEventHandler = new EventHandler();
		updatedEventHandler.setName("updatedEventHandler");
		updatedEventHandler.setEvent("updatedEvent");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

		assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
		verify(jedisProxy, never()).hset(anyString(), anyString(), any());
	}

	@Test
	public void testRemoveEventHandler() {
		EventHandler existingEventHandler = new EventHandler();
		existingEventHandler.setName("existingEventHandler");
		existingEventHandler.setEvent("existingEvent");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(existingEventHandler));

		redisEventHandlerDAO.removeEventHandler(existingEventHandler.getName());

		verify(jedisProxy, times(1)).hdel(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
		assertEquals(EVENT_HANDLERS, stringArgumentCaptor.getAllValues().get(0));
		assertEquals("existingEventHandler", stringArgumentCaptor.getAllValues().get(1));
		verify(jedisProxy, times(1)).srem(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
		assertEquals(nsKey(EVENT_HANDLERS_BY_EVENT, existingEventHandler.getEvent()),
				stringArgumentCaptor.getAllValues().get(0));
		assertEquals(existingEventHandler.getName(), stringArgumentCaptor.getAllValues().get(1));
	}

	@Test
	public void testRemoveEventHandlerNotFound() {
		when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

		assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("nonexistent"));
		verify(jedisProxy, never()).hdel(anyString(), anyString());
		verify(jedisProxy, never()).srem(anyString(), anyString());
	}

	@Test
	public void testGetAllEventHandlers() {
		EventHandler eventHandler1 = new EventHandler();
		eventHandler1.setName("eventHandler1");
		eventHandler1.setEvent("event1");

		EventHandler eventHandler2 = new EventHandler();
		eventHandler2.setName("eventHandler2");
		eventHandler2.setEvent("event2");

		when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("eventHandler1", toJson(eventHandler1),
				"eventHandler2", toJson(eventHandler2)));

		List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

		assertEquals(2, eventHandlers.size());
		assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
		assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());
	}

	@Test
	public void testGetEventHandlerForEvent() {
		EventHandler eventHandler1 = new EventHandler();
		eventHandler1.setName("eventHandler1");
		eventHandler1.setEvent("event1");
		eventHandler1.setActive(true);

		EventHandler eventHandler2 = new EventHandler();
		eventHandler2.setName("eventHandler2");
		eventHandler2.setEvent("event1");
		eventHandler2.setActive(false);

		when(jedisProxy.smembers(anyString())).thenReturn(Set.of("eventHandler1", "eventHandler2"));
		when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler1), toJson(eventHandler2));

		List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(),
				false);

		assertEquals(2, eventHandlers.size());
		assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
		assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());

		eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler1.getEvent(), true);

		assertEquals(1, eventHandlers.size());
		assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
	}

	@Test
	public void testGetEventHandlerForEventNotFound() {
		when(jedisProxy.smembers(anyString())).thenReturn(Set.of());

		List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

		assertEquals(0, eventHandlers.size());
	}

	@Test
	public void testGetEventHandlerForEventTransientException() {
		String key = nsKey(EVENT_HANDLERS_BY_EVENT, "event1");
		doThrow(new TransientException("Test exception")).when(jedisProxy).smembers(key);

		assertThrows(TransientException.class,
				() -> redisEventHandlerDAO.getEventHandlersForEvent("event1", false));
	}

	@Test
	public void testGetEventHandler() {
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName("eventHandler");
		eventHandler.setEvent("event");

		when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));

		EventHandler result = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

		assertNotNull(result);
		assertEquals(eventHandler.getName(), result.getName());
		assertEquals(eventHandler.getEvent(), result.getEvent());
	}

	@Test
	public void testGetEventHandlerNotFound() {
		when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

		assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler("nonexistent"));
	}

	@Test
	public void testGetEventHandlerTransientException() {
		String key = nsKey(EVENT_HANDLERS, "eventHandler");
		doThrow(new TransientException("Test exception")).when(jedisProxy).hget(key, "eventHandler");

		assertThrows(TransientException.class, () -> redisEventHandlerDAO.getEventHandler("eventHandler"));
	}
}