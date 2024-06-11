import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on("|");
        StringBuilder expected = new StringBuilder();
        expected.append("apple").append("|").append("banana").append("|").append("orange");
        StringBuilder result = new StringBuilder();
        joiner.appendTo(result, Arrays.asList("apple", "banana", "orange"));
        assertEquals(expected.toString(), result.toString());
    }
}