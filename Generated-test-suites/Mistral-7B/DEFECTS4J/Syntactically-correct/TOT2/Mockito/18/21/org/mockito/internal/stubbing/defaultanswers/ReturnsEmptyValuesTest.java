import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

public class ReturnsEmptyValuesTest {

    @BeforeAll
    static void initMocks() {
        mockUtil = Mockito.mock(MockUtil.class);
        methodsGuru = Mockito.mock(ObjectMethodsGuru.class);
    }

    @Test
    void testAnswerToStringMethod() {
        Mockito.when(methodsGuru.isToString(Mockito.any()))
                .thenReturn(true);
        Mockito.when(mockUtil.getMockName(Mockito.any()))
                .thenReturn(Mockito.mock(MockName.class));

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Object.class);
        MockName name = Mockito.mock(MockName.class);

        when(name.isDefault()).thenReturn(false);

        String result = (String) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));

        assertEquals("Mock for [mock class], hashCode: 12345", result);

        verify(mockUtil, times(1)).getMockName(mock);
        verify(methodsGuru, times(1)).isToString(Mockito.any());
        verifyNoMoreInteractions(mockUtil, methodsGuru);
    }

    @Test
    void testAnswerCompareToMethod() {
        Mockito.when(methodsGuru.isCompareToMethod(Mockito.any()))
                .thenReturn(true);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock = Mockito.mock(Object.class);
        Object arg = Mockito.mock(Object.class);

        when(mock.equals(arg)).thenReturn(true);

        int result = (int) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("compareTo")));

        assertEquals(0, result);

        verify(mock, times(1)).equals(arg);
        verify(methodsGuru, times(1)).isCompareToMethod(Mockito.any());
        verifyNoMoreInteractions(mock, methodsGuru);
    }

    //Add test case for answer method with other method signatures
}