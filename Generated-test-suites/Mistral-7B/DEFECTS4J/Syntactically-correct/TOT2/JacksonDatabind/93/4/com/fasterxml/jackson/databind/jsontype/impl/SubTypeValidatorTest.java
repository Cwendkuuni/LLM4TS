import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SubTypeValidatorTest {
    @Test
    public void testgetDefaultNoDeserClassNames() {
        Set<String> defaultNoDeserClassNames = SubTypeValidator.getDefaultNoDeserClassNames();
        assertNotNull(defaultNoDeserClassNames);
        assertEquals(22, defaultNoDeserClassNames.size());
    }
}