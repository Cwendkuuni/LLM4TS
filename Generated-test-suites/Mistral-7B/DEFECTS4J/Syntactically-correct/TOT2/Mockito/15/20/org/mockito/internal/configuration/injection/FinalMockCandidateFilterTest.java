package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock;
    private Field field;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = new Object();
        mock = Mockito.mock(Object.class);
        field = new Field("testField", Object.class, fieldInstance.getClass());
    }

    @Test
    public void testFilterCandidateOneMock() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(mock, "testValue");
        assertEquals("testValue", field.get(fieldInstance));
    }

    @Test(expected = MockitoException.class)
    public void testExceptionDuringFieldSetter() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject(); // this line will throw an exception
    }

    @Test
    public void testFilterCandidateNoMatchingMock() {
        List<Object> mocks = new ArrayList<>();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        mocks.add(Mockito.mock(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }
}