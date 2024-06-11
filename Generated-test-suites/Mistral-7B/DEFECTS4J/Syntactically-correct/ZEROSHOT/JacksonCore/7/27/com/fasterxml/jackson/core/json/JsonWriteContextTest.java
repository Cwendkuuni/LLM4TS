import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.util.DupDetector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonWriteContextTest {

 private JsonWriteContext context;
 private DupDetector dupDetector;

 @Before
 public void setUp() {
 dupDetector = mock(DupDetector.class);
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 }

 @Test
 public void testCreateRootContext() {
 JsonWriteContext rootContext = JsonWriteContext.createRootContext();
 assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
 assertNull(rootContext.getParent());
 }

 @Test
 public void testCreateChildArrayContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 JsonWriteContext childContext = parentContext.createChildArrayContext();
 assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
 assertEquals(parentContext, childContext.getParent());
 }

 @Test
 public void testCreateChildObjectContext() {
 JsonWriteContext parentContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 JsonWriteContext childContext = parentContext.createChildObjectContext();
 assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
 assertEquals(parentContext, childContext.getParent());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteFieldNameWhenGotNameIsFalse() throws JsonProcessingException {
 context.writeFieldName("name");
 }

 @Test
 public void testWriteFieldNameWhenGotNameIsTrue() throws JsonProcessingException {
 context.writeFieldName("field1");
 context.writeFieldName("field2");
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteValueWhenCurrentNameIsNull() throws JsonProcessingException {
 context.writeValue();
 }

 @Test
 public void testWriteValueWhenCurrentNameIsNotNull() throws JsonProcessingException {
 context.writeFieldName("field1");
 context.writeValue();
 assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.getStatus());
 }

 @Test
 public void testCheckDupWhenDupDetectorIsNotNull() throws JsonProcessingException {
 when(dupDetector.isDup("field1")).thenReturn(true);
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 context.writeFieldName("field1");
 }

 @Test(expected = JsonGenerationException.class)
 public void testCheckDupWhenDupDetectorDetectsDuplicate() throws JsonProcessingException {
 when(dupDetector.isDup("field1")).thenReturn(true);
 context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
 context.writeFieldName("field1");
 }

 @Test
 public void testReset() {
 context.reset(JsonWriteContext.TYPE_OBJECT);
 assertNull(context.getCurrentName());
 assertNull(context.getCurrentValue());
 }

 @Test
 public void testToString() {
 assertEquals("{}", context.toString());
 }
}