package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.test.*;
import com.fasterxml.jackson.dataformat.xml.*;

import org.testng.annotations.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 private ObjectMapper mapper;

 @BeforeClass
 public void setup() throws Exception {
 mapper = new ObjectMapper(new XMLFactory());
 provider = mapper.getXmlSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws IOException {
 String xml = provider.serializeValueToXml(new Object(), new StringWriter());
 assertEquals("<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", xml);
 }

 @Test
 public void testSerializePrimitive() throws IOException {
 int value = 123;
 String xml = provider.serializeValueToXml(value, new StringWriter());
 assertEquals("<int>123</int>", xml);

 float floatValue = 123.45f;
 xml = provider.serializeValueToXml(floatValue, new StringWriter());
 assertEquals("<float>123.45</float>", xml);
 }

 @Test
 public void testSerializeObject() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setNumber(123);
 String xml = provider.serializeValueToXml(obj, new StringWriter());
 assertEquals("<myClass><name>Test</name><number>123</number></myClass>", xml);
 }

 @Test
 public void testSerializeList() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
 List<Integer> list = new ArrayList<>();
 list.add(1);
 list.add(2);
 String xml = provider.serializeValueToXml(list, new StringWriter());
 assertEquals("<array name=\"item\" size=\"2\">\n<int>1</int>\n<int>2</int>\n</array>", xml);
 }

 @Test
 public void testSerializeFilteredObject() throws IOException {
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setNumber(123);
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("number");
 String xml = mapper.writerWithView(filter).writeValueAsString(obj);
 String expectedXml = "<myClass xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><name>Test</name></myClass>";
 assertEquals(expectedXml, xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeInvalidType() throws IOException {
 String xml = provider.serializeValueToXml(new Object(), new StringWriter());
 fail("Expected JsonMappingException when trying to serialize invalid type");
 }

 @Test(expected = IOException.class)
 public void testSerializeIOException() throws IOException {
 StringWriter writer = new StringWriter();
 writer.close();
 String xml = provider.serializeValueToXml(new Object(), writer);
 fail("Expected IOException when trying to write to closed StringWriter");
 }

 @After
 public void tearDown() throws Exception {
 mapper = null;
 provider = null;
 }
}

class MyClass {
 private String name;
 private int number;

 // getters and setters
}