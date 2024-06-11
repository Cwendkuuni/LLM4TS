import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.InvocationMatcher;
import org.mockito.internal.matchers.PrintSettings;
import org.mockito.internal.matchers.Stubbing;
import org.mockito.internal.util.collections.Collections3;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.invocation.InvocationMatcherImpl;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocationImpl;
import org.mockito.internal.reporting.PrintSettingsImpl;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.verification.VerificationSettings;
import org.powermock.api.mockito.annotations.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.common.PowerMockRunner;
import org.powermock.modules.junit4.common.PowerMocks;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItemInArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@PrepareForTest(InvocationMatcher.class)
public class InvocationMatcherTest {

    private static final long serialVersionUID = -3047126096857467610L;
    private static final String INVOCATION_CLASS_NAME = "org.mockito.internal.invocation.Invocation";
    private static final String ARGUMENTS_COMPARATOR_CLASS_NAME = "org.mockito.internal.matchers.ArgumentsComparator";
    private static final String PRINT_SETTINGS_CLASS_NAME = "org.mockito.internal.reporting.PrintSettings";
    private static final String PRINTING_FRIENDLY_INVOCATION_CLASS_NAME = "org.mockito.internal.reporting.PrintingFriendlyInvocation";

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private MethodInterceptor methodInterceptor;

    @Before
    public void setUp() throws Exception {
        Method method = Reflections.classRealName(INVOCATION_CLASS_NAME).getMethod("getInvocation");
        invocation = mock(Invocation.class);
        when(method.invoke(any(Object.class))).thenReturn(invocation);

        method = Reflections.classRealName(ARGUMENTS_COMPARATOR_CLASS_NAME).getConstructor(Object[].class);
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        when(method.newInstance(any(Object[].class))).thenReturn(argumentsComparator);

        method = Reflections.classRealName(PRINT_SETTINGS_CLASS_NAME).getConstructor();
        PrintSettings printSettings = new PrintSettings();
        when(method.newInstance()).thenReturn(printSettings);

        method = Reflections.classRealName(PRINTING_FRIENDLY_INVOCATION_CLASS_NAME).getConstructor(Invocation.class, List.class, PrintSettings.class);
        PrintingFriendlyInvocation printingFriendlyInvocation = new PrintingFriendlyInvocationImpl(invocation, Collections.<Matcher>emptyList(), printSettings);
        when(method.newInstance(invocation, Collections.<Matcher>emptyList(), printSettings)).thenReturn(printingFriendlyInvocation);

        matchers = new ArrayList<>();
        Matcher argMatcher = argument(eq("argValue"));
        Matcher methodMatcher = argThat(new ArgumentMatcher<Method>() {
            @Override
            public boolean matches(Method method) {
                return method.getName().equals("testMethod");
            }
        });
        matchers.add(argMatcher);
        matchers.add(methodMatcher);

        methodInterceptor = mock(MethodInterceptor.class);
        WhiteboxImpl whitebox = new WhiteboxImpl(methodInterceptor);
        whitebox.setInternalValue("invokedMethod");

        invocationMatcher = new InvocationMatcherImpl(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcherImpl(invocation, matchers);
        assertThat(invocationMatcherWithMatchers.getMatchers(), is(equalTo(matchers)));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcherImpl(invocation);
        List<Matcher> emptyMatchers = Collections.<Matcher>emptyList();
        assertThat(invocationMatcherWithoutMatchers.getMatchers(), is(equalTo(emptyMatchers)));
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method methodfromMatcher = invocationMatcher.getMethod();
        assertThat(method, is(equalTo(methodfromMatcher)));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertThat(invocationFromMatcher, is(equalTo(invocation)));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchersfromMatcher = invocationMatcher.getMatchers();
        assertThat(matchersfromMatcher, is(equalTo(matchers)));
    }

    @Test
    public void testToString() throws Exception {
        String expected = "InvocationMatcher[invocation=Invocation [method=public void test(java.lang.String,int)], argumentsToMatch={ArgumentMatcher [matches = <[argThat(ArgumentMatcher argumentMatcher) > equals(argThat(ArgumentMatcher argumentMatcher)) ]], MethodMatcher [matches = <method test(java.lang.String,int)>]}, printSettings = PrintSettings{}]";
        String toString = invocationMatcher.toString();
        assertThat(toString, is(equalTo(expected)));
    }

    @Test
    public void testMatches() throws Exception {
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithoutMatchers = new InvocationMatcherImpl(invocation);
        InvocationMatcher invocationMatcherWithSameArguments = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithDifferentArguments = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(isNullValue()), argThat(instanceOf(String.class))));
        InvocationMatcher invocationMatcherWithDifferentMethod = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithDifferentMock = new InvocationMatcherImpl(new InvocationOnMock(mock(Object.class)), matchers);

