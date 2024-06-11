import org.junit.Test;
import static org.junit.Assert.*;

public class XMLEncoderTest {
   @Test
   public void testGetEncoder() {
      String encoding = "UTF-8";
      XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
      assertEquals(encoding, encoder.getEncoding());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testGetEncoderNullEncoding() {
      XMLEncoder encoder = XMLEncoder.getEncoder(null);
      fail("IllegalArgumentException expected but not thrown");
   }

   @Test(expected = UnsupportedEncodingException.class)
   public void testGetEncoderUnsupportedEncoding() {
      String encoding = "XYZ";
      try {
         XMLEncoder.getEncoder(encoding);
         fail("UnsupportedEncodingException expected but not thrown");
      } catch (UnsupportedEncodingException e) {
         // expected
      }
   }
}