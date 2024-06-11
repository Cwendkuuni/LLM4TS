import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoConfigurationException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    @InjectMocks
    private PropertyAndSetterInjection injection;

    @Mock
    private TypeBasedCandidateFilter typeBasedCandidateFilterMock;

    @Mock
    private NameBasedCandidateFilter nameBasedCandidateFilterMock;

    @Mock
    private FinalMockCandidateFilter finalMockCandidateFilterMock;

    @Mock
    private ListUtil listUtilMock;

    @Mock
    private FieldInitializer fieldInitializerMock;

    @Mock
    private Reporter reporterMock;

    @Mock
    private Object fieldInstanceNeedingInjectionMock;

    @Mock
    private Class<?> awaitingInjectionClazzMock;

    @Mock
    private Set<Object> mockCandidatesMock;

    @Before
    public void setup() {
        when(typeBasedCandidateFilterMock.filterCandidate(ArgumentMatchers.any(Set.class), ArgumentMatchers.any(Field.class), ArgumentMatchers.any(Object.class))).thenReturn(fieldInstanceNeedingInjectionMock);
        when(nameBasedCandidateFilterMock.filterCandidate(ArgumentMatchers.any(Set.class), ArgumentMatchers.any(Field.class), ArgumentMatchers.anyObject())).thenReturn(fieldInstanceNeedingInjectionMock);
        when(finalMockCandidateFilterMock.filterCandidate(ArgumentMatchers.any(Set.class), ArgumentMatchers.any(Field.class), ArgumentMatchers.anyObject())).thenReturn(fieldInstanceNeedingInjectionMock);
        when(listUtilMock.filter(ArgumentMatchers.anyList(), ArgumentMatchers.any(ListUtil.Filter.class))).thenReturn(new ArrayList<>());
        when(fieldInitializerMock.initialize()).thenReturn(new FieldInitializationReport(fieldInstanceNeedingInjectionMock, awaitingInjectionClazzMock));
        when(reporterMock.fieldInitialisationThrewException(ArgumentMatchers.any(Field.class), ArgumentMatchers.any(Throwable.class))).thenThrow(new MockitoConfigurationException("MISSING_FIELD_ initialization_EXCEPTION"));
    }

    @Test
    public void testProcessInjection_InitializesFieldWithSetter() {
        // Given
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = mock(Object.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        verify(fieldInitializerMock).initialize();
        assertEquals(true, injectionOccurred);
    }

    @Test(expected = MockitoConfigurationException.class)
    public void testProcessInjection_InitializationThrowsException() {
        // Given
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = mock(Object.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();
        when(reporterMock.fieldInitialisationThrewException(injectMocksField, ArgumentMatchers.any(Throwable.class))).thenThrow(new MockitoConfigurationException("MISSING_FIELD_ initialization_EXCEPTION"));

        // When & Then
        injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    }

    @Test
    public void testProcessInjection_NoMockCandidatesFound() {
        // Given
        Field injectMocksField = mock(Field.class);
        Object injectMocksFieldOwner = mock(Object.class);
        Set<Object> mockCandidates = Sets.newMockSafeHashSet();

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Then
        verifyNoInteractions(fieldInitializerMock);
        assertEquals(false, injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_FieldWithSetterInjected() {
        // Given
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = Sets.newMockSafeHashSet();
        Object instance = mock(Object.class);
        List<Field> orderedInstanceFields = Arrays.asList(mock(Field.class), mock(Field.class));

        // When
        boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        verify(typeBasedCandidateFilterMock).filterCandidate(mocks, ArgumentMatchers.any(Field.class), ArgumentMatchers.anyObject());
        verify(nameBasedCandidateFilterMock).filterCandidate(mocks, ArgumentMatchers.any(Field.class), ArgumentMatchers.anyObject());
        verify(finalMockCandidateFilterMock).filterCandidate(mocks, ArgumentMatchers.any(Field.class), ArgumentMatchers.anyObject());
        verify(listUtilMock).filter(orderedInstanceFields, ArgumentMatchers.any(ListUtil.Filter.class));
        verifyNoMoreInteractions(typeBasedCandidateFilterMock, nameBasedCandidateFilterMock, finalMockCandidateFilterMock, listUtilMock, fieldInitializerMock);
        assertEquals(true, injectionOccurred);
    }

    @Test
    public void testInjectMockCandidatesOnFields_FieldWithNoMockCandidates() {
        // Given
        Class<?> awaitingInjectionClazz = mock(Class.class);
        Set<Object> mocks = Sets.newMockSafeHashSet();
        Object instance = mock(Object.class);
        List<Field> orderedInstanceFields = Arrays.asList(mock(Field.class), mock(Field.class));

        // When
        boolean injectionOccurred = injection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

        // Then
        verifyNoInteractions(typeBasedCandidateFilterMock, nameBasedCandidateFilterMock, finalMockCandidateFilterMock, listUtilMock, fieldInitializerMock);
        assertEquals(false, injectionOccurred);
    }
}