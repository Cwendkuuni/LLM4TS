import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

 private PropertyBuilder propertyBuilder;
 private ObjectNode defaultBean;

 @Before
 public void setUp() throws JsonProcessingException {
 JsonMapper mapper = new JsonMapper();
 ObjectNode defaultObjectNode = mapper.createObjectNode();
 defaultBean = mapper.readValue(defaultObjectNode.toString(), Object.class);
 propertyBuilder = new PropertyBuilder(mapper.getSerializationConfig(),
 new BeanDescription(Object.class));
 }

 @Test
 public void testBuilderWithDefaultInclusion() {
 JsonInclude.Value defaultInclusion = JsonInclude.Value.builder()
 .include(JsonInclude.Include.ALWAYS)
 .build();
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null, defaultInclusion, null, null);
 assertEquals(JsonInclude.Value.ALWAYS, propertyBuilder.getDefaultInclusion());
 }

 @Test
 public void testBuilderWithEmptyDefaultInclusion() {
 PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
 assertNull(propertyBuilder.getDefaultInclusion());
 }

 @Test
 public void testBuilderWithUseRealPropertyDefaultsTrue() throws JsonProcessingException {
 Object defaultValue = BeanUtil.getDefaultValue(String.class);
 propertyBuilder.setDefaultBean(defaultBean);
 Object propertyValue = propertyBuilder.getPropertyDefaultValue("propertyName", null, String.class);
 assertEquals(defaultValue, propertyValue);
 }

 @Test
 public void testBuilderWithUseRealPropertyDefaultsFalse() {
 propertyBuilder.setDefaultBean(null);
 Object propertyValue = propertyBuilder.getPropertyDefaultValue("propertyName", null, String.class);
 assertNull(propertyValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindSerializationTypeWithIllegalAnnotations() throws JsonProcessingException {
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String[].class);
 propertyBuilder.findSerializationType(am, false, declaredType);
 }

 @Test
 public void testFindSerializationTypeWithValidAnnotations() throws JsonProcessingException {
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
 propertyBuilder.findSerializationType(am, false, declaredType);
 }

 @Test
 public void testGetDefaultBean() {
 Object defaultBean = propertyBuilder.getDefaultBean();
 assertNotNull(defaultBean);
 }

 @Test
 public void testGetPropertyDefaultValueWithNullBean() {
 assertNull(propertyBuilder.getPropertyDefaultValue("propertyName", null, String.class));
 }

 @Test
 public void testGetPropertyDefaultValueWithDefaultBean() throws JsonProcessingException {
 AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
 JavaType propertyType = mapper.getTypeFactory().constructType(String.class);
 Object propertyValue = propertyBuilder.getPropertyDefaultValue("propertyName", am, propertyType);
 assertNotNull(propertyValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void test_throwWrappedWithNullException() throws JsonProcessingException {
 Throwable exception = new RuntimeException();
 propertyBuilder._throwWrapped(exception, "propertyName", defaultBean);
 }

 @Test
 public void test_throwWrappedWithErrorException() throws JsonProcessingException {
 Throwable exception = new IllegalArgumentException();
 propertyBuilder._throwWrapped(exception, "propertyName", defaultBean);
 }
}