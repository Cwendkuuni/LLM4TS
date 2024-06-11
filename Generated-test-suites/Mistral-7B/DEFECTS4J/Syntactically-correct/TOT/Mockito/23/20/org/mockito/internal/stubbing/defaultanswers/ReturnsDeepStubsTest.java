package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private GenericsNest<String> mockGenericsNest;
 private GenericsNest<Number> mockGenericsNestNumber;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsDeepStubForGenericType() {
 mockGenericsNest = mock(GenericsNest.class, returnsDeepStubs);
 mockGenericsNest.put(ArgumentMatchers.any(), ArgumentMatchers.any());

 GenericsNest<Number> nestedGenericsNest = mockGenericsNest.entrySet().iterator().next().getValue();
 Number number = nestedGenericsNest.iterator().next();

 assertEquals(mockGenericsNest, nestedGenericsNest);
 assertEquals(mockGenericsNest, number);

 verify(mockGenericsNest).put(ArgumentMatchers.any(), ArgumentMatchers.any());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThrowsExceptionForNonMockableType() {
 mockGenericsNest = mock(String[].class, returnsDeepStubs);
 }

 @Test
 public void testReturnsPreviousStubForSameInvocation() {
 GenericsNest<String> firstMockGenericsNest = mock(GenericsNest.class, returnsDeepStubs);
 GenericsNest<String> secondMockGenericsNest = mock(GenericsNest.class, returnsDeepStubs);

 firstMockGenericsNest.put(ArgumentMatchers.any(), ArgumentMatchers.any());
 secondMockGenericsNest.put(ArgumentMatchers.any(), ArgumentMatchers.any());

 GenericsNest<String> thirdMockGenericsNest = firstMockGenericsNest.entrySet().iterator().next().getValue();

 assertEquals(firstMockGenericsNest, thirdMockGenericsNest);
 }
}