package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyFilter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.StdSerializer;
import com.fasterxml.jackson.databind.ser.impl.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.test.BaseTest;

public class StdSerializerTest extends BaseTest {

  private static class TestSerializer extends StdSerializer<String> {

    public TestSerializer() {
      super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonGenerationException {
      jgen.writeString(value);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
        throws JsonMappingException {
      return createSchemaNode("string");
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
        throws JsonMappingException {
      return createSchemaNode("string", isOptional);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException {
      visitor.expectAnyFormat(typeHint);
    }
  }

  @SuppressWarnings("unchecked")
  private static TestSerializer createTestSerializer() {
    return new TestSerializer();
  }

  @org.junit.Test
  public void testSerialization() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TestSerializer serializer = createTestSerializer();
    String value = "test";
    JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), ObjectMapper.DefaultTyping.JAVA_LANG_STRING);
    serializer.serialize(value, gen, null);
    gen.flush();
    byte[] serializedData = gen.getOutputBytes();
    assertEquals("test", new String(serializedData));
  }

  @org.junit.Test
  public void testGetSchema() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TestSerializer serializer = createTestSerializer();
    JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), null);
    assertEquals("string", schema.get("type").asText());
  }

  @org.junit.Test
  public void testGetSchemaOptional() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TestSerializer serializer = createTestSerializer();
    JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), null, true);
    assertEquals("string", schema.get("type").asText());
    assertNull(schema.get("required"));
  }

  @org.junit.Test
  public void testAcceptJsonFormatVisitor() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TestSerializer serializer = createTestSerializer();
    // Create a mock JsonFormatVisitorWrapper
    JsonFormatVisitorWrapper visitorWrapper = mock(JsonFormatVisitorWrapper.class);
    when(visitorWrapper.expectAnyFormat(any(JavaType.class))).thenReturn(null);
    serializer.acceptJsonFormatVisitor(visitorWrapper, null);
  }

  @org.junit.Test
  public void testIsDefaultSerializer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonSerializer<String> serializer = mapper.findValueSerializer(String.class);
    assertTrue(StdSerializer.class.isInstance(serializer));
    assertTrue(StdSerializer.class.isInstance(serializer));
    assertTrue(StdSerializer.class.isInstance(serializer));
    assertTrue(StdSerializer.class.isInstance(serializer));
    assertFalse(StdSerializer.class.isInstance(new ObjectSerializer()));
  }

  @org.junit.Test
  public void testFindConvertingContentSerializer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    TestSerializer serializer = createTestSerializer();
    BeanProperty property = mock(BeanProperty.class);
    Converter<Object, Object> converter = mock(Converter.class);
    when(converter.getOutputType(mock(JsonTypeFactory.class))).thenReturn(String.class);
    when(mapper.converterInstance(property, eq(Converter.class))).thenReturn(converter);

    JsonSerializer<Object> existingSerializer = mock(JsonSerializer.class);
    JsonSerializer<String> result = serializer.findConvertingContentSerializer(mapper.getSerializerProvider(), property, existingSerializer);

    assertTrue(result instanceof StdDelegatingSerializer);
    assertEquals(String.class, result.handledType());
    assertEquals(existingSerializer, ((StdDelegatingSerializer) result).getDelegate());
  }

  @org.junit.Test
  public void testFindPropertyFilter() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    SerializerProvider serializerProvider = mapper.getSerializerProvider();
    TestSerializer serializer = createTestSerializer();
    PropertyFilter filter = serializer.findPropertyFilter(serializerProvider, "testFilterId", mapper.readTree("{}"));
    assertNull(filter);
  }

  @org.junit.Test
  public void testFindPropertyFilterWithPropertyFilter() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    FilterProvider filterProvider = new FilterProvider();
    filterProvider.setFilterId("testFilterId", new PropertyFilter() {
      @Override
      public void serializeAsValue(SerializerProvider p, JsonGenerator jg, Object filterable)
          throws IOException, JsonProcessingException {
        jg.writeString("test");
      }
    });
    mapper.setFilterProvider(filterProvider);
    TestSerializer serializer = createTestSerializer();
    PropertyFilter filter = serializer.findPropertyFilter(mapper.getSerializerProvider(), "testFilterId", mapper.readTree("{}"));
    assertNotNull(filter);
  }
}

class ObjectSerializer{}