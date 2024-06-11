import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Answer<Object> answer = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, "toString", new Object[0]);

        when(answer.answer(invocation)).thenReturn("Mock for String, hashCode: 12345");

        String actual = answer.answer(invocation).toString();
        String expected = "Mock for String, hashCode: 12345";

        assert(actual.equals(expected));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Answer<Object> answer = new ReturnsEmptyValues();
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        InvocationOnMock invocation = new InvocationOnMock(mock1, "compareTo", new Object[]{mock2});

        when(answer.answer(invocation)).thenReturn(1);

        int actual = answer.answer(invocation).compareTo(mock2);
        int expected = 1;

        assert(actual == expected);
    }

    // Continue with test cases for returnValueFor method
}