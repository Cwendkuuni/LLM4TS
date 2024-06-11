import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjection() throws Exception {
        Class mockitoClass = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjection");
        PropertyAndSetterInjection propertyAndSetterInjection = (PropertyAndSetterInjection) mockitoClass.newInstance();

        Object mockOwner = Mockito.spy(new Object());
        Field injectMocksField = Mockito.field(mockOwner, "fieldToBeInjected");
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new Mockito(), new Mockito()});

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, mockOwner);

        // Test basic functionality
        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates));

        // Test edge cases and exception handling
        MockCandidateFilter mockCandidateFilterSpy = spy(propertyAndSetterInjection.mockCandidateFilter);
        doReturn(Collections.emptyList()).when(mockCandidateFilterSpy).filterCandidates(any(Set.class), any(Field.class), any(Object.class));
        doThrow(new MockitoException("Mock not found")).when(mockCandidateFilterSpy).filterCandidate(any(Set.class), any(Field.class), any(Object.class)).thenCallRealMethod();

        assertFalse(propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates));
        verify(mockCandidateFilterSpy).filterCandidate(eq(mockCandidates), eq(injectMocksField), eq(mockOwner));
        verifyNoMoreInteractions(mockCandidateFilterSpy);

        verifyNoMoreInteractions(propertyAndSetterInjection);

        // Test mock candidate with the same type and name
        mockCandidateFilterSpy = spy(propertyAndSetterInjection.mockCandidateFilter);
        doReturn(Collections.singletonList(new Mockito())).when(mockCandidateFilterSpy).filterCandidates(any(Set.class), any(Field.class), any(Object.class));

        MockitoException exception = assertThrows(MockitoException.class, () -> propertyAndSetterInjection.processInjection(injectMocksField, mockOwner, mockCandidates));
        String expectedMessage = "More than one mock candidate with the same type and name (fieldName = fieldToBeInjected, type = class org.mockito.Mockito)";
        assertEquals(expectedMessage, exception.getMessage());

        verify(mockCandidateFilterSpy).filterCandidates(eq(mockCandidates), eq(injectMocksField), eq(mockOwner));
        verifyNoMoreInteractions(mockCandidateFilterSpy);

        verifyNoMoreInteractions(propertyAndSetterInjection);
    }

    @Test
    public void testInitializeInjectMocksField() throws Exception {
        Class mockitoClass = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjection");
        PropertyAndSetterInjection propertyAndSetterInjection = (PropertyAndSetterInjection) mockitoClass.newInstance();

        Object mockOwner = Mockito.spy(new Object());
        Field injectMocksField = Mockito.field(mockOwner, "fieldToBeInjected");

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, mockOwner);

        assertNotNull(report.getFieldInstance());
        assertNotNull(report.getFieldClass());
    }

    @Test
    public void testInjectMockCandidatesOnFields() throws Exception {
        Class mockitoClass = Class.forName("org.mockito.internal.configuration.injection.PropertyAndSetterInjection");
        PropertyAndSetterInjection propertyAndSetterInjection = (PropertyAndSetterInjection) mockitoClass.newInstance();

        Object mockOwner = Mockito.spy(new Object());
        Field injectMocksField = Mockito.field(mockOwner, "fieldToBeInjected");
        Set<Object> mockCandidates = newMockSafeHashSet(new Object[]{new Mockito(), new Mockito()});

        List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(Mockito.class);

        boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, mockOwner, false, orderedInstanceFields);

        assertTrue(injectionOccurred);

        verify(propertyAndSetterInjection).filterCandidate(eq(mockCandidates), any(Field.class), eq(mockOwner));
    }
}