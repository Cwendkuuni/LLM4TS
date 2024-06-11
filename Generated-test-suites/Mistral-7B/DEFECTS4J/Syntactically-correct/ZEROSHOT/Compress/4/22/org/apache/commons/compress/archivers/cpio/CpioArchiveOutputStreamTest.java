import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        File testFile = new File("testFile.txt");
        String testFileContent = "Test content";
        File testDirectory = new File("testDirectory");
        testDirectory.mkdir();
        File testDirectoryFile = new File(testDirectory, "testDirectoryFile.txt");
        testDirectoryFile.createNewFile();

        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testFile.txt");
        testEntry.setSize(testFileContent.length());
        testEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        testEntry = new CpioArchiveEntry();
        testEntry.setName("testDirectory/testDirectoryFile.txt");
        testEntry.setFormat(CpioConstants.FORMAT_NEW);
        testEntry.setSize(testDirectoryFile.length());
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.writeTo(testDirectoryFile.getAbsolutePath().getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        testEntry = new CpioArchiveEntry();
        testEntry.setName("testDirectory/");
        testEntry.setSize(0);
        testEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        byte[] testBytes = outputStream.toByteArray();
        String testString = new String(testBytes);
        Assert.assertTrue(testString.contains(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW)));
        Assert.assertTrue(testString.contains(ArchiveUtils.toAsciiBytes(testEntry.getName())));
        Assert.assertTrue(testString.contains(Long.toHexString(testEntry.getSize()).substring(0, 8)));

        Map<String, CpioArchiveEntry> nameMap = new HashMap<>();
        CpioArchiveEntry readEntry = cpioArchiveOutputStream.getNextEntry();
        while (readEntry != null) {
            nameMap.put(readEntry.getName(), readEntry);
            readEntry = cpioArchiveOutputStream.getNextEntry();
        }
        Assert.assertEquals(3, nameMap.size());
        Assert.assertEquals(testEntry.getName(), nameMap.get("testFile.txt").getName());
        Assert.assertEquals(testEntry.getName(), nameMap.get("testDirectory/testDirectoryFile.txt").getName());
        Assert.assertEquals(testEntry.getName(), nameMap.get("testDirectory/").getName());

        testDirectory.delete();
        testDirectoryFile.delete();
        testFile.delete();
    }
}