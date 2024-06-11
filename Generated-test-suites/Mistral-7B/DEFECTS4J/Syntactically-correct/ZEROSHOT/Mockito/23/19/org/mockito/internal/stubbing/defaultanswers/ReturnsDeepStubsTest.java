import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setup() {
 mock = mock(Object.class, new ReturnsDeepStubs());
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 }

 @Test
 public void testAnswer() throws Exception {
 // Test with a simple type
 Object result = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("testMethod")));
 assertEquals(mock, result);

 // Test with a generic type
 @SuppressWarnings("unchecked")
 interface Interface<T> {
 T method();
 }

 Interface<String> mockInterface = mock(Interface.class, new ReturnsDeepStubs());
 Type genericType = mockInterface.getClass().getGenericInterfaces()[0];
 Type[] types = ((Type[]) genericType.getActualTypeArguments());
 Type returnType = types[0];

 Object resultGeneric = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("method")));
 assertEquals(mockInterface, resultGeneric);

 // Test with a nested generic type
 interface NestedInterface<K> {
 K method();
 K methodNested();
 }

 NestedInterface<Integer> mockNestedInterface = mock(NestedInterface.class, new ReturnsDeepStubs());
 Object resultNested = returnsDeepStubs.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("methodNested")));
 assertEquals(Mockito.mock(Integer.class), resultNested);
 }

 @Test
 public void testGetMock() throws Exception {
 GenericDeclaration genericDeclaration = mock.getClass().getGenericInterfaces()[0];
 Type genericType = genericDeclaration.getTypeArguments()[0];
 Type[] types = ((Type[]) genericType.getActualTypeArguments());
 Type returnType = types[0];

 InvocationOnMock invocation = Mockito.argThat(invocation -> invocation.getMethod().getName().equals("method"));
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 container.setInvocationForStubbing(invocation);
 container.setInvocations(new Object[0]);
 container.setAnswers(new Answer[0]);

 Object result = returnsDeepStubs.getMock(invocation, Mockito.argThat(metadata -> metadata.rawType().equals(returnType.getTypeName())));
 assertEquals(mock, result);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 mockitoCoreSetup(); // need to initialize MockitoCore to mock InvocationContainerImpl

 Object container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getInvocationForStubbing()).thenReturn(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("testMethod")));

 Object mockReturned = returnsDeepStubs.recordDeepStubMock(mock, container);
 assertEquals(mock, mockReturned);

 verify(container).addAnswer(ArgumentMatchers.eq(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock;
 }
 }), ArgumentMatchers.eq(false));
 }

 private void mockitoCoreSetup() {
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InternalMockHandler<Object> containerHandler = new MockUtil().getMockHandler(container);
 MockitoCore mockitoCore = (MockitoCore) handler.getMockSettings().getMockitoCore();
 MockitoCore containerMockitoCore = (MockitoCore) containerHandler.getMockSettings().getMockitoCore();
 mockitoCore.registerMockHandler(containerMockitoCore);
 }
}