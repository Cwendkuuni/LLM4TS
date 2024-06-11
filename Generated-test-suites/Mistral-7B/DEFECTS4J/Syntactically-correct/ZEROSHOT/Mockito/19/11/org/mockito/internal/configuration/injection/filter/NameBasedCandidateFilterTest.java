package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MockInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.mock;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        nextMock = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_oneMock_noConflictingFields() {
        Object mock = mock(String.class);
        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(mock).toString();
            }
        };
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);

        assertEquals(mock, result.getMock());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_noMocks_noConflictingFields() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field("testField") {
            @Override
            public String getName() {
                return "TestField";
            }
        };
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(null, result.getMock());
        assertEquals(null, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_oneMock_conflictingFields() {
        Object mock = mock(String.class);
        Object conflictingMock = mock(String.class);
        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(mock).toString();
            }
        };
        Field conflictingField = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(conflictingMock).toString();
            }
        };
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        mocks.add(conflictingMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(conflictingMock, result.getMock());
        assertEquals(conflictingField, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noConflictingFields() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(mock1).toString();
            }
        };
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result.getMock());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_multipleMocks_conflictingFields() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        Object conflictingMock = mock(String.class);

        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(mock1).toString();
            }
        };
        Field conflictingField = new Field("testField") {
            @Override
            public String getName() {
                return mockUtil.getMockName(conflictingMock).toString();
            }
        };

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(conflictingMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertEquals(conflictingMock, result.getMock());
        assertEquals(conflictingField, result.getField());
        assertEquals(new Object(), result.getFieldInstance());
    }
}