import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.UnixFilePerm;

public class UnixStatTest {

    @Test
    public void testGetFilePermissions() {
        int filePerm = UnixStat.FILE_FLAG;
        int expected = UnixFilePerm.OWNER_READ | UnixFilePerm.GROUP_READ | UnixFilePerm.OTHERS_READ;
        int actual = UnixFilePerm.asInt(UnixStat.getFilePermissions(filePerm));
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckIfFile() {
        int fileFlag = UnixStat.FILE_FLAG;
        boolean expected = true;
        boolean actual = UnixStat.isFile(fileFlag);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckIfDirectory() {
        int dirFlag = UnixStat.DIR_FLAG;
        boolean expected = true;
        boolean actual = UnixStat.isDirectory(dirFlag);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckIfSymbolicLink() {
        int linkFlag = UnixStat.LINK_FLAG;
        boolean expected = true;
        boolean actual = UnixStat.isSymbolicLink(linkFlag);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFlagValue() {
        int invalidFlag = 0xFFFF_FFFF;
        UnixStat.isFile(invalidFlag);
    }

    @Test
    public void testGetDefaultFilePermissions() {
        int expected = UnixFilePerm.OWNER_READ | UnixFilePerm.GROUP_READ | UnixFilePerm.OTHERS_READ;
        int actual = UnixStat.getDefaultFilePermissions();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultDirectoryPermissions() {
        int expected = UnixFilePerm.OWNER_READ | UnixFilePerm.GROUP_READ | UnixFilePerm.GROUP_WRITE | UnixFilePerm.OTHERS_EXECUTE;
        int actual = UnixStat.getDefaultDirectoryPermissions();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultSymbolicLinkPermissions() {
        int expected = UnixFilePerm.OWNER_READ | UnixFilePerm.GROUP_READ | UnixFilePerm.OTHERS_READ;
        int actual = UnixStat.getDefaultSymbolicLinkPermissions();
        assertEquals(expected, actual);
    }
}