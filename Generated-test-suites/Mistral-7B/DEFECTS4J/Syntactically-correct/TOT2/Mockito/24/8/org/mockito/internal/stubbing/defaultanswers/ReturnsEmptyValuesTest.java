import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import static org.junit.Assert.assertEquals;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = new Object();
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        String expectedMockDescription = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        String mockName = answer.answer(Mockito.mockingDetails(mock).getInvocationMockingDetails().getInvocationOnMock());
        assertEquals(expectedMockDescription, mockName);
    }
}