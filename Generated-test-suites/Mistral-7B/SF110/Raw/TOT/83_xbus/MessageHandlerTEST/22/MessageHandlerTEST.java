package net.sf.xbus.base.core.test;

import net.sf.xbus.base.core.*;
import org.junit.*;
import static org.junit.Assert.*;;

public class MessageHandlerTest {
    private static MessageHandler firstInstance;
    private static MessageHandler secondInstance;
    private MessageHandler handler;

    @BeforeClass
    public static void setUpBeforeClass() {
        firstInstance = MessageHandler.getInstance("validBasename");
    }

    @Before
    public void setUp() {
        handler = new MessageHandler("validBasename");
    }

    @Test
    public void testGetInstanceWithValidBasenameAndExistingInstance() {
        secondInstance = MessageHandler.getInstance("validBasename");
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testGetInstanceWithValidBasenameAndNewInstance() {
        secondInstance = MessageHandler.getInstance("validBasename");
        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void testGetInstanceWithInvalidBasename() {
        try {
            MessageHandler.getInstance("invalidBasename");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithValidBasename() {
        handler = new MessageHandler("validBasename");
        assertNotNull(handler.mMessages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidBasename() {
        new MessageHandler("invalidBasename");
    }

    @Test
    public void testAddMessagesWithValidBasenameAndValidDirectory() {
        handler.addMessages("validBasename", "validDirectory");
        assertFalse(handler.mMessages.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMessagesWithValidBasenameAndInvalidDirectory() {
        try {
            handler.addMessages("validBasename", "invalidDirectory");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndNullParams() {
        List<Object> params = null;
        String expectedMessage = "Valid message with null params";
        String actualMessage = handler.getMessageOptional("validKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithValidKeyAndValidParams() {
        List<Object> params = Arrays.asList("test1", "test2");
        String expectedMessage = "Valid message with valid params: $1$ and $2$";
        String actualMessage = handler.getMessageOptional("validKey", params);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageOptionalWithInvalidKey() {
        List<Object> params = null;
        String actualMessage = handler.getMessageOptional("invalidKey", params);
        assertNull(actualMessage);
    }

    @After
    public void tearDown() {
        MessageHandler.mInstances.clear();
    }
}