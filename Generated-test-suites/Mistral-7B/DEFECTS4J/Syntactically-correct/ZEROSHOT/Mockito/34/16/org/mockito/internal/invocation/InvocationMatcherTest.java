import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setUp() {
        stubber = mockito(Object.class);
        mock = stubber.createMock(Object.class);

        Method method = stubber.method(Object.class, String.class);
        invocation = new InvocationOnMock(mock, method, Arrays.asList(new Object[]{"test"}));
        matchers = Arrays.asList(is("test"), isA(String.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void shouldCreateInvocationMatcherWithEmptyListOfMatchersAndInvokeArgumentsToMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();

        assertThat(matchers, is(invocation.argumentsToMatchers()));
    }

    @Test
    public void shouldCreateInvocationMatcherWithProvidedMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(matchers));
    }

    @Test
    public void shouldReturnInvocationMethod() {
        Method expectedMethod = invocation.getMethod();

        Method actualMethod = invocationMatcher.getMethod();

        assertThat(actualMethod, is(expectedMethod));
    }

    @Test
    public void shouldReturnInvocation() {
        Invocation expectedInvocation = invocation;

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertThat(actualInvocation, is(expectedInvocation));
    }

    @Test
    public void shouldReturnListOfMatchers() {
        List<Matcher> expectedMatchers = matchers;

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertThat(actualMatchers, is(expectedMatchers));
    }

    @Test
    public void shouldToStringReturnStringRepresentationOfInvocationWithMatchers() {
        String expectedToString = invocation.toString(matchers, new PrintSettings());

        String actualToString = invocationMatcher.toString();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void shouldMatchInvocationsWithSameMethodAndMock() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationsWithDifferentMocks() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(new Object(), invocation.getMethod(), invocation.getArguments()), matchers);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationsWithDifferentMethods() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method(String.class, String.class), invocation.getArguments()), matchers);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationsWithUnverifiedInvocation() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, invocation.getMethod(), invocation.getArguments()), Collections.emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationsWithOverloadedMethodsButDifferentArguments() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method("test", int.class), new Object[]{1}), matchers);

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldMatchInvocationsWithOverloadedMethodsButSameArguments() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method("test", String.class), new Object[]{"test"}), matchers);

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldNotMatchInvocationsWithDifferentLocations() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, invocation.getMethod(), invocation.getArguments()), matchers, new Location("someClass", 5));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldMatchInvocationsWithSameLocation() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, invocation.getMethod(), invocation.getArguments()), matchers, invocation.getLocation());

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void shouldCaptureArgumentsFromInvocation() {
        Answer<String> answer = arg -> "test" + arg;

        when(mock.toString()).thenAnswer(answer);

        invocationMatcher.captureArgumentsFrom(invocation);

        assertEquals("test", invocationMatcher.getMatchers().get(0).getRealValue());
        assertEquals("Object[]", invocationMatcher.getMatchers().get(1).getDescription().replaceAll("[^\\p{L}\\p{Digit}\\s]", ""));
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowAssertionErrorIfArgumentsDoNotMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(is(1), is("test")));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, invocation.getMethod(), new Object[]{2, "test"}), matchers);

        invocationMatcher.matches(invocationMatcher2);
    }

    @Test
    public void shouldReturnTrueIfInvocationsHaveSameArguments() {
        Object[] invocationArgs = invocation.getArguments();

        assertTrue(invocationMatcher.safelyArgumentsMatch(invocationArgs));
    }

    @Test
    public void shouldReturnFalseIfInvocationsHaveDifferentArguments() {
        Object[] invocationArgs = invocation.getArguments();
        Object[] otherInvocationArgs = new Object[]{1, "test"};

        assertFalse(invocationMatcher.safelyArgumentsMatch(otherInvocationArgs));
    }

    @Test
    public void shouldReturnTrueIfInvocationsHaveSameMethodNameAndUnverified() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method("test"), invocation.getArguments()), Collections.emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void shouldReturnFalseIfInvocationsHaveDifferentMethodNames() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method("anotherMethod"), invocation.getArguments()), Collections.emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void shouldReturnTrueIfInvocationsHaveSameMethodNameAndVerified() {
        doCallRealMethod().when(mock).test("test");

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, invocation.getMethod(), invocation.getArguments()), Collections.emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void shouldReturnFalseIfInvocationsHaveDifferentMethods() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method("anotherMethod"), invocation.getArguments()), Collections.emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }
}