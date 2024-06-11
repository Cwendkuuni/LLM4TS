import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class NameBasedCandidateFilterTest {
	@Test
	public void testNameBasedCandidateFilterConstructor() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		assertNull(filter.next);
	}

	@Test
	public void testFilterCandidateEmptyMockCollection() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Field field = Mockito.field(Object.class, "fieldName");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertNull(result);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateNullMockCollection() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Field field = Mockito.field(Object.class, "fieldName");
		Object fieldInstance = new Object();
		Collection<Object> mocks = null;

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateNullMock() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Field field = Mockito.field(Object.class, "fieldName");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		Object mock = null;

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateNullField() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock = Mockito.mock(Object.class);
		Field nullField = null;
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		filter.filterCandidate(mocks, nullField, fieldInstance);
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateNullFieldInstance() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock = Mockito.mock(Object.class);
		Field field = Mockito.field(Object.class, "fieldName");
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		filter.filterCandidate(mocks, field, null);
	}

	@Test
	public void testFilterCandidateMultipleFieldsWithTheSameName() throws NoSuchFieldException {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock1 = Mockito.mock(Object.class);
		Object mock2 = Mockito.mock(Object.class);
		String mockName1 = MockUtil.getMockName(mock1);
		String mockName2 = MockUtil.getMockName(mock2);
		String sameName = mockName1;
		Field field1 = Mockito.field(Object.class, "fieldName");
		Field field2 = Mockito.field(Object.class, "fieldName");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		Object result = filter.filterCandidate(mocks, field1, fieldInstance);
		assertEquals(mock2, result.getMock());
	}

	@Test
	public void testFilterCandidateSingleMock() throws NoSuchFieldException {
		Object mock = Mockito.mock(Object.class);
		String mockName = MockUtil.getMockName(mock);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Field field = Mockito.field(Object.class, "fieldName");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertTrue(result instanceof OngoingInjecter);
		assertEquals(mock, result.getMock());
	}
}