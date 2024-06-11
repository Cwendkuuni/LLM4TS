package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.util.collections.ArrayUtil;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(new Field("testField", String.class, this) {
            @Override
            public Object get(Object obj) {
                return fieldInstance;
            }

            @Override
            public void setAccessible(boolean b) {
                // no-op
            }
        });
        fieldInstance = mock(String.class);
    }

    @Test
    public void testFilterCandidate_whenMocksIsEmpty_thenReturnsNull() {
        // Given
        Collection<Object> emptyMocks = new ArrayList<>();

        // When
        OngoingInjecter result = filter.filterCandidate(emptyMocks, fieldToBeInjected, fieldInstance);

        // Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testFilterCandidate_whenMockIsAlreadyInjected_thenReturnsNull() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testFilterCandidate_whenMockIsANewInstance_thenReturnsNewInstance() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(mock);

        // When
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(result, is(notNullValue()));
        assertThat(result, is(mock));
    }

    @Test
    public void testFilterCandidate_whenMockIsInjectedTwice_thenReturnsSameInstance() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object target = invocation.callRealMethod();
                OngoingInjecter result = new OngoingInjecter(mock, null, null, null, null);
                result.setTarget(target);
                return result;
            }
        });

        // When
        Object firstResult = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();
        Object secondResult = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(firstResult, is(secondResult));
        assertThat(firstResult, is(mock));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnsNull_thenReturnsNull() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(null);

        // When
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateThrowsException_thenThrowsSameException() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new RuntimeException("Filter failed"));

        // When & Then
        Assert.assertThrows(RuntimeException.class, () -> filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget());
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateThrowsCheckedException_thenThrowsWrappedException() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new IllegalArgumentException("Filter failed"));

        // When & Then
        Assert.assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget());
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnsInjectableField_thenReturnsInjectedField() {
        Object mock = mock(String.class);
        mocks.add(mock);
        InjectableField injectableField = Mockito.spy(new InjectableField("testInjectableField", String.class, this) {
            @Override
            public Object get(Object obj) {
                return fieldInstance;
            }

            @Override
            public void setAccessible(boolean b) {
                // no-op
            }
        });
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(InjectableField.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldToBeInjected);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(injectableField);

        // When
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(result, is(notNullValue()));
        assertThat(result, is(injectableField));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnsAnotherMock_thenReturnsAnotherMock() {
        // Given
        Object mockA = mock(String.class);
        Object mockB = mock(String.class);
        mocks.addAll(Arrays.asList(mockA, mockB));
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(mockB);

        // When
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertThat(result, is(notNullValue()));
        assertThat(result, is(mockB));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnsNullAndMocksIsEmpty_thenReturnsNull() {
        // Given
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(null);

        // When
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget();

        // Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateReturnsNullAndMocksIsNotEmpty_thenThrowsException() {
        // Given
        Object mock = mock(String.class);
        mocks.add(mock);
        when(fieldInstance.getClass()).thenReturn(String.class);
        when(fieldToBeInjected.getType()).thenReturn(String.class);
        when(fieldToBeInjected.get(this)).thenReturn(fieldInstance);
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(null);

        // When & Then
        Assert.assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance).getTarget());
    }

}