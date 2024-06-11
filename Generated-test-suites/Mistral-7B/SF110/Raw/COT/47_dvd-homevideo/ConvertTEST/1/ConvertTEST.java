import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConvertTest
{
    @Test
    public void testConvertConstructor()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        assertNull(convert.m_Quality);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
        assertFalse(convert.m_Error);
        assertNull(convert.m_Thread);
        assertNull(convert.m_Error);
        assertNull(convert.m_BaseErr);
        assertNull(convert.m_transcode);
        assertNull(convert.mplex);
        assertSame(29.97, convert.m_fps, 0.0);

        Convert convert1 = new Convert(0, "dvd-ntsc", "4:3", null);
        assertNull(convert1.m_Quality);
        assertEquals("dvd-ntsc", convert1.m_Format);
        assertEquals("4:3", convert1.m_AspectRatio);
        assertFalse(convert1.m_Error);
        assertNull(convert1.m_Thread);
        assertNull(convert1.m_Error);
        assertNull(convert1.m_BaseErr);
        assertNull(convert1.m_transcode);
        assertNull(convert1.mplex);
        assertSame(29.97, convert1.m_fps, 0.0);

        Convert convert2 = new Convert(1, "dvd-ntsc", "4:3", null);
        assertSame(1, convert2.m_Quality);
        assertEquals("dvd-ntsc", convert2.m_Format);
        assertEquals("4:3", convert2.m_AspectRatio);
        assertFalse(convert2.m_Error);
        assertNull(convert2.m_Thread);
        assertNull(convert2.m_Error);
        assertNull(convert2.m_BaseErr);
        assertNull(convert2.m_transcode);
        assertNull(convert2.mplex);
        assertSame(29.97, convert2.m_fps, 0.0);

        Convert convert3 = new Convert(2, "dvd-ntsc", "4:3", null);
        assertSame(2, convert3.m_Quality);
        assertEquals("dvd-ntsc", convert3.m_Format);
        assertEquals("4:3", convert3.m_AspectRatio);
        assertFalse(convert3.m_Error);
        assertNull(convert3.m_Thread);
        assertNull(convert3.m_Error);
        assertNull(convert3.m_BaseErr);
        assertNull(convert3.m_transcode);
        assertNull(convert3.mplex);
        assertSame(29.97, convert3.m_fps, 0.0);
    }

    @Test
    public void testInitMethod_ThreadIsSelected() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockGUI();
        initMethod.invoke(convert);

        assertTrue(convert.m_Thread.isAlive());
        assertTrue(convert.m_Thread.isInterrupted());
        assertTrue(convert.m_Error);
        assertSame(Convert.ThreadState.TERMINATED, convert.m_Thread.getState());
    }

    @Test
    public void testInitMethod_ThreadIsNotSelected() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockGUI();
        convert.m_GUI.menuChkThread.setSelected(false);
        initMethod.invoke(convert);

        assertFalse(convert.m_Thread.isAlive());
        assertFalse(convert.m_Thread.isInterrupted());
        assertFalse(convert.m_Error);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeMethod_InvalidDirectory()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new MockGUI();
        convert.init();

        File dir = new File("invalid_directory");
        convert.video_files = dir.list();
        convert.Transcode();
    }

    @Test
    public void testTranscodeMethod_ValidDirectory()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new MockGUI();
        convert.init();

        File dir = new File("valid_directory");
        convert.video_files = dir.list();
        convert.Transcode();

        for (int i = 0; i < convert.video_files.length; ++i)
        {
            String fileName = convert.video_files[i];
            assertTrue(new File(convert.m_GUI.strOutputDir + "/" + fileName.substring(0, 11) + ".vob").exists());
            assertTrue(new File(convert.m_GUI.strOutputDir + "/" + fileName).exists());
        }
    }

    @Test(expected = IOException.class)
    public void testTranscodeMethod_IOException() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockIOExceptionGUI();
        initMethod.invoke(convert);

        convert.Transcode();

        assertTrue(convert.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testTranscodeMethod_NullPointerException() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockNullPointerExceptionGUI();
        initMethod.invoke(convert);

        convert.Transcode();

        assertTrue(convert.m_Error);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranscodeMethod_IllegalArgumentException() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockIllegalArgumentExceptionGUI();
        initMethod.invoke(convert);

        convert.Transcode();

        assertTrue(convert.m_Error);
    }

    @Test(expected = Exception.class)
    public void testTranscodeMethod_UnknownException() throws InterruptedException, InvocationTargetException, IllegalAccessException
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        Method initMethod = Convert.class.getDeclaredMethod("init");
        initMethod.setAccessible(true);

        convert.m_GUI = new MockUnknownExceptionGUI();
        initMethod.invoke(convert);

        convert.Transcode();

        assertTrue(convert.m_Error);
    }

    @Test
    public void testMatrixMethod()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new MockFileWriter();
        convert.matrix();

        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(matrixFile.exists());
        assertTrue(matrixFile.length() > 0);
    }
}