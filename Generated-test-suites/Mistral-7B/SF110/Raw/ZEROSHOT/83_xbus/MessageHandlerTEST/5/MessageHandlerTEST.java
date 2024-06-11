package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.Test;

public class MessageHandlerTest {

	@Test
	public void testGetInstance() {
		MessageHandler handler1 = MessageHandler.getInstance("test");
		MessageHandler handler2 = MessageHandler.getInstance("test");
		assertSame(handler1, handler2);
	}

	@Test
	public void testGetMessage() {
		MessageHandler handler = MessageHandler.getInstance("test");
		List<Object> params = new ArrayList<Object>();
		String expectedMessage = "Key: test $1$ not found in message file";
		String resultMessage = handler.getMessage("test", params);
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void testGetMessageOptional() {
		MessageHandler handler = MessageHandler.getInstance("test");
		List<Object> params = new ArrayList<Object>();
		params.add("test");
		String expectedMessage = "Key: test $1$";
		String resultMessage = handler.getMessageOptional("test", params);
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void testGetMessageOptionalWithParams() {
		MessageHandler handler = MessageHandler.getInstance("test");
		List<Object> params = new ArrayList<Object>();
		params.add("test1");
		params.add("test2");
		String expectedMessage = "Key: test $1$ is $2$";
		String resultMessage = handler.getMessageOptional("test", params);
		assertEquals(expectedMessage, resultMessage);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageOptionalWithNullParams() {
		MessageHandler handler = MessageHandler.getInstance("test");
		List<Object> params = null;
		String resultMessage = handler.getMessageOptional("test", params);
		fail("Expected NullPointerException, but got: " + resultMessage);
	}

	@Test
	public void testAddMessages() throws Exception {
		MessageHandler handler = MessageHandler.getInstance("test");
		String basename = "test";
		String dir = "src/test/resources/messages";
		handler.addMessages(basename, dir);
		Properties expectedProps = new Properties();
		expectedProps.setProperty("test.key1", "Test message 1: $1$");
		expectedProps.setProperty("test.key2", "Test message 2: $1$ is $2$");
		Hashtable<String, String> expectedMessages = new Hashtable<String, String>();
		expectedMessages.putAll(expectedProps);
		Hashtable<String, String> actualMessages = (Hashtable<String, String>) handler.mMessages;
		assertEquals(expectedMessages, actualMessages);
	}

	@Test
	public void testGetMessageWithLoadedMessages() {
		MessageHandler handler = MessageHandler.getInstance("test");
		String key = "test.key1";
		List<Object> params = new ArrayList<Object>();
		params.add("test1");
		String expectedMessage = "Test message 1: test1";
		String resultMessage = handler.getMessage(key, params);
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void testGetMessageWithoutLoadedMessages() {
		MessageHandler handler = MessageHandler.getInstance("test");
		String key = "test.key3";
		List<Object> params = null;
		String expectedMessage = null;
		String resultMessage = handler.getMessage(key, params);
		assertNull(resultMessage);
		assertEquals(0, handler.mMessages.size());
	}

	@Test
	public void testTraceError() {
		MessageHandler handler = MessageHandler.getInstance("test");
		String key = "test.error";
		List<Object> params = null;
		String expectedMessage = "Key: test.error not found in message file";
		handler.getMessage(key, params);
		String traceMessage = Trace.getLastMessage();
		assertEquals(expectedMessage, traceMessage);
		Trace.clear();
	}
}