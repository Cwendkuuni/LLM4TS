import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        Iterator<String> iterable = mock(Iterator.class);
        when(iterable.next()).thenReturn("apple").thenReturn("banana").thenReturn("cherry");

        StringBuffer expectedResult = new StringBuffer("apple,banana,cherry");

        StringBuffer result = joiner.appendTo(new StringBuffer(), iterable);

        assertEquals(expectedResult.toString(), result.toString());
    }
}