package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidateWithMatchingType() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mockWithMatchingType = mock(SomeClass.class);
        mocks.add(mockWithMatchingType);
        Field field = new Field("testField", SomeClass.class);
        Object fieldInstance = spy(new SomeClass());

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(mockWithMatchingType, result.getMock());
    }

    @Test
    public void testFilterCandidateWithNoMatchingType() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mockWithMismatchingType = mock(OtherClass.class);
        mocks.add(mockWithMismatchingType);
        Field field = new Field("testField", SomeClass.class);
        Object fieldInstance = spy(new SomeClass());

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithMultipleMatchingTypes() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mock1WithMatchingType = mock(SomeClass.class);
        Object mock2WithMatchingType = mock(SomeClass.class);
        mocks.add(mock1WithMatchingType);
        mocks.add(mock2WithMatchingType);
        Field field = new Field("testField", SomeClass.class);
        Object fieldInstance = spy(new SomeClass());

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        List<Object> resultMockTypes = getMockTypes(result);
        assertEquals(2, resultMockTypes.size());
        assertTrue(resultMockTypes.contains(mock1WithMatchingType));
        assertTrue(resultMockTypes.contains(mock2WithMatchingType));
    }

    @Test
    public void testFilterCandidateWithNextFilterReturningAResult() {
        // given
        Collection<Object> mocks = new ArrayList<>();
        Object mockWithMatchingType = mock(SomeClass.class);
        Object mockWithMismatchingType = mock(OtherClass.class);
        mocks.add(mockWithMatchingType);
        mocks.add(mockWithMismatchingType);
        Field field = new Field("testField", SomeClass.class);
        Object fieldInstance = spy(new SomeClass());

        // when
        OngoingInjecter nextResult = mock(OngoingInjecter.class);
        when(nextMock.filterCandidate(eq(mocks), eq(field), eq(fieldInstance))).thenReturn(nextResult);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertNotNull(result);
        assertEquals(nextResult, result);
    }

    private static List<Object> getMockTypes(OngoingInjecter result) {
        return (List<Object>) ReflectionUtils.getFieldValue(result, "mock");
    }
}