package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.JacksonXMLTestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class XmlSerializerProviderTest extends JacksonXMLTestBase {

 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectMapper objectMapper;

 @BeforeMethod
 public void setUp() {
 objectMapper = new XmlMapper();
 xmlSerializerProvider = objectMapper.getSerializerProvider().findValueSerializerProviderInstance(Object.class);
 }

 @Test
 public void testSerializeNullValue() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 String xml = writer.writeValueAsString(null);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";
 Assert.assertEquals(xml, expectedXml);

 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> serializer = provider.findValueSerializer(XmlSerializerProvider.class, null);
 ObjectWriter writerWithCustomProvider = provider.getSerializerProvider().getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null).writerForRootValue();
 String xmlWithCustomProvider = writerWithCustomProvider.writeValueAsString(null);
 Assert.assertEquals(xmlWithCustomProvider, expectedXml);
 }

 @Test
 public void testSerializePrimitiveTypes() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 int intValue = 123;
 SerializerProvider provider = xmlSerializerProvider;
 JsonSerializer<Integer> integerSerializer = provider.findValueSerializer(Integer.class, null);
 String xml = writer.writeValueAsString(intValue);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<int name=\"123\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"int\">123</int>";
 Assert.assertEquals(xml, expectedXml);

 long longValue = 456;
 JsonSerializer<Long> longSerializer = provider.findValueSerializer(Long.class, null);
 xml = writer.writeValueAsString(longValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<long name=\"456\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"long\">456</long>";
 Assert.assertEquals(xml, expectedXml);

 float floatValue = 789.12f;
 JsonSerializer<Float> floatSerializer = provider.findValueSerializer(Float.class, null);
 xml = writer.writeValueAsString(floatValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<float name=\"789.12\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"float\">789.12</float>";
 Assert.assertEquals(xml, expectedXml);

 double doubleValue = 1234.56;
 JsonSerializer<Double> doubleSerializer = provider.findValueSerializer(Double.class, null);
 xml = writer.writeValueAsString(doubleValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<double name=\"1234.56\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"double\">1234.56</double>";
 Assert.assertEquals(xml, expectedXml);

 boolean booleanValue = true;
 JsonSerializer<Boolean> booleanSerializer = provider.findValueSerializer(Boolean.class, null);
 xml = writer.writeValueAsString(booleanValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<boolean name=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"boolean\">true</boolean>";
 Assert.assertEquals(xml, expectedXml);

 char charValue = 'A';
 JsonSerializer<Character> charSerializer = provider.findValueSerializer(Character.class, null);
 xml = writer.writeValueAsString(charValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<char name=\"A\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"char\">A</char>";
 Assert.assertEquals(xml, expectedXml);

 short shortValue = 111;
 JsonSerializer<Short> shortSerializer = provider.findValueSerializer(Short.class, null);
 xml = writer.writeValueAsString(shortValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<short name=\"111\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"short\">111</short>";
 Assert.assertEquals(xml, expectedXml);

 byte byteValue = 127;
 JsonSerializer<Byte> byteSerializer = provider.findValueSerializer(Byte.class, null);
 xml = writer.writeValueAsString(byteValue);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<byte name=\"127\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"byte\">127</byte>";
 Assert.assertEquals(xml, expectedXml);

 byte[] byteArray = new byte[] {1, 2, 3};
 JsonSerializer<byte[]> byteArraySerializer = provider.findValueSerializer(byte[].class, null);
 xml = writer.writeValueAsString(byteArray);
 expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<byteArray name=\"[1, 2, 3]\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"byte[]\">[1, 2, 3]</byteArray>";
 Assert.assertEquals(xml, expectedXml);
 }

 @Test
 public void testSerializeObjectWithRootElement() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 Person person = new Person("John Doe", 30);
 String xml = writer.writeValueAsString(person);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Person>\n<name>John Doe</name>\n<age>30</age>\n</Person>";
 Assert.assertEquals(xml, expectedXml);

 SerializerProvider provider = new DefaultSerializerProvider();
 BeanSerializerFactory beanSerializerFactory = new BeanSerializerFactory();
 JsonSerializer<Person> personSerializer = beanSerializerFactory.findValueSerializer(Person.class, null);
 ObjectWriter writerWithCustomSerializer = provider.getSerializerProvider().getSerializerFactory()
 .createBeanSerializerFactory(new DefaultSerializerProvider(), null, null)
 .getSerializerProvider().getSerializerProvider()
 .getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null).writerForRootValue();
 Person personToSerialize = new Person("John Doe", 30);
 String xmlWithCustomSerializer = writerWithCustomSerializer.writeValueAsString(personToSerialize);
 Assert.assertEquals(xmlWithCustomSerializer, expectedXml);
 }

 @Test
 public void testSerializeArrayWithRootElement() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 int[] intArray = new int[] {1, 2, 3};
 String xml = writer.writeValueAsString(intArray);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<intArray name=\"[1, 2, 3]\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"int[]\">\n<int>1</int>\n<int>2</int>\n<int>3</int>\n</intArray>";
 Assert.assertEquals(xml, expectedXml);

 SerializerProvider provider = new DefaultSerializerProvider();
 ClassUtil.initClass(IntArraySerializer.class);
 JsonSerializer<int[]> intArraySerializer = provider.findValueSerializer(int[].class, null);
 ObjectWriter writerWithCustomSerializer = provider.getSerializerProvider().getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null).writerForRootValue();
 int[] intArrayToSerialize = new int[] {1, 2, 3};
 String xmlWithCustomSerializer = writerWithCustomSerializer.writeValueAsString(intArrayToSerialize);
 Assert.assertEquals(xmlWithCustomSerializer, expectedXml);
 }

 @Test
 public void testSerializeObjectWithCustomRootElement() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 Person person = new Person("John Doe", 30);
 String xml = writer.writeValueAsString(person);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Person>\n<name>John Doe</name>\n<age>30</age>\n</Person>";

 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Person> personSerializer = provider.findValueSerializer(Person.class, null);
 ObjectWriter writerWithCustomRootElement = provider.getSerializerProvider().getSerializerFactory()
 .createBeanSerializerFactory(new DefaultSerializerProvider(), null, null)
 .getSerializerProvider().getSerializerProvider()
 .getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null)
 .writerForValue(new PropertyName("person"));
 String xmlWithCustomRootElement = writerWithCustomRootElement.writeValueAsString(person);
 String expectedXmlWithCustomRootElement = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<person>\n<Person>\n<name>John Doe</name>\n<age>30</age>\n</Person>\n</person>";
 Assert.assertEquals(xmlWithCustomRootElement, expectedXmlWithCustomRootElement);
 }

 @Test
 public void testSerializeObjectWithEmptyName() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 Person person = new Person("John Doe", 30);
 String xml = writer.writeValueAsString(person);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Person>\n<name>John Doe</name>\n<age>30</age>\n</Person>";

 SerializerProvider provider = new DefaultSerializerProvider();
 ObjectWriter writerWithEmptyName = provider.getSerializerProvider().getSerializerFactory()
 .createBeanSerializerFactory(new DefaultSerializerProvider(), null, null)
 .getSerializerProvider().getSerializerProvider()
 .getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null)
 .writerForValue(new PropertyName(""));
 String xmlWithEmptyName = writerWithEmptyName.writeValueAsString(person);
 String expectedXmlWithEmptyName = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Person>\n<name></name>\n<age>30</age>\n</Person>";
 Assert.assertEquals(xmlWithEmptyName, expectedXmlWithEmptyName);
 }

 @Test
 public void testSerializeNullRootName() throws JsonProcessingException {
 ObjectWriter writer = objectMapper.writerForXml();
 String xml = writer.writeValueAsString(null);
 String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";

 SerializerProvider provider = new DefaultSerializerProvider();
 JsonSerializer<Object> serializer = provider.findValueSerializer(Object.class, null);
 ObjectWriter writerWithNullRootName = provider.getSerializerProvider().getSerializerFactory()
 .createXmlMapper(new DefaultSerializerProvider(), null).writerForRootValue();
 String xmlWithNullRootName = writerWithNullRootName.writeValueAsString(null);
 Assert.assertEquals(xmlWithNullRootName, expectedXml);
 }
}

class IntArraySerializer extends StaxXmlSerializer {
 @Override
 public void serialize(int[] v1, JsonGenerator jg0, SerializerProvider sp) throws IOException {
 jg0.writeStartElement("intArray");
 jg0.writeFieldName("name");
 jg0.writeValue("intArray");
 jg0.writeFieldName("value");
 for (int value : v1) {
 jg0.writeStartElement("int");
 jg0.writeValue(value);
 jg0.writeEndElement();
 }
 jg0.writeEndElement();
 }
}

class Person {
 private final String name;
 private final int age;

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }
}