import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate() {
		// Basic test
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(MockUtil.class, "mockName1");
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Object mock1 = Mockito.spy(MockUtil.class);
		Mockito.when(mock1.getMockName()).thenReturn("mockName1");
		mocks.add(mock1);
		Object mock2 = Mockito.spy(MockUtil.class);
		Mockito.when(mock2.getMockName()).thenReturn("mockName2");
		mocks.add(mock2);

		Field field1 = new Field(MockUtil.class, "mockName1");
		Field field2 = new Field(MockUtil.class, "mockName3");

		Object result = filter.filterCandidate(mocks, field1, fieldInstance);
		assertEquals(mock1, result);

		result = filter.filterCandidate(mocks, field2, fieldInstance);
		assertEquals(mock2, result);
	}

	@Test
	public void testMultipleInstancesOfSameMockName() {
		// Test for multiple instances of the same mock name
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(MockUtil.class, "mockName1");
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Object mock1 = Mockito.spy(MockUtil.class);
		Mockito.when(mock1.getMockName()).thenReturn("mockName1");
		mocks.add(mock1);
		Object mock2 = Mockito.spy(MockUtil.class);
		Mockito.when(mock2.getMockName()).thenReturn("mockName1");
		mocks.add(mock2);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock1, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyCollectionOfMocks() {
		// Test for empty collection of mocks
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(MockUtil.class, "mockName1");
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		fail("Should have thrown an IllegalArgumentException");
	}

	@Test(expected = NullPointerException.class)
	public void testNullMocks() {
		// Test for null mocks
		Collection<Object> mocks = null;
		Field field = new Field(MockUtil.class, "mockName1");
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		fail("Should have thrown a NullPointerException");
	}

	@Test(expected = NullPointerException.class)
	public void testNullField() {
		// Test for null field
		Collection<Object> mocks = new ArrayList<>();
		Field nullField = null;
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, nullField, fieldInstance);
		fail("Should have thrown a NullPointerException");
	}

	@Test(expected = NullPointerException.class)
	public void testNullFieldInstance() {
		// Test for null fieldInstance
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(MockUtil.class, "mockName1");
		Object nullFieldInstance = null;
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, field, nullFieldInstance);
		fail("Should have thrown a NullPointerException");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIncorrectFieldType() {
		// Test for incorrect field type
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(String.class, "mockName1");
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		fail("Should have thrown an IllegalArgumentException");
	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidMockName() {
		// Test for invalid mock name
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(MockUtil.class, null);
		Object fieldInstance = mock(Object.class);
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		fail("Should have thrown an IllegalStateException");
	}
}