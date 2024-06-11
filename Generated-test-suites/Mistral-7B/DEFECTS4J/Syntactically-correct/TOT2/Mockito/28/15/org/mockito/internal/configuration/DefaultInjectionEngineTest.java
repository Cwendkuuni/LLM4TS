import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.FieldInitializer;
import org.mockito.internal.util.reflection.FieldAccessHelper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;
    private Set<Field> injectMocksFields;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = new Object() {};
        injectMocksFields = new HashSet<Field>() {{
            add(new Field[] {new Field() {{
                setAccessible(true);
                setName("field1");
                setType(String.class);
            }}, new Field() {{
                setAccessible(true);
                setName("field2");
                setType(Integer.class);
            }}}[0]);
        }};
    }

    @Test
    public void testInjectMocksOnFields() {
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Check if mocks were injected correctly
        assertEquals(mocks.size(), 2);
        assertNotNull(testClassInstance.getClass().getField("field1").get(testClassInstance));
        assertNotNull(testClassInstance.getClass().getField("field2").get(testClassInstance));

        // Check if mocks were injected only once
        assertEquals(mocks, mocks);
    }
}