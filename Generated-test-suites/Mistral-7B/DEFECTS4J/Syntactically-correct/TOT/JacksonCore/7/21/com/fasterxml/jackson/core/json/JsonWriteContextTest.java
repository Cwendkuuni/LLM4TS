import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private JsonWriteContext objectContext;
 private JsonWriteContext arrayContext;

 @Before
 public void setUp() {
 rootContext = JsonWriteContext.createRootContext();
 objectContext = rootContext.createChildObjectContext();
 arrayContext = rootContext.createChildArrayContext();
 }

 @Test
 public void testWriteFieldName() throws JsonProcessingException {
 String name = "name";
 int status = objectContext.writeFieldName(name);
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 objectContext.setCurrentValue("value");
 int valueStatus = objectContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COLON", JsonWriteContext.STATUS_OK_AFTER_COLON, valueStatus);
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteDuplicateFieldName() throws JsonProcessingException {
 String name = "name";
 objectContext.writeFieldName(name);
 objectContext.writeFieldName(name);
 }

 @Test
 public void testWriteArrayValue() throws JsonProcessingException {
 int status = arrayContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_COMMA", JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
 arrayContext.setCurrentValue(1);
 status = arrayContext.writeValue();
 assertEquals("Expected STATUS_OK_AS_IS", JsonWriteContext.STATUS_OK_AS_IS, status);
 }

 @Test
 public void testWriteRootValue() throws JsonProcessingException {
 int status = rootContext.writeValue();
 assertEquals("Expected STATUS_OK_AS_IS", JsonWriteContext.STATUS_OK_AS_IS, status);
 rootContext.setCurrentValue("value");
 status = rootContext.writeValue();
 assertEquals("Expected STATUS_OK_AFTER_SPACE", JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
 }
}