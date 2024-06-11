package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    private static final String CLASS_WITH_MOCKED_FIELDS = "test.classes.WithMockedFields";
    private static final String FIELD_TO_BE_MOCKED_1 = "fieldToBeMocked1";
    private static final String FIELD_TO_BE_MOCKED_2 = "fieldToBeMocked2";
    private static final String FIELD_TO_BE_MOCKED_3 = "fieldToBeMocked3";

    @Test
    public void testInjectMocksOnFields() {
        Object testClassInstance = setupTestClassInstance();
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = getDeclaredFieldsAnnotatedWithInjectMocks(testClassInstance.getClass());
        Set<Object> mocks = new HashSet<>();
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        Assert.assertEquals(3, mocks.size());

        Object mockedFieldToBeMocked1 = mocks.iterator().next();
        Assert.assertNotNull(mockedFieldToBeMocked1);
        Object mockedFieldToBeMocked2 = mocks.iterator().next();
        Assert.assertNotNull(mockedFieldToBeMocked2);
        Object mockedFieldToBeMocked3 = mocks.iterator().next();
        Assert.assertNotNull(mockedFieldToBeMocked3);

        // Verify that fields have been injected with the correct mocks
        Field fieldToBeMocked1 = getField(testClassInstance.getClass(), FIELD_TO_BE_MOCKED_1);
        Assert.assertNotNull(fieldToBeMocked1);
        Assert.assertSame(mockedFieldToBeMocked1, Whitebox.getInternalState(fieldToBeMocked1, testClassInstance));

        Field fieldToBeMocked2 = getField(testClassInstance.getClass(), FIELD_TO_BE_MOCKED_2);
        Assert.assertNotNull(fieldToBeMocked2);
        Assert.assertSame(mockedFieldToBeMocked2, Whitebox.getInternalState(fieldToBeMocked2, testClassInstance));

        Field fieldToBeMocked3 = getField(testClassInstance.getClass(), FIELD_TO_BE_MOCKED_3);
        Assert.assertNotNull(fieldToBeMocked3);
        Assert.assertSame(mockedFieldToBeMocked3, Whitebox.getInternalState(fieldToBeMocked3, testClassInstance));
    }

    @Test
    public void testInjectMockCandidateWhenNoCandidateFoundByType() {
        Object testClassInstance = setupTestClassInstance();
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        // Mock candidate filter should not find any candidate for the given type
        Class<?> classWithoutMockCandidate = Class.forName("test.classes.WithoutMockCandidate");
        Set<Field> injectMocksFields = getDeclaredFieldsAnnotatedWithInjectMocks(testClassInstance.getClass());
        Set<Object> mocks = new HashSet<>();
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        Assert.assertEquals(0, mocks.size());
    }

    @Test
    public void testInjectMockCandidateWhenMultipleCandidatesFoundByName() {
        Object testClassInstance = setupTestClassInstance();
        DefaultInjectionEngine defaultInjectionEngine = new DefaultInjectionEngine();

        // This class contains multiple candidates with the same name but different types
        Class<?> classWithMultipleCandidatesByName = Class.forName("test.classes.WithMultipleCandidatesByName");
        Set<Field> injectMocksFields = getDeclaredFieldsAnnotatedWithInjectMocks(testClassInstance.getClass());
        Set<Object> mocks = new HashSet<>();
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // As there are multiple candidates with the same name, no exception should be thrown
        Assert.assertEquals(3, mocks.size());
    }

    private void setupMocks() {
        MockitoJUnitRunner runner = new MockitoJUnitRunner();
        runner.startDefaultAutoMocking();
    }

    private Object setupTestClassInstance() {
        try {
            Class<?> testClass = Class.forName(CLASS_WITH_MOCKED_FIELDS);
            return testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Field> getDeclaredFieldsAnnotatedWithInjectMocks(Class<?> clazz) {
        return getFieldsAnnotatedWith(clazz, InjectMocks.class);
    }

    private Set<Field> getFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        Set<Field> fields = new HashSet<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

class InjectMocks {
    // Empty
}