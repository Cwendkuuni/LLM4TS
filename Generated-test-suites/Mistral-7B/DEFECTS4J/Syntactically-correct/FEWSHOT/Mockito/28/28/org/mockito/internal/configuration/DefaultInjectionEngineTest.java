package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.mockitostub.MockitoStubs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        Object testClassInstance = MockitoStubs.spy(TestClass.class);
        Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toCollection(HashSet::new));
        Set<Object> mocks = new HashSet<>();
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            Object expectMock = mocks.stream()
                    .filter(mock -> field.getType().isInstance(mock))
                    .findFirst()
                    .orElse(null);

            if (expectMock != null) {
                Assert.assertNotNull("Field " + field.getName() + " should be injected with a mock",
                        field.get(testClassInstance));
                Assert.assertEquals("Field " + field.getName() + " should be equal to the injected mock",
                        expectMock, field.get(testClassInstance));
            } else {
                Assert.assertNull("Field " + field.getName() + " should not be injected",
                        field.get(testClassInstance));
            }
        }
    }

    @Test
    public void testInjectMockCandidate() {
        Object awaitingInjectionInstance = mock(TestClass.class);
        Set<Object> mocks = new HashSet<>();
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        for (Field field : TestClass.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(InjectMocks.class)) {
                continue;
            }

            Object fieldInstance = field.get(awaitingInjectionInstance);
            Class<?> awaitingInjectionClazz = fieldInstance.getClass();

            for (Field fieldToInject : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
                MockCandidateFilter candidateFilter = injectionEngine.mockCandidateFilter;
                Object mockCandidate = candidateFilter.filterCandidate(mocks, fieldToInject, fieldInstance);

                if (mockCandidate != null) {
                    FieldType fieldType = new FieldType(fieldToInject);
                    Assert.assertTrue("Field " + fieldType.getName() + " should be set by property setter",
                            fieldType.isSettableByProperty());
                    fieldToInject.setAccessible(true);
                    fieldToInject.set(awaitingInjectionInstance, mockCandidate);
                    Assert.assertSame("Field " + fieldType.getName() + " should be equal to the injected mock",
                            mockCandidate, fieldToInject.get(awaitingInjectionInstance));
                    mocks.remove(mockCandidate);
                    break;
                } else {
                    Assert.assertNull("Field " + fieldType.getName() + " should not be injected",
                            fieldToInject.get(awaitingInjectionInstance));
                }
            }
        }
    }

    @Test
    public void testInjectMockCandidate_MultipleCandidates() {
        Object awaitingInjectionInstance = mock(TestClass.class);
        Set<Object> mocks = new HashSet<>(Arrays.asList(mock(Dependency1.class), mock(Dependency2.class)));
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        for (Field field : TestClass.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(InjectMocks.class)) {
                continue;
            }

            Object fieldInstance = field.get(awaitingInjectionInstance);
            Class<?> awaitingInjectionClazz = fieldInstance.getClass();

            for (Field fieldToInject : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
                MockCandidateFilter candidateFilter = injectionEngine.mockCandidateFilter;
                Object mockCandidate1 = candidateFilter.filterCandidate(mocks, fieldToInject, fieldInstance);

                if (mockCandidate1 != null) {
                    FieldType fieldType = new FieldType(fieldToInject);
                    Assert.assertTrue("Field " + fieldType.getName() + " should be set by property setter",
                            fieldType.isSettableByProperty());
                    fieldToInject.setAccessible(true);
                    fieldToInject.set(awaitingInjectionInstance, mockCandidate1);
                    mocks.remove(mockCandidate1);
                    break;
                }

                Object mockCandidate2 = candidateFilter.filterCandidate(mocks, fieldToInject, fieldInstance);

                if (mockCandidate2 != null) {
                    FieldType fieldType = new FieldType(fieldToInject);
                    Assert.assertFalse("Field " + fieldType.getName() + " should not be set by property setter",
                            fieldType.isSettableByProperty());
                    fieldToInject.setAccessible(true);
                    fieldToInject.set(awaitingInjectionInstance, mockCandidate2);
                    Assert.assertSame("Field " + fieldType.getName() + " should be equal to the injected mock",
                            mockCandidate2, fieldToInject.get(awaitingInjectionInstance));
                    break;
                }
            }
        }
    }

    @Test
    public void testInjectMockCandidate_NoCandidate() {
        Object awaitingInjectionInstance = mock(TestClass.class);
        Set<Object> mocks = new HashSet<>();
        DefaultInjectionEngine injectionEngine = new DefaultInjectionEngine();

        for (Field field : TestClass.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(InjectMocks.class)) {
                continue;
            }

            Object fieldInstance = field.get(awaitingInjectionInstance);
            Class<?> awaitingInjectionClazz = fieldInstance.getClass();

            for (Field fieldToInject : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
                MockCandidateFilter candidateFilter = injectionEngine.mockCandidateFilter;
                Object mockCandidate = candidateFilter.filterCandidate(mocks, fieldToInject, fieldInstance);

                if (mockCandidate != null) {
                    break;
                }

                Assert.assertNull("Field " + fieldToInject.getName() + " should not be injected",
                        fieldToInject.get(awaitingInjectionInstance));
            }
        }
    }

    private static Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        Field[] declaredFields = awaitingInjectionClazz.getDeclaredFields();
        Arrays.sort(declaredFields, new Comparator<Field>() {
            @Override
            public int compare(Field field1, Field field2) {
                int field1Modifiers = Modifier.getDefaults() | field1.getModifiers();
                int field2Modifiers = Modifier.getDefaults() | field2.getModifiers();

                if ((field1Modifiers & Modifier.TRANSIENT) != 0
                        && (field2Modifiers & Modifier.TRANSIENT) == 0) {
                    return 1;
                } else if ((field2Modifiers & Modifier.TRANSIENT) != 0
                        && (field1Modifiers & Modifier.TRANSIENT) == 0) {
                    return -1;
                } else if (field1.isAnnotationPresent(Override.class)
                        && field2.isAnnotationPresent(Override.class)) {
                    Class<?> field1Type = field1.getType();
                    Class<?> field2Type = field2.getType();

                    if (field1Type.isAssignableFrom(field2Type)) {
                        return 1;
                    } else if (field2Type.isAssignableFrom(field1Type)) {
                        return -1;
                    }
                } else {
                    Class<?> field1Type = field1.getType();
                    Class<?> field2Type = field2.getType();

                    if (field1Type.isAssignableFrom(field2Type)) {
                        return 1;
                    } else if (field2Type.isAssignableFrom(field1Type)) {
                        return -1;
                    }
                }

                return 0;
            }
        });
        return declaredFields;
    }

    static class FieldType {
        private final String name;

        public FieldType(Field field) {
            name = field.getName();
        }

        public String getName() {
            return name;
        }

        public boolean isSettableByProperty() {
            return Modifier.isPublic(Modifier.getModifiers(Field.class.getDeclaredField("modifiers")))
                    && Modifier.isPublic(Modifier.getModifiers(Field.class.getDeclaredField("writable")))
                    && Modifier.isPublic(Modifier.getModifiers(Field.class.getDeclaredField("writableBoolean")));
        }
    }

    static class TestClass {
        @InjectMocks
        private Dependency1 dependency1 = new Dependency1();
        @InjectMocks
        private Dependency2 dependency2 = new Dependency2();
    }

    static class Dependency1 {
        private Dependency3 dependency3;
    }

    static class Dependency2 {
        private Dependency3 dependency3;
    }

    static class Dependency3 {
    }
}