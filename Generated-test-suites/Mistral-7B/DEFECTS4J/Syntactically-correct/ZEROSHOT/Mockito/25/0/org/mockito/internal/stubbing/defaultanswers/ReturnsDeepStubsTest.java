package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.testing.internal.stubbing.answers.ReturnsDeepStubs;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 @Rule
 public MockitoSessionRules mockitoSessionRules = new MockitoSessionRules();

 @Mock
 private GenericsNest<Number> genericsNest;

 @Mock
 private GenericsNest<Map.Entry<Number, Number>> genericsNestEntrySet;

 @Mock
 private GenericsNest<Set<Number>> genericsNestEntrySetIterator;

 @Mock
 private Number number;

 @Before
 public void setup() {
 Mockito.initMocks(this);
 }

 @Test
 public void testAnswerWithDeepStubs() throws Exception {
 // test case for generic interfaces with nested generics
 Mockito.when(genericsNest.entrySet()).thenAnswer(new ReturnsDeepStubs());
 Mockito.when(genericsNestEntrySet.iterator()).thenReturn(Mockito.mock(Iterator.class));
 Mockito.when(genericsNestEntrySetIterator.next()).thenReturn(Mockito.mock(Map.Entry.class, ArgumentMatchers.any(Number.class)));
 Mockito.when(genericsNestEntrySetIterator.next().getValue()).thenReturn(number);

 GenericsNest<Number> mockGenericsNest = Mockito.mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 Number mockNumber = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 Type genericType = mockGenericsNest.getClass().getGenericSuperclass();
 Type numberType = Number.class.getGenericInterfaces()[0];
 Type mapEntryType = Map.Entry.class.getGenericInterfaces()[0];
 Type setType = Collections.class.getGenericSuperclass();
 Type iterableType = Iterator.class.getGenericInterfaces()[0];

 Type[] genericTypes = new Type[]{numberType, mapEntryType, setType, iterableType, genericType};

 Assert.assertEquals(mockNumber, mockGenericsNest.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testGetMock() throws Exception {
 Object mock = Mockito.mock(Objects.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
 Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object returnValue = returnsDeepStubs.getMock(invocation, container);

 Assert.assertNotNull(returnValue);
 Assert.assertSame(returnValue, container.getInvocationForStubbing().getMock());
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(handler.getInvocationContainer()).thenReturn(container);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);

 Assert.assertNotNull(mock);
 Assert.assertSame(mock, container.getInvocationForStubbing().getMock());
 }
}