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

import org.junit.Test;

public class MessageHandlerTest {

	private static final String BASENAME = "test";
	private static final String DIR = "testMessages";
	private static final String MESSAGE_KEY = "test.key";
	private static final String MESSAGE_PARAM = "test.param";
	private static final String MESSAGE_VALUE = "Test message with {0}";

	@Test
	public void testGetInstance() {
		MessageHandler handler1 = MessageHandler.getInstance(BASENAME);
		MessageHandler handler2 = MessageHandler.getInstance(BASENAME);
		assertEquals(handler1, handler2);
	}

	@Test
	public void testGetMessage() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		String message = handler.getMessage(MESSAGE_KEY, null);
		assertNotNull(message);
		assertTrue(message.startsWith(MESSAGE_VALUE));
	}

	@Test
	public void testGetMessageOptional() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		String message = handler.getMessageOptional(MESSAGE_KEY, null);
		assertNotNull(message);
		assertTrue(message.startsWith(MESSAGE_VALUE));

		List params = new ArrayList();
		params.add(MESSAGE_PARAM);
		message = handler.getMessageOptional(MESSAGE_KEY, params);
		assertNotNull(message);
		assertTrue(message.contains(MESSAGE_PARAM));
	}

	@Test
	public void testAddMessages() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		assertTrue(handler.mMessages == null);

		handler = new MessageHandler(BASENAME);
		assertNotNull(handler.mMessages);

		Properties messages = handler.mMessages;
		assertEquals(2, messages.size());
		assertNotNull(messages.get(MESSAGE_KEY));
		assertEquals(MESSAGE_VALUE, messages.get(MESSAGE_KEY));
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageWithNullKey() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		handler.getMessage(null, null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMessageWithNullParams() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		handler.getMessage(MESSAGE_KEY, null);
	}

	@Test
	public void testTraceMessage() {
		MessageHandler handler = MessageHandler.getInstance(BASENAME);
		String key = "test.trace.key";
		List params = new ArrayList();
		params.add("test.trace.param");
		String message = handler.getMessage(key, params);
		assertEquals("Key: " + key + " not found in message file", message);
		Trace.error(message);
		assertTrue(Trace.getLastErrorMessage().startsWith(message));
	}

	@Test
	public void testReplaceAll() {
		String text = "{0} is {1}.";
		List params = new ArrayList();
		params.add("John");
		params.add("happy");
		String result = XStringSupport.replaceAll(text, "{0}", params.get(0));
		assertEquals("John is happy.", result);
	}

	static class MessagesFilterTest {
		@Test
		public void testAccept() {
			MessagesFilter filter = new MessagesFilter("test", "_ENGLISH.properties");
			assertTrue(filter.accept(null, "test_ENGLISH.properties"));
			assertTrue(filter.accept(null, "test_FRENCH.properties"));
			assertFalse(filter.accept(null, "test.properties"));
			assertFalse(filter.accept(null, "test_ENGLISH_FRENCH.properties"));
		}
	}

	static class ConfigurationTest {
		@Test
		public void testClass() {
			assertNotNull(Configuration.class);
		}
	}

	static {
		try {
			File dir = new File(DIR);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file1 = new File(DIR, "test_ENGLISH.properties");
			FileWriter writer1 = new FileWriter(file1);
			writer1.write("test.key=" + MESSAGE_VALUE);
			writer1.close();

			File file2 = new File(DIR, "test_FRENCH.properties");
			FileWriter writer2 = new FileWriter(file2);
			writer2.write("test.key=" + MESSAGE_VALUE.replace("Test", "Test Gallic"));
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException raised: " + e.getMessage());
		}
	}

	private static class MessagesFilter {
		private String mPrefix;
		private String mPostfix;

		public MessagesFilter(String prefix, String postfix) {
			mPrefix = prefix;
			mPostfix = postfix;
		}

		boolean accept(File dir, String filename) {
			return filename.startsWith(mPrefix) && filename.endsWith(mPostfix);
		}
	}
}