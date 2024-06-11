import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.stubbing.AnnotationUtils;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerifiedType;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[0];

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyArguments() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(is(String.class));
        Object[] arguments = new Object[0];

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NoSuspiciousMatches() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(is(String.class));
        Object[] arguments = new Object[]{new String("test")};

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(new Integer[0]));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(PowerMockito.isA(String.class));
        matchers.add(is(Integer.class));
        Object[] arguments = new Object[]{new String("test"), 42};

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(new Integer[1]));
        assertThat(result[0], is(1));
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatchesMultiple() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(PowerMockito.isA(String.class));
        matchers.add(PowerMockito.isA(Integer.class));
        Object[] arguments = new Object[]{new String("test1"), 42, new String("test2")};

        Integer[] result = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertThat(result, is(new Integer[1]));
        assertThat(result[0], is(1));
    }

    @Test
    public void testSafelyMatches_True() {
        Matcher<String> matcher = is("test");

        boolean result = ArgumentMatchingTool.safelyMatches(matcher, "test");

        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_False() {
        Matcher<String> matcher = is("test");

        boolean result = ArgumentMatchingTool.safelyMatches(matcher, 42);

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_True() {
        Matcher<String> matcher = is("test");

        boolean result = ArgumentMatchingTool.toStringEquals(matcher, "test");

        assertTrue(result);
    }

    @Test
    public void testToStringEquals_False() {
        Matcher<String> matcher = is("test");

        boolean result = ArgumentMatchingTool.toStringEquals(matcher, "test1");

        assertFalse(result);
    }

    @Test
    public void testToStringEquals_Null() {
        Matcher<String> matcher = is("test");

        boolean result = ArgumentMatchingTool.toStringEquals(matcher, null);

        assertFalse(result);
    }

    @Test
    public void testArgumentMatchingTool_VerifyZeroInteractions() {
        Object mock = mock(String.class);

        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        verifyZeroInteractions(mock, argumentMatchingTool);
    }

    @Test
    public void testArgumentMatchingTool_VerifyNoMoreInteractions() {
        Object mock = mock(String.class);

        argumentMatchingTool.verify(mock);
        argumentMatchingTool.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testArgumentMatchingTool_Verify() {
        Object mock = mock(String.class);

        AnnotationUtils.replay(mock);

        argumentMatchingTool.verify(mock).toString();

        AnnotationUtils.verify(mock);
    }

    @Test
    public void testArgumentMatchingTool_VerifyMultiple() {
        Object mock = mock(String.class);

        Object arg1 = "test1";
        Object arg2 = "test2";

        AnnotationUtils.replay(mock, arg1, arg2);

        argumentMatchingTool.verify(mock).toString();
        argumentMatchingTool.verify(mock).toUpperCase();
        argumentMatchingTool.verify(mock).length();
        argumentMatchingTool.verify(mock, VerificationMode.IN_ORDER).toString();
        argumentMatchingTool.verify(mock, VerificationMode.IN_ORDER).toUpperCase();
        argumentMatchingTool.verify(mock, VerificationMode.IN_ORDER).length();

        VerifiedType[] expected = new VerifiedType[]{
                VerifiedType.verified(mock).usingMethod("toString"),
                VerifiedType.verified(mock).usingMethod("toUpperCase"),
                VerifiedType.verified(mock).usingMethod("length")
        };

        AnnotationUtils.verify(mock, expected);
    }
}