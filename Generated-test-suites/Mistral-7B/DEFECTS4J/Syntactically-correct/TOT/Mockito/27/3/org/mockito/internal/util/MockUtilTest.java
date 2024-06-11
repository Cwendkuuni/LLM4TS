import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.misusing.NotAMockException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Class<String> stringClass;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 setting = new MockSettingsImpl();
 stringClass = String.class;
 spiedInstance = new Object();
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock for a valid class with no extra interfaces
 T mock1 = mockUtil.createMock(String.class, setting);
 assertNotNull(mock1);
 assertEquals(String.class, mock1.getClass());

 // Test creating a mock for a valid class with extra interfaces
 MockSettingsImpl settingsWithInterfaces = new MockSettingsImpl();
 settingsWithInterfaces.setExtraInterfaces(new Class<?>[] {Serializable.class});
 T mock2 = mockUtil.createMock(String.class, settingsWithInterfaces);
 assertNotNull(mock2);
 assertEquals(String.class, mock2.getClass());
 assertTrue(mock2 instanceof Serializable);

 // Test creating a mock for a null class throws NotAMockException
 try {
 mockUtil.createMock(null, setting);
 fail("Should have thrown NotAMockException");
 } catch (NotAMockException e) {
 // Expected
 }

 // Test creating a mock for a class that is already a mock throws NotAMockException
 T existingMock = mockUtil.createMock(spiedInstance.getClass(), setting);
 try {
 mockUtil.createMock(existingMock.getClass(), setting);
 fail("Should have thrown NotAMockException");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockForInvalidClass() {
 mockUtil.createMock(int.class, setting);
 fail("Should have thrown NotAMockException");
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 // Test creating a mock with a spied instance and copying its state
 String spiedString = "Mockito";
 setting.setSpiedInstance(spiedString);
 T mockWithSpiedInstance = mockUtil.createMock(String.class, setting);
 assertEquals(spiedString, mockWithSpiedInstance.toString());

 // Test creating a mock with a spied instance and no state copying
 setting.setSpiedInstance(spiedString);
 setting.setInitiateMockName(false);
 T mockWithNoStateCopying = mockUtil.createMock(String.class, setting);
 assertNull(mockWithNoStateCopying.toString());
 }

 @Test
 public void testResetMock() {
 // Test resetting a mock
 T mockToReset = mockUtil.createMock(String.class, setting);
 resetMock(mockToReset);
 verifyNoMoreInteractions(mockToReset);
 }

 @Test
 public void testResetMockWithNullMock() {
 // Test resetting a null mock throws NotAMockException
 mockUtil.resetMock(null);
 fail("Should have thrown NotAMockException");
 }

 @Test
 public void testIsMock() {
 // Test isMock returns true for a mock
 T mock = mockUtil.createMock(String.class, setting);
 assertTrue(mockUtil.isMock(mock));

 // Test isMock returns false for a non-mock
 Object nonMock = new Object();
 assertFalse(mockUtil.isMock(nonMock));
 }

 @Test
 public void testGetMockHandler() {
 // Test getMockHandler returns the correct handler for a mock
 T mock = mockUtil.createMock(String.class, setting);
 MockHandlerInterface<T> handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 assertEquals(mock.getClass(), handler.getMock().getClass());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 fail("Should have thrown NotAMockException");
 }

 @Test
 public void testGetInterceptor() {
 // Test getInterceptor returns the correct interceptor for a mock
 T mock = mockUtil.createMock(String.class, setting);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 fail("Should have thrown NotAMockException");
 }

 @Test
 public void testGetMockName() {
 // Test getMockName returns the correct name for a mock
 T mock = mockUtil.createMock(String.class, setting);
 MockName name = mockUtil.getMockName(mock);
 assertEquals("MockitoMock_" + mock.getClass().getName() + "_" + hashCode(mock), name.getName());
 }

 @Test
 public void testGetMockNameWithNullMock() {
 // Test getMockName returns null for a null mock
 MockName name = mockUtil.getMockName(null);
 assertNull(name);
 }
}