import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.util.collections.LinkedHashSet;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private static final String TESTED_CLASS_NAME = "org.mockito.internal.invocation.InvocationMatcher";

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new LinkedHashSet<>();
        InvocationMatcher invocationMatcherSpy = spy(new InvocationMatcher(invocation, matchers));
        this.invocationMatcher = Whitebox.getInternalState(invocationMatcher, "invocationMatcher");
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchersList() {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, List.of(matcher1, matcher2));

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(List.of(matcher1, matcher2)));
    }

    @Test
    public void testGetMethod() {
        // Given
        Method method = mock(Method.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(method), Collections.emptyList());

        // Then
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        // Given
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), List.of(matcher1, matcher2));

        // When
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Then
        assertThat(matchers, is(List.of(matcher1, matcher2)));
    }

    @Test
    public void testToString() {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, List.of(matcher1, matcher2));

        // Then
        String result = invocationMatcher.toString();

        // Verify that toString calls the correct methods
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getMatchers();

        // Verify that toString returns the correct string
        String expected = String.format("InvocationMatcher[%s, %s]", invocation.toString(), matchers);
        assertThat(result, is(expected));
    }

    @Test
    public void testMatches() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class), Collections.emptyList());
        InvocationMatcher invocationMatcher2 = spy(new InvocationMatcher(mock(Invocation.class), Collections.emptyList()));
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(mock(Invocation.class), List.of(mock(Matcher.class)));

        // When
        boolean result1 = invocationMatcher1.matches(invocationMatcher1);
        boolean result2 = invocationMatcher1.matches(invocationMatcher2);
        boolean result3 = invocationMatcher2.matches(invocationMatcher1);
        boolean result4 = invocationMatcher2.matches(invocationMatcher2);
        boolean result5 = invocationMatcher1.matches(invocationMatcher3);

        // Then
        assertThat(result1, is(true));
        assertThat(result2, is(false));
        assertThat(result3, is(false));
        assertThat(result4, is(true));
        assertThat(result5, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Given
        Object[] objectArray1 = new Object[0];
        Object[] objectArray2 = new Object[]{"test"};

        // When
        boolean result1 = invocationMatcher.safelyArgumentsMatch(objectArray1);
        boolean result2 = invocationMatcher.safelyArgumentsMatch(objectArray2);

        // Then
        assertThat(result1, is(true));
        assertThat(result2, is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // When
        boolean result = invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation1, Collections.emptyList()));

        // Then
        verifyZeroInteractions(invocationMatcher);
        verify(invocationMatcher).hasSameMethod(new InvocationMatcher(invocation2, Collections.emptyList()));
        verify(invocationMatcher).safelyArgumentsMatch(invocation2.getArguments());
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Invocation invocation1 = mock(InvocationOnMock.class);
        Invocation invocation2 = mock(InvocationOnMock.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        // When
        boolean result = invocationMatcher.hasSameMethod(new InvocationMatcher(invocation1, Collections.emptyList()));

        // Then
        verifyZeroInteractions(invocationMatcher);
        verify(invocationMatcher).hasSameMethod(new InvocationMatcher(invocation2, Collections.emptyList()));
        assertThat(result, is(true));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);
        Matcher matcher = mock(Matcher.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, List.of(matcher));

        // When
        Object[] arguments = new Object[]{"test"};
        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(invocation, arguments, null, null, null, null));

        // Then
        verify(matcher).captureFrom(arguments[0]);
    }

    @Test
    public void testCreateFrom() {
        // Given
        List<Invocation> invocations = List.of(mock(InvocationOnMock.class), mock(InvocationOnMock.class));

        // When
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Then
        assertThat(invocationMatchers.size(), is(2));
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertThat(invocationMatcher.getInvocation(), instanceOf(InvocationOnMock.class));
        }
    }
}