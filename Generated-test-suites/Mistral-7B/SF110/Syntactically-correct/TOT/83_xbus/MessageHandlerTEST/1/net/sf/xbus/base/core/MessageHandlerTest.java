import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.xbus.base.core.MessageHandler;
import net.sf.xbus.base.core.trace.Trace;
import net.sf.xbus.base.core.strings.XStringSupport;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MessageHandlerTest {
    private static MessageHandler messageHandler;
    private static final String BASENAME = "testMessages";

    @BeforeClass
    public static void setup() {
        messageHandler = MessageHandler.getInstance(BASENAME);
        messageHandler.addMessages(BASENAME, "src/test/resources/messages");
    }

    @Test
    public void testGetMessageWithValidKeyAndParams() {
        List params = new ArrayList();
        params.add("TestParam1");
        params.add(123);

        String expectedMessage = "Message with TestParam1: $1$ and number: $2$";
        String actualMessage = messageHandler.getMessage("test.message.key", params);

        String replacedMessage = XStringSupport.replaceAll(expectedMessage, "$1$", "TestParam1Replaced");
        String replacedActualMessage = XStringSupport.replaceAll(actualMessage, "$1$", "TestParam1Replaced");

        assertEquals(replacedMessage, replacedActualMessage);
    }

    @Test
    public void testGetMessageWithValidKey() {
        String expectedMessage = "This is a message with no params";
        String actualMessage = messageHandler.getMessage("test.message.noParams");

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMessageWithNullKey() {
        List params = new ArrayList();
        params.add("TestParam1");
        params.add(123);

        String actualMessage = messageHandler.getMessage(null, params);
        assertNull(actualMessage);
    }

    @Test
    public void testGetMessageWithNullParams() {
        String actualMessage = messageHandler.getMessage("test.message.key", null);
        assertNull(actualMessage);
    }

    @Test
    public void testGetMessageWithEmptyHashtable() {
        MessageHandler emptyMessageHandler = new MessageHandler("emptyMessages");

        String actualMessage = emptyMessageHandler.getMessageOptional("test.message.key", null);
        assertNull(actualMessage);
    }
}