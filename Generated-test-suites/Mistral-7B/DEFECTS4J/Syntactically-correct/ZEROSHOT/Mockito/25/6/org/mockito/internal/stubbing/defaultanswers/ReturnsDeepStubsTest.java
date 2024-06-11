package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsPreviouslyCreatedMock() {
 Object clz = Mockito.mock(Object.class);
 mock = Mockito.mock(Map.class, new ReturnsDeepStubs());

 when(mock.entrySet().iterator().next().getValue()).thenReturn(clz);

 Object deepStub = mock.entrySet().iterator().next().getValue().iterator().next();
 assertSame(clz, deepStub);

 verify(clz).someMethod();
 }

 @Test
 public void testReturnsEmptyValueForNonMockableType() {
 Object nonMockableType = new Object() {};
 when(mock.someMethod()).thenReturn(nonMockableType);

 Object result = mock.someMethod();
 assertNotNull(result);
 assertEquals(nonMockableType, result);
 }

 @Test
 public void testRecordDeepStubMock() {
 Object clz = Mockito.mock(Object.class);
 mock = Mockito.mock(Map.class, new ReturnsDeepStubs());

 when(mock.entrySet().iterator().next().getValue()).thenReturn(clz);
 when(mock.someMethod()).thenCallRealMethod();

 Object deepStub = mock.entrySet().iterator().next().getValue().iterator().next();
 assertSame(clz, deepStub);

 verify(clz).someMethod();

 Object returnedMock = mock.someMethod();
 assertSame(clz, returnedMock);
 }

 @Test
 public void testDeepStubbingWithNestedGenerics() {
 Object key = Mockito.mock(Number.class);
 Object value = Mockito.mock(Number.class);
 Object map = Mockito.mock(Map.class, new ReturnsDeepStubs());

 when(map.entrySet().iterator().next().getKey()).thenReturn(key);
 when(map.entrySet().iterator().next().getValue()).thenReturn(value);
 when(value.iterator().next()).thenReturn(Mockito.mock(Number.class));

 Object nestedMap = map.entrySet().iterator().next();
 Object nestedNumber = nestedMap.entrySet().iterator().next().getValue().iterator().next();

 assertSame(value, nestedNumber);
 }

 @Test
 public void testDeepStubbingWithNestedGenericsAndCustomAnswer() {
 Object key = Mockito.mock(Number.class);
 Object value = Mockito.mock(Number.class);
 Object map = Mockito.mock(Map.class, new ReturnsDeepStubs());
 Object customAnswer = Mockito.mock(Answer.class);

 when(map.entrySet().iterator().next().getKey()).thenReturn(key);
 when(map.entrySet().iterator().next().getValue()).thenReturn(value);
 when(value.iterator().next()).thenAnswer(customAnswer);

 Object nestedMap = map.entrySet().iterator().next();
 Object nestedNumber = nestedMap.entrySet().iterator().next().getValue().iterator().next();

 verify(customAnswer).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertSame(value, nestedNumber);
 }

 @Test
 public void testDeepStubbingWithListOfMaps() {
 Object map1 = Mockito.mock(Map.class, new ReturnsDeepStubs());
 Object map2 = Mockito.mock(Map.class, new ReturnsDeepStubs());
 Object list = Mockito.mock(List.class);

 when(map1.entrySet().iterator().next().getValue()).thenReturn(map2);
 when(map2.entrySet().iterator().next().getKey()).thenReturn("key2");
 when(map2.entrySet().iterator().next().getValue()).thenReturn("value2");

 when(list.get(0)).thenReturn(map1);
 when(list.get(1)).thenReturn(map2);

 Object result = ((Map) list.get(0)).entrySet().iterator().next().getValue();
 Map<String, Object> resultMap = (Map<String, Object>) result;
 assertEquals("key2", resultMap.keySet().iterator().next());
 assertEquals("value2", resultMap.values().toArray()[0]);
 }

 @Test
 public void testDeepStubbingWithCustomType() {
 Type customType = new TypeToken<CustomType>() {}.getType();
 Object customTypeMock = Mockito.mock(customType);
 Object map = Mockito.mock(Map.class, new ReturnsDeepStubs());

 when(map.entrySet().iterator().next().getValue()).thenReturn(customTypeMock);

 Object result = ((Map) map.entrySet().iterator().next().getValue()).values().toArray()[0];
 assertEquals(customTypeMock, result);
 }

 class CustomType extends HashMap<String, String> {}
}