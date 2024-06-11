import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext context = new DeserializationContext(null, null, null);
        JavaType type = new JavaType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
        // No exception should be thrown
    }
}