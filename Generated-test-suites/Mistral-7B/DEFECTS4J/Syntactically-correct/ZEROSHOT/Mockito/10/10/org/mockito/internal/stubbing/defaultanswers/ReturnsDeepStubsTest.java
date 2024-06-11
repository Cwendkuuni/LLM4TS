package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.AnswerWithMock;
import org.mockito.internal.stubbing.AnswerWithStub;
import org.mockito.internal.stubbing.DeepStubbedAnswer;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMethod;
import org.mockito.stubbing.AnswerWithDeepStub;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test class for {{ReturnsDeepStubs}}.
 */
public class ReturnsDeepStubsTest {

 public static class TestClass<T extends Number> {

     interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
     }

     GenericsNest<Number> mock;

     void test() {
         Number number = mock.entrySet().iterator().next().getValue().iterator().next();
         System.out.println(number.getClass());
     }
 }

 // Test method for constructor and answer method
 @SuppressWarnings("unchecked")
 @Test
 public void testDeepStubs() {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
     ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);

     // Test that the deep stub is returned when the invocation matches
     InvocationOnMock invocation = Mockito.mock(InvocationOnMethod.class);
     when(invocation.getMethod()).thenReturn(getMethodWithReturnType(Number.class));
     when(mock.entrySet().iterator().next()).thenReturn(mock(Map.Entry.class));
     when(mock(Map.Entry.class).getValue()).thenReturn(mock(Number.class));
     when(mock(Number.class).iterator()).thenReturn(mock(Iterators.Iterator.class));
     when(mock(Iterators.Iterator.class).next()).thenReturn(mock(Number.class));

     verify(mock).answer(invocationCaptor.capture());
     InvocationOnMock capturedInvocation = invocationCaptor.getValue();
     Object returnedObject = returnsDeepStubs.answer(capturedInvocation);

     Assert.assertSame(mock, returnedObject);

     // Test that the shallow stub is returned when the invocation does not match
     verifyNoMoreInteractions(mock);
     verify(mock).answer(Mockito.eq(invocation));

     // Test that the shallow stub is returned when the return type is not mockable
     mock = mock(String.class, new ReturnsDeepStubs());
     capturedInvocation = ArgumentCaptor.forClass(InvocationOnMock.class).capture(mock.test());

     returnedObject = returnsDeepStubs.answer(capturedInvocation);

     Assert.assertNotSame(mock, returnedObject);
     Assert.assertEquals(Mockito.RETURNS_DEEP_STUBS, returnedObject);
 }

 // Test method for deepStub method
 @Test
 public void testDeepStub() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     InternalMockHandler<Object> mockHandler = new MockUtil().getMockHandler(mock(Object.class));
     InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

     Object deepStub = new Object() {
     };
     container.addAnswer(new AnswerWithMock<Object>(deepStub), false);
     Object invocationMock = mock(Object.class);
     ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
     when(mock(Object.class).answer(invocationCaptor.capture())).thenAnswer(new AnswerWithStub<Object>(deepStub));

     Object returnedObject = returnsDeepStubs.deepStub(Mockito.mock(InvocationOnMock.class), Mockito.mock(GenericMetadataSupport.class));

     Assert.assertSame(deepStub, returnedObject);
 }

 // Test method for newDeepStubMock method
 @Test
 public void testNewDeepStubMock() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
     Object mock = mock(Object.class, new ReturnsDeepStubs());
     Object newDeepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

     Assert.assertSame(mock, newDeepStubMock);
 }

 // Test method for withSettingsUsing method
 @Test
 public void testWithSettingsUsing() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
     MockSettings mockSettings = withSettings().serializable();

     Object mock = mock(Object.class, returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));

     Assert.assertEquals(mockSettings, mockitoCore().getMockSettings(mock));
     Assert.assertEquals(returnsDeepStubs, mockitoCore().getAnswer(mock));
 }

 // Test method for deepStub Annotation
 @Test
 public void testDeepStubAnnotation() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     ArgumentCaptor<Class<?>> answerAnnotationsCaptor = ArgumentCaptor.forClass(Answer.class);
     Object mock = mock(Object.class);

     when(mockitoCore().getAnswer(mock)).thenReturn(returnsDeepStubs);
     when(mockitoCore().getMockSettings(mock)).thenReturn(withSettings().defaultAnswer(Mockito.RETURNS_DEEP_STUBS));

     Class<?> aClass = Class.forName("org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs");
     Class<?> annotationType = aClass.getAnnotation(Answer.class);

     List<Class<?>> answerAnnotations = Arrays.asList(mockitoCore().getAnswer(mock).getClass().getAnnotations());

     Assert.assertEquals(annotationType, answerAnnotations.get(0));
 }

 // Test method for ReturnsDeepStubsSerializationFallback class
 @Test
 public void testReturnsDeepStubsSerializationFallback() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
     ReturnsDeepStubs returnsDeepStubsSerializationFallback = spy(new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata));

     Object mock = mock(Object.class, returnsDeepStubs);
     InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
     ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
     when(mockitoCore().getAnswer(mock)).thenReturn(returnsDeepStubs);
     when(mockitoCore().getMockSettings(mock)).thenReturn(withSettings().defaultAnswer(returnsDeepStubsSerializationFallback));

     when(returnsDeepStubsSerializationFallback.answer(invocationMock)).thenAnswer(new AnswerWithStub<Object>(mock));

     Object returnedObject = returnsDeepStubsSerializationFallback.answer(invocationMock);

     Assert.assertSame(mock, returnedObject);
 }

 // Test method for DeeplyStubbedAnswer class
 @Test
 public void testDeeplyStubbedAnswer() throws Exception {
     ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
     Object mock = mock(Object.class);
     Object deepStub = mock(Object.class);
     InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
     ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);

     InvocationContainerImpl container = new InvocationContainerImpl();
     container.addAnswer(new DeeplyStubbedAnswer(deepStub), false);
     when(mockitoCore().getMockSettings(mock)).thenReturn(withSettings().defaultAnswer(new ReturnsDeepStubsSerializationFallback(null)));
     when(mockitoCore().getAnswer(mock)).then(invocation -> new DeeplyStubbedAnswer(mock));

     when(mock(Object.class).answer(invocationCaptor.capture())).thenAnswer(new AnswerWithStub<Object>(deepStub));

     Assert.assertSame(deepStub, returnsDeepStubs.answer(invocationMock));
 }

 // Helper method to get the method with the given return type
 private Method getMethodWithReturnType(Class<?> returnType) throws Exception {
     Class<?> aClass = Class.forName("org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubsTest$TestClass");
     Method method = aClass.getDeclaredMethod("test");
     Type returnTypeType = method.getGenericReturnType();
     if (returnTypeType instanceof ParameterizedType) {
         Type[] actualTypeArguments = ((ParameterizedType) returnTypeType).getActualTypeArguments();
         Type genericReturnType = actualTypeArguments[0];
         if (genericReturnType instanceof Class) {
             return (Method) ((Class<?>) genericReturnType).getDeclaredMethod("getValue");
         }
     }
     throw new Exception("Method does not have the given return type.");
 }
}