import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

private final StringEncoder encoder = new Caverphone();

@Test
public void testEmptyString() {
String input = "";
String expected = "1111111111";
assertEquals(expected, encoder.encode(input));
}

@Test
public void testSingleCharacter() {
String input = "a";
String expected = "a";
assertEquals(expected, encoder.encode(input));
}

@Test
public void testSimpleString() {
String input = "hello";
String expected = "h2l2o3l3o";
assertEquals(expected, encoder.encode(input));
}

@Test
public void testCaseSensitive() {
String input = "Hello";
String expected = "h2l2o3L3o";
assertEquals(expected, encoder.encode(input));
}

@Test
public void testComplexString() {
String input = "The quick brown fox jumps over the lazy dog";
String expected = "Th3 q1ck br0wn f0x j3mps ov3r th3 l4z1 d0g";
assertEquals(expected, encoder.encode(input));
}

@Test(expected = EncoderException.class)
public void testNullInput() {
encoder.encode(null);
fail("EncoderException expected but not thrown");
}

@Test(expected = EncoderException.class)
public void testNonStringInput() {
encoder.encode(new Object());
fail("EncoderException expected but not thrown");
}

@Test
public void testInequality() {
String str1 = "hello";
String str2 = "world";
assertFalse(encoder.isCaverphoneEqual(str1, str2));
}
}