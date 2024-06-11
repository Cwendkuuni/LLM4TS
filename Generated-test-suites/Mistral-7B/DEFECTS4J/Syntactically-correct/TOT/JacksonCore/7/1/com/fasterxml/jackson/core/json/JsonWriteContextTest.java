import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

 private JsonWriteContext createContext(DupDetector dupDetector) {
 return JsonWriteContext.createRootContext(dupDetector);
 }

 @Test
 public void testRootContextWriteValue() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeValue();
 assertEquals("/", context.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testRootContextWriteValueTwice() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeValue();
 context.writeValue();
 }

 @Test
 public void testArrayContextWriteValue() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 arrayContext.writeValue();
 assertEquals("[", arrayContext.toString());

 JsonWriteContext valueContext = arrayContext.createChildArrayContext();
 valueContext.writeValue();
 assertEquals("[,", arrayContext.toString());

 valueContext.writeValue();
 assertEquals("[,]", arrayContext.toString());

 valueContext.reset();
 valueContext.writeValue();
 assertEquals("[,]", arrayContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testArrayContextWriteValueWithIndex() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext arrayContext = parentContext.createChildArrayContext();
 arrayContext.writeValue();
 assertEquals("[", arrayContext.toString());

 JsonWriteContext valueContext = arrayContext.createChildArrayContext();
 valueContext.writeValue();
 assertEquals("[,", arrayContext.toString());

 valueContext.writeValue("test");
 }

 @Test
 public void testObjectContextWriteValue() throws JsonProcessingException {
 JsonWriteContext parentContext = createContext(null);
 JsonWriteContext objectContext = parentContext.createChildObjectContext();
 objectContext.writeFieldName("field1");
 objectContext.writeValue("value1");
 objectContext.writeFieldName("field2");
 objectContext.writeValue("value2");
 objectContext.writeValue();
 assertEquals("{\"field1\":\"value1\",\"field2\":\"value2\"}", objectContext.toString());
 }

 @Test(expected = JsonProcessingException.class)
 public void testObjectContextWriteFieldNameTwice() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeFieldName("field1");
 context.writeFieldName("field1");
 }

 @Test(expected = JsonProcessingException.class)
 public void testObjectContextWriteValueWithoutFieldName() throws JsonProcessingException {
 JsonWriteContext context = createContext(null);
 context.writeValue();
 }

 @Test
 public void testDupDetector() throws JsonProcessingException {
 DupDetector dupDetector = new DupDetector();
 JsonWriteContext context = createContext(dupDetector);
 context.writeFieldName("field1");
 context.writeFieldName("field1");
 try {
 context.writeValue();
 } catch (JsonProcessingException e) {
 assertEquals("Duplicate field 'field1'", e.getMessage());
 }
 }
}