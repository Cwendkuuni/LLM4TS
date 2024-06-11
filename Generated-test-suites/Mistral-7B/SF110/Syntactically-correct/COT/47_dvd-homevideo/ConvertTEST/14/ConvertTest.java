import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    @Test
    public void testConvertConstructor() {
        String format = "dvd-ntsc";
        String aspectRatio = "16:9";
        GUI DVD_GUI = new GUI(); // Assuming GUI class is available
        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        assertEquals(format, convert.m_Format);
        assertEquals(aspectRatio, convert.m_AspectRatio);
    }

    // Test other constructor with given arguments
}