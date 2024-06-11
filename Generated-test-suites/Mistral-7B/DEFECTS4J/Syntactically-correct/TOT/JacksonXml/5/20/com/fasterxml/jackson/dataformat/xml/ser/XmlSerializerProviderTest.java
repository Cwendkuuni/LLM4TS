import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlMapper mapper;

 @Before
 public void setup() {
 mapper = new XmlMapper();
 }

 @Test
 public void testSerializeNull() throws IOException {
 XmlSerializerProvider xmlProvider = mapper.getSerializerProvider();
 ObjectNode nullNode = mapper.createObjectNode();
 JsonGenerator gen = xmlProvider.getGenerator();
 xmlProvider.serializeValue(gen, null);
 String result = gen.getOutputFactory().writeValueAsString(gen.getBuffer());
 assertEquals("<null/>", result);
 }

 @Test
 public void testSerializeObjectWithRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Person.class);
 ObjectNode personNode = mapper.createObjectNode();
 Person person = new Person("John Doe", 30);
 JsonSerializer<Person> serializer = mapper.findValueSerializer(Person.class, null);
 JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
 SimpleBeanPropertyFilter filter = introspector.findFilterInstance(Person.class, null, null, null, null, null);
 serializer = serializer.withFilter(filter);
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(xmlProvider);
 xmlProvider.serializeValue(personNode, person);
 String result = personNode.toString();
 assertEquals("<person><name>John Doe</name><age>30</age></person>", result);
 }

 @Test
 public void testSerializeObjectWithoutRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Person.class);
 ObjectNode personNode = mapper.createObjectNode();
 Person person = new Person("John Doe", 30);
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writeValue(personNode, person);
 String result = personNode.toString();
 assertTrue(result.startsWith("<person>"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithInvalidRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Person.class);
 ObjectNode personNode = mapper.createObjectNode();
 Person person = new Person("John Doe", 30);
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writeValue(personNode, person);
 fail("Expected JsonMappingException but got: " + personNode.toString());
 }

 @Test
 public void testSerializeArray() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(new int[0].getClass().getComponentType(), int[].class);
 int[] intArray = {1, 2, 3};
 ObjectNode arrayNode = mapper.createObjectNode();
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writeValue(arrayNode, intArray);
 String result = arrayNode.toString();
 assertEquals("<int-array><int>1</int><int>2</int><int>3</int></int-array>", result);
 }

 @Test
 public void testSerializeArrayWithEmptyArray() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 int[] intArray = {};
 ObjectNode arrayNode = mapper.createObjectNode();
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writeValue(arrayNode, intArray);
 String result = arrayNode.toString();
 assertEquals("<int-array/>", result);
 }

 @Test
 public void testSerializeArrayWithNullElements() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 int[] intArray = {1, null, 3};
 ObjectNode arrayNode = mapper.createObjectNode();
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writereset();
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.writereset();
 mapper.writeValue(arrayNode, intArray);
 String result = arrayNode.toString();
 assertEquals("<int-array><int>1</int><int>null</int><int>3</int></int-array>", result);
 }

 @Test
 public void testSerializeArrayWithEmptyArrayAndNullElements() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 int[] intArray = {null};
 ObjectNode arrayNode = mapper.createObjectNode();
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writereset();
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 mapper.writereset();
 mapper.writeValue(arrayNode, intArray);
 String result = arrayNode.toString();
 assertEquals("<int-array/>", result);
 }

 @Test
 public void testSerializeBeanWithToStringSerializer() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(Person.class);
 Person person = new Person("John Doe", 30);
 ObjectNode personNode = mapper.createObjectNode();
 JsonSerializer<Person> serializer = mapper.findValueSerializer(Person.class, null);
 serializer = serializer.withSerializerProperties(new PropertyName("serializationFilter"), new SimpleFilterProvider().filter("toString", new ToStringSerializer(Person.class)));
 mapper.setSerializerProvider(new XmlSerializerProvider(mapper.getRootValueSerializer(), mapper.getSerializationConfig(), mapper.getDefaultSerializerFactory()));
 mapper.setSerializerProvider(mapper.getDefaultSerializerProvider());
 mapper.writeValue(personNode, person);
 String result = personNode.toString();
 assertEquals("<person><name>John Doe</name><age>30</age></person>", result);
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