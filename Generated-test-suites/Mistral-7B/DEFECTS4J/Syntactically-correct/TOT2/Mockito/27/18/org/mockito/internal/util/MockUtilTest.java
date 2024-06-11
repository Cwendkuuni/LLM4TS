import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<DummyClass> classToMock = DummyClass.class;
        T mock = MockUtil.createMock(classToMock, settings);
        verify(mock).someMethod(); // Replace 'someMethod' with a real method from DummyClass
    }
}