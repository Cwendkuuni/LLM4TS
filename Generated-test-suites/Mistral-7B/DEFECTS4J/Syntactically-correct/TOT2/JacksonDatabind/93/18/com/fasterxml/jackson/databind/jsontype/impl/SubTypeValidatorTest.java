import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testDefaultNoDeserClassNames() {
        Set<String> defaultNoDeserClassNames = SubTypeValidator.getDefaultNoDeserClassNames();
        assertNotNull(defaultNoDeserClassNames);
        // Add more assertions if needed
    }
}