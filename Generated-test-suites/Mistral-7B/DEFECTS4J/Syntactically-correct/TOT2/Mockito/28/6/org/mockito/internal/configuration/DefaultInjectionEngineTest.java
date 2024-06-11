import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        defaultInjectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = mock(Object.class);
    }

    @Test
    public void testInjectMocksOnFields() {
        Field field1 = Mockito.mock(SomeClass1.class);
        Field field2 = Mockito.mock(SomeClass2.class);
        injectMocksFields.add(new Field[]{field1, field2});
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        assertNotNull(field1.get(testClassInstance));
        assertNotNull(field2.get(testClassInstance));
    }
}