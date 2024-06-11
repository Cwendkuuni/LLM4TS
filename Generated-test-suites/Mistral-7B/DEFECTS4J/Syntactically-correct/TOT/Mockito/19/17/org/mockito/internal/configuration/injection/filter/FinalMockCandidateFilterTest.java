package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Reporter reporter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        reporter = MockitoStubs.spy(Reporter.class);
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        Field field = MockitoStubs.spy(Field.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).setField(fieldInstance, field); // BeanPropertySetter
        verifyZeroInteractions(mock2); // FieldSetter is not called since there's only one mock
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = MockitoStubs.spy(Field.class);

        filter.filterCandidate(mocks, field, fieldInstance);

        // No interaction is expected since the method returns null and we don't care about the injected mock
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        Field field = MockitoStubs.spy(Field.class);

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail("Should throw exception when unable to inject");
        } catch (RuntimeException e) {
            // Expected
            new Reporter().cannotInjectDependency(field, mock1, e);
        }
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        Field field = MockitoStubs.spy(Field.class);

        try {
            filter.filterCandidate(null, field, fieldInstance);
            fail("Should throw exception when mocks are null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}