import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext rootContext;
 private DupDetector dupDetector;

 @Before
 public void setup() {
 rootContext = JsonWriteContext.createRootContext(dupDetector = new DupDetector());
 }

 @Test
 public void testWriteFieldName_ValidScenario() throws JsonProcessingException {
 // Given: A root context with a dup detector
 // When: Write a field name "test"
 rootContext.writeFieldName("test");

 // Then: The index should be 0 and the status should be STATUS_OK_AS_IS
 assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.getStatus());
 assertEquals(0, rootContext.getCurrentIndex());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_DupDetector_DuplicateField() throws JsonProcessingException {
 // Given: A root context with a dup detector
 // And: A duplicate field name "test"
 rootContext.writeFieldName("test");
 dupDetector.addDuplicate("test");

 // When: Try to write the same field name again
 rootContext.writeFieldName("test");

 // Then: A JsonProcessingException should be thrown
 }

 @Test
 public void testWriteValue_RootContext() throws JsonProcessingException {
 // Given: A root context

 // When: Write a value
 rootContext.writeValue();

 // Then: The index should be 1 and the status should be STATUS_OK_AFTER_SPACE
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.getStatus());
 assertEquals(1, rootContext.getCurrentIndex());
 }

 @Test
 public void testCreateChildArrayContext() throws JsonProcessingException {
 // Given: A root context
 JsonWriteContext arrayContext = rootContext.createChildArrayContext();

 // Then: The type should be TYPE_ARRAY, the parent should be the root context, and the dup detector should be the same
 assertEquals(JsonWriteContext.TYPE_ARRAY, arrayContext.getType());
 assertEquals(rootContext, arrayContext.getParent());
 assertSame(dupDetector, arrayContext.getDupDetector());
 }

 @Test
 public void testCreateChildObjectContext() throws JsonProcessingException {
 // Given: A root context
 JsonWriteContext objectContext = rootContext.createChildObjectContext();

 // Then: The type should be TYPE_OBJECT, the parent should be the root context, and the dup detector should be the same
 assertEquals(JsonWriteContext.TYPE_OBJECT, objectContext.getType());
 assertEquals(rootContext, objectContext.getParent());
 assertSame(dupDetector, objectContext.getDupDetector());
 }

 @Test
 public void testWriteFieldName_ObjectContext_ValidScenario() throws JsonProcessingException {
 // Given: A root context with a child object context
 rootContext.createChildObjectContext();
 JsonWriteContext objectContext = rootContext.getChild();
 String name = "test";

 // When: Write a field name "test"
 objectContext.writeFieldName(name);

 // Then: The index should be 0 and the status should be STATUS_OK_AFTER_COLON
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.getStatus());
 assertEquals(0, objectContext.getCurrentIndex());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldName_ObjectContext_DupDetector_DuplicateField() throws JsonProcessingException {
 // Given: A root context with a child object context
 rootContext.createChildObjectContext();
 JsonWriteContext objectContext = rootContext.getChild();
 String name = "test";

 // And: A dup detector with a duplicate field name "test"
 dupDetector.addDuplicate(name);

 // When: Try to write the same field name again
 objectContext.writeFieldName(name);

 // Then: A JsonProcessingException should be thrown
 }
}