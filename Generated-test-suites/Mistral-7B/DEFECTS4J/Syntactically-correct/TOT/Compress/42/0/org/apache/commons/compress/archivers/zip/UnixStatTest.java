import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals("Unexpected link flag value", expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals("Unexpected file flag value", expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals("Unexpected directory flag value", expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals("Unexpected default link permissions", expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals("Unexpected default directory permissions", expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals("Unexpected default file permissions", expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidPermissionMaskCombination() {
        int permissionMask = 01111000; // Combination of invalid bits
        try {
            UnixStat.PERM_MASK = permissionMask;
            fail("Expected IllegalStateException for invalid permission mask");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}