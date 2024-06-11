package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.*;
import org.testng.annotations.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlProvider;
 private ObjectMapper mapper;
 private ObjectNode rootNode;

 @BeforeMethod
 public void setup() throws Exception {
 SerializationConfig config = new SerializationConfig();
 XmlMapper xmlMapper = new XmlMapper(new XmlSerializerProviderFactory());
 mapper = xmlMapper.configure(config, new SimpleModule());
 xmlProvider = mapper.getDefaultSerializerProvider();
 rootNode = TestUtil.createEmptyObjectNode();
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.getRootValueSeparator()).thenReturn("");
 when(gen.writeFieldName("item")).thenReturn(rootNode);
 when(gen.writeNull()).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(null);

 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), xmlProvider);
 provider.serializeValue(gen, null);

 verify(gen, times(2)).writeStartObject();
 verify(gen, times(1)).writeFieldName("item");
 verify(gen, times(1)).writeNull();
 verify(gen, times(1)).writeEndObject();
 }

 @Test
 public void testSerializeObject() throws IOException {
 Object obj = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.getRootValueSeparator()).thenReturn("");
 when(gen.writeFieldName("item")).thenReturn(rootNode);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("name")).thenReturn(rootNode);
 when(gen.writeValue("value")).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(null);

 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), xmlProvider);
 provider.serializeValue(gen, obj);

 assertEquals("name", rootNode.get("item").get("name").asText());
 assertEquals("value", rootNode.get("item").get("value").asText());
 }

 @Test
 public void testSerializeArray() throws IOException {
 Object[] array = new Object[] {new Object(), null, new Object()};
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.getRootValueSeparator()).thenReturn(" ");
 when(gen.writeFieldName("item")).thenReturn(rootNode);
 when(gen.writeStartArray()).thenReturn(rootNode);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("name")).thenReturn(rootNode);
 when(gen.writeValue("value")).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(rootNode);
 when(gen.writeNull()).thenReturn(rootNode);
 when(gen.writeEndArray()).thenReturn(null);
 when(gen.writeEndObject()).thenReturn(null);

 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), xmlProvider);
 provider.serializeValue(gen, array);

 ObjectNode arrayNode = rootNode.get("item");
 assertEquals(2, arrayNode.size());
 assertEquals("name", arrayNode.get(0).get("name").asText());
 assertEquals("value", arrayNode.get(0).get("value").asText());
 assertNull(arrayNode.get(1));
 }

 @Test
 public void testSerializeCustomRootName() throws IOException {
 Object obj = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.getRootValueSeparator()).thenReturn("");
 when(gen.writeFieldName("item")).thenReturn(rootNode);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("name")).thenReturn(rootNode);
 when(gen.writeValue("value")).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(null);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("customRootName")).thenReturn(rootNode);
 when(gen.setNextName("customRootName")).thenReturn(true);
 when(gen.initGenerator()).thenReturn(null);

 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), xmlProvider);
 SerializerProvider oldProvider = xmlProvider;
 xmlProvider = provider;
 try {
 ObjectNode result = mapper.writeValueAsTree(obj);
 assertEquals("customRootName", result.get("customRootName").getName());
 } finally {
 xmlProvider = oldProvider;
 }
}

 @Test
 public void testSerializeArrayWithCustomRootName() throws IOException {
 Object[] array = new Object[] {new Object(), null, new Object()};
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.getRootValueSeparator()).thenReturn(" ");
 when(gen.writeFieldName("item")).thenReturn(rootNode);
 when(gen.writeStartArray()).thenReturn(rootNode);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("name")).thenReturn(rootNode);
 when(gen.writeValue("value")).thenReturn(rootNode);
 when(gen.writeEndObject()).thenReturn(rootNode);
 when(gen.writeNull()).thenReturn(rootNode);
 when(gen.writeEndArray()).thenReturn(null);
 when(gen.writeEndObject()).thenReturn(null);
 when(gen.writeStartObject()).thenReturn(rootNode);
 when(gen.writeString("customRootName")).thenReturn(rootNode);
 when(gen.setNextName("customRootName")).thenReturn(true);
 when(gen.initGenerator()).thenReturn(null);
 when(gen.writeStartArray()).thenReturn(rootNode);

 XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(), xmlProvider);
 SerializerProvider oldProvider = xmlProvider;
 xmlProvider = provider;
 try {
 ObjectNode result = mapper.writeValueAsTree(array);
 assertEquals("customRootName", result.get("customRootName").getName());
 ObjectNode arrayNode = result.get("customRootName").get("item");
 assertEquals(2, arrayNode.size());
 assertEquals("name", arrayNode.get(0).get("name").asText());
 assertEquals("value", arrayNode.get(0).get("value").asText());
 assertNull(arrayNode.get(1));
 } finally {
 xmlProvider = oldProvider;
 }
}
}