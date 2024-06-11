import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswerToStringMethod() {
        MockName name = mock(MockName.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(mock(Method.class));
        invocation.setMock(mock);
        invocation.setArguments(new Object[]{name});

        String result = (String) returnsEmptyValues.answer(invocation);

        //Verify that toString method is called
        verify(mock).toString();

        //Assert that the result is as expected
        assertNotNull(result);
        assertTrue(result.contains("Mock for"));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mockA = mock(Object.class);
        Object mockB = mock(Object.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(mock(Method.class));
        invocation.setArguments(new Object[]{mockA, mockB});

        int result = (Integer) returnsEmptyValues.answer(invocation);

        //Assert that compareTo method is not called
        verify(mockA, never()).compareTo(mockB);

        //Assert that the result is as expected (anything but 0 is good)
        assertNotNull(result);
        assertTrue(result > 0);
    }
}