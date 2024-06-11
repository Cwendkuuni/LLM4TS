import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testToStringAnswer() {
 MockitoAnswer mockAnswer = Mockito.mock(Answer.class);
 Mockito.when(mockAnswer.answer(Mockito.any())).then(returnsEmptyValues);

 Object mock = Mockito.mock(String.class);
 Mockito.when(mockAnswer.answer(Mockito.argThat(arg -> arg.getMock() == mock))).thenReturn("Mock for String, hashCode: " + mock.hashCode());
 String expected = "Mock for String, hashCode: 123";

 String result = (String) mockAnswer.answer(new InvocationOnMock(null, null, String.class, "toString()"));

 assertThat(result).isEqualTo(expected);
 }

 @Test
 public void testCompareToMethodAnswer() {
 Object mock1 = Mockito.mock(Number.class);
 Object mock2 = Mockito.mock(Number.class);
 Mockito.when(mock1.compareTo(mock2)).thenReturn(0);
 int result = ((Number) mock1).compareTo(mock2);

 assertThat(result).isEqualTo(0);
 }

 @Test
 public void testPrimitiveTypesAnswer() {
 for (Class<?> type : PrimitiveTypes.getAllPrimitivesAndWrappers()) {
 Object result = returnsEmptyValues.returnValueFor(type);
 Object expected = Primitives.defaultValueForPrimitiveOrWrapper(type);

 assertThat(result).isEqualTo(expected);
 }
 }

 @Test
 public void testCollectionTypesAnswer() {
 for (Class<?> type : Collections.class.getInterfaces()) {
 Object result = returnsEmptyValues.returnValueFor(type);

 if (Collection.class.isAssignableFrom(type)) {
 Collection<?> expected = Collections.emptyList();
 assertThat(result).isInstanceOf(Collection.class);
 assertThat(result).isEqualTo(expected);
 } else if (Set.class.isAssignableFrom(type)) {
 Set<?> expected = Collections.emptySet();
 assertThat(result).isInstanceOf(Set.class);
 assertThat(result).isEqualTo(expected);
 }
 }
 }

 private static class MockitoAnswer implements Answer<Object> {
 private Answer<Object> answer;

 public MockitoAnswer(Answer<Object> answer) {
 this.answer = answer;
 }

 @Override
 public Object answer(InvocationOnMock invocation) {
 return answer.answer(invocation);
 }
 }

 private static class PrimitiveTypes {
 //...
 }

}