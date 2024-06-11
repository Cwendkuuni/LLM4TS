import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(":");
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("apple", "banana", "cherry");

        joiner.appendTo(stringBuilder, iterable);

        String expected = "apple:banana:cherry";
        String actual = stringBuilder.toString();

        assertEquals(expected, actual);
    }
}