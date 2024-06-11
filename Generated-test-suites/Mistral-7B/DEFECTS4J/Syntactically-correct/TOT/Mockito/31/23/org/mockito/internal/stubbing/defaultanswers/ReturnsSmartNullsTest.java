import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private MyLegacyClass legacyClass;

 @Before
 public void setup() {
     returnsSmartNulls = new ReturnsSmartNulls();
     legacyClass = mock(MyLegacyClass.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullWhenMethodReturnsNull() {
     MyData data = null;
     when(legacyClass.getMyData(ArgumentMatchers.anyInt())).thenReturn(data);

     String result = legacyClass.getDescriptionOfMyData(1);
     assertNotNull(result); // will pass, as "SmartNull returned by unstubbed getMyData method on mock" is returned instead

     verify(legacyClass).getMyData(ArgumentMatchers.eq(1));
 }

 @Test(expected = NullPointerException.class)
 public void testThrowsNPEWhenMethodReturnsOrdinaryNull() {
     when(legacyClass.getMyData(ArgumentMatchers.anyInt())).thenReturn(null);

     legacyClass.getDescriptionOfMyData(1);
 }

 @Test
 public void testDoesNotReturnSmartNullForToStringMethod() {
     MyData data = new MyData();
     when(legacyClass.getMyData(ArgumentMatchers.anyInt())).thenReturn(data);

     String result = legacyClass.toString();
     assertEquals("MyLegacyClass@42", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowsExceptionWhenCannotCreateSmartNull() {
     when(legacyClass.getMyData(ArgumentMatchers.anyInt())).thenReturn(new IllegalStateException());

     ReturnsSmartNulls wrongAnswer = Mockito.mock(ReturnsSmartNulls.class);
     wrongAnswer.answer(Mockito.mock(InvocationOnMock.class));

     legacyClass.setAnswer(0, wrongAnswer); // doesn't work since the type of getMyData(int) cannot be imposterised
 }
}

class MyData {}
class MyLegacyClass {
 private ReturnsSmartNulls answer;

 public MyData getMyData(int index) {
     return answer.answer(Mockito.mock(InvocationOnMock.class));
 }

 public String getDescriptionOfMyData(int index) {
     MyData data = getMyData(index);
     return data.toString();
 }
}