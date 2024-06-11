import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;
 private DeserializationContext deserializationContext;
 private SubTypeValidator subTypeValidator;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 deserializationContext = objectMapper.getDeserializationContext();
 subTypeValidator = SubTypeValidator.instance();
 }

 @Test
 public void testValidTypeDeserialization() throws Exception {
 String validClassName = "com.example.ValidType";
 JavaType validType = TypeFactory.defaultInstance().constructType(Class.forName(validClassName));
 subTypeValidator.validateSubType(deserializationContext, validType);
 }

 @Test
 public void testNastyTypeDeserialization() throws Exception {
 String nastyClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType nastyType = TypeFactory.defaultInstance().constructType(Class.forName(nastyClassName));

 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(deserializationContext, nastyType));
 }

 @Test
 public void testSpringTypeDeserialization() throws Exception {
 String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springType = TypeFactory.defaultInstance().constructType(Class.forName(springClassName));

 assertThrows(JsonMappingException.class, () -> subTypeValidator.validateSubType(deserializationContext, springType));
 }

 @Test
 public void testInterfaceTypeDeserialization() throws Exception {
 String interfaceClassName = "com.example.MyInterface";
 JavaType interfaceType = TypeFactory.defaultInstance().constructType(new TypeReference<Class<?>>() {});
 interfaceType = TypeFactory.defaultInstance().constructType(Class.forName(interfaceClassName.substring(0, interfaceClassName.lastIndexOf("."))));

 subTypeValidator.validateSubType(deserializationContext, interfaceType);
 }
}