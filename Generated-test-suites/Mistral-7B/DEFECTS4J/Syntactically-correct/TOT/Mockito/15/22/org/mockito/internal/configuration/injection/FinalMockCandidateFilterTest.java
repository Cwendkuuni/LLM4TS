
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private List<Object> mocks;

    @Before
    public void setUp() {
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        filter = spy(new FinalMockCandidateFilter());
    }

    @Test
    public void testFilterCandidate_SingleMock_Success() {
        Object mock = mock(Object.class);

        when(mocks.size()).thenReturn(1);
        when(mocks.get(0)).thenReturn(mock);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock).then(ArgumentMatchers.any(InvocationOnMock.class)).answers(invocation -> {
            new FieldSetter(fieldInstance, field).set(mock);
            return null;
        });

        assertTrue(actual.thenInject());
    }

    @Test
    public void testFilterCandidate_SingleMock_Failure() {
        Object mock = mock(Object.class);

        when(mocks.size()).thenReturn(1);
        when(mocks.get(0)).thenReturn(mock);
        when(mock.then(Mockito.any(FieldSetter.class))).thenThrow(new RuntimeException("Test Exception"));

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actual);
        verify(mock, never()).set(fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Failure() {
        assertFalse(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
        verify(mocks, never()).get(0);
    }

    @Test
    public void testFilterCandidate_SingleMock_Success_WithCustomFieldSetter() {
        Object mock = mock(Object.class);
        Stubber stubber = Mockito.doNothing().when(mock).set(fieldInstance);

        when(mocks.size()).thenReturn(1);
        when(mocks.get(0)).thenReturn(mock);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock).then(ArgumentMatchers.any(InvocationOnMock.class)).then(stubber);

        assertTrue(actual.thenInject());
        stubber.verify();
    }

    @Test
    public void testFilterCandidate_SingleMock_Success_WithCustomFieldSetter_Failure() {
        Object mock = mock(Object.class);
        Stubber stubber = Mockito.doThrow(new RuntimeException("Test Exception")).when(mock).set(fieldInstance);

        when(mocks.size()).thenReturn(1);
        when(mocks.get(0)).thenReturn(mock);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(actual);
        verify(mock, never()).set(fieldInstance);
        stubber.verify();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_Success() {
        Object mock1 = mock(Object.class);

        when(mocks.size()).thenReturn(2);
        when(mocks.get(0)).thenReturn(mock1);

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1, never()).set(fieldInstance);
        assertTrue(actual.thenInject());
    }
}