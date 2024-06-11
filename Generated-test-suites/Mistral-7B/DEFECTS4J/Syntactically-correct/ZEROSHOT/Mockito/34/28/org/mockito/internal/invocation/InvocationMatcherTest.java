import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationMatcher emptyMatcher;
    private Invocation invocation;
    private Invocation candidateInvocation;
    private Object[] arguments;
    private Object[] candidateArguments;

    @Before
    public void setUp() {
        Method method = Mockito.method(String.class, "testMethod", String[].class);
        invocation = Mockito.mock(Invocation.class);
        candidateInvocation = Mockito.mock(Invocation.class);
        arguments = new Object[]{"arg1", "arg2"};
        candidateArguments = new Object[]{"arg1", "arg3"};

        matcher = new InvocationMatcher(invocation, Arrays.asList(
                ArgumentMatchers.argument(0, is("arg1")),
                ArgumentMatchers.argument(1, is("arg2"))
        ));

        emptyMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testNewInstanceWithMatchers() {
        // given

        // when
        InvocationMatcher newMatcher = new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.any()));

        // then
        assertThat(newMatcher, notNullValue());
    }

    @Test
    public void testGetMethod() {
        // given

        // when
        Method method = matcher.getMethod();

        // then
        assertThat(method, sameInstance(invocation.getMethod()));
    }

    @Test
    public void testGetInvocation() {
        // given

        // when
        Invocation invocationFromMatcher = matcher.getInvocation();

        // then
        assertThat(invocationFromMatcher, sameInstance(this.invocation));
    }

    @Test
    public void testGetMatchers() {
        // given

        // when
        List<Matcher> matchersFromMatcher = matcher.getMatchers();

        // then
        assertThat(matchersFromMatcher, sameInstance(matcher.matchers));
    }

    @Test
    public void testToString() {
        // given

        // when
        String toString = matcher.toString(new PrintSettings());

        // then
        assertThat(toString, notNullValue());
    }

    @Test
    public void testMatches() {
        // given

        // when
        boolean matches = matcher.matches(candidateInvocation);

        // then
        assertThat(matches, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // given

        // when
        boolean safelyArgumentsMatch = matcher.safelyArgumentsMatch(candidateArguments);

        // then
        assertThat(safelyArgumentsMatch, is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        // given

        // when
        boolean hasSimilarMethod = matcher.hasSimilarMethod(candidateInvocation);

        // then
        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // given

        // when
        boolean hasSameMethod = matcher.hasSameMethod(candidateInvocation);

        // then
        assertThat(hasSameMethod, is(false));
    }

    @Test
    public void testLocation() {
        // given

        // when
        Location location = matcher.getLocation();

        // then
        assertThat(location, notNullValue());
    }

    @Test
    public void testToStringWithPrintSettings() {
        // given

        // when
        String toString = matcher.toString(new PrintSettings());

        // then
        assertThat(toString, notNullValue());
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given

        // when
        matcher.captureArgumentsFrom(candidateInvocation);

        // then
        // no verification needed, as mocks are stubs, not spies
        // but we should see no exceptions thrown
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        // given

        // when
        InvocationMatcher newMatcher = new InvocationMatcher(invocation);

        // then
        assertThat(newMatcher, notNullValue());
    }

    @Test
    public void testMatchesWithEmptyMatcher() {
        // given

        // when
        boolean matches = emptyMatcher.matches(candidateInvocation);

        // then
        assertThat(matches, is(true));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        // given

        // when
        boolean matches = matcher.matches(invocation);

        // then
        assertThat(matches, is(true));
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        // given

        // when
        boolean hasSimilarMethod = matcher.hasSimilarMethod(invocation);

        // then
        assertThat(hasSimilarMethod, is(true));
    }

    @Test
    public void testHasSameMethodWithSameInvocation() {
        // given

        // when
        boolean hasSameMethod = matcher.hasSameMethod(invocation);

        // then
        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationButSameMethod() {
        // given
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.method(String.class, "testMethod", String[].class);

        // when
        boolean hasSameMethod = matcher.hasSameMethod(new InvocationMatcher(differentInvocation, Collections.emptyList(), method));

        // then
        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethodAndArgs() {
        // given
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.method(String.class, "testMethod", String[].class);

        // when
        InvocationMatcher matcherForDifferentInvocation = new InvocationMatcher(differentInvocation, Collections.emptyList(), method);
        boolean matches = matcherForDifferentInvocation.matches(invocation);

        // then
        assertThat(matches, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethodAndDifferentArgs() {
        // given
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.method(String.class, "testMethod", String[].class);

        // when
        InvocationMatcher matcherForDifferentInvocation = new InvocationMatcher(differentInvocation, Collections.emptyList(), method);
        boolean matches = matcherForDifferentInvocation.matches(new InvocationMatcher(invocation, Arrays.asList(
                ArgumentMatchers.argument(0, is("arg1")),
                ArgumentMatchers.argument(1, is("arg3"))
        )));

        // then
        assertThat(matches, is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchesWithDifferentInvocationButDifferentMethod() {
        // given
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method1 = Mockito.method(String.class, "testMethodA", String[].class);
        Method method2 = Mockito.method(String.class, "testMethodB", String[].class);

        // when
        InvocationMatcher matcherForDifferentInvocation = new InvocationMatcher(differentInvocation, Collections.emptyList(), method1);
        matcherForDifferentInvocation.matches(new InvocationMatcher(invocation, Collections.emptyList(), method2));

        // then
        // exception is thrown
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethodAndSameArgsButDifferentOrder() {
        // given
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method = Mockito.method(String.class, "testMethod", String[].class);

        // when
        InvocationMatcher matcherForDifferentInvocation = new InvocationMatcher(differentInvocation, Collections.emptyList(), method);
        boolean matches = matcherForDifferentInvocation.matches(new InvocationMatcher(invocation, Arrays.asList(
                ArgumentMatchers.argument(1, is("arg1")),
                ArgumentMatchers.argument(0, is("arg2"))
        )));

        // then
        assertThat(matches, is(false));
    }
}