import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockObject;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsDeepStubForPrimitiveType() {
 Object mockInt = mock(int.class, returnsDeepStubs);
 assertEquals(mockInt, mockInt);
 }

 @Test
 public void testReturnsDeepStubForObjectType() {
 Object mockObject = mock(Object.class, returnsDeepStubs);
 assertEquals(mockObject, mockObject);

 Object nestedMock = mock(HashMap.class, returnsDeepStubs);
 assertEquals(nestedMock, nestedMock);

 Map<Integer, Number> map = (Map<Integer, Number>) nestedMock;
 Number nestedNumber = map.keySet().iterator().next();
 assertEquals(nestedNumber, nestedNumber);
 }

 @Test
 public void testReturnsDeepStubForNestedObjectType() {
 Object mockMap = mock(Map.class, returnsDeepStubs);
 Map<Integer, Map<String, Number>> nestedMap = (Map<Integer, Map<String, Number>>) mockMap;

 Map<String, Number> nestedMapValue = nestedMap.values().iterator().next();
 Number nestedNumber = nestedMapValue.keySet().iterator().next();

 assertEquals(nestedNumber, nestedNumber);
 }

 @Test
 public void testReturnsSameMockForSameInvocation() {
 Object mockMap = mock(Map.class, returnsDeepStubs);
 Object mockKey1 = mock(String.class, returnsDeepStubs);
 Object mockValue1 = mock(Number.class, returnsDeepStubs);
 Object mockKey2 = mock(String.class, returnsDeepStubs);
 Object mockValue2 = mock(Number.class, returnsDeepStubs);

 Map<String, Number> map = (Map<String, Number>) mockMap;
 map.put(mockKey1, mockValue1);
 map.put(mockKey2, mockValue2);

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(Map.class);
 invocation.getArguments()[0] = mockMap;

 Object deepStub1 = returnsDeepStubs.answer(invocation);
 Object deepStub2 = returnsDeepStubs.answer(invocation);

 assertEquals(deepStub1, deepStub2);
 }

 @Test
 public void testDoesNotReturnDeepStubForNonMockableType() {
 Object mockString = mock(String.class, returnsDeepStubs);
 String nonMockableString = new String("test");

 assertEquals(mockString, mockString);
 assertEquals(mockString, nonMockableString);
 }

 @Test
 public void testVerificationMatchesDeepStub() {
 Object mockMap = mock(Map.class, returnsDeepStubs);
 Object mockKey = mock(String.class, returnsDeepStubs);
 Object mockValue = mock(Number.class, returnsDeepStubs);

 Map<String, Number> map = (Map<String, Number>) mockMap;
 map.put(mockKey, mockValue);

 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(Map.class);
 invocation.getArguments()[0] = mockMap;

 mockMap.put(ArgumentMatchers.eq(mockKey), ArgumentMatchers.eq(mockValue));

 returnsDeepStubs.answer(invocation);

 verify(mockMap).put(ArgumentMatchers.eq(mockKey), ArgumentMatchers.eq(mockValue));
 }
}