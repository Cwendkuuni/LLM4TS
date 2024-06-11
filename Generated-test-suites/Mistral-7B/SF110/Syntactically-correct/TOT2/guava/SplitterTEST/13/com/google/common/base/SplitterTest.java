import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.base.Splitter;

public class SplitterTest {
    @Test
    public void testOnMethod() {
        Splitter splitter = Splitter.on('-');
        String input = "test-1-test-2-test-3";
        Iterator<String> iterator = splitter.split(input);

        List<String> expected = Arrays.asList("test", "1", "test", "2", "test", "3");
        List<String> actual = new ArrayList<>();
        while (iterator.hasNext()) {
            actual.add(iterator.next());
        }

        assertEquals(expected, actual);
        assertNotNull(iterator);
    }
}