import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ARFormatException;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ArchiveStreamFactory.class, ArArchiveInputStream.class, ArArchiveOutputStream.class,
        ZipArchiveInputStream.class, ZipArchiveOutputStream.class, TarArchiveInputStream.class,
        TarArchiveOutputStream.class, JarArchiveInputStream.class, JarArchiveOutputStream.class,
        CpioArchiveInputStream.class, CpioArchiveOutputStream.class, DumpArchiveInputStream.class,
        DumpArchiveOutputStream.class})
public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setup() throws IOException {
        inputStream = Mockito.mock(InputStream.class);
        outputStream = Mockito.mock(OutputStream.class);
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(ZipArchiveInputStream.class).withArguments(eq(inputStream)).thenReturn(mock(ZipArchiveInputStream.class));
        ArchiveInputStream stream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertEquals(ZipArchiveInputStream.class, stream.getClass());
        PowerMockito.verifyNew(ZipArchiveInputStream.class).withArguments(eq(inputStream));
    }

    @Test
    public void testCreateArchiveInputStreamUnknown() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(ZipArchiveInputStream.class).withArguments(any()).thenThrow(new ArchiveException());
        try {
            ArchiveStreamFactory.createArchiveInputStream("unknown", inputStream);
            fail();
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(TarArchiveInputStream.class).withArguments(eq(inputStream)).thenReturn(mock(TarArchiveInputStream.class));
        ArchiveInputStream stream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertEquals(TarArchiveInputStream.class, stream.getClass());
        PowerMockito.verifyNew(TarArchiveInputStream.class).withArguments(eq(inputStream));
    }

    @Test
    public void testCreateArchiveInputStreamAr() throws IOException, ArchiveException, ARFormatException {
        PowerMockito.whenNewInstance(ArArchiveInputStream.class).withArguments(eq(inputStream)).thenReturn(mock(ArArchiveInputStream.class));
        ArchiveInputStream stream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertEquals(ArArchiveInputStream.class, stream.getClass());
        PowerMockito.verifyNew(ArArchiveInputStream.class).withArguments(eq(inputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(ZipArchiveOutputStream.class).withArguments(eq(outputStream)).thenReturn(mock(ZipArchiveOutputStream.class));
        ArchiveOutputStream stream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertEquals(ZipArchiveOutputStream.class, stream.getClass());
        PowerMockito.verifyNew(ZipArchiveOutputStream.class).withArguments(eq(outputStream));
    }

    @Test
    public void testCreateArchiveOutputStreamUnknown() throws IOException, ArchiveException {
        try {
            ArchiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
            fail();
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(TarArchiveOutputStream.class).withArguments(eq(outputStream), anyString(), eq(true)).thenReturn(mock(TarArchiveOutputStream.class));
        ArchiveOutputStream stream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream, "test.tar");
        assertEquals(TarArchiveOutputStream.class, stream.getClass());
        PowerMockito.verifyNew(TarArchiveOutputStream.class).withArguments(eq(outputStream), eq("test.tar"), eq(true));
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws IOException, ArchiveException {
        PowerMockito.whenNewInstance(ArArchiveOutputStream.class).withArguments(eq(outputStream), anyString(), eq(false)).thenReturn(mock(ArArchiveOutputStream.class));
        ArchiveOutputStream stream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream, "test.a");
        assertEquals(ArArchiveOutputStream.class, stream.getClass());
        PowerMockito.verifyNew(ArArchiveOutputStream.class).withArguments(eq(outputStream), eq("test.a"), eq(false));
    }

    @Test
    public void testCreateArchiveInputStreamStreamSignature() throws IOException, ArchiveException {
        // Mock ZipArchiveInputStream
        byte[] zipSignature = {0x04, 0x03, 0x4b, 0x50, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        PowerMockito.whenNewInstance(ZipArchiveInputStream.class).withArguments(eq(inputStream)).thenReturn(mock(ZipArchiveInputStream.class));
        doAnswer(invocation -> {
            byte[] signature = invocation.getArgument(0);
            assertEquals(zipSignature, signature);
            return null;
        }).when(mock(ZipArchiveInputStream.class)).getSignature();

        // Test
        ArchiveInputStream stream = ArchiveStreamFactory.createArchiveInputStream(inputStream);
        assertEquals(ZipArchiveInputStream.class, stream.getClass());

        // Mock ArArchiveInputStream
        byte[] arSignature = {0x60, 0x5a, 0x2d, 0x00, 0x61, 0x73, 0x73, 0x53, 0x79, 0x73, 0x61, 0x6d, 0x65, 0x64, 0x2d, 0x61, 0x76, 0x65, 0x2d, 0x31, 0x2e, 0x31};
        PowerMockito.whenNewInstance(ArArchiveInputStream.class).withArguments(eq(inputStream)).thenReturn(mock(ArArchiveInputStream.class));
        doAnswer(invocation -> {
            byte[] signature = invocation.getArgument(0);
            assertEquals(arSignature, signature);
            return null;
        }).when(mock(ArArchiveInputStream.class)).getSignature();

        // Test
        ArchiveInputStream arStream = ArchiveStreamFactory.createArchiveInputStream(inputStream);
        assertEquals(ArArchiveInputStream.class, arStream.getClass());
    }
}