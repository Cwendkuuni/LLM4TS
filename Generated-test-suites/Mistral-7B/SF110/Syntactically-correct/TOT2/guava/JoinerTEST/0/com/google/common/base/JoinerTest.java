import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToWithIterableAndAppendable() {
        Joiner joiner = Joiner.on("|");
        StringBuilder expected = new StringBuilder();
        expected.append("test1|test2|test3|test4");

        StringBuilder appendable = new StringBuilder();
        joiner.appendTo(appendable, Arrays.asList("test1", "test2", "test3", "test4"));

        assertEquals(expected.toString(), appendable.toString());
    }
}