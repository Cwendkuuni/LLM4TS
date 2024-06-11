import static org.junit.Assert.assertEquals;
import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(":");
        StringBuilder expected = new StringBuilder();
        expected.append("one:two:three:four:five");

        StringBuilder result = joiner.appendTo(expected, Arrays.asList("one", "two", "three", "four", "five"));

        assertEquals(expected.toString(), result.toString());
    }
}