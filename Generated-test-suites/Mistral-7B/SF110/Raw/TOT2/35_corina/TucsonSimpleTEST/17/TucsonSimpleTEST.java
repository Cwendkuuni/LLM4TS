import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TucsonSimpleTest {
    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        String expectedCode = "123456";
        String actualCode = new TucsonSimple().make6digitCode(sample);
        assertEquals(expectedCode, actualCode);

        Sample sample2 = new Sample();
        String expectedCode2 = "000123";
        String actualCode2 = new TucsonSimple().make6digitCode(sample2);
        assertEquals(expectedCode2, actualCode2);
    }
}