import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(ctxt, type);

        verifyNoMoreInteractions(ctxt);
        verifyNoMoreInteractions(type);
    }
}