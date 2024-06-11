import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig config;
 private PropertyBuilder propertyBuilder;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 config = new SerializationConfig(objectMapper.getDefaultConfig());
 propertyBuilder = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 }

 @Test
 public void testBuildWriter_DefaultValues() throws JsonProcessingException {
 TestPojo pojo = new TestPojo();
 pojo.setStringProperty("defaultValue");

 AnnotatedClass ac = new AnnotatedClass(TestPojo.class);
 AnnotatedMember am = ac.findMember("stringProperty");
 JavaType declaredType = new TypeReference<String>() {}.getType();
 JsonSerializer<String> stringSerializer = new StringSerializer();
 TypeSerializer typeSerializer = new TypeSerializer() {};
 TypeSerializer contentTypeSerializer = new TypeSerializer() {};

 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, stringSerializer, typeSerializer, contentTypeSerializer, am, false);
 assertEquals("defaultValue", writer.getValue(pojo));
 }

 @Test
 public void testBuildWriter_NullValues() throws JsonProcessingException {
 TestPojo pojo = new TestPojo();

 AnnotatedClass ac = new AnnotatedClass(TestPojo.class);
 AnnotatedMember am = ac.findMember("intProperty");
 JavaType declaredType = new TypeReference<Integer>() {}.getType();
 JsonSerializer<Integer> intSerializer = new MinIntSerializer();
 TypeSerializer typeSerializer = new TypeSerializer() {};
 TypeSerializer contentTypeSerializer = new TypeSerializer() {};

 // Test with default exclusion
 JsonInclude.Value defaultInclusion = new JsonInclude.Value(JsonInclude.Include.ALWAYS);
 PropertyBuilder propertyBuilderDefault = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 propertyBuilderDefault._defaultInclusion = defaultInclusion;
 BeanPropertyWriter writerDefault = propertyBuilderDefault.buildWriter(null, null, declaredType, intSerializer, typeSerializer, contentTypeSerializer, am, false);
 assertNull(writerDefault.getValue(pojo));

 // Test with non-default exclusion
 JsonInclude.Value nonDefaultInclusion = new JsonInclude.Value(JsonInclude.Include.NON_DEFAULT);
 PropertyBuilder propertyBuilderNonDefault = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 propertyBuilderNonDefault._defaultInclusion = nonDefaultInclusion;
 BeanPropertyWriter writerNonDefault = propertyBuilderNonDefault.buildWriter(null, null, declaredType, intSerializer, typeSerializer, contentTypeSerializer, am, false);
 assertNull(writerNonDefault.getValue(pojo));
 }

 @Test
 public void testBuildWriter_EmptyCollections() throws JsonProcessingException {
 TestPojo pojo = new TestPojo();
 pojo.setListProperty(new ArrayList<>());
 pojo.setSetProperty(new HashSet<>());

 AnnotatedClass ac = new AnnotatedClass(TestPojo.class);
 AnnotatedMember amList = ac.findMember("listProperty");
 JavaType declaredTypeList = new TypeReference<List<String>>() {}.getType();
 JsonSerializer<List<String>> listSerializer = new MinListSerializer<String>();
 TypeSerializer typeSerializerList = new TypeSerializer() {};
 TypeSerializer contentTypeSerializerList = new TypeSerializer() {};

 AnnotatedMember amSet = ac.findMember("setProperty");
 JavaType declaredTypeSet = new TypeReference<Set<String>>() {}.getType();
 JsonSerializer<Set<String>> setSerializer = new MinSetSerializer<String>();
 TypeSerializer typeSerializerSet = new TypeSerializer() {};
 TypeSerializer contentTypeSerializerSet = new TypeSerializer() {};

 // Test with default exclusion
 JsonInclude.Value defaultInclusion = new JsonInclude.Value(JsonInclude.Include.ALWAYS);
 PropertyBuilder propertyBuilderDefault = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 propertyBuilderDefault._defaultInclusion = defaultInclusion;
 BeanPropertyWriter writerListDefault = propertyBuilderDefault.buildWriter(null, null, declaredTypeList, listSerializer, typeSerializerList, contentTypeSerializerList, amList, false);
 ObjectNode jsonListDefault = writerListDefault.writeValueAsTree(pojo);
 assertNotNull(jsonListDefault);
 assertTrue(jsonListDefault.isEmpty());

 BeanPropertyWriter writerSetDefault = propertyBuilderDefault.buildWriter(null, null, declaredTypeSet, setSerializer, typeSerializerSet, contentTypeSerializerSet, amSet, false);
 ObjectNode jsonSetDefault = writerSetDefault.writeValueAsTree(pojo);
 assertNotNull(jsonSetDefault);
 assertTrue(jsonSetDefault.isEmpty());

 // Test with non-empty exclusion
 JsonInclude.Value nonEmptyInclusion = new JsonInclude.Value(JsonInclude.Include.NON_EMPTY);
 PropertyBuilder propertyBuilderNonEmpty = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 propertyBuilderNonEmpty._defaultInclusion = nonEmptyInclusion;
 BeanPropertyWriter writerListNonEmpty = propertyBuilderNonEmpty.buildWriter(null, null, declaredTypeList, listSerializer, typeSerializerList, contentTypeSerializerList, amList, false);
 ObjectNode jsonListNonEmpty = writerListNonEmpty.writeValueAsTree(pojo);
 assertNotNull(jsonListNonEmpty);
 assertTrue(jsonListNonEmpty.isEmpty());

 BeanPropertyWriter writerSetNonEmpty = propertyBuilderNonEmpty.buildWriter(null, null, declaredTypeSet, setSerializer, typeSerializerSet, contentTypeSerializerSet, amSet, false);
 ObjectNode jsonSetNonEmpty = writerSetNonEmpty.writeValueAsTree(pojo);
 assertNotNull(jsonSetNonEmpty);
 assertTrue(jsonSetNonEmpty.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildWriter_InvalidAnnotations() throws JsonProcessingException {
 TestPojo pojo = new TestPojo();

 AnnotatedClass ac = new AnnotatedClass(TestPojo.class);
 AnnotatedMember am = ac.findMember("intProperty");
 JavaType declaredType = new TypeReference<Integer>() {}.getType();
 JsonSerializer<Integer> intSerializer = new MinIntSerializer();
 TypeSerializer typeSerializer = new TypeSerializer() {};
 TypeSerializer contentTypeSerializer = new TypeSerializer() {};

 JsonInclude.Value exclusion = new JsonInclude.Value(JsonInclude.Include.NON_DEFAULT);
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(TestPojo.class));
 propertyBuilder._defaultInclusion = exclusion;
 propertyBuilder._annotationIntrospector = new AnnotationIntrospector() {
 @Override public JsonSerializer<?> findNullSerializer(AnnotatedMember am) {
 throw new RuntimeException("Test error");
 }
 };
 BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, declaredType, intSerializer, typeSerializer, contentTypeSerializer, am, false);
 }
}

class TestPojo {
 private String stringProperty;
 private Integer intProperty;
 private List<String> listProperty;
 private Set<String> setProperty;

 public String getStringProperty() {
 return stringProperty;
 }

 public void setStringProperty(String value) {
 this.stringProperty = value;
 }

 public Integer getIntProperty() {
 return intProperty;
 }

 public void setIntProperty(Integer value) {
 this.intProperty = value;
 }

 public List<String> getListProperty() {
 return listProperty;
 }

 public void setListProperty(List<String> value) {
 this.listProperty = value;
 }

 public Set<String> getSetProperty() {
 return setProperty;
 }

 public void setSetProperty(Set<String> value) {
 this.setProperty = value;
 }
}

class MinIntSerializer extends StdSerializer<Integer> {}
class MinListSerializer<T> extends StdSerializer<List<T>> {}
class MinSetSerializer<T> extends StdSerializer<Set<T>> {}