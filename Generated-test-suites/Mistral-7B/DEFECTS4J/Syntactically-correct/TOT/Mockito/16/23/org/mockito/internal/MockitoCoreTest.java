import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Assert;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockUtil mockUtil;
 private ThreadSafeMockingProgress mockingProgress;
 private List<Object> mocks;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 reporter = MockitoCore.newReporter();
 mockUtil = MockitoCore.newMockUtil();
 mockingProgress = MockitoCore.newThreadSafeMockingProgress();
 }

 @Test
 public void testMockCreation() {
 // Test mock creation with default settings
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 MockitoCore core = new MockitoCore();
 Foo mockFoo = core.mock(Foo.class, mockSettings, false);
 // Test mock creation with custom settings
 MockSettingsImpl customMockSettings = new MockSettingsImpl().mockBehavior(MockBehavior.STRICT_STUBS);
 MockitoCore coreWithCustomSettings = new MockitoCore();
 Bar mockBar = coreWithCustomSettings.mock(Bar.class, customMockSettings, false);
 }

 @Test
 public void testStubbing() {
 Foo foo = mock(Foo.class);
 OngoingStubbing<Foo> stubbing = MockitoCore.stub(foo);
 stubbing.thenReturn(new Foo());
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 when(foo.getFooString(any(String.class))).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Mocked response";
 }
 });
 verify(foo).getFooString("Argument passed to getFooString method");
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNullMock() {
 MockitoCore core = new MockitoCore();
 core.verify(null, Mockito.verifyNoMoreInteractions());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 Object notAMock = new Object();
 MockitoCore core = new MockitoCore();
 core.verify(notAMock, Mockito.verifyNoMoreInteractions());
 }

 @Test
 public void testReset() {
 Foo foo = mock(Foo.class);
 OngoingStubbing<Foo> stubbing = MockitoCore.stub(foo);
 stubbing.thenReturn(new Foo());
 reset(foo);
 verify(foo).getFooString(any(String.class));
 verifyNoMoreInteractions(foo);
 }

 @Test
 public void testInOrder() {
 Foo foo1 = mock(Foo.class);
 Foo foo2 = mock(Foo.class);
 InOrder inOrder = Mockito.inOrder(foo1, foo2);
 // Test in order verification
 when(foo1.getFooString(any(String.class))).thenReturn("Return value for foo1");
 when(foo2.getFooString(any(String.class))).thenReturn("Return value for foo2");
 inOrder.verify(foo1).getFooString("Argument passed to getFooString method for foo1");
 inOrder.verify(foo2).getFooString("Argument passed to getFooString method for foo2");
 // Test out of order verification
 inOrder.verify(foo2).getFooString("Argument passed to getFooString method for foo2");
 inOrder.verify(foo1).getFooString("Argument passed to getFooString method for foo1");
 }

 @Test
 public void testDoAnswer() {
 Foo foo = mock(Foo.class);
 Stub<Foo> stub = Mockito.stub(foo);
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Mocked response";
 }
 };
 stub.doAnswer(answer);
 verify(foo).getFooString(any(String.class));
 }

 @Test
 public void testStubVoid() {
 Foo foo = mock(Foo.class);
 Stubber stubber = MockitoCore.stubVoid(foo);
 stubber.doNothing();
 foo.doSomething();
 verify(foo).doSomething();
 }

 @Test
 public void testValidateMockitoUsage() {
 MockitoCore core = new MockitoCore();
 core.validateMockitoUsage();
 }

 @Test
 public void testGetLastInvocation() {
 Foo foo = mock(Foo.class);
 OngoingStubbing<Foo> stubbing = MockitoCore.stub(foo);
 stubbing.thenReturn(new Foo());
 Invocation lastInvocation = core.getLastInvocation();
 Assert.assertEquals("getFooString", lastInvocation.getMethod().getName());
 }
}