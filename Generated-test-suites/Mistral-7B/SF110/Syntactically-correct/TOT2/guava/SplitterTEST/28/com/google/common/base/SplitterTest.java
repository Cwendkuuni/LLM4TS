import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SplitterTest {

    @Test
    public void testOnSeparator() {
        String input = "test,separator,test,test";
        Splitter splitter = Splitter.on(',').split(input);
        List<String> result = Splitter.on('').withKeyValueSeparator(',').split(input);
        assertEquals(Arrays.asList("test", "separator", "test", "test"), splitter.splitToList(input));
        assertEquals(Arrays.asList("test=test", "separator=test", "test=test"), result);
    }
}