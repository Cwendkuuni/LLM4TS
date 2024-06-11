import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String input = "test";
        String expectedOutput = "t2t2t2t2t2t2t2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();
        String input = "";
        String expectedOutput = "1111111111";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testNullString() {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.caverphone(input);
    }

    @Test
    public void testStringWithWhitespaces() {
        Caverphone caverphone = new Caverphone();
        String input = "   Test   ";
        String expectedOutput = "T2T2T2T2T2T2T2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStringWithSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        String input = "Test@123";
        String expectedOutput = "T2T2T2T2T2T2T2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStringWithUppercaseLetters() {
        Caverphone caverphone = new Caverphone();
        String input = "TEST";
        String expectedOutput = "T2T2T2T2T2T2T2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStringStartingWithCough() {
        Caverphone caverphone = new Caverphone();
        String input = "coughTest";
        String expectedOutput = "coup2fT2T2T2T2T2T2T2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStringStartingWithGn() {
        Caverphone caverphone = new Caverphone();
        String input = "gnTest";
        String expectedOutput = "2nT2T2T2T2T2T2T2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStringEndingWithMb() {
        Caverphone caverphone = new Caverphone();
        String input = "Testmb";
        String expectedOutput = "T2T2T2T2T2T2T2m2";
        String output = caverphone.caverphone(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = EncoderException.class)
    public void testNonStringInput() {
        Caverphone caverphone = new Caverphone();
        Integer input = new Integer(1);
        try {
            caverphone.encode(input);
        } catch (EncoderException e) {
            // expected exception
        }
    }

    @Test
    public void testStringIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String input1 = "test";
        String input2 = "test";
        String expectedOutput = "t2t2t2t2t2t2t2";
        String output1 = caverphone.caverphone(input1);
        String output2 = caverphone.caverphone(input2);
        assertTrue(caverphone.isCaverphoneEqual(input1, input2));
        assertEquals(output1, output2);
    }

    @Test
    public void testStringIsCaverphoneNotEqual() {
        Caverphone caverphone = new Caverphone();
        String input1 = "test";
        String input2 = "test2";
        String expectedOutput1 = "t2t2t2t2t2t2t2";
        String expectedOutput2 = "t2t2t2t2t2t2t2t";
        String output1 = caverphone.caverphone(input1);
        String output2 = caverphone.caverphone(input2);
        assertFalse(caverphone.isCaverphoneEqual(input1, input2));
        assertNotEquals(output1, output2);
    }
}
//