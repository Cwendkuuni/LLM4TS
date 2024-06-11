import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

 @Test
 public void testDefaultPrettyPrinterConstruction() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 assertNotNull(prettyPrinter);
 }

 @Test
 public void testDefaultPrettyPrinterWithRootSeparator() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("test");
 assertNotNull(prettyPrinter);
 assertEquals("test", prettyPrinter.getRootSeparator().asString());
 }

 @Test
 public void testDefaultPrettyPrinterWithNullRootSeparator() {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(null);
 assertNull(prettyPrinter.getRootSeparator());
 }

 @Test
 public void testWriteRootValueSeparator() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertNull(jsonGenerator.getOutputBuffer().toString());

 prettyPrinter = new DefaultPrettyPrinter("test");
 jsonGenerator.writeStartObject();
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 assertEquals("test", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteStartObject() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 prettyPrinter.writeStartObject(jsonGenerator);
 assertEquals("{", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteObjectEntrySeparator() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 jsonGenerator.writeStartObject();
 prettyPrinter.beforeObjectEntries(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());

 jsonGenerator.writeFieldName("test");
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 assertEquals(" :", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteObjectEntries() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 jsonGenerator.writeStartObject();
 prettyPrinter.beforeObjectEntries(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());

 jsonGenerator.writeFieldName("test1");
 jsonGenerator.writeValue("value1");
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 jsonGenerator.writeFieldName("test2");
 jsonGenerator.writeValue("value2");
 jsonGenerator.writeEndObject();

 assertEquals("{\n  \"test1\": \"value1\",\n  \"test2\": \"value2\"\n}", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteStartArray() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 prettyPrinter.writeStartArray(jsonGenerator);
 assertEquals("[", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteArrayValueSeparator() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 jsonGenerator.writeStartArray();
 prettyPrinter.beforeArrayValues(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());

 jsonGenerator.writeValue("value1");
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 assertEquals(",\n  ", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.writeValue("value2");
 jsonGenerator.writeEndArray();

 assertEquals("[\"value1\",\"value2\"]", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }

 @Test
 public void testWriteArrayValues() throws IOException {
 DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
 JsonGenerator jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 jsonGenerator.writeStartArray();
 prettyPrinter.beforeArrayValues(jsonGenerator);
 assertEquals("", jsonGenerator.getOutputBuffer().toString());

 jsonGenerator.writeValue("value1");prettyPrinter.writeArrayValueSeparator(jsonGenerator);jsonGenerator.writeValue("value2");jsonGenerator.writeEndArray();

 assertEquals("[\"value1\",\"value2\"]", jsonGenerator.getOutputBuffer().toString());
 jsonGenerator.close();
 }
}