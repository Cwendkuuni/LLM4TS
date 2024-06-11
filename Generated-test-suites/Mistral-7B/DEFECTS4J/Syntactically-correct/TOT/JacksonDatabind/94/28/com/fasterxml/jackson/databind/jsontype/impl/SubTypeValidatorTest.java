import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testValidateSubTypeWithValidType() throws Exception {
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mock(DeserializationContext.class), validType);
 }

 @Test
 public void testValidateSubTypeWithBlacklistedType() throws Exception {
 JavaType blacklistedType = objectMapper.getTypeFactory().constructType(InvokerTransformer.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), blacklistedType));
 }

 @Test
 public void testValidateSubTypeWithSpringType() throws Exception {
 Class<?> springType = Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 JavaType springTypeJavaType = objectMapper.getTypeFactory().constructType(springType);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), springTypeJavaType));
 }

 @Test
 public void testValidateSubTypeWithInterface() throws Exception {
 JavaType interfaceType = objectMapper.getTypeFactory().constructType(Runnable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mock(DeserializationContext.class), interfaceType);
 }

 @Test
 public void testValidateSubTypeWithSuperclassOfBlacklistedType() throws Exception {
 Class<?> superclassOfBlacklistedType = Class.forName("org.apache.commons.collections4.Transformer");
 JavaType superclassOfBlacklistedTypeJavaType = objectMapper.getTypeFactory().constructType(superclassOfBlacklistedType);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mock(DeserializationContext.class), superclassOfBlacklistedTypeJavaType);
 }
}