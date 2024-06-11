import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class SubTypeValidatorTest {

  @Test
  public void testValidateSubType_ValidType() throws JsonMappingException {
    DeserializationContext context = mock(DeserializationContext.class);
    JavaType javaType = mock(JavaType.class);

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(context, javaType);

    // No exception was thrown, so the test passed
  }

  @Test(expected = JsonMappingException.class)
  public void testValidateSubType_IllegalType() throws JsonMappingException {
    DeserializationContext context = mock(DeserializationContext.class);
    JavaType javaType = mock(JavaType.class);

    doReturn("org.apache.commons.collections.functors.InvokerTransformer").when(javaType).getRawClass();

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(context, javaType);

    // An exception was thrown and expected, so the test passed
  }

  @Test(expected = JsonMappingException.class)
  public void testValidateSubType_ParentOfIllegalType() throws JsonMappingException {
    DeserializationContext context = mock(DeserializationContext.class);
    JavaType javaType = mock(JavaType.class);

    doReturn(SubTypeValidator.class).when(javaType).getRawClass();
    doReturn(SubTypeValidator.class.getSuperclass()).when(javaType).getSuperclass();
    doReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean").when(javaType).getName();

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(context, javaType);

    // An exception was thrown and expected, so the test passed
  }

  @Test
  public void testValidateSubType_ValidSpringType() throws JsonMappingException {
    DeserializationContext context = mock(DeserializationContext.class);
    JavaType javaType = mock(JavaType.class);

    doReturn("org.springframework.beans.factory.config.AbstractApplicationContext").when(javaType).getRawClass();

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(context, javaType);

    // No exception was thrown, so the test passed
  }

  @Test(expected = JsonMappingException.class)
  public void testValidateSubType_NullContext() throws JsonMappingException {
    JavaType javaType = mock(JavaType.class);

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(null, javaType);

    // An exception was thrown and expected, so the test passed
  }

  @Test(expected = JsonMappingException.class)
  public void testValidateSubType_NullJavaType() throws JsonMappingException {
    DeserializationContext context = mock(DeserializationContext.class);

    SubTypeValidator validator = new SubTypeValidator();
    validator.validateSubType(context, null);

    // An exception was thrown and expected, so the test passed
  }
}