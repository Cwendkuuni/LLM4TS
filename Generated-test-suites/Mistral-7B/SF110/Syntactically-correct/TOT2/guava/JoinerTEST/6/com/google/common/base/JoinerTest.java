import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder expected = new StringBuilder();
        expected.append("test1").append(",").append("test2").append(",").append("test3");

        StringBuilder actual = new StringBuilder();
        joiner.appendTo(actual, Arrays.asList("test1", "test2", "test3"));

        assertEquals(expected.toString(), actual.toString());
    }
}