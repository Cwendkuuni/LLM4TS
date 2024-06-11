import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private ObjectMapper mapper;

 @Test
 public void testValidType() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":\"java.lang.String\"}";
 SubTypeValidator validator = mapper.readValue(json, SubTypeValidator.class);
 assertNotNull(validator);
 }

 @Test(expected = JsonMappingException.class)
 public void testNastyClass() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":\"org.apache.commons.collections.functors.InvokerTransformer\"}";
 mapper.readValue(json, SubTypeValidator.class);
 fail("Should have thrown an exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClass() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":\"org.springframework.beans.factory.ObjectFactory\"}";
 mapper.readValue(json, SubTypeValidator.class);
 fail("Should have thrown an exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testIllegalSubclass() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":\"com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator$1\"}";
 mapper.readValue(json, SubTypeValidator.class);
 fail("Should have thrown an exception");
 }

 @Test
 public void testEmptyClassName() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":\"\"}";
 try {
 mapper.readValue(json, SubTypeValidator.class);
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().startsWith("Illegal type (") && e.getMessage().endsWith(") to deserialize:"));
 }
 }

 @Test
 public void testNullClassName() throws JsonMappingException {
 mapper = new ObjectMapper();
 mapper.disable(DeserializationFeature.FAIL_ON_EMPTY_BEANS);
 String json = "{\"class\":null}";
 try {
 mapper.readValue(json, SubTypeValidator.class);
 fail("Should have thrown an exception");
 } catch (JsonMappingException e) {
 assertTrue(e.getMessage().startsWith("Cannot deserialize value of type com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator"));
 }
 }
}