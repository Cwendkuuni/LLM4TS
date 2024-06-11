package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private List<Object> mocks;

    @Before
    public void setup() {
        field = Mockito.spy(Field.class);
        fieldInstance = Mockito.mock(Object.class);
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mocks.get(0);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertFalse(injecter.thenInject()); //should not inject when there's only one mock
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject()); //should not inject with multiple mocks
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithThrowingException() {
        when(field.set(fieldInstance, any())).thenAnswer((Answer<Void>) invocation -> {
            throw new RuntimeException();
        });
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = Collections.emptyList();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject()); //should not inject with an empty Collection
    }
}