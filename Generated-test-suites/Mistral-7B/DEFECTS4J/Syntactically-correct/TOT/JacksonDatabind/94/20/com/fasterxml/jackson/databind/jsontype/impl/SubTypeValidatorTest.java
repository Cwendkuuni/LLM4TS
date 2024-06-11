import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(MAPPER.getDeserializationContext(), validType);
 }

 @Test
 public void testSpringType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springType = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");
 validator.validateSubType(MAPper.getDeserializationContext(), springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = TypeFactory.defaultInstance().constructType(Object.class, "org.apache.commons.collections4.functors.InstantiateTransformer");
 try {
 validator.validateSubType(MAPper.getDeserializationContext(), nastyType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringInterface() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springInterface = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.beans.factory.config.AbstractApplicationContext");
 try {
 validator.validateSubType(MAPper.getDeserializationContext(), springInterface.getRawClass().getInterfaces()[0].getTypeId());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}