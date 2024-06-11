import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private static final String TESTED_CLASS_NAME = ReturnsSmartNulls.class.getName();

 @Mock
 private Foo mockFoo;

 @InjectMocks
 private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

 @Test
 public void testSmartNullReturnedByToStringMethod() {
     String expectedSmartNullMessage = "SmartNull returned by unstubbed getName(*) method on mock";
     when(mockFoo.getName()).thenReturn(new Object(){
         @Override
         public String toString() {
             return null;
         }
     });

     String actualReturnValue = (String) returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testToString"), new Object[0], new Object[0], mockFoo));

     assertEquals(expectedSmartNullMessage, actualReturnValue.toString());
     verify(mockFoo).getName();
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethod() {
     when(mockFoo.someMethod()).thenReturn(null);

     Object actualReturnValue = returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testNullReturnedByUnstubbedMethod"), new Object[0], new Object[0], mockFoo));

     assertNull(actualReturnValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethodWithArgument() {
     when(mockFoo.someMethod(anyInt())).thenReturn(null);

     ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testNullReturnedByUnstubbedMethodWithArgument"), new Object[0], new Object[]{1}, mockFoo, integerArgumentCaptor.capture()));

     int actualArgumentValue = integerArgumentCaptor.getValue();

     assertEquals(1, actualArgumentValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethodWithTwoArguments() {
     when(mockFoo.someMethod(anyInt(), anyInt())).thenReturn(null);

     ArgumentCaptor<Integer> firstIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> secondIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testNullReturnedByUnstubbedMethodWithTwoArguments"), new Object[0], new Object[]{1, 2}, mockFoo, firstIntegerArgumentCaptor.capture(), secondIntegerArgumentCaptor.capture()));

     int firstActualArgumentValue = firstIntegerArgumentCaptor.getValue();
     int secondActualArgumentValue = secondIntegerArgumentCaptor.getValue();

     assertEquals(1, firstActualArgumentValue);
     assertEquals(2, secondActualArgumentValue);
 }

 @Test(expected = NullPointerException.class)
 public void testNullReturnedByUnstubbedMethodWithThreeArguments() {
     when(mockFoo.someMethod(anyInt(), anyInt(), anyInt())).thenReturn(null);

     ArgumentCaptor<Integer> firstIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> secondIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> thirdIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testNullReturnedByUnstubbedMethodWithThreeArguments"), new Object[0], new Object[]{1, 2, 3}, mockFoo, firstIntegerArgumentCaptor.capture(), secondIntegerArgumentCaptor.capture(), thirdIntegerArgumentCaptor.capture()));

     int firstActualArgumentValue = firstIntegerArgumentCaptor.getValue();
     int secondActualArgumentValue = secondIntegerArgumentCaptor.getValue();
     int thirdActualArgumentValue = thirdIntegerArgumentCaptor.getValue();

     assertEquals(1, firstActualArgumentValue);
     assertEquals(2, secondActualArgumentValue);
     assertEquals(3, thirdActualArgumentValue);
 }

 @Test
 public void testStubbedMethodReturnsDefaultValue() {
     when(mockFoo.someMethod()).thenReturn("default value");

     String actualReturnValue = (String) returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testStubbedMethodReturnsDefaultValue"), new Object[0], new Object[0], mockFoo));

     assertEquals("default value", actualReturnValue);
 }

 @Test
 public void testStubbedMethodReturnsSmartNull() {
     when(mockFoo.someMethod()).thenAnswer(Answers.callRealMethod());

     String actualReturnValue = (String) returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testStubbedMethodReturnsSmartNull"), new Object[0], new Object[0], mockFoo));

     assertNull(actualReturnValue);
 }

 @Test
 public void testStubbedMethodReturnsSmartNullWithArgument() {
     when(mockFoo.someMethod(anyInt())).thenAnswer(Answers.callRealMethod());

     ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testStubbedMethodReturnsSmartNullWithArgument"), new Object[0], new Object[]{1}, mockFoo, integerArgumentCaptor.capture()));

     int actualArgumentValue = integerArgumentCaptor.getValue();

     assertEquals(1, actualArgumentValue);
 }

 @Test
 public void testStubbedMethodReturnsSmartNullWithTwoArguments() {
     when(mockFoo.someMethod(anyInt(), anyInt())).thenAnswer(Answers.callRealMethod());

     ArgumentCaptor<Integer> firstIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> secondIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testStubbedMethodReturnsSmartNullWithTwoArguments"), new Object[0], new Object[]{1, 2}, mockFoo, firstIntegerArgumentCaptor.capture(), secondIntegerArgumentCaptor.capture()));

     int firstActualArgumentValue = firstIntegerArgumentCaptor.getValue();
     int secondActualArgumentValue = secondIntegerArgumentCaptor.getValue();

     assertEquals(1, firstActualArgumentValue);
     assertEquals(2, secondActualArgumentValue);
 }

 @Test
 public void testStubbedMethodReturnsSmartNullWithThreeArguments() {
     when(mockFoo.someMethod(anyInt(), anyInt(), anyInt())).thenAnswer(Answers.callRealMethod());

     ArgumentCaptor<Integer> firstIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> secondIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
     ArgumentCaptor<Integer> thirdIntegerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

     returnsSmartNulls.answer(new InvocationOnMock(this, getClass().getMethod("testStubbedMethodReturnsSmartNullWithThreeArguments"), new Object[0], new Object[]{1, 2, 3}, mockFoo, firstIntegerArgumentCaptor.capture(), secondIntegerArgumentCaptor.capture(), thirdIntegerArgumentCaptor.capture()));

     int firstActualArgumentValue = firstIntegerArgumentCaptor.getValue();
     int secondActualArgumentValue = secondIntegerArgumentCaptor.getValue();
     int thirdActualArgumentValue = thirdIntegerArgumentCaptor.getValue();

     assertEquals(1, firstActualArgumentValue);
     assertEquals(2, secondActualArgumentValue);
     assertEquals(3, thirdActualArgumentValue);
 }
}