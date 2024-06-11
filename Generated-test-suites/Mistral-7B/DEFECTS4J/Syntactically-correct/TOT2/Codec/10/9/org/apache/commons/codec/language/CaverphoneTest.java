import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        String input = "test";
        String expected = "t2e2s2t";
        String actual = caverphone.caverphone(input);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        caverphone.caverphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testEmptyInput() {
        caverphone.caverphone("");
    }

    @Test
    public void testEncodeObject() {
        Object input = "test";
        String expected = "t2e2s2t";
        String actual = (String) caverphone.encode(input);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectNull() {
        caverphone.encode(null);
    }

    @Test
    public void testEdgeCases() {
        String edgeCase1 = "CavershamProject";
        String edgeCase2 = "UniversityOfOtago";
        String edgeCase3 = "AlgorithmCreatedByTheApacheSoftwareFoundation";
        String edgeCase4 = "Wikipedia-Caverphone";
        String edgeCase5 = "Caverphone2.0specification";
        String expectedEdgeCase1 = "C2avers2p23rj5o5p3i3t2g";
        String expectedEdgeCase2 = "U3niversity3Of3Otago";
        String expectedEdgeCase3 = "A3lgorithm3C3r3e4a7t3e3d3b3y3T3h3e3A3p3";
        String expectedEdgeCase4 = "W3ikipedia-3Caverphone";
        String expectedEdgeCase5 = "C3averphone2.3p3s3s3p3c3i3p3t3i3o3n3";

        String actualEdgeCase1 = caverphone.caverphone(edgeCase1);
        String actualEdgeCase2 = caverphone.caverphone(edgeCase2);
        String actualEdgeCase3 = caverphone.caverphone(edgeCase3);
        String actualEdgeCase4 = caverphone.caverphone(edgeCase4);
        String actualEdgeCase5 = caverphone.caverphone(edgeCase5);

        assertEquals(expectedEdgeCase1, actualEdgeCase1);
        assertEquals(expectedEdgeCase2, actualEdgeCase2);
        assertEquals(expectedEdgeCase3, actualEdgeCase3);
        assertEquals(expectedEdgeCase4, actualEdgeCase4);
        assertEquals(expectedEdgeCase5, actualEdgeCase5);
    }

    @Test
    public void testExceptionHandling() {
        try {
            caverphone.caverphone("12345");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}