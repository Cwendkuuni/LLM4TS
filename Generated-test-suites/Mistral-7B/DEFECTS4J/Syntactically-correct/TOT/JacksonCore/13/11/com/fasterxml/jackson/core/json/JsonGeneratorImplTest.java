package com.fasterxml.jackson.core.json; // or the package where JsonGeneratorImpl is located

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonGeneratorImplTest {

 private IOContext ioContext;
 private ObjectCodec objectCodec;
 private JsonGenerator jsonGenerator;

 @Before
 public void setup() throws JacksonAnnotationIntrospector.FindAnnotationWithValue {
 ioContext = new IOContext();
 objectCodec = new ObjectMapper().getFactory().createObjectCodec();
 jsonGenerator = objectCodec.autowired(JsonGeneratorImpl.class, ioContext, Feature.AUTO_CLOSE_TARGET.getMask(), objectCodec);
 }

 @Test
 public void testDefaultConfiguration() throws IOException {
 JsonFactory factory = new JsonFactory();
 JsonGenerator generator = factory.createGenerator(new StringWriter());
 // Use JsonGeneratorImpl's test methods to validate default configuration
 generator.writeStartObject();
 generator.writeStringField("name", "John Doe");
 generator.writeEndObject();
 generator.close();

 String result = generator.getOutput().toString();
 assertEquals("{\"name\":\"John Doe\"}", result);
 }

 @Test
 public void testQuoteFieldNames() throws IOException {
 JsonGenerator generator = jsonGenerator.enabled(Feature.QUOTE_FIELD_NAMES);
 generator.writeStartObject();
 generator.writeStringField("name", "John Doe"); // no quotes
 generator.writeEndObject();
 generator.close();

 String result = generator.getOutput().toString();
 assertEquals("{\"name\":\"John Doe\"}", result);

 generator = jsonGenerator.disabled(Feature.QUOTE_FIELD_NAMES);
 generator.writeStartObject();
 generator.writeStringField("name", "John Doe"); // no quotes
 generator.writeEndObject();
 generator.close();

 result = generator.getOutput().toString();
 assertEquals("{name:\"John Doe\"}", result);
 }

 @Test
 public void testOutputEscapes() throws IOException {
 CharacterEscapes characterEscapes = new StandardCharacterEscapes();
 JsonGenerator generator = jsonGenerator.setCharacterEscapes(characterEscapes);
 generator.setHighestEscapedChar(123); // <- set a custom escaping character
 generator.writeStartObject();
 generator.writeStringField("name", "\u007B"); // use the custom escaping character
 generator.writeEndObject();
 generator.close();

 String result = generator.getOutput().toString();
 assertEquals("{\"name\":\"\\u007B\"}", result);
 }

 @Test
 public void testNullValues() throws IOException {
 JsonGenerator generator = jsonGenerator;
 generator.writeStartObject();
 generator.writeNullField("name");
 generator.writeEndObject();
 generator.close();

 String result = generator.getOutput().toString();
 assertEquals("{% null %}", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConfiguration() throws IOException {
 JsonGenerator generator = jsonGenerator;
 // Try to set invalid configurations
 generator.setHighestEscapedChar(-1);
 generator.writeStartObject();
 generator.writeFieldName("name"); // no value
 generator.writeEndObject();
 generator.close();
 }
}