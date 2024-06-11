import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatchers() throws Exception {
        // Given
        Invocation invocation = mock(InvocationOnMock.class);
        Object[] arguments = new Object[0];
        List<Matcher> matchers = Collections.emptyList();

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
        assertThat(invocationMatcher.toString(), is(notNullValue()));
        verifyZeroInteractions(invocation);
    }

    @Test
    public void testInvocationMatcherWithMatchers() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.is(3));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.toString(), is(notNullValue()));

        // Test matchers
        verify(invocation).argThat(ArgumentMatchers.eq(1));
        verify(invocation).argThat(ArgumentMatchers.is(3));
    }

    @Test
    public void testInvocationMatcherWithCustomMatcher() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, new MyCustomMatcher()};
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), new MyCustomMatcher());

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
        assertThat(invocationMatcher.toString(), is(notNullValue()));

        // Test custom matcher
        verify(invocation).argThat(new MyMatcherDecorator(new MyCustomMatcher()));
    }

    @Test
    public void testToString() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.is(3));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        String expected = String.format("Invocation on %s%n" +
                "    method: %s%n" +
                "    arguments: [%s, %s, %s]%n",
                invocation.getMock().getClass().getName(),
                invocation.getMethod().toGenericString(),
                ArgumentMatchers.present(),
                ArgumentMatchers.present(),
                ArgumentMatchers.present()
        );
        String actual = invocationMatcher.toString();

        // Then
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.is(3));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationOnMock anotherInvocation = mock(InvocationOnMock.class);
        Object[] anotherArguments = new Object[]{1, 2, 3};

        // Then
        verify(invocation).matches(invocationMatcher);
        verifyZeroInteractions(anotherInvocation);

        // Test if matches
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation);
        boolean result = invocationMatcher.matches(anotherInvocationMatcher);
        assertThat(result, is(false));

        // Test if matches with same method
        InvocationOnMock sameInvocation = mock(InvocationOnMock.class);
        boolean resultSameMethod = invocationMatcher.matches(new InvocationMatcher(sameInvocation));
        assertThat(resultSameMethod, is(true));

        // Test if matches with same arguments
        InvocationOnMock sameInvocationWithSameArguments = mock(InvocationOnMock.class);
        Object[] sameArguments = arguments.clone();
        boolean resultSameArguments = invocationMatcher.matches(new InvocationMatcher(new InvocationMatcher(sameInvocationWithSameArguments).withArguments(sameArguments)));
        assertThat(resultSameArguments, is(true));

        // Test if matches with different arguments
        Object[] differentArguments = new Object[]{1, 2, 4};
        boolean resultDifferentArguments = invocationMatcher.matches(new InvocationMatcher(new InvocationMatcher(anotherInvocation).withArguments(differentArguments)));
        assertThat(resultDifferentArguments, is(false));
    }

    @Test
    public void testSafelyArgumentsMatch() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.is(3)));

        // Then
        Object[] anotherArguments = new Object[]{1, 2, 3};
        boolean result = invocationMatcher.safelyArgumentsMatch(anotherArguments);
        assertThat(result, is(true));

        // Test if safety check fails
        Object[] differentArguments = new Object[]{1, 2, 4};
        boolean resultDifferentArguments = invocationMatcher.safelyArgumentsMatch(differentArguments);
        assertThat(resultDifferentArguments, is(false));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.is(3)));

        // Then
        Object[] differentArguments = new Object[]{1, 2, 4};
        invocationMatcher.safelyArgumentsMatch(differentArguments);
    }

    @Test
    public void testHasSimilarMethod() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);
        Invocation anotherInvocation = mock(InvocationOnMock.class);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(anotherInvocation, Collections.emptyList());

        // Then
        Method invocationMethod = invocation.getMethod();
        Method anotherInvocationMethod = anotherInvocation.getMethod();
        boolean result = invocationMatcher.hasSimilarMethod(anotherInvocationMatcher);
        assertThat(result, is(false));

        // Test if same method and same arguments
        invocationMethod = invocation.getMethod();
        anotherInvocationMethod = invocationMethod;
        result = invocationMatcher.hasSimilarMethod(anotherInvocationMatcher);
        assertThat(result, is(true));

        // Test if same method and different arguments
        Method differentMethod = mock(Method.class);
        invocationMethod = invocation.getMethod();
        anotherInvocationMethod = differentMethod;
        result = invocationMatcher.hasSimilarMethod(anotherInvocationMatcher);
        assertThat(result, is(false));

        // Test if different methods
        Method differentMethod1 = mock(Method.class);
        Method differentMethod2 = mock(Method.class);
        result = invocationMatcher.hasSimilarMethod(anotherInvocationMatcher);
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        // Given
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new MyCustomMatcher()), ArgumentMatchers.is(3));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        int indexOfVararg = arguments.length - 1;
        for (int position = 0; position < indexOfVararg; position++) {
            Matcher matcher = matchers.get(position);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturedArguments.getValue();
                verify(invocation).argThat(CapturesArguments.capture(ArgumentMatchers.eq(arguments[position])));
                verify(invocation).argThat(CapturesArguments.capture(capturedArgument));
                assertThat(capturedArgument, is(arguments[position]));
            }
        }

        for (int position = indexOfVararg; position < matchers.size(); position++) {
            Matcher matcher = matchers.get(position);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturedArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturedArguments.getValue();
                verify(invocation).argThat(CapturesArguments.capture(arguments[position - indexOfVararg]));
                verify(invocation).argThat(CapturesArguments.capture(capturedArgument));
                assertThat(capturedArgument, is(arguments[position - indexOfVararg]));
            }
        }
    }

    @Test
    public void testCreateFrom() throws Exception {
        // Given
        List<Invocation> invocations = Arrays.asList(mock(InvocationOnMock.class), mock(InvocationOnMock.class));

        // When
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        Invocation invocation1 = invocationMatcher1.getInvocation();
        Invocation invocation2 = invocationMatcher2.getInvocation();
        List<Matcher> matchers1 = invocationMatcher1.getMatchers();
        List<Matcher> matchers2 = invocationMatcher2.getMatchers();

        // Test that invocation are the same
        verify(invocation1).getMock().equals(invocation2.getMock());

        // Test that matchers are different
        assertThat(matchers1, is(not(matchers2)));

        // Test that arguments matchers are different
        for (int position = 0; position < matchers1.size(); position++) {
            Matcher matcher1 = matchers1.get(position);
            Matcher matcher2 = matchers2.get(position);
            if (matcher1 instanceof CapturesArguments && matcher2 instanceof CapturesArguments) {
                CapturesArguments capturedArguments1 = (CapturesArguments) matcher1;
                CapturesArguments capturedArguments2 = (CapturesArguments) matcher2;
                Object capturedArgument1 = capturedArguments1.getValue();
                Object capturedArgument2 = capturedArguments2.getValue();

                // Test that captured arguments are different
                verify(invocation1).argThat(CapturesArguments.capture(ArgumentsMatcherTest.DIFFERENT_ARGUMENT));
                verify(invocation2).argThat(CapturesArguments.capture(ArgumentsMatcherTest.DIFFERENT_ARGUMENT));
                assertThat(capturedArgument1, is(not(capturedArgument2)));
            }
        }
    }
}

class MyCustomMatcher implements Matcher<Object> {
    @Override
    public boolean matches(Object actual) {
        return actual.getClass().getName().equals("java.lang.String");
    }
}

class MyMatcherDecorator implements Matcher<Object> {
    private Matcher<Object> matcher;

    public MyMatcherDecorator(Matcher<Object> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object actual) {
        return matcher.matches(actual);
    }
}