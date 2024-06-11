import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private interface TestInterface {
 String getSmartNull();
 }

 @Test
 public void testGetSmartNull_whenMethodIsCalled_thenSmartNullIsReturned() {
     TestInterface testInterface = mock(TestInterface.class, new ReturnsSmartNulls());
     String nullValue = testInterface.getSmartNull();
     verify(testInterface).getSmartNull();
     assertNull(nullValue);
     // Print stack trace to check the line where unstubbed method was called
     System.out.println(nullValue.getClass().getStackTrace()[0]);
  }

 @Test(expected = NullPointerException.class)
 public void testGetSmartNull_whenMethodThrowsNullPointerException_thenSmartNullIsReturned() {
     TestInterface testInterface = mock(TestInterface.class, new ReturnsSmartNulls());
     testInterface.getSmartNullThrowsNPE(); // Unstubbed method that throws NPE
 }

 @Test
 public void testGetSmartNull_whenMethodReturnsNonNullValue_thenNonNullValueIsReturned() {
     TestInterface testInterface = mock(TestInterface.class, new ReturnsSmartNulls());
     String nonNullValue = testInterface.getNonNullMethod(); // Stubbed method that returns a non-null value
     assertEquals("Expected non-null value", nonNullValue, testInterface.getSmartNull());
 }
}