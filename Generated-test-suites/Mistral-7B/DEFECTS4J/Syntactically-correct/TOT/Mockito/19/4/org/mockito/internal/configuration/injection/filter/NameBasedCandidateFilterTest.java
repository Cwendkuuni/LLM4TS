package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Stubber stubber;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setup() {
 stubber = Mockito.stubber(Mockito.class);
 mockUtilMocks();
 filter = new NameBasedCandidateFilter(new DummyFilter(stubber));
 }

 @Test
 public void testFilterCandidate_singleMockMatchesName_returnsMock() {
 // Given
 Object fieldInstance = mock(Object.class);
 when(mock1.getClass()).thenReturn(Object.class);
 when(mock1.getName()).thenReturn("expectedName");

 // When
 OngoingInjecter result = filter.filterCandidate(singletonList(mock1),
 new Field("fieldName", Field.typeInt), fieldInstance);

 // Then
 assertSame(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameName_returnsFirstMatch() {
 // Given
 when(mock1.getName()).thenReturn("expectedName");
 when(mock2.getName()).thenReturn("expectedName");

 // When
 OngoingInjecter result = filter.filterCandidate(asList(mock1, mock2),
 new Field("fieldName", Field.typeInt), fieldInstance);

 // Then
 assertSame(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_noMocksMatchName_returnsNull() {
 // Given
 when(mock3.getClass()).thenReturn(Object.class);
 when(mock3.getName()).thenReturn("differentName");

 // When
 OngoingInjecter result = filter.filterCandidate(asList(mock1, mock2, mock3),
 new Field("fieldName", Field.typeInt), fieldInstance);

 // Then
 assertNull(result.getMock());
 }

 @Test
 public void testFilterCandidate_errorCase_throwsReflectionException() {
 // Given
 try {
 throw new RuntimeException("ReflectionException");
 } catch (RuntimeException e) {
 when(mockUtil.getMockName(mock1)).thenThrow(e);
 }

 // When
 try {
 filter.filterCandidate(singletonList(mock1), new Field("fieldName", Field.typeInt), fieldInstance);
 } catch (Exception e) {
 // Then
 assertEquals(ReflectionException.class, e.getClass());
 }
 }

 private void mockUtilMocks() {
 mockUtil = Mockito.spy(new MockUtil());
 stubber.stub(MockUtil.class).getMockName((Object) mock1).andReturn("mock1Name");
 stubber.stub(MockUtil.class).getMockName((Object) mock2).andReturn("mock2Name");
 stubber.stub(MockUtil.class).getMockName((Object) mock3).andReturn("mock3Name");
 }

 private List<Object> singletonList(Object o) {
 return new ArrayList<Object>() {{
 add(o);
 }};
 }

 private List<Object> asList(Object... objects) {
 return new ArrayList<Object>(List.of(objects));
 }
}

class DummyFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks,
 Field field, Object fieldInstance) {
 throw new UnsupportedOperationException();
 }
}