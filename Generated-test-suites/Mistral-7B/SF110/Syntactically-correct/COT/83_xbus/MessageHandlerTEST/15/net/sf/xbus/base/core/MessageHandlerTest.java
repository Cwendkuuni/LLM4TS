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
		MessageHandler instance1 = MessageHandler.getInstance("test");
		MessageHandler instance2 = MessageHandler.getInstance("test");
		assertEquals(instance1, instance2);
	}

	@Test
	public void testAddMessages() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		assertNotNull(handler.mMessages);
	}

	@Test
	public void testGetMessage() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		String message = handler.getMessage("test.key", null);
		assertNotNull(message);
	}

	@Test
	public void testGetMessageOptional() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		String message = handler.getMessageOptional("test.key", new ArrayList<Object>());
		assertNotNull(message);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageNullParams() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		handler.getMessage("test.key", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMessageInvalidKey() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		handler.getMessage("test.invalidKey", null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageOptionalNullList() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		handler.getMessageOptional("test.key", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMessageOptionalEmptyList() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		handler.getMessageOptional("test.key", new ArrayList<Object>());
		handler.getMessageOptional("test.key", new ArrayList<Object>());
	}

	@Test
	public void testGetMessageOptionalWithParams() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		List<Object> params = new ArrayList<Object>();
		params.add("testParam1");
		params.add(123);
		String message = handler.getMessageOptional("test.keyWithParams", params);
		assertNotNull(message);
		assertEquals(message, "Key: test.keyWithParams {0} {1}");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMessageOptionalInvalidParam() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/");
		List<Object> params = new ArrayList<Object>();
		params.add("testParam1");
		params.add(new Object());
		handler.getMessageOptional("test.keyWithParams", params);
	}

	@Test
	public void testGetMessageOptionalWithEmptyFile() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/empty/messages/");
		String message = handler.getMessageOptional("test.key", null);
		assertNull(message);
	}

	@Test(expected = FileNotFoundException.class)
	public void testGetMessageOptionalWithMissingFile() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/missing/messages/");
		String message = handler.getMessageOptional("test.key", null);
		assertNull(message);
	}

	@Test(expected = IOException.class)
	public void testGetMessageOptionalWithIOException() {
		MessageHandler handler = new MessageHandler("test");
		handler.addMessages("test", "/path/to/messages/withIOException/");
		String message = handler.getMessageOptional("test.key", null);
		assertNull(message);
	}
}