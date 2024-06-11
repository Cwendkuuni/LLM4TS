import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

 private Object mock;
 private Stubber stubber;

 @Before
 public void setUp() {
 mock = Mockito.mock(Object.class);
 stubber = mockito3(mock);
 }

 @Test
 public void testInvocationMatcher() throws Exception {
 // Test constructor with method and empty matchers list
 Invocation invocation = new Invocation(mock, getClass().getMethod("testMethod", int.class, String.class), new Object[] {1, "arg2"}, new Location(5, 10), null);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);

 // Test matches method
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Arrays.<Matcher>asList(is(equalTo(1)), is(equalTo("arg2"))));
 assertTrue(invocationMatcher1.matches(invocationMatcher2));

 // Test toString method
 String expectedToString = "InvocationMatcher [invocation=Invocation [mock=[MockMockito], method=testMethod(int, String), arguments=[1, arg2], location=Location [line=5, column=10]], matchers=[]]";
 assertEquals(expectedToString, invocationMatcher1.toString());

 // Test hasSameMethod method
 InvocationMatcher invocationMatcher3 = new InvocationMatcher(new Invocation(mock, getClass().getMethod("testMethod", String.class), new Object[] {"arg1"}, new Location(10, 15), null));
 assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher3));

 // Test toString with printSettings
 PrintSettings printSettings = new PrintSettings();
 String expectedToStringWithPrintSettings = "InvocationMatcher [invocation=Invocation [mock=[MockMockito], method=testMethod(int, String), arguments=[1, arg2], location=Location [line=5, column=10]], matchers=[]]\n\n" +
         "Invocation:\n" +
         "  This is your mock:\n" +
         "  - Class: org.mockito.internal.invocation.InvocationMatcher\n" +
         "  - Interface: java.lang.reflect.Method\n" +
         "  - Name: testMethod\n" +
         "  - Return type: void\n" +
         "  - Arguments: [1, arg2]\n" +
         "  - Location:\n" +
         "    Line: 5\n" +
         "    Column: 10\n" +
         "  - Invocation text:\n" +
         "    testMethod(1, arg2)\n" +
         "  - Mocked method:\n" +
         "    public void testMethod(int arg1, java.lang.String arg2);\n" +
         "    - at InvocationMatcherTest.lambda$testInvocationMatcher$0(InvocationMatcherTest.java:36)\n" +
         "    - at InvocationMatcherTest.testInvocationMatcher(InvocationMatcherTest.java:35)\n" +
         "  - Arguments:\n" +
         "    [0] = 1\n" +
         "    [1] = arg2\n" +
         "  - Mock type:\n" +
         "    org.mockito.internal.stubbing.MockTypeImpl@4ba5397\n" +
         "  - Invocation type:\n" +
         "    InvocationOnMock [mock=org.mockito.internal.stubbing.MockTypeImpl@4ba5397, method=testMethod(int, String), arguments=[1, arg2], target=InvocationMatcherTest, classLoader=sun.misc.Launcher$AppClassLoader@18b9aac]\n" +
         "  - Invocation argument values:\n" +
         "    [0] = 1\n" +
         "    [1] = arg2\n" +
         "  - Matchers:\n" +
         "    []\n" +
         "  - Mockito version:\n" +
         "    Mockito core 2.23.0\n";
 InvocationMatcher invocationMatcher4 = new InvocationMatcher(invocation);
 String actualToStringWithPrintSettings = invocationMatcher4.toString(printSettings);
 assertEquals(expectedToStringWithPrintSettings, actualToStringWithPrintSettings);

 // Test matches method with different mocks
 InvocationMatcher invocationMatcher5 = new InvocationMatcher(new Invocation(Mockito.mock(Object.class), getClass().getMethod("testMethod", int.class, String.class), new Object[] {2, "arg2"}, new Location(15, 20), null));
 assertFalse(invocationMatcher1.matches(invocationMatcher5));

 // Test matches method with different method names
 InvocationMatcher invocationMatcher6 = new InvocationMatcher(new Invocation(mock, getClass().getMethod("testOtherMethod", int.class, String.class), new Object[] {1, "arg2"}, new Location(5, 10), null));
 assertFalse(invocationMatcher1.matches(invocationMatcher6));

 // Test matches method with overloaded methods
 InvocationMatcher invocationMatcher7 = new InvocationMatcher(new Invocation(mock, getClass().getMethod("testMethod", int.class), new Object[] {1}, new Location(5, 10), null));
 assertFalse(invocationMatcher1.matches(invocationMatcher7));

 // Test captureArgumentsFromInvocation method
 InvocationMatcher invocationMatcher8 = new InvocationMatcher(invocation);
 invocationMatcher8.captureArgumentsFrom(new InvocationOnMock(mock, getClass().getMethod("testMethod", int.class, String.class), new Object[] {3, "arg2"}, new Location(5, 10), null));
 Matcher argMatcher = invocationMatcher8.getMatchers().get(0);
 assertThat(argMatcher, is(instanceOf(org.mockito.internal.matchers.ArgumentsCaptor.class)));
 ArgumentsCaptor argumentsCaptor = (ArgumentsCaptor) argMatcher;
 Object capturedArg1 = argumentsCaptor.getValue(0);
 Object capturedArg2 = argumentsCaptor.getValue(1);
 assertEquals(3, capturedArg1.intValue());
 assertEquals("arg2", capturedArg2.toString());

 // Test exception when arguments don't match
 try {
 InvocationMatcher invocationMatcher9 = new InvocationMatcher(new Invocation(mock, getClass().getMethod("testMethod", int.class, String.class), new Object[] {"notArg2"}, new Location(5, 10), null));
 InvocationMatcher invocationMatcher10 = new InvocationMatcher(new InvocationOnMock(mock, getClass().getMethod("testMethod", int.class, String.class), new Object[] {1, "arg2"}, new Location(5, 10), null));
 invocationMatcher9.matches(invocationMatcher10);
 fail("Unexpected match");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test(expected = MockitoException.class)
 public void testArgumentsMismatch() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, getClass().getMethod("testMethod", int.class, String.class), new Object[] {1, "arg2"}, new Location(5, 10), null));
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, getClass().getMethod("testMethod", int.class), new Object[] {1}, new Location(5, 10), null));
 invocationMatcher.matches(invocationMatcher2);
 fail("Unexpected match");
 }
}