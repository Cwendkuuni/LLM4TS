package org.znerd.xmlenc;
import org.junit.Test;
import org.znerd.xmlenc.XMLEncoder;
import static org.junit.Assert.assertEquals;

public class XMLEncoderTest {

    @Test
    public void testGetEncoder() throws Exception {
        String encoding = "UTF-8";
        XMLEncoder encoder = XMLEncoder.getEncoder(encoding);
        assertEquals(encoding, encoder.getEncoding());
    }
}