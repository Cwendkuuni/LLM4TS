import static org.junit.Assert.*;
import net.sf.xbus.base.core.*;
import org.junit.*;
import java.util.*;

public class MessageHandlerTest {
   MessageHandler messageHandler;

   @Before
   public void setUp() {
       messageHandler = MessageHandler.getInstance("testBasename");
   }

   @Test
   public void testGetMessage() {
       List params = new ArrayList();
       String expectedMessage = "Expected message";
       String actualMessage = messageHandler.getMessage("key", params);
       assertEquals(expectedMessage, actualMessage);
   }

   @Test
   public void testGetMessageOptional() {
       List params = null;
       String expectedMessage = "Optional message";
       String actualMessage = messageHandler.getMessageOptional("key", params);
       assertEquals(expectedMessage, actualMessage);
   }

   @Test
   public void testGetInstance() {
       MessageHandler instance1 = MessageHandler.getInstance("testBasename");
       MessageHandler instance2 = MessageHandler.getInstance("testBasename");
       assertSame(instance1, instance2);
   }

   @Test(expected = NullPointerException.class)
   public void testGetInstanceNullBasename() {
       MessageHandler.getInstance(null);
   }

   @Test
   public void testAddMessages() {
       messageHandler = new MessageHandler("testBasename");
       messageHandler.addMessages("testBasename", "testDir");
       String expectedMessage = "Expected message from added file";
       String actualMessage = messageHandler.getMessage("key_from_added_file", null);
       assertEquals(expectedMessage, actualMessage);
   }

   @Test(expected = FileNotFoundException.class)
   public void testAddMessagesFileNotFound() {
       messageHandler = new MessageHandler("testBasename");
       messageHandler.addMessages("testBasename", "testDir");
       messageHandler.getMessage("key_from_non_existent_file", null);
   }
}