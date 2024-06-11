import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 SimpleModule module = new SimpleModule();
 mapper = new ObjectMapper();
 mapper.registerModule(module);
 }

 @Test
 public void testStringJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 StringTestBean stringTestBean = new StringTestBean("Test String");
 String accessorValue = stringTestBean.getValue();
 JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.valueToTree(stringTestBean);
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 assertEquals("Test String", valueSerializer.getValueAsString(serializerForJsonNode.getSerializableValue()));
 }

 @Test
 public void testIntegerJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 IntegerTestBean integerTestBean = new IntegerTestBean(123);
 Integer accessorValue = integerTestBean.getValue();
 JsonValueSerializer serializer = mapper.findValueSerializer(Integer.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.valueToTree(integerTestBean);
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 assertEquals(123, valueSerializer.getValueAsInt(serializerForJsonNode.getSerializableValue()));
 }

 @Test
 public void testIntegerArrayJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 IntegerArrayTestBean integerArrayTestBean = new IntegerArrayTestBean(new int[]{1, 2, 3});
 Integer[] accessorValue = integerArrayTestBean.getValue();
 JsonValueSerializer serializer = mapper.findValueSerializer(int[].class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.valueToTree(integerArrayTestBean);
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 int[] deserializedValue = valueSerializer.getValueAsIntArray(serializerForJsonNode.getSerializableValue());
 assertNotNull(deserializedValue);
 assertEquals(3, deserializedValue.length);
 assertEquals(1, deserializedValue[0]);
 assertEquals(2, deserializedValue[1]);
 assertEquals(3, deserializedValue[2]);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 JsonValueSerializer serializer = mapper.findValueSerializer(String.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.createObjectNode();
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 valueSerializer.serialize(null, serializerForJsonNode, new SerializerProvider());
 }

 @Test(expected = JsonMappingException.class)
 public void testUnregisteredJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 Class<?> unregisteredClass = new Class<?>(){};
 JsonValueSerializer serializer = mapper.findValueSerializer(unregisteredClass, null);
 assertNull(serializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnsupportedJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 JsonValueSerializer serializer = mapper.findValueSerializer(Object.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.createObjectNode();
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 valueSerializer.serialize(new Object(), serializerForJsonNode, new SerializerProvider());
 }

 @Test
 public void testEnumJsonValueSerializer() throws JsonMappingException, JsonGenerationException {
 MyEnumTestBean enumTestBean = new MyEnumTestBean(MyEnum.VALUE_1);
 MyEnum accessorValue = enumTestBean.getValue();
 JsonValueSerializer serializer = mapper.findValueSerializer(MyEnum.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.valueToTree(enumTestBean);
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 assertEquals(MyEnum.VALUE_1, valueSerializer.getValueAsEnum(MyEnum.class, serializerForJsonNode.getSerializableValue()));
 }

 @Test
 public void testEnumJsonValueSerializerWithCustomSerializer() throws JsonMappingException, JsonGenerationException {
 MyEnumTestBeanWithCustomSerializer enumTestBean = new MyEnumTestBeanWithCustomSerializer(MyEnum.VALUE_1);
 MyEnum accessorValue = enumTestBean.getValue();
 SimpleModule module = new SimpleModule();
 module.addSerializer(MyEnum.class, new MyEnumSerializer());
 mapper.registerModule(module);
 JsonValueSerializer serializer = mapper.findValueSerializer(MyEnum.class, null);
 JsonSerializer<Object> valueSerializer = serializer.getValueSerializer();
 ObjectNode jsonNode = mapper.valueToTree(enumTestBean);
 JsonSerializer<Object> serializerForJsonNode = jsonNode.getSerializer();
 assertEquals(MyEnum.VALUE_1, valueSerializer.getValueAsEnum(MyEnum.class, serializerForJsonNode.getSerializableValue()));
 }
}

class StringTestBean {
 private String value;

 public StringTestBean(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}

class IntegerTestBean {
 private int value;

 public IntegerTestBean(int value) {
 this.value = value;
 }

 public int getValue() {
 return value;
 }
}

class IntegerArrayTestBean {
 private int[] value;

 public IntegerArrayTestBean(int[] value) {
 this.value = value;
 }

 public int[] getValue() {
 return value;
 }
}

enum MyEnum {
 VALUE_1,
 VALUE_2,
 VALUE_3
}

class MyEnumSerializer extends JsonSerializer<MyEnum> {
 @Override
 public void serialize(MyEnum enumValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
 jsonGenerator.writeStringValue(enumValue.name());
 }
}

class MyEnumTestBean {
 private MyEnum value;

 public MyEnumTestBean(MyEnum value) {
 this.value = value;
 }

 public MyEnum getValue() {
 return value;
 }
}

class MyEnumTestBeanWithCustomSerializer {
 private MyEnum value;

 public MyEnumTestBeanWithCustomSerializer(MyEnum value) {
 this.value = value;
 }

 public MyEnum getValue() {
 return value;
 }
}