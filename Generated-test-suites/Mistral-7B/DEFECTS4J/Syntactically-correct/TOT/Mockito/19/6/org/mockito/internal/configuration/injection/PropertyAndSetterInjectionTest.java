package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object testObject;
    private List<Field> fields;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        testObject = new TestClass();
        fields = Arrays.asList(testObject.getClass().getDeclaredFields());
    }

    @Test
    public void testInjectMocksWithNoSettersOrFields() {
        // Given: Test class has no fields or setters annotated with @InjectMocks
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When: Process injection
        assertFalse(injectionStrategy.processInjection(null, testObject, mockCandidates));
    }

    @Test
    public void testInjectMocksWithFieldInjection() {
        // Given: Test class has a field annotated with @InjectMocks
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class), mock(Dependency2.class));

        // When: Process injection
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(injectMocksField, testObject);
        assertNotNull(report);

        // Then: The field should be injected with a mock
        Object injectedMock = report.getInjectedMock();
        assertNotNull(injectedMock);
        assertSame(injectedMock, injectMocksField.get(testObject));

        // Given: The test class has a dependency in its hierarchy
        Class<?> dependencyClass = Dependency1.class;
        Field dependencyField = dependencyClass.getDeclaredField("dependencyField");
        dependencyField.annotate(InjectMocks.class);

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(dependencyClass, mockCandidates, testObject);
        assertTrue(injectionOccurred);

        // Then: The dependency should be injected with a mock
        Object dependencyInstance = dependencyField.get(testObject.getDependency1());
        assertNotNull(dependencyInstance);
        assertSame(dependencyInstance, mockCandidates.iterator().next());
    }

    @Test
    public void testInjectMocksWithPropertySetter() {
        // Given: Test class has a field annotated with @InjectMocks and a setter method
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class), mock(Dependency2.class));

        // When: Process injection
        FieldInitializationReport report = injectionStrategy.initializeInjectMocksField(injectMocksField, testObject);
        assertNotNull(report);

        // Then: The field should be injected with a mock using the setter method
        Object injectedMock = report.getInjectedMock();
        assertNotNull(injectedMock);
        assertSame(injectedMock, testObject.setInjectMocksField(mock(Dependency1.class)));

        // Given: The test class has a dependency in its hierarchy with a setter method
        Class<?> dependencyClass = Dependency1.class;
        Field dependencyField = dependencyClass.getDeclaredField("dependencyField");
        dependencyField.annotate(InjectMocks.class);

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(dependencyClass, mockCandidates, testObject);
        assertTrue(injectionOccurred);

        // Then: The dependency should be injected with a mock using the setter method
        Object dependencyInstance = testObject.getDependency1();
        assertNotNull(dependencyInstance);
        assertSame(dependencyInstance, mockCandidates.iterator().next());
        assertSame(testObject.getDependency1().getDependencyField(), mock(Dependency2.class));
    }

    @Test(expected = BaseMockitoException.class)
    public void testInjectMocksWithMultipleCandidatesByName() {
        // Given: Test class has a field annotated with @InjectMocks and multiple candidates with the same name
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class), mock(Dependency2.class), mock(Dependency1.class));

        // When: Process injection
        injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);

        // Then: An exception should be thrown
    }

    @Test(expected = BaseMockitoException.class)
    public void testInjectMocksWithMultipleCandidatesByTypeInFirstPass() {
        // Given: Test class has a field annotated with @InjectMocks and multiple candidates of the same type
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class), mock(Dependency1.class));

        // When: Process injection
        injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);

        // Then: An exception should be thrown
    }

    @Test
    public void testInjectMocksWithNoCandidates() {
        // Given: Test class has a field annotated with @InjectMocks but no candidates are found
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Collections.emptySet();

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithEmptyList() {
        // Given: Test class has a field annotated with @InjectMocks but the list of candidates is empty
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithNoInjectableFields() {
        // Given: Test class has a field annotated with @InjectMocks but no injectable fields are found
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class), mock(Dependency2.class));

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithNoMockCandidates() {
        // Given: Test class has a field annotated with @InjectMocks but no mock candidates are found
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithInjectionFailure() {
        // Given: Test class has a field annotated with @InjectMocks, but the injection fails
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class));

        // When: Process injection
        injectionStrategy.injectMockCandidates(testObject.getClass(), mockCandidates, testObject);

        // Then: The field should not be injected
        assertNull(injectMocksField.get(testObject));
    }

    @Test
    public void testInjectMocksWithNoConstructor() {
        // Given: Test class has a field annotated with @InjectMocks and no constructor is available
        Class<?> noConstructorClass = new NoConstructorClass();
        Field injectMocksField = noConstructorClass.getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(noConstructorClass, mockCandidates, testObject);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithInjectionAndSpy() {
        // Given: Test class has a field annotated with @InjectMocks and a field spied before the injection
        TestClass testObjectSpy = spy(new TestClass());
        Field injectMocksField = testObjectSpy.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class));

        // When: Process injection
        injectionStrategy.injectMockCandidates(testObjectSpy.getClass(), mockCandidates, testObjectSpy);

        // Then: The field should be injected with a mock
        Object injectedMock = testObjectSpy.getInjectMocksField();
        assertNotNull(injectedMock);
        assertSame(injectedMock, mockCandidates.iterator().next());
    }

    @Test
    public void testInjectMocksWithNestedClass() {
        // Given: Test class has a nested class with a field annotated with @InjectMocks
        TestClass testObject = new TestClass();
        Class<?> nestedClass = testObject.getNestedClass();
        Field injectMocksField = nestedClass.getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class));

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.injectMockCandidates(nestedClass, mockCandidates, testObject);
        assertTrue(injectionOccurred);

        // Then: The field should be injected with a mock
        Object injectedMock = testObject.getNestedClass().getInjectMocksField();
        assertNotNull(injectedMock);
        assertSame(injectedMock, mockCandidates.iterator().next());
    }

    @Test
    public void testInjectMocksWithCyclicDependency() {
        // Given: Test class has a cyclic dependency
        TestClassA testClassA = new TestClassA();
        TestClassB testClassB = new TestClassB();
        testClassA.setTestClassB(testClassB);
        testClassB.setTestClassA(testClassA);

        // When: Process injection
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(TestClassA.class), mock(TestClassB.class));
        injectionStrategy.processInjection(null, testClassA, mockCandidates);

        // Then: An exception should be thrown
    }

    @Test
    public void testInjectMocksWithFinalField() {
        // Given: Test class has a final field annotated with @InjectMocks
        TestClass testObject = new TestClass();
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        injectMocksField.setAccessible(true);
        Modifier.setFinal(injectMocksField.getModifiers(), true);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class));

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates);
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMocksWithStaticField() {
        // Given: Test class has a static field annotated with @InjectMocks
        TestClass testObject = new TestClass();
        Field injectMocksField = testObject.getClass().getDeclaredField("injectMocksField");
        injectMocksField.annotate(InjectMocks.class);
        injectMocksField.setAccessible(true);
        Modifier.setStatic(injectMocksField.getModifiers(), true);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet(mock(Dependency1.class));

        // When: Process injection
        boolean injectionOccurred = injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates);
        assertFalse(injectionOccurred);
    }

    static class TestClass {
        @InjectMocks
        private Object injectMocksField;

        @InjectMocks
        private Dependency1 dependency1;
        private Dependency1 dependencyField;

        @InjectMocks
        private NestedClass nestedClass;

        public void setDependencyField(Dependency1 dependencyField) {
            this.dependencyField = dependencyField;
        }

        public Dependency1 getDependencyField() {
            return dependencyField;
        }

        public static class NestedClass {
            @InjectMocks
            private Object injectMocksField;
        }

        public Object getInjectMocksField() {
            return injectMocksField;
        }

        public Dependency1 getDependency1() {
            return dependency1;
        }

        public Class<?> getNestedClass() {
            return new NestedClass();
        }
    }

    static class Dependency1 {
        @InjectMocks
        private Dependency2 dependencyField;

        public Dependency2 getDependencyField() {
            return dependencyField;
        }
    }

    static class Dependency2 {
    }

    static class NoConstructorClass {
        @InjectMocks
        private Object injectMocksField;
    }
}