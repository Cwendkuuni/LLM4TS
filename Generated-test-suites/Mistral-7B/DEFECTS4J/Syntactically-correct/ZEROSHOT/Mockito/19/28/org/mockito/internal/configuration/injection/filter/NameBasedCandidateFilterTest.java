package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.junit.jupiter.MockitoExtension.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.runner.MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

@RunWith(MockitoExtension.class)
public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidateWhenNoConflictingFields() {
		// given
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
		Field field = new Field("testField", String.class, null);
		Object fieldInstance = mock(Object.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		assertNull(result);
	}

	@Test
	public void testFilterCandidateWhenConflictingFieldsWithCorrectNameMatched() {
		// given
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Collection<Object> mocks = Arrays.asList(mock(ObjectWithTestField.class), mock(ObjectWithTestField.class));
		Field field = new Field("testField", String.class, null);
		Object fieldInstance = mock(ObjectWithTestField.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		assertNull(result);
	}

	@Test
	public void testFilterCandidateWhenConflictingFieldsWithIncorrectNameMatched() {
		// given
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Collection<Object> mocks = Arrays.asList(mock(ObjectWithTestField.class), mock(ObjectWithOtherField.class));
		Field field = new Field("testField", String.class, null);
		Object fieldInstance = mock(ObjectWithTestField.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		assertNull(result);
	}

	@Test
	public void testFilterCandidateWhenConflictingFieldsWithCorrectNameMatchedInNextFilter() {
		// given
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class, new Answer<OngoingInjecter>() {
			@SuppressWarnings("unchecked")
			@Override
			public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (OngoingInjecter) args[0];
			}
		});
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
		Collection<Object> mocks = Arrays.asList(mock(ObjectWithTestField.class), mock(ObjectWithOtherField.class), mock(ObjectWithTestField.class));
		Field field = new Field("testField", String.class, null);
		Object fieldInstance = mock(ObjectWithTestField.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		assertNull(result);

		// verify
		Stubber stubber = mock.stub(nextMock);
		stubber.when(nextMock.filterCandidate(Arrays.asList(mock(ObjectWithTestField.class), mock(ObjectWithTestField.class)), field, fieldInstance))
				.thenReturn(mock(OngoingInjecter.class));

		result = filter.filterCandidate(mocks, field, fieldInstance);

		stubber.verify(nextMock).filterCandidate(Arrays.asList(mock(ObjectWithTestField.class), mock(ObjectWithTestField.class)), field, fieldInstance);
	}

	private static class ObjectWithTestField {
		public String testField;
	}

	private static class ObjectWithOtherField {
		public String otherField;
	}

}