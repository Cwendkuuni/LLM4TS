import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule powerMockRule = new PowerMockRule();

    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Object mock1;
    private Object mock2;

    @Before
    public void setUp() throws Exception {
        AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
        Class<FinalMockCandidateFilter> finalMockCandidateFilterClass = classLoader.loadClass("org.mockito.internal.configuration.injection.FinalMockCandidateFilter");
        finalMockCandidateFilter = (FinalMockCandidateFilter) finalMockCandidateFilterClass.getDeclaredConstructor().newInstance();

        mocks = new ArrayList<>();
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        field = new Field("testField", Object.class, Object.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_oneMock_success() {
        when(mock1.getClass()).thenReturn(Object.class);
        when(field.isAccessible()).thenReturn(true);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).getClass();
        verify(field).isAccessible();
        verify(mock1).getClass();
        verify(mock1).getClass(); // Verify that getClass() is called twice

        Answer<Boolean> answer = Whitebox.getInternalState(injecter, "thenInjectAnswer");
        assertTrue(answer.answer(null));
        verify(mock1).getClass(); // Verify that getClass() is called after the injection
    }

    @Test
    public void testFilterCandidate_oneMock_failure() {
        when(mock1.getClass()).thenReturn(Object.class);
        when(field.isAccessible()).thenReturn(true);
        when(mock1.getClass()).thenThrow(new RuntimeException("Test Exception"));

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).getClass();
        verify(mock1).getClass();
        verify(mock1).getClass(); // Verify that getClass() is called three times

        MockitoException mockitoException = assertThrows(MockitoException.class, () -> {
            Whitebox.invokeMethod(injecter, "thenInject");
        });

        assertEquals("Problems injecting dependency in testField", mockitoException.getMessage());
        verify(mock1, never()).getClass(); // Verify that getClass() is not called after the exception
    }

    @Test
    public void testFilterCandidate_multipleMocks_success() {
        when(mock1.getClass()).thenReturn(Object.class);
        when(mock2.getClass()).thenReturn(Object.class);
        when(field.isAccessible()).thenReturn(true);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1, never()).getClass();
        verify(mock2, never()).getClass();

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        when(mock1.getClass()).thenReturn(Object.class);
        when(mock2.getClass()).thenReturn(Object.class);
        when(field.isAccessible()).thenReturn(true);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1, never()).getClass();
        verify(mock2, never()).getClass();

        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_noAccessibleField() {
        field = new Field("nonAccessibleField", Object.class, Object.class);
        field.setAccessible(false);

        when(mock1.getClass()).thenReturn(Object.class);

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
        verify(mock1, never()).getClass();
    }

    @Test
    public void testFilterCandidate_noMockCollection() {
        finalMockCandidateFilter = spy(finalMockCandidateFilter);
        doReturn(null).when(finalMockCandidateFilter).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));

        MockitoException mockitoException = assertThrows(MockitoException.class, () -> {
            PowerMockito.mockStatic(FinalMockCandidateFilter.class);
            PowerMockito.whenNew(FinalMockCandidateFilter.class).withArguments(any(Collection.class), any(Field.class), any(Object.class)).thenReturn(finalMockCandidateFilter);
            PowerMockito.mockStatic(org.mockito.internal.util.reflection.FieldSetter.class);
            PowerMockito.when(FieldSetter.setterFor(any(Field.class), any(Object.class), any(Object.class))).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    throw new RuntimeException("Test Exception");
                }
            });
            Whitebox.invokeMethod(finalMockCandidateFilter, "filterCandidate", mocks, field, fieldInstance);
        });

        assertEquals("Problems injecting dependency in non-accessible field: testField", mockitoException.getMessage());
    }
}