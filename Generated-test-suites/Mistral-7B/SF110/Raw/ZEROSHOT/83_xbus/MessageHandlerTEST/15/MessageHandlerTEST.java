package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.Strings.XStringSupport;
import net.sf.xbus.base.core.trace.Trace;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

 private static MessageHandler messageHandler;

 @BeforeClass
 public static void setup() {
 messageHandler = MessageHandler.getInstance("test");
 }

 @Test
 public void testGetInstance() {
 MessageHandler instance1 = MessageHandler.getInstance("test");
 MessageHandler instance2 = MessageHandler.getInstance("test");
 assertSame(instance1, instance2);
 }

 @Test
 public void testGetMessageNullKey() {
 assertNull(messageHandler.getMessage(null, null));
 }

 @Test
 public void testGetMessageEmptyList() {
 String message = messageHandler.getMessage("testKey", new ArrayList<Object>());
 assertEquals("Key: testKey not found in message file", message);
 }

 @Test
 public void testGetMessageOptionalNullKey() {
 assertNull(messageHandler.getMessageOptional(null, null));
 }

 @Test
 public void testGetMessageOptionalEmptyList() {
 String message = messageHandler.getMessageOptional("testKey", new ArrayList<Object>());
 assertEquals("Key: testKey not found in message file", message);
 }

 @Test
 public void testGetMessageOptionalParamsReplaced() {
 String key = "testMessage{$0} and {$1}";
 List<Object> params = new ArrayList<Object>();
 params.add("value1");
 params.add("value2");
 String message = messageHandler.getMessageOptional(key, params);
 String expectedMessage = "testMessagevalue1 and value2";
 assertEquals(expectedMessage, message);
 }

 @Test
 public void testGetMessageOptionalParamsNull() {
 String key = "testMessage{$0} and {$1}";
 List<Object> params = null;
 String message = messageHandler.getMessageOptional(key, params);
 assertEquals("Key: testMessage not found in message file", message);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMessageOptionalParamsNullElement() {
 String key = "testMessage{$0} and {$1}";
 List<Object> params = new ArrayList<Object>();
 params.add("value1");
 params.add(null);
 messageHandler.getMessageOptional(key, params);
 }

 @Test
 public void testAddMessages() {
 Locale english = new Locale("en", "US");
 Locale german = new Locale("de", "DE");
 Properties englishProperties = new Properties();
 Properties germanProperties = new Properties();
 String englishFileName = "testMessages_en_US.properties";
 String germanFileName = "testMessages_de_DE.properties";
 EnglishMessageHandler englishMessageHandler = new EnglishMessageHandler();
 GermanMessageHandler germanMessageHandler = new GermanMessageHandler();

 englishProperties.setProperty("testMessage", "This is a test message (English: {0})");
 germanProperties.setProperty("testMessage", "Dies ist ein Testnachricht (Deutsch: {0})");

 try {
 File tempDir = File.createTempFile("test", null);
 tempDir.mkdir();
 tempDir.deleteOnExit();

 File englishFile = new File(tempDir, englishFileName);
 File germanFile = new File(tempDir, germanFileName);

 FileWriter englishWriter = new FileWriter(englishFile);
 englishWriter.write(englishProperties.getPropertyStrings().toString());
 englishWriter.close();

 FileWriter germanWriter = new FileWriter(germanFile);
 germanWriter.write(germanProperties.getPropertyStrings().toString());
 germanWriter.close();

 messageHandler.addMessages("test", tempDir.getAbsolutePath());
 assertEquals(1, messageHandler.getMessageCount("test"));

 String englishMessage = messageHandler.getMessage("testMessage", null);
 assertEquals("This is a test message (English: {})", englishMessage);

 String germanMessage = messageHandler.getMessage("testMessage", null, german);
 assertEquals("Dies ist ein Testnachricht (Deutsch: {})", germanMessage);

 tempDir.deleteRecursively();
 }
 catch (IOException e) {
 fail("Failed to write test properties files: " + e.getMessage());
 }
}

 @Test(expected = IllegalArgumentException.class)
 public void testAddMessagesNullDir() {
 messageHandler.addMessages(null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMessagesEmptyDir() {
 messageHandler.addMessages("", "");
 }

 @Test(expected = FileNotFoundException.class)
 public void testAddMessagesNonExistentDir() {
 messageHandler.addMessages("nonExistentDir", "");
 }

 @Test
 public void testGetMessageCount() {
 assertEquals(0, messageHandler.getMessageCount("nonExistentMessages"));
 }

}

class EnglishMessageHandler extends MessageHandler {
 protected EnglishMessageHandler() {
 super("test");
 }
}

class GermanMessageHandler extends MessageHandler {
 protected GermanMessageHandler() {
 super("test");
 }
}