package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

    private static final String CLASS_WITH_INJECT_MOCKS_ANNOTATION = "test.TestClassWithInjectMocks";
    private static final String FIELD_1_NAME = "field1";
    private static final String FIELD_2_NAME = "field2";
    private static final Class<?> FIELD_1_TYPE = String.class;
    private static final Class<?> FIELD_2_TYPE = Integer.class;

    private DefaultInjectionEngine engine;
    private Set<Object> mocks;
    private Set<Field> injectMocksFields;
    private Object testClassInstance;

    @Before
    public void setUp() {
        engine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = spy(getClass().getDeclaredConstructor().newInstance());
        injectMocksFields = new HashSet<>();
        injectMocksFields.add(Whitebox.getInternalState(testClassInstance, FIELD_1_NAME));
        injectMocksFields.add(Whitebox.getInternalState(testClassInstance, FIELD_2_NAME));
    }

    @Test
    public void testInjectMocks() {
        // given
        Object field1Mock = mock(FIELD_1_TYPE);
        Object field2Mock = mock(FIELD_2_TYPE);
        mocks.add(field1Mock);
        mocks.add(field2Mock);

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verify(field1Mock).equals(injectMocksFields.get(0).get(testClassInstance));
        verify(field2Mock).equals(injectMocksFields.get(1).get(testClassInstance));
        verifyNoMoreInteractions(field1Mock, field2Mock);
        verifyZeroInteractions(mocks);
    }

    @Test
    public void testNoMockCandidateFound() {
        // given
        when(injectMocksFields.get(0).getType()).thenReturn(new ClassNotFoundException("No mock candidate found for type: " + FIELD_1_TYPE.getName()));

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verifyZeroInteractions(mocks);
    }

    @Test
    public void testMultipleMockCandidatesFound() {
        // given
        Object mock1 = mock(FIELD_1_TYPE);
        Object mock2 = mock(FIELD_1_TYPE);
        mocks.add(mock1);
        mocks.add(mock2);

        when(injectMocksFields.get(0).getType().getDeclaredField("field1")).thenReturn(injectMocksFields.get(0));
        when(injectMocksFields.get(0).getType().getDeclaredField("field2")).thenReturn(injectMocksFields.get(1));

        when(mockCandidateFilter.filterCandidate(mocks, injectMocksFields.get(0), mock1)).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(mocks, injectMocksFields.get(0), mock2)).thenReturn(mockCandidateFilter);

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verify(mock1).equals(injectMocksFields.get(0).get(testClassInstance));
        verify(mock2).equals(injectMocksFields.get(1).get(testClassInstance));
        verifyNoMoreInteractions(mock1, mock2);
        verifyZeroInteractions(mocks);
    }

    @Test
    public void testInjectMockByPropertySetter() {
        // given
        Object field1Mock = mock(FIELD_1_TYPE);
        Object field1Instance = new Object() {
            public void setField1(Object field1) { this.field1 = field1; }
            Object field1;
        };
        when(injectMocksFields.get(0).getType()).thenReturn(field1Instance.getClass());
        when(injectMocksFields.get(0).getName()).thenReturn("field1");

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verify(field1Mock).equals(field1Instance.field1);
        verifyNoMoreInteractions(field1Mock);
        verifyZeroInteractions(mocks);
    }

    @Test
    public void testInjectMockByFieldInjection() {
        // given
        Object field1Mock = mock(FIELD_1_TYPE);
        Object field1Instance = new Object() {
            Object field1;
        };
        when(injectMocksFields.get(0).getType()).thenReturn(field1Instance.getClass());
        when(injectMocksFields.get(0).getName()).thenReturn("field1");

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verify(field1Instance).setField1(field1Mock);
        verify(field1Mock).equals(field1Instance.field1);
        verifyNoMoreInteractions(field1Mock);
        verifyZeroInteractions(mocks);
    }

    @Test
    public void testInjectMockOnSuperClass() {
        // given
        Object superClassMock = mock(SuperClass.class);
        Object superClassInstance = new SuperClass() {
            public String superField = "superField";
        };
        when(superClassInstance.getClass()).thenReturn(SuperClass.class);

        Class<?> testClass = Class.forName("test.TestClassWithSuperClassInjectMocksAnnotation");
        Object testClassInstance = testClass.getDeclaredConstructor().newInstance();

        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(Whitebox.getInternalState(testClassInstance, "superField"));

        MockitoJUnitRunner runner = new MockitoJUnitRunner();
        runner.injectMocks(superClassMock);

        // when
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // then
        verify(superClassMock).equals(injectMocksFields.get(0).get(testClassInstance));
        verifyNoMoreInteractions(superClassMock);
        verifyZeroInteractions(mocks);
    }
}

class SuperClass {
    String superField;
}