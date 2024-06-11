package net.sf.xbus.base.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;

import org.junit.BeforeClass;
import org.junit.Test;

public class MessageHandlerTest {

    private static MessageHandler messageHandler;
    private static final String BASE_NAME = "MessageHandlerTest";
    private static final String MESSAGE_FILE_NAME = "testMessageFile.properties";

    @BeforeClass
    public static void setup() {
        messageHandler = MessageHandler.getInstance(BASE_NAME);
        try {
            File dir = new File("src/test/resources/");
            Properties newProps = new Properties();
            newProps.load(new FileInputStream(dir + MESSAGE_FILE_NAME));
            messageHandler.addMessages(BASE_NAME, "src/test/resources/");
            newProps.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMessageOptional() {
        List params = Arrays.asList("param1", "param2");
        String expectedMessage = "Key: ${1} not found in message file ${0}";
        String actualMessage = messageHandler.getMessageOptional("Key1", params);
        assertEquals(expectedMessage, actualMessage);

        expectedMessage = "Key: param1, param2 not found in message file MessageHandlerTest";
        actualMessage = messageHandler.getMessageOptional("Key2", params);
        assertEquals(expectedMessage, actualMessage);

        params = Collections.emptyList();
        expectedMessage = "Key: not found in message file MessageHandlerTest";
        actualMessage = messageHandler.getMessageOptional("Key3", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessage() {
        List params = Arrays.asList("param1", "param2");
        String expectedMessage = "Key1: ${1} not found in message file MessageHandlerTest";
        String actualMessage = messageHandler.getMessage("Key1", params);
        Trace.setErrorPrintStream(System.out);
        assertEquals(expectedMessage, actualMessage);
        Trace.setErrorPrintStream(System.err);

        expectedMessage = "Key2: param1, param2 not found in message file MessageHandlerTest";
        actualMessage = messageHandler.getMessage("Key2", params);
        assertEquals(expectedMessage, actualMessage);

        params = Collections.emptyList();
        expectedMessage = "Key3: not found in message file MessageHandlerTest";
        actualMessage = messageHandler.getMessage("Key3", params);
        assertEquals(expectedMessage, actualMessage);

        expectedMessage = "Key1: param1, param2";
        actualMessage = messageHandler.getMessage("Key1_ENGLISH", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMessageNullParams() {
        messageHandler.getMessage("Key4", null);
    }
}