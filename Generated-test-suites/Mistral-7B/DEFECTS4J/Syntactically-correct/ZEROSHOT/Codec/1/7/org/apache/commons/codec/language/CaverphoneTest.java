import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException but received none.");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleWord() {
 String result = caverphone.caverphone("apple");
 assertEquals("A2p2l2e", result);
 }

 @Test
 public void testComplexWord() {
 String result = caverphone.caverphone("Caversham Project at the University of Otago");
 assertEquals("C2a2v2e2r2s2h2a2m2 P3r3o3j3e3t3 U3t3g3a3t3 O3t3a3g3o", result);
 }

 @Test
 public void testReplaceQu() {
 String result = caverphone.caverphone("quincy");
 assertEquals("k2i2n2c2y", result);
 }

 @Test
 public void testReplaceCi() {
 String result = caverphone.caverphone("citrus");
 assertEquals("s2i2t2r2s", result);
 }

 @Test
 public void testReplaceCe() {
 String result = caverphone.caverphone("celery");
 assertEquals("s2e2l2e2r2y", result);
 }

 @Test
 public void testReplaceCy() {
 String result = caverphone.caverphone("cyclone");
 assertEquals("s2l2k2n2e", result);
 }

 @Test
 public void testReplaceTch() {
 String result = caverphone.caverphone("catch");
 assertEquals("2t2ch", result);
 }

 @Test
 public void testReplaceC() {
 String result = caverphone.caverphone("cat");
 assertEquals("k2t2k", result);
 }

 @Test
 public void testReplaceQ() {
 String result = caverphone.caverphone("quiet");
 assertEquals("k2i2t2k2i2t", result);
 }

 @Test
 public void testReplaceX() {
 String result = caverphone.caverphone("exact");
 assertEquals("k2i2x2k2t", result);
 }

 @Test
 public void testReplaceV() {
 String result = caverphone.caverphone("victory");
 assertEquals("f2i2t2o2r2y", result);
 }

 @Test
 public void testReplaceDg() {
 String result = caverphone.caverphone("digest");
 assertEquals("d2g2e2s2t", result);
 }

 @Test
 public void testReplaceTio() {
 String result = caverphone.caverphone("tion");
 assertEquals("s2i2o2n", result);
 }

 @Test
 public void testReplaceTia() {
 String result = caverphone.caverphone("tin");
 assertEquals("t2i2n", result);
 }

 @Test
 public void testReplaceD() {
 String result = caverphone.caverphone("dog");
 assertEquals("d2g", result);
 }

 @Test
 public void testReplacePh() {
 String result = caverphone.caverphone("phone");
 assertEquals("f2h2e", result);
 }

 @Test
 public void testReplaceB() {
 String result = caverphone.caverphone("bat");
 assertEquals("b2t", result);
 }

 @Test
 public void testReplaceSh() {
 String result = caverphone.caverphone("shoulder");
 assertEquals("s2h2d2r", result);
 }

 @Test
 public void testReplaceZ() {
 String result = caverphone.caverphone("zebra");
 assertEquals("s2b2r2a", result);
 }

 @Test
 public void testReplaceInitialAEIOU() {
 String result = caverphone.caverphone("example");
 assertEquals("A2p2l2a2m2e2x2a2m2e", result);
 }

 @Test
 public void testReplaceFinalE() {
 String result = caverphone.caverphone("bake");
 assertEquals("b2k2e", result);
 }

 @Test
 public void testReplaceInitialRough() {
 String result = caverphone.caverphone("rough");
 assertEquals("rou2f", result);
 }

 @Test
 public void testReplaceInitialTough() {
 String result = caverphone.caverphone("tough");
 assertEquals("tou2f", result);
 }

 @Test
 public void testReplaceInitialEnough() { // 2.0 only
 String result = caverphone.caverphone("enough");
 assertEquals("enou2f", result);
 }

 @Test
 public void testReplaceInitialTrough() { // 2.0 only
 String result = caverphone.caverphone("trough");
 assertEquals("trou2f", result);
 }

 @Test
 public void testReplaceInitialGn() {
 String result = caverphone.caverphone("gnat");
 assertEquals("2n2t", result);
 }

 @Test
 public void testReplaceInitialMb() {
 String result = caverphone.caverphone("mb");
 assertEquals("m2b", result);
 }

 @Test
 public void testEncodeDecode() {
 String original = "Caversham Project at the University of Otago";
 String encoded = caverphone.caverphone(original);
 String decoded = caverphone.decode(encoded);
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeDecodeNull() {
 try {
 caverphone.decode(null);
 fail("Expected DecoderException but received none.");
 } catch (DecoderException e) {
 // Expected
 }

 try {
 caverphone.encode((Object) null);
 fail("Expected EncoderException but received none.");
 } catch (EncoderException e) {
 // Expected
 }
 }
}