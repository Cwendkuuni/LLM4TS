package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        Object realObject = new Object() {
            public String method1() {
                return "real method 1";
            }
        };

        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = Mockito.mock(Object.class);

        Mockito.doAnswer(answer).when(mockObject).method1();

        String result = mockObject.method1();

        Assert.assertEquals("real method 1", result);
        verify(mockObject).callRealMethod();

        // Testing the real method call with an argument
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doAnswer(answer).when(mockObject).method2(argumentCaptor.capture());
        mockObject.method2("test argument");
        Assert.assertNotNull(argumentCaptor.getValue());
        verify(realObject).method1();
    }

    @Test(expected = Throwable.class)
    public void testThrowingExceptionFromRealMethod(
            @InvocationOnMock Object realObject) throws Throwable {
        CallsRealMethods answer = new CallsRealMethods();
        Object mockObject = Mockito.mock(Object.class);
        Mockito.doAnswer(answer).when(mockObject).method3();

        Assert.assertThrows(RuntimeException.class, () -> {
            mockObject.method3();
            realObject.methodThatThrowsException();
        });
        verify(realObject).methodThatThrowsException();
    }
}