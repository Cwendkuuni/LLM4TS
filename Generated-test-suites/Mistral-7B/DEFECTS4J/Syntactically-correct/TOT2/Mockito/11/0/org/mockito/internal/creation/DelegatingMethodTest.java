import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method methodMock = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(methodMock);
        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        assertEquals(expectedReturnType, actualReturnType);
    }
}