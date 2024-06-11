import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PropertyAndSetterInjectionTest {

    @InjectMocks
    private PropertyAndSetterInjection injectionStrategy;

    @Mock
    private SomeClass someMock;

    @Mock
    private AnotherClass anotherMock;

    private static class SomeClass {
        @InjectMocks
        private AnotherClass anotherField;

        public AnotherClass getAnotherField() {
            return anotherField;
        }
    }

    private static class AnotherClass {}

    @Before
    public void setupMocks() {
        Mockito.initMocks(this);
    }

    @Test
    public void test_successful_injection() {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(someMock);
        mockCandidates.add(anotherMock);

        Field field = new Field("anotherField", AnotherClass.class, SomeClass.class);
        Object instance = new SomeClass();

        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(field, instance);
        assertSame(anotherMock, report.getFieldInstance());
        assertSame(AnotherClass.class, report.fieldClass());

        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance));
        assertSame(anotherMock, injectionStrategy.getFieldValue(field, instance));
    }

    @Test
    public void test_successful_injection_with_no_setters() {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(someMock);
        mockCandidates.add(anotherMock);

        Field field = new Field("anotherField", AnotherClass.class, SomeClass.class);

        Object instance = new SomeClass() {
            @Override
            public AnotherClass getAnotherField() {
                return null;
            }
        };

        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance));
        assertSame(anotherMock, injectionStrategy.getFieldValue(field, instance));
    }

    @Test(expected = IllegalStateException.class)
    public void test_injection_fail_when_no_mock_found() {
        Set<Object> mockCandidates = new HashSet<>();

        Field field = new Field("anotherField", AnotherClass.class, SomeClass.class);
        Object instance = new SomeClass();

        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance));
        assertNull(injectionStrategy.getFieldValue(field, instance));

        Set<Object> emptyMockCandidates = Collections.emptySet();
        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, emptyMockCandidates, instance));
        assertNull(injectionStrategy.getFieldValue(field, instance));
    }

    @Test
    public void test_injection_fail_due_to_multiple_candidates() {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(someMock);
        mockCandidates.add(anotherMock);
        mockCandidates.add(someMock);

        Field field = new Field("anotherField", AnotherClass.class, SomeClass.class);
        Object instance = new SomeClass();

        assertFalse(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance));
    }

    @Test
    public void test_injection_fail_when_field_is_final() {
        Set<Object> mockCandidates = new HashSet<>();

        final class FinalClass {}
        Field field = new Field("finalField", FinalClass.class, SomeClass.class);

        Object instance = new SomeClass();

        try {
            injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance);
            assertFalse(true);
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void test_injection_fail_when_constructor_initialization_throws() {
        Set<Object> mockCandidates = new HashSet<>();

        class FailingClass {
            public FailingClass() {
                throw new RuntimeException("Constructor failed");
            }
        }

        Field field = new Field("failingField", FailingClass.class, SomeClass.class);
        Object instance = new SomeClass();

        injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance);
    }

    @Test
    public void test_injection_fail_when_no_fields_pass_filter() {
        Set<Object> mockCandidates = new HashSet<>();

        List<Field> fields = Arrays.asList(new Field("nonInjectMocksField", String.class, SomeClass.class));

        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, new SomeClass()));
        assertEquals(Collections.emptyList(), injectionStrategy.orderedInstanceFieldsFrom(SomeClass.class));
    }

    @Test
    public void test_injection_with_deep_inheritance() {
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(anotherMock);

        class GrandChildClass extends AnotherClass {}
        class ChildClass extends SomeClass {
            @InjectMocks
            private GrandChildClass grandChildField;
        }
        Object instance = new ChildClass();

        assertTrue(injectionStrategy.injectMockCandidates(SomeClass.class, mockCandidates, instance));
        assertSame(anotherMock, injectionStrategy.getFieldValue(new Field("anotherField", AnotherClass.class, SomeClass.class), instance));
    }
}