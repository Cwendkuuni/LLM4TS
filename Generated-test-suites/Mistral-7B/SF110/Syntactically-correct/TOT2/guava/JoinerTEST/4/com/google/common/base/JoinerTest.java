import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JoinerTest {
    @Test
    public void testOn() {
        String separator1 = "|";
        Joiner joiner1 = Joiner.on(separator1);
        String separator2 = '#';
        Joiner joiner2 = Joiner.on(separator2);

        assertEquals(separator1, joiner1.separator);
        assertEquals(separator2, joiner2.separator);
    }
}