        InvocationMatcher invocationMatcherToCompare = new InvocationMatcherImpl(invocation, matchers);
        Invocation actualInvocation = mock(Invocation.class);
        Object[] arguments = new Object[]{"argValue1", 1};
        when(actualInvocation.getArguments()).thenReturn(arguments);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());

        InvocationMatcher invocationMatcherToCompareWithDifferentArguments = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(isNullValue()), argThat(instanceOf(String.class))));

        // test matches
        verify(methodInterceptor).invoke(eq(invocation.getMock()), eq(invocation.getMethod()), eq(arguments), any(VerificationSettings.class), any(Stubbing.class));
        verifyNoMoreInteractions(methodInterceptor);

        // test matches with same invocation
        assertTrue(invocationMatcherWithMatchers.matches(invocationMatcherWithMatchers));

        // test matches with empty list of matchers
        assertTrue(invocationMatcherWithoutMatchers.matches(invocationMatcherWithoutMatchers));

        // test matches with same arguments
        assertTrue(invocationMatcherWithSameArguments.matches(invocationMatcherToCompare));

        // test does not match with different arguments
        assertFalse(invocationMatcherWithSameArguments.matches(invocationMatcherToCompareWithDifferentArguments));

        // test does not match with different method
        assertFalse(invocationMatcherWithDifferentMethod.matches(invocationMatcherWithSameArguments));

        // test does not match with different mock
        assertFalse(invocationMatcherWithDifferentMock.matches(invocationMatcherWithSameArguments));
    }

    @Test
    public void testSafelyArgumentsMatch() throws Exception {
        InvocationMatcher invocationMatcher = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithSameArguments = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithDifferentArguments = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(isNullValue()), argThat(instanceOf(String.class))));

        Object[] arguments = new Object[]{"argValue1", 1};
        when(invocation.getArguments()).thenReturn(arguments);

        // test safelyArgumentsMatch with same arguments
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));

        // test safelyArgumentsMatch with different arguments
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{"argValue2", 2}));

        // test safelyArgumentsMatch with matcher
        ArgumentMatcher argMatcher = argument(eq("argValue1"));
        InvocationMatcher invocationMatcherWithArgMatcher = new InvocationMatcherImpl(invocation, Arrays.asList(argMatcher));
        assertTrue(invocationMatcherWithArgMatcher.safelyArgumentsMatch(new Object[]{"argValue1", 1}));
        assertFalse(invocationMatcherWithArgMatcher.safelyArgumentsMatch(new Object[]{"argValue2", 2}));

        // test safelyArgumentsMatch with different InvocationMatcher
        InvocationMatcher invocationMatcherToCompare = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(isNullValue()), argThat(instanceOf(String.class))));
        assertFalse(invocationMatcher.safelyArgumentsMatch(invocationMatcherToCompare));
    }

    @Test
    public void testHasSameMethod() throws Exception {
        InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithDifferentMethod = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithOverloadedMethod = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(instanceOf(Integer.class))));

        Method method = invocation.getMethod();
        Method methodToCompare = mock(Method.class);
        when(methodToCompare.getName()).thenReturn("testMethodDifferentName");

        // test hasSameMethod with same method
        assertTrue(invocationMatcherWithSameMethod.hasSameMethod(invocationMatcherWithSameMethod));

        // test hasSameMethod with different method
        assertFalse(invocationMatcherWithSameMethod.hasSameMethod(invocationMatcherWithDifferentMethod));

        // test hasSameMethod with overloaded method
        assertFalse(invocationMatcherWithSameMethod.hasSameMethod(invocationMatcherWithOverloadedMethod));
    }

    @Test
    public void testHasSimilarMethod() throws Exception {
        InvocationMatcher invocationMatcherWithSameMethod = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithDifferentMethod = new InvocationMatcherImpl(invocation, matchers);
        InvocationMatcher invocationMatcherWithSameMethodDifferentInvocation = new InvocationMatcherImpl(new InvocationOnMock(mock(Object.class)), matchers);
        InvocationMatcher invocationMatcherWithOverloadedMethod = new InvocationMatcherImpl(invocation, Arrays.asList(argThat(instanceOf(Integer.class))));

        // test hasSimilarMethod with same method and same invocation
        assertTrue(invocationMatcherWithSameMethod.hasSimilarMethod(invocationMatcherWithSameMethod));

        // test hasSimilarMethod with different method and same invocation
        assertFalse(invocationMatcherWithSameMethod.hasSimilarMethod(invocationMatcherWithDifferentMethod));

        // test hasSimilarMethod with different invocation and same method
        assertFalse(invocationMatcherWithSameMethod.hasSimilarMethod(invocationMatcherWithSameMethodDifferentInvocation));

        // test hasSimilarMethod with overloaded method
        assertFalse(invocationMatcherWithSameMethod.hasSimilarMethod(invocationMatcherWithOverloadedMethod));
    }

    @Test
    public void testToStringWithPrintSettings() throws Exception {
        InvocationMatcher invocationMatcher = new InvocationMatcherImpl(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setShowArguments(true);
        printSettings.setShowMockName(true);

        String expectedString = "InvocationMatcher[invocation=Invocation [method=public void test(java.lang.String,int)], argumentsToMatch={ArgumentMatcher [matches = <[argThat(ArgumentMatcher argumentMatcher) > equals(argThat(ArgumentMatcher argumentMatcher)) ]], MethodMatcher [matches = <method test(java.lang.String,int)>]}, printSettings = PrintSettings{showArguments=true, showMockName=true}]";
        String toString = invocationMatcher.toString(new PrintSettingsImpl(printSettings));
        assertThat(toString, is(equalTo(expectedString)));
    }

    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        InvocationMatcher invocationMatcher = new InvocationMatcherImpl(invocation, matchers);
        Invocation invocationToCaptureFrom = mock(Invocation.class);
        Object[] arguments = new Object[]{"argValue1", 1};
        when(invocationToCaptureFrom.getArguments()).thenReturn(arguments);

        invocationMatcher.captureArgumentsFrom(invocationToCaptureFrom);

        Matcher argMatcher = argument(eq("argValue1"));
        verify(argMatcher).captureFrom(argThat(instanceOf(Object.class)));
        verify(argMatcher).captureFrom(arguments[0]);

        MethodMatcher methodMatcher = argThat(new ArgumentMatcher<Method>() {
            @Override
            public boolean matches(Method method) {
                return method.getName().equals("testMethod");
            }
        });
        verify(methodMatcher).captureFrom(invocation.getMethod());
    }

    @Test
    public void testCreateFrom() throws Exception {
        List<Invocation> invocations = Collections3.newArrayList(invocation, new InvocationOnMock(mock(Object.class)));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcherFromInvocation = invocationMatchers.get(0);
        assertThat(invocationMatcherFromInvocation.getMatchers(), is(equalTo(matchers)));

        InvocationMatcher invocationMatcherFromAnotherInvocation = invocationMatchers.get(1);
        List<Matcher> emptyMatchers = Collections.<Matcher>emptyList();
        assertThat(invocationMatcherFromAnotherInvocation.getMatchers(), is(equalTo(emptyMatchers)));
    }
}