import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanTest {
    @Test
    public void testEmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    @Test
    public void testNullString() {
        String result = SoundexUtils.clean(null);
        assertNull(result);
    }

    @Test
    public void testOnlyLetters() {
        String str = "abcdefg";
        String result = SoundexUtils.clean(str);
        assertEquals("ABCDEFG", result);
    }

    @Test
    public void testMixedString() {
        String str = "ab1c2de3fg";
        String result = SoundexUtils.clean(str);
        assertEquals("ABCDE", result);
    }
}