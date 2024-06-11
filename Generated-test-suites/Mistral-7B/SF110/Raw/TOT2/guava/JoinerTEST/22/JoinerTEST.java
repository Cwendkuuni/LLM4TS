import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;

public class JoinerTest {
    @Test
    public void testAppendToIterable() throws IOException {
        String separator = "_";
        Joiner joiner = Joiner.on(separator);
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(separator).append("test2").append(separator).append("test3");

        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("test1", "test2", "test3"));
        assertEquals(expected.toString(), actual.toString());
    }
}