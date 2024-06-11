import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.testutils.FieldTestUtils;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.testutils.FieldTestUtils.fieldWithName;
import static org.mockito.internal.util.reflection.testutils.FieldTestUtils.fieldWithType;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;
    private ListUtil.Filter<Field> notFinalOrStatic;
    private Field testField;
    private Object testFieldOwner;
    private Set<Object> mockCandidates;

    @Before
    public void setup() {
        notFinalOrStatic = new ListUtil.Filter<Field>() {
            public boolean isOut(Field object) {
                return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
            }
        };

        testField = fieldWithName("testField", Object.class);
        testFieldOwner = new Object();
        mockCandidates = new HashSet<>();
        mockCandidates.add(new MockObject());
        mockCandidates.add(new OtherMockObject());
    }

    @Test
    public void testProcessInjection() throws Exception {
        // Given
        FieldInitializationReport report = new FieldInitializer(testFieldOwner, testField).initialize();
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(testField, testFieldOwner, mockCandidates);

        // Then
        assertNotNull(report.fieldInstance());
        assertTrue(injectionOccurred);
    }

    @Test
    public void testProcessInjection_noMockFound() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();

        // When
        boolean injectionOccurred = propertyAndSetterInjection.processInjection(testField, testFieldOwner, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
    }

    private static class MockObject {
        // Empty class
    }

    private static class OtherMockObject {
        // Empty class
    }

    @Test
    public void testInjectMockCandidates() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(fieldWithName("field1", String.class), fieldWithName("field2", Integer.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_noMockFound() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(fieldWithType(String.class), fieldWithType(Integer.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_moreThanOneMockFound() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(new MockObject1());
        mocks.add(new MockObject2());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(fieldWithName("field1", String.class), fieldWithName("field2", Integer.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertFalse(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_mockFoundByType() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(new MockObjectType());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(fieldWithType(MockObjectType.class), fieldWithType(Integer.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
    }

    @Test
    public void testInjectMockCandidates_mockFoundByName() {
        // Given
        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
        Set<Object> mocks = new HashSet<>();
        mocks.add(new MockObjectName());
        Object instance = new Object();
        List<Field> orderedInstanceFields = Arrays.asList(fieldWithName("mockObjectName", MockObjectName.class), fieldWithType(Integer.class));

        // When
        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        assertTrue(injectionOccurred);
    }

    private static class MockObjectType {
        // Empty class
    }

    private static class MockObjectName {
        // Empty class with a name annotation
    }
}