import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private Stubber stubber;

 @Before
 public void setup() {
 mockUtil = spy(new MockUtil());
 stubber = mock(Stubber.class);
 filter = new NameBasedCandidateFilter(null);
 }

 @Test
 public void testFilterCandidate_OneMock_NoConflictingFields_ReturnMock() {
 Object mock = mock(Object.class);
 Field field = new Field("testField", Object.class, mock.getClass());

 OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock); }}, field, mock);

 assertEquals(mock, result.getMock());
 }

 @Test
 public void testFilterCandidate_OneMock_ConflictingField_ReturnCorrectMock() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Field field = new Field("testField", Object.class, mock1.getClass());

 when(mockUtil.getMockName(mock1)).thenReturn("mock1");
 when(mockUtil.getMockName(mock2)).thenReturn("mock1");
 when(mockUtil.getMockName(mock1)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "field1";
 }
 });
 when(mockUtil.getMockName(mock2)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "field2";
 }
 });

 OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock1); add(mock2); }}, field, mock1);

 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_ReturnCorrectMock() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Field field = new Field("testField", Object.class, mock1.getClass());

 when(mockUtil.getMockName(mock1)).thenReturn("mock1");
 when(mockUtil.getMockName(mock2)).thenReturn("mock2");

 OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock1); add(mock2); }}, field, mock1);

 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_ConflictingFields_ReturnCorrectMock() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Field field1 = new Field("testField1", Object.class, mock1.getClass());
 Field field2 = new Field("testField1", Object.class, mock2.getClass());

 when(mockUtil.getMockName(mock1)).thenReturn("mock1_field1");
 when(mockUtil.getMockName(mock2)).thenReturn("mock2_field1");

 when(mockUtil.getMockName(mock1)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "mock1_field1";
 }
 });
 when(mockUtil.getMockName(mock2)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "mock2_field2";
 }
 });

 when(mockUtil.getMockName(mock1)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "mock1_field2";
 }
 });

 OngoingInjecter result = filter.filterCandidate(new ArrayList<Object>() {{ add(mock1); add(mock2); }}, field1, mock1);

 assertEquals(mock1, result.getMock());
 }
}