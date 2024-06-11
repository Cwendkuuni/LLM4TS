import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with a valid class name
        String validClassName = "com.example.ValidClass";
        when(type.getRawClass().getName()).thenReturn(validClassName);
        assertNull(validator.validateSubType(context, type));

        // Test with a class name in the blacklist
        String illegalClassName = "org.apache.commons.collections.functors.InvokerTransformer";
        when(type.getRawClass().getName()).thenReturn(illegalClassName);
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
        assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", exception.getMessage());

        verify(context, times(2)).findContextualValue("", (Class<?>) anyClass());
    }
}