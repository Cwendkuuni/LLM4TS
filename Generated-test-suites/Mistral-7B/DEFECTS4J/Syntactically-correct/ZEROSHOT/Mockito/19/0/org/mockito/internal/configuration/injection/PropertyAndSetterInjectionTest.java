package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TestClassWithFields;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.TestUtil.getAccessibleField;
import static org.mockito.internal.util.reflection.TestUtil.setAccessibleValue;

public class PropertyAndSetterInjectionTest {

    private Reporter reporter;
    private PropertyAndSetterInjection injection;
    private TestClassWithFields testClass;

    @Before
    public void setUp() {
        reporter = new Reporter();
        injection = new PropertyAndSetterInjection();
        testClass = new TestClassWithFields();
    }

    @Test
    public void shouldProcessInjectionSuccessfully() throws Exception {
        // given
        Field injectMocksField = TestClassWithFields.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithMocks());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test(expected = MockitoException.class)
    public void shouldThrowExceptionWhenInitializationFails() throws Exception {
        // given
        Field injectMocksField = TestClassWithFields.class.getDeclaredField("injectMocksFieldWithException");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        // when & then
        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }

    @Test
    public void shouldInjectMockUsingSetter() throws Exception {
        // given
        Field injectMocksField = TestClassWithFields.class.getDeclaredField("injectMocksFieldUsingSetter");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithMockUsingSetter());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        TestClassWithMockUsingSetter mock = (TestClassWithMockUsingSetter) getAccessibleValue(injectMocksField, injectMocksFieldOwner);
        assertNotNull("Mock should not be null", mock.mock);
    }

    @Test
    public void shouldInjectMockUsingField() throws Exception {
        // given
        Field injectMocksField = TestClassWithFields.class.getDeclaredField("injectMocksFieldUsingField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithMockUsingField());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        TestClassWithMockUsingField mock = (TestClassWithMockUsingField) getAccessibleValue(injectMocksField, injectMocksFieldOwner);
        assertNotNull("Mock should not be null", mock.mock);
    }

    @Test
    public void shouldInjectMocksInHierarchy() throws Exception {
        // given
        Field injectMocksField = TestClassWithNestedFields.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithNestedFields();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithMockNestedField());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        TestClassWithMockNestedField mock = (TestClassWithMockNestedField) getAccessibleValue(injectMocksField, injectMocksFieldOwner);
        assertNotNull("Mock should not be null", mock.nestedMock);
    }

    @Test
    public void shouldNotInjectDuplicateMock() throws Exception {
        // given
        Field injectMocksField = TestClassWithNestedFields.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithNestedFields();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithMockNestedField());
        mockCandidates.add(new TestClassWithMockNestedField());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        TestClassWithNestedFields instance = (TestClassWithNestedFields) injectMocksFieldOwner;
        assertNotNull("Mock should not be null", instance.field1.mock);
        assertNull("Second mock should not be injected", instance.field2.mock);
    }

    @Test
    public void shouldNotInjectNullMock() throws Exception {
        // given
        Field injectMocksField = TestClassWithFields.class.getDeclaredField("injectMocksFieldUsingNullMock");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void shouldNotInjectMockAnnotatedWithInjectMocks() throws Exception {
        // given
        Field injectMocksField = TestClassWithAnnotatedFields.class.getDeclaredField("annotatedField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new Object();
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(new TestClassWithAnnotatedFields());

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        TestClassWithAnnotatedFields instance = (TestClassWithAnnotatedFields) injectMocksFieldOwner;
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void shouldNotInjectMockWithFinalField() throws Exception {
        // given
        Field injectMocksField = TestClassWithFinalField.class.getDeclaredField("finalField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithFinalField();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void shouldNotInjectMockWithStaticField() throws Exception {
        // given
        Field injectMocksField = TestClassWithStaticField.class.getDeclaredField("staticField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithStaticField();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void shouldNotInjectMockWithPrivateField() throws Exception {
        // given
        Field injectMocksField = TestClassWithPrivateField.class.getDeclaredField("privateField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithPrivateField();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    @Test
    public void shouldNotInjectMockWithProtectedField() throws Exception {
        // given
        Field injectMocksField = TestClassWithProtectedField.class.getDeclaredField("protectedField");
        injectMocksField.setAccessible(true);
        Object injectMocksFieldOwner = new TestClassWithProtectedField();
        Set<Object> mockCandidates = new HashSet<>();

        // when
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // then
        assertFalse("No mocks were injected", result);
        assertNull("Injected field should be null", getAccessibleValue(injectMocksField, injectMocksFieldOwner));
    }

    private static class TestClassWithFields {
        @InjectMocks
        private FieldWithAnnotations injectMocksField;
        private FieldWithAnnotations injectMocksFieldWithException;
        private FieldWithAnnotations injectMocksFieldUsingSetter;
        private FieldWithAnnotations injectMocksFieldUsingField;
        private FieldWithAnnotations injectMocksFieldUsingNullMock;
        private FieldWithAnnotations injectMocksFieldWithAnnotation;
        private FieldWithAnnotations injectMocksFieldWithFinal;
        private FieldWithAnnotations injectMocksFieldWithStatic;
        private FieldWithAnnotations injectMocksFieldWithPrivate;
        private FieldWithAnnotations injectMocksFieldWithProtected;

        static class FieldWithAnnotations {
            // empty class
        }
    }

    private static class TestClassWithMockNestedField {
        @Mock private NestedMock nestedMock;
    }

    private static class TestClassWithMockUsingSetter {
        @Mock private Mock mock;

        public void setMock(Mock mock) {
            this.mock = mock;
        }
    }

    private static class TestClassWithMockUsingField {
        @Mock private Mock mock;

        public Mock getMock() {
            return mock;
        }

        public void setMock(Mock mock) {
            this.mock = mock;
        }
    }

    private static class TestClassWithAnnotatedFields {
        @InjectMocks @InjectMocks
        private FieldWithAnnotations annotatedField;
    }

    private static class TestClassWithFinalField {
        @InjectMocks
        private final FieldWithAnnotations finalField;

        static class FieldWithAnnotations {
            // empty class
        }
    }

    private static class TestClassWithStaticField {
        @InjectMocks
        private static FieldWithAnnotations staticField;

        static class FieldWithAnnotations {
            // empty class
        }
    }

    private static class TestClassWithPrivateField {
        private @InjectMocks
        private FieldWithAnnotations privateField;

        static class FieldWithAnnotations {
            // empty class
        }
    }

    private static class TestClassWithProtectedField {
        protected @InjectMocks
        private FieldWithAnnotations protectedField;

        static class FieldWithAnnotations {
            // empty class
        }
    }
}