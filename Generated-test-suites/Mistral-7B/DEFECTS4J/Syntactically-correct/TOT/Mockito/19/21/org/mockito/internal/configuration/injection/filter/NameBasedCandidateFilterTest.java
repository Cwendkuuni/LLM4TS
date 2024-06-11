import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        filter = new NameBasedCandidateFilter(null);
        mockUtil = Mockito.spy(new MockUtil());
    }

    @Test
    public void testFilterCandidate_OneMockMatchesName_ReturnsThatMock() {
        // Given
        String mockName = "mockName";
        Object mock = mock(Object.class, "mockName");
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock, result.getMock());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
        // Given
        String mockName = "mockName";
        Object mock1 = mock(Object.class, "mockName");
        Object mock2 = mock(Object.class, "mockName");
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Object fieldInstance = new Object();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock2, result.getMock());
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_NoMocksMatchName_DelegatesToNextFilter() {
        // Given
        String mockName = "mockName";
        Object mock = mock(Object.class, "anotherMockName");
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Object fieldInstance = new Object();

        NameBasedCandidateFilter nextFilter = mock(NameBasedCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextFilter, result.getFilter());
    }

    @Test
    public void testFilterCandidate_ConflictingFields_ReturnsCorrectMock() {
        // Given
        String mockName = "mockName";
        Object mock1 = mock(Object.class, "mockName");
        Object mock2 = mock(Object.class, "mockName");
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = new Field("testField1") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Field field2 = new Field("testField2") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Object fieldInstance = new Object();

        // When
        OngoingInjecter result1 = filter.filterCandidate(mocks, field1, fieldInstance);
        OngoingInjecter result2 = filter.filterCandidate(mocks, field2, fieldInstance);

        // Then
        assertEquals(mock1, result1.getMock());
        assertEquals(field1, result1.getField());
        assertEquals(fieldInstance, result1.getFieldInstance());

        assertEquals(mock2, result2.getMock());
        assertEquals(field2, result2.getField());
        assertEquals(fieldInstance, result2.getFieldInstance());
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithSameName_DifferentType_DelegatesToNextFilter() {
        // Given
        String mockName = "mockName";
        Object mock1 = mock(String.class, "mockName");
        Object mock2 = mock(Integer.class, "mockName");
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = new Field("testField") {
            @Override
            public String getName() {
                return mockName;
            }
        };

        Object fieldInstance = new Object();

        NameBasedCandidateFilter nextFilter = mock(NameBasedCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextFilter, result.getFilter());
    }
}