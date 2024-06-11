package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");
        Object mock = mock(Object.class);
        OngoingInjecter expectedInjecter = mock(OngoingInjecter.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(actualInjecter);
        assertNotNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = MockUtil.getField(Object.class, "testField");

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, null);

        // Then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullField() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field nullField = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, nullField, fieldInstance);

        // Then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithInaccessibleField() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field inaccessibleField = new Field("inaccessibleField", Object.class, FinalMockCandidateFilter.class);
        inaccessibleField.setAccessible(true);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, inaccessibleField, fieldInstance);

        // Then
        assertNotNull(actualInjecter);
        assertNull(actualInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullMock() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");
        Object nullMock = null;

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testFilterCandidateWithRuntimeExceptionDuringPropertySetter() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");
        Object mock = mock(Object.class);
        OngoingInjecter injecter = mock(OngoingInjecter.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        new FieldSetter(fieldInstance, field).set(mock);

        // When & Then
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
        } catch (RuntimeException e) {
            new Reporter().cannotInjectDependency(field, mock, e);
        }
    }

    @Test
    public void testFilterCandidateWithRuntimeExceptionDuringFieldAccess() throws Exception {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = mock(Object.class);
        Field field = MockUtil.getField(Object.class, "testField");
        Object mock = mock(Object.class);
        OngoingInjecter injecter = mock(OngoingInjecter.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When & Then
        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            new FieldSetter(fieldInstance, field).set(mock);
        } catch (RuntimeException e) {
            new Reporter().cannotInjectDependency(field, mock, e);
        }
    }
}