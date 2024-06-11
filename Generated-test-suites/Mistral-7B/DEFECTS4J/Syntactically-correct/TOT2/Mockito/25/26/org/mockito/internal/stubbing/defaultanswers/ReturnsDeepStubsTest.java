import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        // given
        Object mock = Mockito.mock(Object.class, Answers.RETURNS_DEEP_STUBS);

        // when
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = Mockito.doCallRealMethod().answer(invocation).getMock();

        // then
        assertEquals(mock, result);
    }

    @Test
    public void testAnswer_NullInvocationTarget() {
        // given
        Object mock = Mockito.mock(Object.class, Answers.RETURNS_DEEP_STUBS);
        InvocationOnMock nullInvocation = null;

        // when, then
        Assert.assertThrows(NullPointerException.class, () -> Mockito.doCallRealMethod().answer(nullInvocation).getMock());
    }

    @Test
    public void testAnswer_NullInvokedMethod() {
        // given
        Object mock = Mockito.mock(Object.class, Answers.RETURNS_DEEP_STUBS);
        Object nullReturn = null;

        // when
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Object result = Mockito.doCallRealMethod().answer(invocation).getMock();

        // then
        Assert.assertNull(result);
    }

    @Test
    public void testAnswer_DifferentReturnTypes() {
        // given
        Object mock = Mockito.mock(Integer.class, Answers.RETURNS_DEEP_STUBS);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // when
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Object result = Mockito.doCallRealMethod().answer(invocation).getMock();

        // then
        Assert.assertEquals(mock, result);
    }

    // Add test cases for other edge cases and exceptions here

    // ...
}