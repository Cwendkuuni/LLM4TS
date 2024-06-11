import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil = new MockUtil();

    @Before
    public void setup() {
        filter = new NameBasedCandidateFilter(null);
    }

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        Object fieldInstance = new Object();
        Field field = Mockito.field(Object.class, "fieldName");

        List<Object> mocks = Collections.emptyList();

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NullMocksCollection() {
        Object fieldInstance = new Object();
        Field field = Mockito.field(Object.class, "fieldName");

        List<Object> mocks = null;

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NullField() {
        Object mock = Mockito.mock(Object.class);
        Object fieldInstance = new Object();

        List<Object> mocks = Collections.singletonList(mock);

        // When
        Object result = filter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Object mock = Mockito.mock(Object.class);
        Object fieldInstance = null;
        Field field = Mockito.field(Object.class, "fieldName");

        List<Object> mocks = Collections.singletonList(mock);

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
    }

    // Previously written test case for normal functionality
    @Test
    public void testFilterCandidate() {
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Object fieldInstance = new Object();
        Field field = Mockito.field(Object.class, "fieldName");

        List<Object> mocks = Arrays.asList(mock1, mock2);

        // Given
        when(mockUtil.getMockName(mock1)).thenReturn("fieldName1");
        when(mockUtil.getMockName(mock2)).thenReturn("fieldName2");

        // When
        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertSame(mock1, result);
    }
}