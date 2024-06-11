import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        when(type.getRawClass()).thenReturn(Object.class); // Assuming Object class is not in the set of nasty classes

        validator.validateSubType(ctxt, type);
    }
}