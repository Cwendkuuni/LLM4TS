package org.mockito.internal.invocation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.matchers.Arguments.argThat;
import static org.mockito.internal.matchers.Arguments.isA;
import static org.mockito.internal.matchers.Arguments.isNull;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.matchers.Arguments.startsWith;
import static org.mockito.internal.matchers.Arguments.value;
import static org.mockito.internal.reporting.PrintSettings.printError;
import static org.mockito.internal.reporting.PrintSettings.printWarning;
import static org.mockito.internal.reporting.PrintSettings.suppress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.collections.Arrays;
import org.mockito.internal.util.reflection.FieldAccess;
import org.mockito.internal.util.reflection.Reflector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {
    private static final String MOCKITO_VERSION = "2.24.4";
    private static final String METHOD_NAME = "testMethod";
    private static final Class<?> MOCKITO_INTERNAL_INVOCATION_MATCHER = InvocationMatcher.class;
    private static final Class<?> MOCKITO_INTERNAL_CAPTURES_ARGUMENTS = CapturesArguments.class;
    private static final String INVOCATION_MATCHER_SERIALIZED =
            "org.mockito.internal.invocation.InvocationMatcher:6:{" +
                    "\"invocation\":{" +
                    "\"method\":{\"name\":\"" + METHOD_NAME + "\",\"returnType\":java.lang.Object.class}" +
                    ",\"arguments\":[null,2,\"three\"]," +
                    "\"location\":{" +
                    "\"line\":7," +
                    "\"file\":\"MockitoTest.java\"" +
                    "}," +
                    "\"matchers\":{" +
                    "\"\":{" +
                    "\"type\":\"java.lang.String\"," +
                    "\"value\":\"four\"" +
                    "}," +
                    "\"classMatcher\":{" +
                    "\"type\":\"org.mockito.ArgumentMatcher\"," +
                    "\"value\":{" +
                    "\"type\":\"java.lang.Class\"," +
                    "\"value\":java.util.ArrayList.class" +
                    "}" +
                    "}" +
                    "}," +
                    "\"serialVersionUID\":-3047126096857467610L" +
                    "}";

    private static final InvocationMatcher TESTED_INSTANCE = new InvocationMatcher(
            new InvocationOnMock(new Object(), new Method(METHOD_NAME, new Class[0]), new Object[0], new Object[0]),
                    Collections.<Matcher>emptyList());

    private InvocationMatcher invocationMatcher;
    private MethodInterceptor methodInterceptor;
    private PrintSettings printSettings;
    private Reflector reflector;
    private ArgumentCaptor<List<Matcher>> matchersCaptor;

    @Before
    public void setUp() {
        methodInterceptor = new MethodInterceptor() {};
        printSettings = new PrintSettings();
        reflector = new Reflector();
        matchersCaptor = ArgumentCaptor.forClass(List.class);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyListMatcher() {
        invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(METHOD_NAME, new Class[0]),
                new Object[0], new Object[0]), Collections.<Matcher>emptyList());
        assertThat(invocationMatcher.getMethod(), is(same(TESTED_INSTANCE.getMethod())));
        assertThat(invocationMatcher.getInvocation(), is(same(TESTED_INSTANCE.getInvocation())));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), equalsTo("one"), equalTo(2));
        invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(METHOD_NAME, new Class[0]),
                new Object[0], new Object[0]), matchers);
        assertThat(invocationMatcher.getMethod(), is(same(TESTED_INSTANCE.getMethod())));
        assertThat(invocationMatcher.getInvocation(), is(same(TESTED_INSTANCE.getInvocation())));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() throws Exception {
        Method testMethod = Reflector.getDeclaredMethod(MockitoInternalInvocationMatcher.class, "getMethod");
        Method actualMethod = invocationMatcher.getMethod();
        assertThat(testMethod, is(same(actualMethod)));
    }

    @Test
    public void testGetInvocation() throws Exception {
        Method testMethod = Reflector.getDeclaredMethod(MockitoInternalInvocationMatcher.class, "getInvocation");
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertThat(testMethod, is(sameMethod(testMethod, actualInvocation)));
    }

    @Test
    public void testGetMatchers() throws Exception {
        Method testMethod = Reflector.getDeclaredMethod(MockitoInternalInvocationMatcher.class, "getMatchers");
        List<Matcher> actualMatchers = (List<Matcher>) Reflector.invokeMethod(invocationMatcher, testMethod.getName());
        assertThat(testMethod, is(sameMethod(testMethod, actualMatchers)));
    }

    @Test
    public void testToString() {
        String printSettingsValue = printWarning();
        printSettings.setPrintWarning(suppress(printSettingsValue));
        String actualToString = invocationMatcher.toString(printSettings);
        printSettings.setPrintWarning(printError());
        String expectedToString = TESTED_INSTANCE.toString(printSettings);
        assertThat(actualToString, is(equalTo(expectedToString)));
    }

    @Test
    public void testMatches() throws Exception {
        Invocation actualInvocation = new InvocationOnMock(new Object(), new Method(METHOD_NAME, new Class[0]),
                new Object[0], new Object[0]);
        boolean actualResult = invocationMatcher.matches(actualInvocation);
        boolean expectedResult = TESTED_INSTANCE.matches(actualInvocation);
        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments = new Object[]{1, "two", 3};
        boolean actualResult = invocationMatcher.safelyArgumentsMatch(arguments);
        boolean expectedResult = TESTED_INSTANCE.safelyArgumentsMatch(arguments);
        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocationWithSameMethod = new InvocationOnMock(new Object(), TESTED_INSTANCE.getMethod(),
                new Object[0], new Object[0]);
        Invocation invocationWithDifferentMethod = new InvocationOnMock(new Object(), new Method("differentMethodName", new Class[0]),
                new Object[0], new Object[0]);
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocationWithSameMethod);
        boolean expectedResult = TESTED_INSTANCE.hasSimilarMethod(invocationWithSameMethod);
        assertThat(actualResult, is(equalTo(expectedResult)));

        actualResult = invocationMatcher.hasSimilarMethod(invocationWithDifferentMethod);
        expectedResult = TESTED_INSTANCE.hasSimilarMethod(invocationWithDifferentMethod);
        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocationWithSameMethod = new InvocationOnMock(new Object(), TESTED_INSTANCE.getMethod(),
                new Object[0], new Object[0]);
        boolean actualResult = invocationMatcher.hasSameMethod(invocationWithSameMethod);
        boolean expectedResult = TESTED_INSTANCE.hasSameMethod(invocationWithSameMethod);
        assertThat(actualResult, is(equalTo(expectedResult)));
    }

    @Test
    public void testGetLocation() throws Exception {
        Method testMethod = Reflector.getDeclaredMethod(MockitoInternalInvocationMatcher.class, "getLocation");
        Location actualLocation = (Location) Reflector.invokeMethod(invocationMatcher, testMethod.getName());
        Location expectedLocation = TESTED_INSTANCE.getLocation();
        assertThat(actualLocation, is(sameLocation(expectedLocation)));
    }

    @Test
    public void testToStringWithMatchers() throws Exception {
        PrintSettings printSettings = new PrintSettings();
        String actualToString = invocationMatcher.toString(printSettings);
        String expectedToString = TESTED_INSTANCE.toString(printSettings);
        assertThat(actualToString, is(equalTo(expectedToString)));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> capturedMatchers = matchersCaptor.captureAllArgs(invocationMatcher::captureArgumentsFrom, new InvocationOnMock(new Object(), TESTED_INSTANCE.getMethod(), new Object[0], new Object[0]));
        List<Matcher> expectedMatchers = TESTED_INSTANCE.getMatchers();
        assertThat(capturedMatchers, is(sameCollection(expectedMatchers)));
    }

    @Test
    public void testSerialization() throws Exception {
        String jsonString = FieldAccess.getPrivateFieldValue(MOCKITO_INTERNAL_INVOCATION_MATCHER, MOCKITO_VERSION) + "\n" + MOCKITO_INTERNAL_INVOCATION_MATCHER.getName() + ":" +
                "{" +
                "\"invocation\":{" +
                "\"method\":{" +
                "\"name\":\"" + METHOD_NAME + "\",\"returnType\":java.lang.Object.class" +
                ",\"annotations\":[\n" +
                "{\"annotationType\":\"org.mockito.Mock\"," +
                "\"values\":{\"mockClass\":java.lang.Object.class,\"mock\":org.junit.runner.RunWith.class,\"value\":org.mockito.runners.MockitoJUnitRunner.class}}" +
                "]," +
                "\"arguments\":[null,2,\"three\"]," +
                "\"location\":{" +
                "\"line\":7," +
                "\"file\":\"MockitoTest.java\"" +
                "}," +
                "\"matchers\":{" +
                "\"\":{" +
                "\"type\":\"java.lang.String\"," +
                "\"value\":\"four\"" +
                "}," +
                "\"classMatcher\":{" +
                "\"type\":\"org.mockito.ArgumentMatcher\"," +
                "\"value\":{" +
                "\"type\":\"java.util.ArrayList\"," +
                "\"value\":[\n" +
                "{\"type\":\"org.hamcrest.CoreMatchers\"," +
                "\"value\":{" +
                "\"type\":\"org.hamcrest.Matcher\"," +
                "\"value\":{\"type\":\"java.lang.String\"," +
                "\"value\":\"one\"}}," +
                "{\"type\":\"org.hamcrest.Matcher\"," +
                "\"value\":{\"type\":\"org.mockito.ArgumentMatcher\"," +
                "\"value\":{\"type\":\"java.lang.Class\"," +
                "\"value\":java.util.ArrayList.class}}}" +
                "}" +
                "}," +
                "\"serialVersionUID\":-3047126096857467610L" +
                "}";

        InvocationMatcher serializedInstance = (InvocationMatcher) Reflector.fromJson(jsonString, InvocationMatcher.class);

        Method testMethod = Reflector.getDeclaredMethod(MockitoInternalInvocationMatcher.class, "getMethod");
        Method actualMethod = serializedInstance.getMethod();
        Method expectedMethod = TESTED_INSTANCE.getMethod();
        assertThat(testMethod, is(sameMethod(testMethod, actualMethod)));

        assertThat(serializedInstance.getInvocation(), is(same(TESTED_INSTANCE.getInvocation())));
        assertThat(serializedInstance.getMatchers(), is(TESTED_INSTANCE.getMatchers()));
    }
}