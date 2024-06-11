import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.when;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializableMethod() {
        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        mockSettings = mockSettings.serializable();
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());

        // Test with null array
        Class<?>[] nullExtraInterfaces = null;
        mockSettings = mockSettings.extraInterfaces(nullExtraInterfaces);
        assertFalse(mockSettings.isSerializable());

        // Test with non-interface class
        Class<?> nonInterfaceClass = Object.class;
        mockSettings = mockSettings.extraInterfaces(nonInterfaceClass);
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testDefaultAnswerMethod() {
        Answer<Object> answer = mock(Answer.class);
        mockSettings = mockSettings.defaultAnswer(answer);
        Object result = mockSettings.getDefaultAnswer();
        assertEquals(answer, result);

        // Test with null answer
        Answer<Object> nullAnswer = null;
        mockSettings = mockSettings.defaultAnswer(nullAnswer);
        assertNull(mockSettings.getDefaultAnswer());
    }
}