import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.NamePattern;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Object fieldInstance;
 private Collection<Object> mocks;
 private Field field;
 private String fieldName;

 @Before
 public void setup() {
 fieldName = "testFieldName";
 field = MockUtil.getField(this, fieldName);
 fieldInstance = this;
 mocks = Arrays.asList(mock(MockitoTestClass.class), mock(AnotherMockitoTestClass.class));
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_singleMock_sameName() {
 Object mock = mocks.get(0);
 String mockName = MockUtil.getMockName(mock);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock, result);
 verify(nextMock).filterCandidate(Collections.singletonList(mock), field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_singleMock_differentName() {
 Object mock = mocks.get(0);
 Object conflictingMock = mock(ConflictingMockitoTestClass.class);
 String mockName = MockUtil.getMockName(mock);
 String conflictingMockName = MockUtil.getMockName(conflictingMock);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(conflictingMock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_sameName() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 String mock1Name = MockUtil.getMockName(mock1);
 String mock2Name = MockUtil.getMockName(mock2);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_differentName() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 Object conflictingMock = mock(ConflictingMockitoTestClass.class);
 String mock1Name = MockUtil.getMockName(mock1);
 String mock2Name = MockUtil.getMockName(mock2);
 String conflictingMockName = MockUtil.getMockName(conflictingMock);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(conflictingMock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_sameType_conflictingName() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 Object conflictingMock = mock(ConflictingMockitoTestClass.class);
 String mock1Name = MockUtil.getMockName(mock1);
 String mock2Name = MockUtil.getMockName(mock2);
 String conflictingMockName = MockUtil.getMockName(conflictingMock);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(conflictingMock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_sameType_sameName() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 String mockName = MockUtil.getMockName(mock1);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_sameType_sameName_multipleFields() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 String mockName = MockUtil.getMockName(mock1);
 String conflictingMockName = MockUtil.getMockName(mock2);
 String conflictingFieldName = "conflictingFieldName";
 Field conflictingField = MockUtil.getField(this, conflictingFieldName);
 Object conflictingFieldInstance = this;
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock2);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_sameType_sameName_multipleFields_multipleFieldsInstance() {
 Object mock1 = mocks.get(0);
 Object mock2 = mocks.get(1);
 Object conflictingMock = mock(ConflictingMockitoTestClass.class);
 String mockName = MockUtil.getMockName(mock1);
 String conflictingMockName = MockUtil.getMockName(conflictingMock);
 String conflictingFieldName = "conflictingFieldName";
 Field conflictingField = MockUtil.getField(this, conflictingFieldName);
 Object conflictingFieldInstance = new Object();
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(conflictingMock);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result);
 }

 private static class ConflictingMockitoTestClass {
 }

 @Test
 public void testFilterCandidate_emptyMockCollection() {
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

 Object result = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);

 assertEquals(null, result);
 }
}