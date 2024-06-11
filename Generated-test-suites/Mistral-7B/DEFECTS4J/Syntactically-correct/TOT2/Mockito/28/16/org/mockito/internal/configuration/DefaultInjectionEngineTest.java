import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OMockRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Set<Field> injectMocksFields;
    private Object testClassInstance;

    @Rule
    public OMockRule mockRule = new OMockRule();

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        injectMocksFields = new HashSet<>();
        testClassInstance = mockRule.createMock(Object.class);
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
        injectMocksFields.add(new Field[]{new Field[]{new Field(ClassA.class, "fieldA"), new Field(ClassB.class, "fieldB")}}[0]);
    }

    @Test
    public void testInjectMocksOnFields() {
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        verify(testClassInstance).setFieldA(mocks.iterator().next());
        verify(testClassInstance).setFieldB(mocks.iterator().next());
    }
}