import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 }

 @Test
 public void testDeepStubAnswerForSimpleType() {
 // given
 Object deepStub = returnsDeepStubs.deepStub(mock(Object.class), mock(GenericMetadataSupport.class));

 // when
 Object result = returnsDeepStubs.answer(new InvocationOnMock(mock, newMethod(Object.class, "testMethod", newClass[].class, (Object[]) new Object[0])));

 // then
 assertEquals(deepStub, result);
 }

 @Test
 public void testDeepStubAnswerForNestedGenerics() {
 // given
 Object mockGenericsNest = mock(GenericsNest.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when
 Number number = ((GenericsNest<?>) mockGenericsNest).entrySet().iterator().next().getValue().iterator().next();

 // then
 assertEquals(deepStub(mockGenericsNest, mock(GenericMetadataSupport.class).resolveGenericReturnType(newMethod(GenericsNest.class, "entrySet", newClass[].class, (Class[]) new Class[0]))), number);
 }

 @Test(expected = Throwable.class)
 public void testDeepStubAnswerForNonMockableType() {
 // given
 Object mockIoException = mock(IOException.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when, then
 assertThrows(UnsupportedOperationException.class, () -> returnsDeepStubs.deepStub(mockIoException, mock(GenericMetadataSupport.class)));
 }

 @Test(expected = Throwable.class)
 public void testDeepStubAnswerWithNestedNonMockableType() {
 // given
 Object mockGenericsNestWithNonMockableType = mock(GenericsNest.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));

 // when, then
 assertThrows(UnsupportedOperationException.class, () -> returnsDeepStubs.deepStub(mock(IOException.class), mock(GenericMetadataSupport.class).resolveGenericReturnType(newMethod(GenericsNest.class, "entrySet", newClass[].class, (Class[]) new Class[0]))));
 }

 private Object deepStub(Object mock, GenericMetadataSupport genericMetadata) {
 return returnsDeepStubs.deepStub(new InvocationOnMock(mock, newMethod(mock.getClass(), "testMethod", newClass[].class, (Object[]) new Object[0])), genericMetadata);
 }

 private Method newMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
 try {
 return clazz.getMethod(methodName, parameterTypes);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException(e);
 }
 }

 private Class<?> newClass(String className) {
 try {
 return Class.forName(className);
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }

 private List<Class<?>> newClassArray(Class<?>... classes) {
 return Arrays.asList(classes);
 }
}