package net.sf.xbus.base.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.junit.Test;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.StringsSupport;
import net.sf.xbus.base.core.trace.Trace;

public class MessageHandlerTest {

	private static final String BASE_NAME = "test";
	private static final String XBUS_ETC = "xbus-etc";
	private static final String XBUS_PLUGIN_ETC = "xbus-plugin-etc";
	private static final String MESSAGE_FILE_NAME = "test_en_US.properties";
	private static final String MESSAGE_KEY = "testKey";
	private static final String MESSAGE_VALUE = "Test value";
	private static final String PARAMETER_VALUE = "Test parameter";

	static {
		System.setProperty("user.dir", System.getProperty("java.class.path").substring(0, System.getProperty("java.class.path").lastIndexOf(File.separator)));
	}

	@Test
	public void testGetInstance() {
		MessageHandler instance1 = MessageHandler.getInstance(BASE_NAME);
		MessageHandler instance2 = MessageHandler.getInstance(BASE_NAME);

		assertSame(instance1, instance2);
	}

	@Test
	public void testAddMessages() {
		MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

		handler.addMessages(BASE_NAME, XBUS_ETC);
		handler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);

		Hashtable messages = handler.mMessages;
		assertEquals(2, messages.size());
		assertNull(messages.get(MESSAGE_KEY));
	}

	@Test
	public void testGetMessage() {
		MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

		handler.addMessages(BASE_NAME, XBUS_ETC);
		handler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);

		Properties testProperties = new Properties();
		testProperties.setProperty(MESSAGE_KEY, MESSAGE_VALUE);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		testProperties.store(outputStream, null);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		File messageFile = new File(XBUS_ETC, MESSAGE_FILE_NAME);
		messageFile.createNewFile();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(messageFile);
			inputStream.transferTo(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			Trace.error("Error creating test file: " + e.getMessage());
		}

		String message = handler.getMessage(MESSAGE_KEY, null);
		assertEquals(MESSAGE_VALUE, message);

		messageFile.delete();
	}

	@Test
	public void testGetMessageOptional() {
		MessageHandler handler = MessageHandler.getInstance(BASE_NAME);

		handler.addMessages(BASE_NAME, XBUS_ETC);
		handler.addMessages(BASE_NAME, XBUS_PLUGIN_ETC);

		List<String> messageParameters = new ArrayList<>();
		messageParameters.add(PARAMETER_VALUE);

		String message = handler.getMessageOptional(MESSAGE_KEY, messageParameters);
		assertNull(message);

		Properties testProperties = new Properties();
		testProperties.setProperty(MESSAGE_KEY + "$0", MESSAGE_VALUE);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		testProperties.store(outputStream, null);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

		File messageFile = new File(XBUS_ETC, MESSAGE_FILE_NAME);
		messageFile.createNewFile();
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(messageFile);
			inputStream.transferTo(fileOutputStream);
			fileOutputStream.close();
		} catch (IOException e) {
			Trace.error("Error creating test file: " + e.getMessage());
		}

		message = handler.getMessageOptional(MESSAGE_KEY, messageParameters);
		assertEquals(MESSAGE_VALUE.replace("$0", PARAMETER_VALUE), message);

		messageFile.delete();
	}
}