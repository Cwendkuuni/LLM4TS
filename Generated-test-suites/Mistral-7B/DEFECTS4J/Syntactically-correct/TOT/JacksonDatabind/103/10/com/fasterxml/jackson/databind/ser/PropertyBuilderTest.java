import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private SerializationConfig config;
 private BeanDescription beanDesc;
 private Object defaultBean;

 @Before
 public void setup() throws JsonMappingException {
 config = new SerializationConfig(new AnnotationIntrospector());
 ObjectMapper mapper = new ObjectMapper(config);
 BeanFactory beanFactory = new BeanFactory();
 defaultBean = beanFactory.getBean(TestBean.class);
 beanDesc = mapper.getTypeFactory().constructType(TestBean.class).getBeanDescription();
 }

 @Test
 public void testBuildWriterWithDefaultValues() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 assertEquals("defaultValue", "defaultValue", writer.getValueProvider().get(defaultBean, "defaultValue"));
 }

 @Test
 public void testBuildWriterWithCustomValue() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 Object customValue = new Object();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false).withValueProvider(new StaticValueProvider(customValue));
 assertEquals("customValue", customValue, writer.getValueProvider().get(defaultBean, "defaultValue"));
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullValue() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 try {
 writer.getValueProvider().get(defaultBean, "defaultValue");
 } catch (JsonProcessingException e) {
 fail("Should have thrown JsonMappingException");
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNullProperty() {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 try {
 writer.getValueProvider().get(null, "nonExistentProperty");
 } catch (JsonMappingException e) {
 assertEquals("noSuchProperty('nonExistentProperty')", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithNonSerializableProperty() {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 try {
 writer.getValueProvider().get(defaultBean, "nonSerializableProperty");
 } catch (JsonMappingException e) {
 assertEquals("class [Ljava.lang.Object; cannot be instantiated value for property [Ljava.lang.Object; of type class com.fasterxml.jackson.databind.PropertyBuilder]", e.getMessage());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testBuildWriterWithUnsupportedType() {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, TestUnsupportedType.class.getType(), null, null, null, null, false);
 try {
 writer.getValueProvider().get(defaultBean, "unsupportedProperty");
 } catch (JsonMappingException e) {
 assertEquals("No serializer found for class com.fasterxml.jackson.databind.PropertyBuilder$TestUnsupportedType and no properties discovered to serialize", e.getMessage());
 }
 }

 @Test
 public void testBuildWriterWithCustomSerializer() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 JsonSerializer<String> customSerializer = new StubSerializer();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false)
 .withSerializer(new BeanSerializerFactory.BeanPropertySerializerModifier() {
 @Override public JsonSerializer<?> modifySerializer(SerializerProvider serializers, BeanPropertyWriter writer, BeanPropertyDefinition propDef, JavaType declaredType) {
 return customSerializer;
 }
 });
 assertEquals("customSerializerValue", "customSerializerValue", writer.getValueProvider().get(defaultBean, "defaultValue"));
 }

 @Test
 public void testBuildWriterWithNullSerializationType() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 BeanPropertyWriter writerWithEmpty = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false).withoutContentTypeSerializer();
 assertNotSame(writer, writerWithEmpty);
 }

 @Test
 public void testBuildWriterWithEmptyContentTypeSerializer() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false).withoutContentTypeSerializer();
 BeanPropertyWriter writerWithEmpty = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false).withContentTypeSerializer(TypeSerializers.emptySerializer());
 assertNotSame(writer, writerWithEmpty);
 }

 @Test
 public void testBuildWriterWithDefaultInclusion() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false);
 assertEquals(JsonInclude.Include.DEFAULT_INCLUDE, writer.getSerializationConfig().getDefaultPropertyInclusion());
 }

 @Test
 public void testBuildWriterWithExplicitInclusion() throws JsonMappingException {
 SerializerProvider prov = new StandardSerializerProvider();
 BeanPropertyWriter writer = new PropertyBuilder(config, beanDesc).buildWriter(prov, null, Object.class.getType(), null, null, null, null, false)
 .withSerializationConfig(new SerializationConfig(new AnnotationIntrospector()) {
 @Override
 public JsonInclude.Value findPropertyInclusion(BeanPropertyDefinition property) {
 return JsonInclude.Value.includeAnnotations(property.getAnnotationMembers());
 }
 });
 assertEquals(JsonInclude.Include.ALWAYS, writer.getSerializationConfig().getDefaultPropertyInclusion());
 }
}

class StubSerializer extends JsonSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString("customSerializerValue");
 }
}

class TestBean {
 private String defaultValue;
 private TestUnsupportedType unsupportedProperty;
}

class TestUnsupportedType {
}