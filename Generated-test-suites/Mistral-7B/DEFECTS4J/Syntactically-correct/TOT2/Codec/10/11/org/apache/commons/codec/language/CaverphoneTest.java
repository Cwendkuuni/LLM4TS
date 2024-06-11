import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Caverphone;

public class CaverphoneTest {
    
    @Test
    public void testCaverphoneEmptyString() {
        Caverphone encoder = new Caverphone();
        String expected = "1111111111";
        String actual = encoder.caverphone("");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCaverphoneLowerCaseString() {
        Caverphone encoder = new Caverphone();
        String input = "hello world";
        String expected = "h2l2o2l2w2o2r2l2d2";
        String actual = encoder.caverphone(input);
        assertEquals(expected, actual);
    }
    
    @Test(expected = EncoderException.class)
    public void testCaverphoneNullString() {
        Caverphone encoder = new Caverphone();
        String input = null;
        encoder.caverphone(input);
    }
    
    @Test
    public void testCaverphoneLongString() {
        Caverphone encoder = new Caverphone();
        String input = "this is a very long string that we want to test with caverphone encoding algorithm";
        String expected = "t2h2i2s2i2a2v2e2r2y2i2t2h2a2t2w2o2t2h2a2t2w2o2t2h2a2t2c2o2d2e2l2o2g2h2t2";
        String actual = encoder.caverphone(input);
        assertEquals(expected, actual);
    }
    
    // Add edge cases and exception handling tests here
}