import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    private Convert convert;
    private GUI gui;

    @Before
    public void setUp() {
        gui = new GUI() {
            // Dummy implementation for GUI class
        };
    }

    @Test
    public void testConvertConstructorWithFormat() {
        convert = new Convert("dvd-ntsc", gui);
        assertNotNull(convert.m_Format);
        assertEquals("dvd-ntsc", convert.m_Format);
    }

    @Test
    public void testConvertConstructorWithFormatAndAspectRatio() {
        convert = new Convert("dvd-ntsc", "4:3", gui);
        assertNotNull(convert.m_Format);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertNotNull(convert.m_AspectRatio);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testConvertConstructorWithQualityFormatAndAspectRatio() {
        convert = new Convert(5, "dvd-ntsc", "4:3", gui);
        assertNotNull(convert.m_Quality);
        assertEquals(5, convert.m_Quality);
        assertNotNull(convert.m_Format);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertNotNull(convert.m_AspectRatio);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testInit() {
        convert = new Convert("dvd-ntsc", gui);
        assertFalse(convert.init());
        convert = new Convert("dvd-ntsc", gui);
        convert.m_Thread = new Thread(convert);
        convert.m_Thread.start();
        assertTrue(convert.init());
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullGui() {
        convert = new Convert("dvd-ntsc", null);
    }

    @Test
    public void testTranscode() {
        convert = new Convert("dvd-ntsc", gui);
        convert.init();
        assertTrue(convert.init());
        convert.Transcode();
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithNullVideoFilesDir() {
        convert = new Convert("dvd-ntsc", gui);
        convert.m_GUI.strOutputDir = null;
        convert.Transcode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranscodeWithInvalidQuality() {
        convert = new Convert(-1, "dvd-ntsc", "4:3", gui);
        convert.init();
        convert.Transcode();
    }
}