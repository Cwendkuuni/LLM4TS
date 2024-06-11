package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLHandledException;

@SuppressWarnings("unchecked")
public class SingleConnectionDataSourceTest {

	private SingleConnectionDataSource dataSource;
	private Connection connection;
	private EventPublisher eventPublisher;

	@Before
	public void setup() throws SQLException {
		connection = mock(Connection.class);
		eventPublisher = mock(EventPublisher.class);

		dataSource = new SingleConnectionDataSource(true);
		dataSource.setConnection(connection);
		dataSource.setEventPublisher(eventPublisher);
	}

	@Test
	public void testGetConnection() throws SQLException {
		Connection result = dataSource.getConnection();
		assertSame(connection, result);
	}

	@Test
	public void testGetConnectionWithWrongCredentials() {
		assertThrows(SQLHandledException.class, () -> dataSource.getConnection("wrongUsername", "wrongPassword"));
	}

	@Test
	public void testGetConnectionWithLock() throws SQLException, InterruptedException {
		dataSource.setTimeOutMillis(100);
		Connection result = dataSource.getConnection();
		assertSame(connection, result);
	}

	@Test
	public void testGetConnectionWithLockAndTimeout() throws SQLException, InterruptedException {
		dataSource.setTimeOutMillis(10);
		assertThrows(ConflictException.class, () -> dataSource.getConnection());
	}

	@Test
	public void testResetConnection() throws SQLException {
		dataSource.resetConnection();
		verify(connection).close();
		assertNull(dataSource.getConnection());
	}

	@Test
	public void testAddInitializer() {
		ConnectionInitializer initializer = mock(ConnectionInitializer.class);
		dataSource.addInitializer(initializer);
		verify(connection).setAutoCommit(true);
		verify(connection).setReadOnly(false);
		verify(connection).setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		verify(initializer).init(connection);
	}

	@Test
	public void testClose() throws SQLException {
		dataSource.close();
		verify(connection).close();
	}

	@Test
	public void testInnerCreateConnection() throws SQLException {
		Connection result = dataSource.innerCreateConnection();
		assertNotNull(result);
	}

	@Test
	public void testConnectionResetEvent() throws SQLException {
		ConnectionResetEvent event = mock(ConnectionResetEvent.class);
		when(eventPublisher.publishEvent(event)).thenReturn(true);

		dataSource.onConnectionReset(connection);
		verify(eventPublisher).publishEvent(event);
	}

	@Test
	public void testGetConnectionProxy() throws SQLException {
		ReentrantLock lock = new ReentrantLock();
		Connection result = dataSource.getConnectionProxy(connection);
		assertSame(connection, result);

		ArgumentCaptor<InvocationHandler> captor = ArgumentCaptor.forClass(InvocationHandler.class);
		verify(connection).newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
				captor.capture());

		InvocationHandler handler = captor.getValue();
		assertNotSame(connection, handler);

		Object target = Proxy.getInvocationHandler(result);
		assertEquals(handler, target);

		Method closeMethod = Connection.class.getDeclaredMethod("close");
		((CloseIgnoreInvocationHandler) handler).invoke(result, closeMethod, null);

		verify(lock, times(1)).unlock();
	}

	interface ConnectionInitializer {
		void init(Connection connection) throws SQLException;
	}

	private static class ConnectionResetEvent {
	}

	private static class NewConnectionEvent {
	}

	private static class CloseIgnoreInvocationHandler implements InvocationHandler {
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if ("equals".equals(method.getName())) {
				return (proxy == args[0]);
			} else if ("hashCode".equals(method.getName())) {
				return System.identityHashCode(proxy);
			} else if ("unwrap".equals(method.getName())) {
				if (((Class<?>) args[0]).isInstance(proxy)) {
					return proxy;
				}
			} else if ("isWrapperFor".equals(method.getName())) {
				if (((Class<?>) args[0]).isInstance(proxy)) {
					return true;
				}
			} else if ("close".equals(method.getName())) {
				return null;
			} else if ("isClosed".equals(method.getName())) {
				return false;
			} else {
				return method.invoke(connection, args);
			}
		}
	}
}

class BaseClassBasedDataSource {
	
	protected void prepareConnection(Connection con) throws SQLException {
	}

	protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
		return null;
	}

	protected void onConnectionCreated(Connection connection) throws SQLException {
	}

	protected void initConnection(Connection connection) throws SQLException {
	}
}