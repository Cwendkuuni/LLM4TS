import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

 private DefaultPrettyPrinter prettyPrinter;
 private JsonGenerator jsonGenerator;

 @Before
 public void setup() {
 prettyPrinter = new DefaultPrettyPrinter();
 jsonGenerator = new JsonFactory().createJsonGenerator(new StringWriter());
 }

 @Test
 public void testWriteRootValueSeparator() throws IOException {
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("", result); // No separator is printed when null is passed

 String rootSeparator = "test";
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 prettyPrinter.writeRootValueSeparator(jsonGenerator);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals(rootSeparator, result);
 }

 @Test
 public void testWriteStartObject() throws IOException {
 prettyPrinter.writeStartObject(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{", result);
 }

 @Test
 public void testWriteObjectFieldValueSeparator() throws IOException {
 prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("", result); // No separator is printed by default

 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter._spacesInObjectEntries = false;
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals(":", result);

 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter._spacesInObjectEntries = true;
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals(" :", result);
 }

 @Test
 public void testWriteObjectEntrySeparator() throws IOException {
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("", result); // No separator is printed by default

 result = jsonGenerator.getOutputBuffer().toString();
 jsonGenerator.writeString("field1");
 prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals(":", result);
 }

 @Test
 public void testWriteEndObject() throws IOException {
 prettyPrinter.writeEndObject(jsonGenerator, 0);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("}", result);

 prettyPrinter.writeStartObject(jsonGenerator);
 prettyPrinter.writeFieldName("field1");
 jsonGenerator.writeString("value1");
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\"field1\":\"value1\"}", result);
 }

 @Test
 public void testWriteStartArray() throws IOException {
 prettyPrinter.writeStartArray(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("[", result);
 }

 @Test
 public void testWriteArrayValueSeparator() throws IOException {
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("", result); // No separator is printed by default

 result = jsonGenerator.getOutputBuffer().toString();
 jsonGenerator.writeNumber(1);
 prettyPrinter.writeArrayValueSeparator(jsonGenerator);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals(",", result);
 }

 @Test
 public void testWriteEndArray() throws IOException {
 prettyPrinter.writeEndArray(jsonGenerator, 0);
 String result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("]", result);

 prettyPrinter.writeStartArray(jsonGenerator);
 jsonGenerator.writeNumber(1);
 prettyPrinter.writeEndArray(jsonGenerator, 1);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("[1]", result);
 }

 @Test
 public void testWithRootSeparator() throws IOException {
 String rootSeparator = "test";
 prettyPrinter = new DefaultPrettyPrinter(new SerializedString(rootSeparator));
 prettyPrinter.writeStartObject(jsonGenerator);
 jsonGenerator.writeString("field1");
 jsonGenerator.writeRaw(":");
 jsonGenerator.writeString("value1");
 jsonGenerator.writeEndObject(jsonGenerator);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("{\"field1\":\"value1\"}", result);
 result = result.substring(0, result.length() - 1); // Remove trailing '}'
 assertEquals(rootSeparator, result.substring(0, result.indexOf(":")));
 }

 @Test
 public void testWithObjectIndenter() throws IOException {
 Indenter objectIndenter = new Indenter() {
 @Override public void writeIndentation(JsonGenerator g, int level) throws IOException { g.writeRaw("\t"); }
 @Override public boolean isInline() { return false; }
 };
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter._objectIndenter = objectIndenter;
 prettyPrinter.writeStartObject(jsonGenerator);
 jsonGenerator.writeFieldName("field1");
 jsonGenerator.writeString("value1");
 prettyPrinter.writeEndObject(jsonGenerator, 1);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("\t{\"field1\":\"value1\"}\n", result);
 }

 @Test
 public void testWithArrayIndenter() throws IOException {
 Indenter arrayIndenter = new Indenter() {
 @Override public void writeIndentation(JsonGenerator g, int level) throws IOException { g.writeRaw("\t"); }
 @Override public boolean isInline() { return false; }
 };
 prettyPrinter = new DefaultPrettyPrinter();
 prettyPrinter._arrayIndenter = arrayIndenter;
 prettyPrinter.writeStartArray(jsonGenerator);
 jsonGenerator.writeNumber(1);
 jsonGenerator.writeNumber(2);
 prettyPrinter.writeEndArray(jsonGenerator, 2);
 result = jsonGenerator.getOutputBuffer().toString();
 assertEquals("\t[1,\n\t\t2]\n", result);
 }
}