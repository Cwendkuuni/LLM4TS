import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        SubTypeValidator validator = new SubTypeValidator();

        when(ctxt.getContextClassLoader().loadClass(SubTypeValidator.class.getName()))
                .thenReturn(SubTypeValidator.class);
        when(type.getRawClass()).thenReturn(SubTypeValidator.class);

        String fullClassName = "com.example.MyInvalidClass";
        validator.validateSubType(ctxt, type);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
}