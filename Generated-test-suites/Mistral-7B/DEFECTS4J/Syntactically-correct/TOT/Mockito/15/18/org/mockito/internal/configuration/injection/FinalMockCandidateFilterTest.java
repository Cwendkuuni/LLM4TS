package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        fieldInstance = spy(new Object() {});
        field = FieldReflector.getField(fieldInstance, "fieldToInject");
    }

    @Test
    public void testSingleMockInjection() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
        verify(mock1).equals(fieldInstance);
    }

    @Test
    public void testMultipleMocksNoInjection() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testExceptionDuringInjection() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        FilterExceptionThrowingInjecter exceptionThrowingInjecter = spy(new OngoingInjecter() {
            @Override
            public boolean thenInject() {
                throw new RuntimeException("Exception during injection");
            }
        });

        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return exceptionThrowingInjecter.thenInject();
            }
        }).when(mock1).thenAnswer(ArgumentMatchers.anyInt(), ArgumentMatchers.anyField(field.getName()), ArgumentMatchers.any());

        filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertThrows(MockitoException.class, () -> injecter.thenInject());
    }
}

class FilterExceptionThrowingInjecter extends OngoingInjecter {
    @Override
    public boolean thenInject() {
        throw new RuntimeException("Exception during injection");
    }
}