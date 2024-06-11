import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection injectionStrategy;
    private Object mockCandidates;
    private Object fieldInstance;

    @Before
    public void setup() {
        injectionStrategy = new PropertyAndSetterInjection();
        mockCandidates = Arrays.asList(new MockitoTestClass(), new AnotherMockitoTestClass(), new NonMockitoTestClass()).toArray();
        fieldInstance = new TestClassWithInjectMocksField();
    }

    @Test
    public void testProcessInjection_Success() {
        Set<Object> mocks = newMockSafeHashSet(mockCandidates);
        FieldInjectedMock mock = mock(FieldInjectedMock.class);
        when(mockCandidateFilter.filterCandidate(mocks, anyField(), eq(fieldInstance))).thenReturn(mock);

        assertTrue(injectionStrategy.processInjection(new FieldAnnotationWithInjectMocks(), fieldInstance, mocks));
        verify(mock, times(1)).thenInject();
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_Failed_NoCandidatesFound() {
        Set<Object> mocks = newMockSafeHashSet(mockCandidates);
        when(mockCandidateFilter.filterCandidate(mocks, anyField(), eq(fieldInstance))).thenReturn(null);

        assertFalse(injectionStrategy.processInjection(new FieldAnnotationWithInjectMocks(), fieldInstance, mocks));
    }

    @Test(expected = InvocationTargetException.class)
    public void testInitializeInjectMocksField_Failed_ExceptionThrown() {
        Field field = mock(Field.class);
        Object fieldOwner = mock(Object.class);
        when(field.getType()).thenReturn(Object.class);
        when(fieldOwner.getClass()).thenReturn(TestClassWithInjectMocksField.class);
        when(fieldOwner.getName()).thenReturn("testField");
        when(field.getName()).thenReturn("testField");

        assertThrows(InvocationTargetException.class, () -> injectionStrategy.initializeInjectMocksField(field, fieldOwner));
    }

    @Test
    public void testInjectMockCandidates_Success() {
        Class<?> awaitingInjectionClazz = TestClassWithInjectMocksField.class;
        Set<Object> mocks = newMockSafeHashSet(mockCandidates);
        Object instance = fieldInstance;

        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

        FieldInjectedMock mock1 = mock(FieldInjectedMock.class);
        FieldInjectedMock mock2 = mock(FieldInjectedMock.class);
        when(mockCandidateFilter.filterCandidate(mocks, anyField(), eq(instance))).thenReturn(mock1).thenReturn(mock2);
        when(mock1.thenInject()).thenReturn(mock1);

        assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
        verify(mock1, times(1)).thenInject();
        verify(mock2, never()).thenInject();
    }

    @Test
    public void testInjectMockCandidates_NoCandidatesFound() {
        Class<?> awaitingInjectionClazz = TestClassWithInjectMocksField.class;
        Set<Object> mocks = newMockSafeHashSet(mockCandidates);
        Object instance = fieldInstance;

        List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

        when(mockCandidateFilter.filterCandidate(mocks, anyField(), eq(instance))).thenReturn(null);

        assertFalse(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mocks, instance));
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injectionStrategy.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }

    @SuppressWarnings("unused")
    private static class TestClassWithInjectMocksField {
        @InjectMocks
        private FieldInjectedMock fieldInjectedMock;

        private FieldAnnotationWithInjectMocks fieldAnnotationWithInjectMocks;
    }

    @SuppressWarnings("unused")
    private static class FieldAnnotationWithInjectMocks {
        @InjectMocks
        private FieldInjectedMock fieldInjectedMock;
    }

    @SuppressWarnings("unused")
    private static class FieldInjectedMock {

    }

    @SuppressWarnings("unused")
    private static class MockitoTestClass implements MockCandidateFilter {
        @Override
        public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
            return this;
        }

        @Override
        public Object thenInject() {
            return newInstanceOf(FieldInjectedMock.class);
        }
    }

    @SuppressWarnings("unused")
    private static class AnotherMockitoTestClass implements MockCandidateFilter {
        @Override
        public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
            return this;
        }

        @Override
        public Object thenInject() {
            return newInstanceOf(FieldInjectedMock.class);
        }
    }

    @SuppressWarnings("unused")
    private static class NonMockitoTestClass implements MockCandidateFilter {
        @Override
        public Object filterCandidate(Set<Object> mocks, Field field, Object instance) {
            return null;
        }

        @Override
        public Object thenInject() {
            throw new UnsupportedOperationException();
        }
    }
}