package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = Mockito.answer(new CallsRealMethods());
        Object result = target.methodCallingRealMethod(1);
        assertEquals(result, answer.answer(Mockito.argThat(i -> i.getTarget() == target && i.getMethod().getName().equals("methodCallingRealMethod"))));
        verify(target).methodCallingRealMethod(1);
    }

    @Test
    public void testCallsRealMethodsWithArguments() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = Mockito.answer(new CallsRealMethods());
        Object arg1 = new Object();
        Object arg2 = new Object();
        Object result = target.methodCallingRealMethod(arg1, arg2);
        assertEquals(result, answer.answer(Mockito.argThat(i -> i.getTarget() == target && i.getMethod().getName().equals("methodCallingRealMethod"))).getArguments()[0]);
        assertEquals(arg1, answer.answer(Mockito.argThat(i -> i.getTarget() == target && i.getMethod().getName().equals("methodCallingRealMethod"))).getArguments()[1]);
        assertEquals(arg2, answer.answer(Mockito.argThat(i -> i.getTarget() == target && i.getMethod().getName().equals("methodCallingRealMethod"))).getArguments()[2]);
        verify(target).methodCallingRealMethod(arg1, arg2);
    }

    @Test
    public void testCallsRealMethodsWithException() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = Mockito.answer(new CallsRealMethods());
        try {
            target.methodCallingRealMethodWithException();
        } catch (Exception e) {
            Object result = e.getCause();
            assertEquals(result, answer.answer(Mockito.argThat(i -> i.getTarget() == target && i.getMethod().getName().equals("methodCallingRealMethodWithException"))));
            verify(target).methodCallingRealMethodWithException();
        }
    }

    @Test
    public void testCallsRealMethodsInStaticContext() {
        Object target = mock(TestClass.class);
        CallsRealMethods answer = Mockito.answer(new CallsRealMethods());
        Object staticTarget = TestClass.staticMethodCallingRealMethod();
        assertEquals(staticTarget, answer.answer(Mockito.argThat(i -> i.getTarget() == TestClass.class && i.getMethod().getName().equals("staticMethodCallingRealMethod"))));
        verify(TestClass.class).staticMethodCallingRealMethod();
    }

    interface TestClass {
        Object methodCallingRealMethod(int arg1) throws Exception;
        Object methodCallingRealMethod(Object arg1, Object arg2) throws Exception;
        static Object staticMethodCallingRealMethod() throws Exception {
            return new Object();
        }

        Object methodCallingRealMethodWithException() throws Exception;
    }
}