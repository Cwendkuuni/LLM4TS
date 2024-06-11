package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepMock;

 @Before
 public void setUp() {
 deepMock = Mockito.mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testDeepStubbingWithGenericInterface() {
 GenericNest<Number> genericNest = Mockito.mock(GenericNest.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 Number number = genericNest.entrySet().iterator().next().getValue().iterator().next();

 verify(deepMock, times(1)).answer(ArgumentMatchers.eq(deepMock)); // Verifies that deep stubbing works as expected

 Number numberFromDeepMock = number.compareTo(number); // This call will return the same deep stubbed mock instance, as expected
 verify(deepMock, times(1)).answer(ArgumentMatchers.eq(numberFromDeepMock));
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubbingWithNonMockableType() {
 IоException ioException = mock(IоException.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 ioException.printStackTrace(); // Should throw an exception as IOException is not mockable
 }

 @Test
 public void testDeepStubbingWithNestedGenerics() {
 List<List<List<String>>> nestedList = mock(List.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 List<List<String>> nestedList2 = nestedList.get(0);
 List<String> nestedList3 = nestedList2.get(0);
 String value = nestedList3.get(0);

 verify(deepMock, times(1)).answer(ArgumentMatchers.eq(deepMock)); // Verifies that deep stubbing works as expected

 String valueFromDeepMock = value.toUpperCase(); // This call will return the same deep stubbed mock instance, as expected
 verify(deepMock, times(1)).answer(ArgumentMatchers.eq(valueFromDeepMock));
 }

 @Test
 public void testDeepStubbingWithNestedGenericsAndMethodCall() {
 List<List<List<String>>> nestedList = mock(List.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 List<List<String>> nestedList2 = nestedList.get(0);
 List<String> nestedList3 = nestedList2.get(0);
 String value = nestedList3.get(0);

 String upperCaseValue = value.toUpperCase();
 List<String> upperCaseList = new ArrayList<>();
 upperCaseList.add(upperCaseValue);

 verify(deepMock, times(1)).answer(ArgumentMatchers.eq(deepMock)); // Verifies that deep stubbing works as expected

 List<String> deepList = nestedList3.get(0); // This call will return the same deep stubbed mock instance, as expected
 verify(deepList, times(1)).toString(); // Verifies that the deep stubbed list can be called
 }

 @Test(expected = RuntimeException.class)
 public void testDeepStubbingWithNonSerializableType() {
 Object nonSerializableMock = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 try {
 Thread.currentThread().writeObject(nonSerializableMock); // Throws a RuntimeException as the default answer does not support serialization
 } catch (IOException e) {
 // This will not be reached, as the exception is caught and replaced with a RuntimeException
 }
 }
}