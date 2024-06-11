package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

	private static MessageHandler instance;
	private static List<String> params;

	@BeforeClass
	public static void setupClass() {
		instance = MessageHandler.getInstance("testMessageHandler");
		params = new ArrayList<String>();
	}

	@Test
	public void testGetInstance() {
		MessageHandler handler1 = MessageHandler.getInstance("testMessageHandler");
		MessageHandler handler2 = MessageHandler.getInstance("testMessageHandler");
		assertEquals(handler1, handler2);
	}

	@Test
	public void testGetMessageWithValidKeyAndParams() {
		String message = instance.getMessage("testKey", params);
		assertNotNull(message);
		assertTrue(message.contains("testKey"));
		assertTrue(message.contains("$1$"));
		assertTrue(message.contains("$2$"));
		String paramsMessage = instance.getMessageOptional("testKey", params);
		assertNotNull(paramsMessage);
		assertEquals(message, paramsMessage);
	}

	@Test
	public void testGetMessageWithNullParams() {
		String message = instance.getMessage("testKey", null);
		assertNotNull(message);
		assertTrue(message.contains("testKey"));
		assertTrue(message.contains("$1$"));
		assertTrue(message.contains("$2$"));
		String paramsMessage = instance.getMessageOptional("testKey", null);
		assertNotNull(paramsMessage);
		assertEquals(message, paramsMessage);
	}

	@Test
	public void testGetMessageWithInvalidKey() {
		String message = instance.getMessage("invalidKey", params);
		assertNull(message);
	}

	@Test
	public void testGetMessageWithEmptyKey() {
		String message = instance.getMessage("", params);
		assertNull(message);
	}

	@Test
	public void testGetMessageWithEmptyParams() {
		String message = instance.getMessage("testKey", new ArrayList<String>());
		assertNotNull(message);
		assertTrue(message.contains("testKey"));
		assertTrue(message.contains("$1$"));
		assertTrue(message.contains("$2$"));
		String paramsMessage = instance.getMessageOptional("testKey", new ArrayList<String>());
		assertNotNull(paramsMessage);
		assertEquals(message, paramsMessage);
	}

	@Test
	public void testGetMessageWithNullKey() {
		String message = instance.getMessage(null, params);
		assertNull(message);
		String paramsMessage = instance.getMessageOptional(null, params);
		assertNull(paramsMessage);
	}

	@Test
	public void testAddMessages() {
		instance.addMessages("testMessageHandler", "testDir");
		Properties props = instance.mMessages;
		assertNotNull(props);
		String key = "testKey";
		String message = "Test key message $1$ $2$";
		String expectedMessage = "Test key message <null> <null>";
		props.setProperty(key, message);
		String getMessage = instance.getMessage(key, params);
		assertEquals(expectedMessage, getMessage);
		String getMessageOptional = instance.getMessageOptional(key, params);
		assertEquals(expectedMessage, getMessageOptional);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageWithNullMessageTable() {
		instance.mMessages = null;
		instance.getMessage("testKey", params);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddMessagesWithNullBasename() {
		instance.addMessages(null, "testDir");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddMessagesWithNullDir() {
		instance.addMessages("testMessageHandler", null);
	}

	@Test(expected = FileNotFoundException.class)
	public void testAddMessagesWithMissingFile() {
		instance.addMessages("testMessageHandler", "testDir");
		Properties props = instance.mMessages;
		assertNotNull(props);
		String key = "testKey";
		String postfix = "_en.properties";
		String fileName = "testMessageHandler_en.properties";
		File file = new File("testDir", fileName);
		file.delete();
		instance.addMessages("testMessageHandler", "testDir");
		assertEquals(0, props.size());
		instance.addMessages("testMessageHandler", "testDir");
		assertEquals(0, props.size());
		props = instance.mMessages;
		assertTrue(props.isEmpty());
	}

	@Test(expected = IOException.class)
	public void testAddMessagesWithIOException() throws IOException {
		instance.addMessages("testMessageHandler", "testDir");
		Properties props = instance.mMessages;
		assertNotNull(props);
		String key = "testKey";
		String postfix = "_en.properties";
		String fileName = "testDir/testMessageHandler_en.properties";
		File file = new File(fileName);
		FileOutputStream stream = new FileOutputStream(file);
		stream.write("testKey=Test message $1$ $2$".getBytes());
		stream.close();
		instance.addMessages("testMessageHandler", "testDir");
		String getMessage = instance.getMessage("testKey", params);
		assertEquals("Test message <null> <null>", getMessage);
		props = instance.mMessages;
		assertEquals(1, props.size());
		assertNotNull(props.getProperty(key));
		String getMessageOptional = instance.getMessageOptional("testKey", params);
		assertEquals("Test message <null> <null>", getMessageOptional);
	}
}