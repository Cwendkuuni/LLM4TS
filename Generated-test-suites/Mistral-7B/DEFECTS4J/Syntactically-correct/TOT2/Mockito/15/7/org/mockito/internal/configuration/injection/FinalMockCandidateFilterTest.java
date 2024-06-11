package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private List<Object> mocks;

    @Before
    public void setup() {
        fieldInstance = mock(Object.class);
        field = Mockito.spy(Field.class);
        mocks = Collections.singletonList(mock(Object.class));
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mocks.get(0);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertFalse(injecter.thenInject());

        try {
            new FieldSetter(fieldInstance, field).set(mock);
            assertFalse(injecter.thenInject());
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() {
        Object mock = mocks.get(0);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        try {
            new FieldSetter(fieldInstance, field).set(mock);
            injecter.thenInject();
        } catch (Exception e) {
            // Expected
        }

        injecter.thenInject();
    }
}