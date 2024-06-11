import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class SubTypeValidatorTest {

  @Test
  public void testValidateSubTypeWithValidType() {
    SubTypeValidator validator = SubTypeValidator.instance();
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JavaType validType = mock(JavaType.class);

    validator.validateSubType(ctxt, validType);
  }

  @Test
  public void testValidateSubTypeWithInvalidType() {
    SubTypeValidator validator = SubTypeValidator.instance();
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JavaType invalidType = mock(JavaType.class);
    when(invalidType.getRawClass().getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

    assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, invalidType));
  }
}