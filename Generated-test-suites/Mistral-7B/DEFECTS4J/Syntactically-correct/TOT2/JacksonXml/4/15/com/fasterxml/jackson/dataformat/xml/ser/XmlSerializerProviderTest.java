package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 private JsonGenerator genMock;

 @Before
 public void setup() {
 genMock = mock(JsonGenerator.class);
 provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}));
 }

 @Test
 public void testSerializeValueNull() throws JsonProcessingException {
 // Basic test case
 provider.serializeValue(genMock, null);
 verify(genMock, times(1)).writeStartObject();
 verify(genMock, times(1)).writeFieldName("null");
 verify(genMock, times(1)).writeEnd();

 // Edge case: root name from config
 provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}), new SerializationConfig(), new SerializerFactory());
 provider.setSerializationConfig(new SerializationConfig());
 provider.getSerializationConfig().setRootValueSeparator(":");
 provider.serializeValue(genMock, null);
 verify(genMock, times(1)).writeStartObject();
 verify(genMock, times(1)).writeFieldName("null:");
 verify(genMock, times(1)).writeEnd();
 }

 @Test
 public void testSerializeValuePrimitiveTypes() throws JsonProcessingException {
 provider.serializeValue(genMock, 42);
 verify(genMock, times(1)).writeNumber(42.0);

 provider.serializeValue(genMock, true);
 verify(genMock, times(1)).writeBoolean(true);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueInvalidType() throws JsonProcessingException {
 provider.serializeValue(genMock, new Object() {});
 }

 @Test
 public void testSerializeValueWithRootName() throws JsonProcessingException {
 provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}), new SerializationConfig(), new SerializerFactory());
 provider.serializeValue(genMock, "testValue");
 verify(genMock, times(1)).writeStartObject();
 verify(genMock, times(1)).writeFieldName("test");
 verify(genMock, times(1)).writeValue("testValue");
 verify(genMock, times(1)).writeEnd();
 }

 @Test
 public void testSerializeValueArray() throws JsonProcessingException {
 provider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"test"}), new SerializationConfig(), new SerializerFactory());
 Object[] values = new Object[]{1, 2, 3};
 provider.serializeValue(genMock, values);
 verify(genMock, times(1)).writeStartObject();
 verify(genMock, times(1)).writeFieldName("item");
 provider.serializeValue(genMock, values[0]);
 provider.serializeValue(genMock, values[1]);
 provider.serializeValue(genMock, values[2]);
 verify(genMock, times(1)).writeEnd();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueArrayWithoutRootName() throws JsonProcessingException {
 provider = new XmlSerializerProvider(new SerializerFactory());
 Object[] values = new Object[]{1, 2, 3};
 provider.serializeValue(genMock, values);
 }

 @Test
 public void testSerializeValueWithSerializer() throws JsonProcessingException {
 JsonSerializer<String> serializer = mock(JsonSerializer.class);
 when(serializer.serialize(any(Object.class), eq(genMock), any(JsonSerializerProvider.class)))
 .thenReturn(null);
 provider.setSerializers(new SimpleSerializerProvider(serializer));
 provider.serializeValue(genMock, "testValue");
 verify(serializer, times(1)).serialize(eq("testValue"), eq(genMock), any(JsonSerializerProvider.class));
 }

 @Test(expected = IOException.class)
 public void testSerializeValueIOException() throws JsonProcessingException, IOException {
 when(genMock.writeStartObject()).thenThrow(new IOException("Failed to start object"));
 provider.serializeValue(genMock, "testValue");
 }

 @Test(expected = XMLStreamException.class)
 public void testSerializeValueXmlStreamException() throws JsonProcessingException, XMLStreamException {
 when(genMock.setDefaultNamespace(anyString())).thenThrow(new XMLStreamException("Failed to set default namespace"));
 provider.serializeValue(genMock, "testValue");
 }
}