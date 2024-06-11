import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.internal.util.collections.Sets;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PropertyAndSetterInjectionTest {

    @Mock
    private SomeClass someMock;

    @InjectMocks
    private PropertyAndSetterInjection injection;

    private Set<Object> mockCandidates = Sets.newMockSafeHashSet();

    @Before
    public void setupMocks() {
        mockCandidates.add(someMock);
    }

    @Test
    public void testProcessInjection_FieldAnnotatedWithInjectMocks_Success() throws Exception {
        // Given
        Field injectMocksField = Whitebox.getInternalState(injection, "injectMocksField");
        assertNull(injectMocksField.get(injection));

        // When
        boolean injectionOccurred = injection.processInjection(injectMocksField, injection, mockCandidates);
        assertTrue(injectionOccurred);

        // Then
        assertNotNull(injectMocksField.get(injection));
        Mockito.verify(someMock).someMethod(); // verify the mock is used by the strategy
    }

    @Test
    public void testProcessInjection_FieldAnnotatedWithInjectMocks_NoCandidateFound() throws Exception {
        // Given
        Field injectMocksField = Whitebox.getInternalState(injection, "injectMocksField");
        assertNull(injectMocksField.get(injection));

        // When & Then
        boolean injectionOccurred = injection.processInjection(injectMocksField, injection, Collections.emptySet());
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(injection));
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjection_FieldAnnotatedWithInjectMocks_InvalidCandidateType() throws Exception {
        // Given
        Field injectMocksField = Whitebox.getInternalState(injection, "injectMocksField");
        injectMocksField.set(injection, new Object());

        // When
        Set<Object> mockCandidates = new HashSet<>();
        mockCandidates.add(someMock);
        boolean injectionOccurred = injection.processInjection(injectMocksField, injection, mockCandidates);

        // Then
        assertFalse(injectionOccurred);
        assertNull(injectMocksField.get(injection));
        Mockito.verifyNoInteractions(someMock);

        // When & Then (error scenario)
        injectionOccurred = injection.processInjection(injectMocksField, injection, mockCandidates);
    }
}

class SomeClass {
    public void someMethod() {}
}