import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericNest;
 private Object mockNumber;
 private Object mockMap;

 @Before
 public void setup() throws Exception {
 mockMap = mock(Map.class);
 mockNumber = mock(Number.class);
 when(mockMap.entrySet().iterator().next().getValue()).thenReturn(mockNumber);
 returnsDeepStubs = new ReturnsDeepStubs();
 mockGenericNest = mock(GenericsNest.class, new ReturnsDeepStubs());
 }

 @Test
 public void testReturnsDeepStubs() throws Exception {
 Object deepStub = returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 ArgumentMatchers.typeEq(GenericsNest.class).and(ArgumentMatchers.isA(InvocationOnMock.class)
 .and(ArgumentMatchers.isA(InvocationContainerImpl.class)
 .and(arg -> arg.getInvocationForStubbing().getMethod().getReturnType().getTypeName().equals("java.util.Set<java.lang.Number>"))));
 return invocation;
 }));

 assertEquals(mockMap, deepStub);
 }

 @Test
 public void testDeepStubsNestedGenericReturnType() throws Exception {
 Object deepStubMapEntry = returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 ArgumentMatchers.typeEq(GenericsNest.class).and(ArgumentMatchers.isA(InvocationOnMock.class)
 .and(ArgumentMatchers.isA(InvocationContainerImpl.class)
 .and(arg -> arg.getInvocationForStubbing().getMethod().getReturnType().getTypeName().equals("java.util.Map<?,java.util.Set<Number>>"))));
 return invocation;
 }));

 Map<Object, Set<Number>> deepStubMap = (Map<Object, Set<Number>>) deepStubMapEntry;
 assertNotNull(deepStubMap);
 Set<Number> deepStubSet = deepStubMap.get(mockNumber);
 assertNotNull(deepStubSet);
 assertEquals(mockNumber, deepStubSet.iterator().next());
 }

 @Test
 public void testDeepStubsVerification() throws Exception {
 verify(mockMap).entrySet();
 verify(mockNumber).iterator();
 verify(mockNumber.iterator()).next();
 }

 @Test
 public void testDeepStubsSerialization() throws Exception {
 Object deepStubGenericNest = returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 ArgumentMatchers.typeEq(GenericsNest.class).and(ArgumentMatchers.isA(InvocationOnMock.class)
 .and(ArgumentMatchers.isA(InvocationContainerImpl.class)
 .and(arg -> arg.getInvocationForStubbing().getMethod().getReturnType().getTypeName().equals("java.util.Map<?,GenericsNest>"))));
 return invocation;
 }));

 assertNotNull(deepStubGenericNest);
 assertNotNull(deepStubGenericNest.getClass().getSuperclass());
 assertEquals(GenericsNest.class, deepStubGenericNest.getClass().getSuperclass());

 List<Object> deepStubList = Arrays.asList(deepStubGenericNest);
 Object serializedDeepStubList = Mockito.serialization().serialize(deepStubList);
 Object deserializedDeepStubList = Mockito.serialization().deserialize(serializedDeepStubList, List.class);

 assertEquals(deepStubList, deserializedDeepStubList);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubsNestedGenericsException() throws Exception {
 when(mockMap.entrySet().iterator().next().getValue()).thenReturn(mockNumber);
 mockNumber = mock(Number[].class);

 Object deepStub = returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 ArgumentMatchers.typeEq(GenericsNest.class).and(ArgumentMatchers.isA(InvocationOnMock.class)
 .and(ArgumentMatchers.isA(InvocationContainerImpl.class)
 .and(arg -> arg.getInvocationForStubbing().getMethod().getReturnType().getTypeName().equals("java.util.Map<?,Number[]>"))));
 return invocation;
 }));

 fail("Should throw an IllegalStateException when trying to deep stub an array type.");
 }

}