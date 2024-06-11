import com.fasterxml.jackson.core.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature.VisitedChecker;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig config;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 }

 @Test
 public void testBuilderWithNullDefaultBeanAndNonDefaultInclusion() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNonDefaultBeanAndNonDefaultInclusion() throws Exception {
 MyPojo defaultBean = new MyPojo();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 propertyBuilder._defaultBean = defaultBean;
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 assertEquals(defaultBean.getDefaultValue(), beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNonDefaultBeanAndNullValueToSuppress() throws Exception {
 MyPojo defaultBean = new MyPojo();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 propertyBuilder._defaultBean = defaultBean;
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNonEmptyInclusion() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 propertyBuilder.setDefaultInclusion(JsonInclude.Value.includeAnnotations());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNonDefaultInclusion() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 propertyBuilder.setDefaultInclusion(JsonInclude.Value.nonDefault());
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test(expected = JsonMappingException.class)
 public void testBuilderWithInvalidType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyNonPojoClass.class, null, null, null, null, false);
 fail("Expected JsonMappingException not thrown");
 }

 @Test
 public void testBuilderWithNonSerializableType() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyDefinition propDef = new BeanPropertyDefinition("nonSerializableField", String.class);
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, propDef, String.class, null, null, null, null, false);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithValueToSuppress() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 Object valueToSuppress = "defaultValue";
 beanPropertyWriter.assignValueToSuppress(valueToSuppress);
 assertEquals(valueToSuppress, beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNullValueToSuppress() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 beanPropertyWriter.assignValueToSuppress(null);
 assertNull(beanPropertyWriter.getValueToSuppress());
 }

 @Test
 public void testBuilderWithNullSerializer() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 JsonSerialize serializer = null;
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, serializer, null, null, null, false);
 assertNotNull(beanPropertyWriter.getNullSerializer());
 }

 @Test
 public void testBuilderWithCustomSerializer() throws Exception {
 MyCustomSerializer myCustomSerializer = new MyCustomSerializer();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, myCustomSerializer, null, null, null, false);
 assertEquals(myCustomSerializer, beanPropertyWriter.getNullSerializer());
 }

 @Test
 public void testBuilderWithCustomNullSerializer() throws Exception {
 MyCustomNullSerializer myCustomNullSerializer = new MyCustomNullSerializer();
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 beanPropertyWriter.assignNullSerializer(myCustomNullSerializer);
 assertEquals(myCustomNullSerializer, beanPropertyWriter.getNullSerializer());
 }

 @Test
 public void testBuilderWithUnwrappingNameTransformer() throws Exception {
 MyPojo pojo = new MyPojo();
 pojo.setInnerPojo(new InnerPojo());
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false);
 NameTransformer nameTransformer = new MyNameTransformer();
 BeanPropertyWriter unwrappedWriter = beanPropertyWriter.unwrappingWriter(nameTransformer);
 assertNotNull(unwrappedWriter);
 }

 @Test
 public void testBuilderWithInvalidNameTransformer() throws Exception {
 PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(MyPojo.class));
 NameTransformer nameTransformer = new MyInvalidNameTransformer();
 assertThrows(IllegalArgumentException.class, () -> propertyBuilder.buildWriter(null, null, MyPojo.class, null, null, null, null, false).unwrappingWriter(nameTransformer));
 }

 private static class MyPojo {
 private String name;
 private InnerPojo innerPojo;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public InnerPojo getInnerPojo() {
 return innerPojo;
 }

 public void setInnerPojo(InnerPojo innerPojo) {
 this.innerPojo = innerPojo;
 }
 }

 private static class MyInvalidNameTransformer implements NameTransformer {
 @Override
 public String transformName(String name) {
 throw new IllegalArgumentException("Invalid name transformer");
 }
 }

 private static class MyCustomSerializer extends JsonSerializer<MyPojo> {
 @Override
 public void serialize(MyPojo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString("Custom serialization");
 }
 }

 private static class MyCustomNullSerializer extends JsonSerializer<MyPojo> {
 @Override
 public void serialize(MyPojo value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 if (value == null) {
 gen.writeString("Custom null serialization");
 } else {
 gen.writeString("Custom serialization");
 }
 }
 }

 private static class MyNameTransformer implements NameTransformer {
 @Override
 public String transformName(String name) {
 return "innerPojo";
 }
 }

 private static class InnerPojo {
 private String innerName;

 public String getInnerName() {
 return innerName;
 }

 public void setInnerName(String innerName) {
 this.innerName = innerName;
 }
 }
}