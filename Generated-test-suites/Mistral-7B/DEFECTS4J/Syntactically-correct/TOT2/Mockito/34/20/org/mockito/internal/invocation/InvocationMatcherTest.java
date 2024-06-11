import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.mockito.internal.matchers.ArgThat.is;
import static org.mockito.internal.matchers.ArgThat.not;
import static org.mockito.internal.reporting.PrintSettings.printError;
import static org.mockito.internal.reporting.PrintSettings.printWarning;
import static org.mockito.internal.reporting.PrintSettings.printVerbose;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.mockito.junit.jUpgrade.TestMethods.test;
import static org.mockito.junit.jUpgrade.TestMethods.verify;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Object(), new Object(), new Method(Test.class.getDeclaredMethod("testMethod", Object.class)), new Location(1, 1), new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        test(matcher)
                .given(printSettings -> setInternalState(printSettings, "errorPrinting", printError()))
                .given(printSettings -> setInternalState(printSettings, "warningPrinting", printWarning()))
                .given(printSettings -> setInternalState(printSettings, "verbosePrinting", printVerbose()))
                .when(matcher::matches)
                .then(is(true));

        verify(matcher).getMethod();
        verify(matcher).getInvocation();
        verify(matcher).getMatchers();
        verify(matcher).toString();
        verify(matcher).matches(invocation);
    }
}