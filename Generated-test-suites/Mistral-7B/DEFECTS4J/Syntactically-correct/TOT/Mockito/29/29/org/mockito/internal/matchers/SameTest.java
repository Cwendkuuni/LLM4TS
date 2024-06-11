import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), true);
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), false);
    }

    @Test
    public void testNullWanted() {
        Object actual = new Object();

        ArgumentMatcher<Object> matcher = new Same(null);

        assertThat(matcher.matches(actual), false);
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(null), false);
    }

    @Test
    public void testStringWanted() {
        String wanted = "Hello";
        String actual = "Hello";

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), true);
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "Hello";
        String actual = "World";

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), false);
    }

    @Test
    public void testCharacterWanted() {
        char wanted = 'A';
        char actual = 'A';

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), true);
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'A';
        char actual = 'B';

        ArgumentMatcher<Object> matcher = new Same(wanted);

        assertThat(matcher.matches(actual), false);
    }

    @Test
    public void testMockitoMock() {
        Object wanted = mock(Object.class);
        Object actual = mock(Object.class);

        ArgumentMatcher<Object> matcher = new Same(wanted);

        Mockito.verify(matcher).matches(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncompatibleTypes() {
        Object wanted = new Object();
        int actual = 5;

        ArgumentMatcher<Object> matcher = new Same(wanted);

        matcher.matches(actual);
    }
}