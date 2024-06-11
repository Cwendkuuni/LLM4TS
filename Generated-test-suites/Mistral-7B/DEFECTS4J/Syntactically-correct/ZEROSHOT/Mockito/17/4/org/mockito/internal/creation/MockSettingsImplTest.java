import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMethod() {
        MockSettings settings = new MockSettingsImpl().serializable();
        Assert.assertTrue(settings.isSerializable());
    }

    @Test(expected = AssertionError.class)
    public void testExtraInterfacesNull() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(null);
    }

    @Test(expected = AssertionError.class)
    public void testExtraInterfacesEmptyArray() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(new Class[0]);
    }

    @Test(expected = AssertionError.class)
    public void testExtraInterfacesNullInterface() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(new Class[]{null});
    }

    @Test(expected = AssertionError.class)
    public void testExtraInterfacesNonInterface() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(new Class[]{String.class});
    }

    @Test
    public void testGetMockNameMethod() {
        MockSettings settings = new MockSettingsImpl();
        settings.initiateMockName(String.class);
        MockName mockName = settings.getMockName();
        Assert.assertNotNull(mockName);
    }

    @Test
    public void testGetExtraInterfacesMethod() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(new Class[]{Serializable.class});
        Class<?>[] expectedExtraInterfaces = new Class[]{Serializable.class};
        Class<?>[] actualExtraInterfaces = settings.getExtraInterfaces();
        Assert.assertArrayEquals(expectedExtraInterfaces, actualExtraInterfaces);
    }

    @Test
    public void testGetSpiedInstanceMethod() {
        Object spiedInstance = new Object();
        MockSettings settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        Object actualSpiedInstance = settings.getSpiedInstance();
        Assert.assertEquals(spiedInstance, actualSpiedInstance);
    }

    @Test
    public void testNameMethod() {
        MockSettings settings = new MockSettingsImpl().name("MockName");
        String actualName = settings.getName();
        Assert.assertEquals("MockName", actualName);
    }

    @Test
    public void testDefaultAnswerMethod() {
        Answer<Object> answer = Mockito.mock(Answer.class);
        MockSettings settings = new MockSettingsImpl().defaultAnswer(answer);
        Answer<Object> actualDefaultAnswer = settings.getDefaultAnswer();
        Assert.assertEquals(answer, actualDefaultAnswer);
    }

    @Test
    public void testIsSerializableMethod() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(new Class[]{Serializable.class});
        boolean actualIsSerializable = settings.isSerializable();
        Assert.assertTrue(actualIsSerializable);
    }

    @Test
    public void testInitiateMockNameMethod() {
        Class<?> classToMock = String.class;
        MockSettings settings = new MockSettingsImpl();
        settings.initiateMockName(classToMock);
        MockName actualMockName = settings.getMockName();
        Assert.assertNotNull(actualMockName);
        Assert.assertEquals(classToMock.getName(), actualMockName.name());
    }
}