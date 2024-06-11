package net.sf.xbus.base.core.test;

import org.junit.*;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import net.sf.xbus.base.core.strings.*;
import java.io.*;
import java.util.*;

public class MessageHandlerTest {
    private static final String TEST_BASE_NAME = "test";

    @BeforeClass
    public static void initializeTests() {
        // Initialize class-level test data or setup
    }

    @AfterClass
    public static void tearDownTests() {
        // Clean up class-level test data or resources
    }

    @Before
    public void setUp() {
        // Initialize instance-level test data or setup
    }

    @After
    public void tearDown() {
        // Clean up instance-level test data or resources
    }

    @Test
    public void testgetMessageOptional_KeyFoundWithParams() {
        // Test case for getMessageOptional method with key that exists and params
        String expectedMessage = "Expected message with params";
        String key = "TestKey";
        List<String> params = Arrays.asList("Param1", "Param2");
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessageOptional(key, params);

        String messageText = actualMessage.substring(0, actualMessage.indexOf("$"));
        assertEquals(key, messageText);

        int counter = 1;
        for (String param : params) {
            String paramText = param.isEmpty() ? "<null>" : param;
            int index = actualMessage.indexOf("$" + counter + "$");
            assertEquals(paramText, actualMessage.substring(index + 1, actualMessage.indexOf("$", index + 1)));
            ++counter;
        }

        assertEquals(expectedMessage, messageHandler.getMessage(key, params));
    }

    @Test
    public void testgetMessageOptional_KeyNotFound() {
        // Test case for getMessageOptional method with key that doesn't exist and empty params list
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessageOptional("NonExistentKey", Collections.emptyList());

        assertEquals("Key: NonExistentKey not found in message file", actualMessage);
    }

    @Test
    public void testgetMessageOptional_EmptyParamsList() {
        // Test case for getMessageOptional method with key that exists and empty params list
        String expectedMessage = "Expected message";
        String key = "TestKey";
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessageOptional(key, Collections.emptyList());

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testgetMessage_KeyFoundWithParams() {
        // Test case for getMessage method with key that exists and params
        String expectedMessage = "Expected message with params";
        String key = "TestKey";
        List<String> params = Arrays.asList("Param1", "Param2");
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessage(key, params);

        String messageText = actualMessage.substring(0, actualMessage.indexOf("$"));
        assertEquals(key, messageText);

        int counter = 1;
        for (String param : params) {
            String paramText = param.isEmpty() ? "<null>" : param;
            int index = actualMessage.indexOf("$" + counter + "$");
            assertEquals(paramText, actualMessage.substring(index + 1, actualMessage.indexOf("$", index + 1)));
            ++counter;
        }

        assertEquals(expectedMessage, messageHandler.getMessageOptional(key, params));
    }

    @Test
    public void testgetMessage_KeyNotFound() {
        // Test case for getMessage method with key that doesn't exist and empty params list
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessage("NonExistentKey", Collections.emptyList());

        assertEquals("Key: NonExistentKey not found in message file", actualMessage);
    }

    @Test
    public void testgetMessage_EmptyParamsList() {
        // Test case for getMessage method with key that exists and empty params list
        String expectedMessage = "Expected message";
        String key = "TestKey";
        MessageHandler messageHandler = MessageHandler.getInstance(TEST_BASE_NAME);

        String actualMessage = messageHandler.getMessage(key, Collections.emptyList());

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetInstance_BasenameExists() {
        // Test case for MessageHandler.getInstance method with existing basename
        String expectedBaseName = TEST_BASE_NAME;
        MessageHandler expectedMessageHandler = MessageHandler.getInstance(expectedBaseName);
        String expectedMessageFile1 = "expected_message_file_1.properties";
        String expectedMessageFile2 = "expected_message_file_2.properties";
        Properties expectedMessages1 = new Properties();
        Properties expectedMessages2 = new Properties();

        expectedMessages1.setProperty("TestKey1", "Expected message 1");
        expectedMessages1.setProperty("TestKey2", "Expected message 2 with {0} and {1}");
        expectedMessages1.setProperty("TestKey3", "Expected message 3");

        expectedMessages2.setProperty("TestKey1", "Expected message 1 alternative");
        expectedMessages2.setProperty("TestKey2", "Expected message 2 with {2} and {3}");
        expectedMessages2.setProperty("TestKey4", "Expected message 4");

        File expectedMessageFile1Path = new File("test" + File.separator + "message_files" + File.separator + expectedBaseName + "_en_US" + File.separator + expectedMessageFile1);
        File expectedMessageFile2Path = new File("test" + File.separator + "message_files" + File.separator + expectedBaseName + "_en_US" + File.separator + expectedMessageFile2);

        try (InputStream expectedMessageInputStream1 = new FileInputStream(expectedMessageFile1Path)) {
            expectedMessages1.load(expectedMessageInputStream1);
        } catch (IOException e) {
            fail("Expected message file 1 not found: " + expectedMessageFile1Path);
        }

        try (InputStream expectedMessageInputStream2 = new FileInputStream(expectedMessageFile2Path)) {
            expectedMessages2.load(expectedMessageInputStream2);
        } catch (IOException e) {
            fail("Expected message file 2 not found: " + expectedMessageFile2Path);
        }

        MessageHandler actualMessageHandler = MessageHandler.getInstance(expectedBaseName);

        assertEquals(expectedMessages1, actualMessageHandler.getMessages(expectedBaseName + "_en_US"));
        assertEquals(expectedMessages2, actualMessageHandler.getMessages(expectedBaseName + "_en_US_alternative"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstance_BasenameDoesNotExist() {
        // Test case for MessageHandler.getInstance method with non-existent basename
        MessageHandler.getInstance("NonExistentBaseName");
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstance_BasenameIsNull() {
        // Test case for MessageHandler.getInstance method with null basename
        messageHandler.getMessage(null, null);
    }
}