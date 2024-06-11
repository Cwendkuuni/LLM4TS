import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockSettings;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 returnsDeepStubs = Mockito.spy(mock);
 }

 @Test
 public void testDeepStubbingWithSimpleType() {
 Object deepStub = mockitoCore().mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 assertSame(deepStub, returnsDeepStubs.answer(argument(deepStub)));
 }

 @Test
 public void testDeepStubbingWithGenericType() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mockNest = mock(GenericsNest.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 Number number = mockNest.entrySet().iterator().next().getValue().iterator().next();
 assertSame(mockNest, returnsDeepStubs.answer(argument(number)));
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubbingWithNonMockableType() {
 byte[] bytes = new byte[10];
 try {
 mockitoCore().mock(bytes.getClass(), withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 fail("Should not be able to mock non-mockable type");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testDeepStubbingWithVerification() {
 Object deepStub = mockitoCore().mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 verify(deepStub).toString();
 }

 @Test
 public void testDeepStubbingWithNestedVerification() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Number> mockNest = mock(GenericsNest.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 Number number = mockNest.entrySet().iterator().next().getValue().iterator().next();
 verify(number).toString();
 }

 @Test
 public void testDeepStubbingWithAnswerRecording() {
 Object deepStub = mockitoCore().mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 when(deepStub.toString()).thenReturn("deep stub");
 assertEquals("deep stub", returnsDeepStubs.answer(argument(deepStub)).toString());
 }

 @Test
 public void testDeepStubbingWithAnswerRecordingAndVerification() {
 Object deepStub = mockitoCore().mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 when(deepStub.toString()).thenReturn("deep stub");
 assertEquals("deep stub", returnsDeepStubs.answer(argument(deepStub)).toString());
 verify(deepStub).toString();
 }

 private MockitoCore mockitoCore() {
 return Mockito.mock(MockitoCore.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 }
}