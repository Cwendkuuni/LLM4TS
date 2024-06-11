import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.core.type.TypeReference;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws Exception {
        JavaType type = new JsonFactory().getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new TestContext(), type);
        // add assertion here if needed
    }

    @Test
    public void testValidateSubType_IllegalType() throws Exception {
        JavaType type = new TypeReference<SubTypeValidator>() {}.getType();
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(new TestContext(), type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Reference ref = e.getOriginalCause();
            assertThat(ref, is(instanceOf(JsonMappingException.class)));
            // add assertions here as needed
        }
    }

    @Test
    public void testValidateSubType_NastyClass() throws Exception {
        JavaType type = new TypeReference<Com.mchange.v2.c3p0.WrapperConnectionPoolDataSource>() {}.getType();
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(new TestContext(), type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            String message = e.getMessage();
            assertThat(message, is(containsString("Illegal type")));
            // add assertions here as needed
        }
    }

    @Test
    public void testValidateSubType_ValidTypeWithPrefix() throws Exception {
        JavaType type = new TypeReference<Object>() {}.getType();
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new TestContext(), type);
        // add assertion here if needed
    }

    static class TestContext extends DeserializationContext {
    }

    // other test cases can be added here
}