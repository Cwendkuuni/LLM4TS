import static org.junit.Assert.*;
import com.google.common.base.Joiner;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class JoinerTest {

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringWriter stringWriter = new StringWriter();
        List<String> list = Arrays.asList("element1", "element2", "element3");

        joiner.appendTo(stringWriter, list.iterator());

        String expectedResult = "element1,element2,element3";
        String actualResult = stringWriter.toString();

        assertEquals(expectedResult, actualResult);
    }
}