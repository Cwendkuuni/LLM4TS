import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int actual = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFlag() {
        int actual = UnixStat.FILE_FLAG;
        int expected = 0100000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDirFlag() {
        int actual = UnixStat.DIR_FLAG;
        int expected = 040000;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultLinkPerm() {
        int actual = UnixStat.DEFAULT_LINK_PERM;
        int expected = 0777;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultDirPerm() {
        int actual = UnixStat.DEFAULT_DIR_PERM;
        int expected = 0755;
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultFilePerm() {
        int actual = UnixStat.DEFAULT_FILE_PERM;
        int expected = 0644;
        assertEquals(expected, actual);
    }

    @Test (expected = IllegalStateException.class)
    public void testInvalidBitMask() {
        int actual = UnixStat.PERM_MASK | UnixStat.LINK_FLAG; // combining invalid bits
        throw new IllegalStateException("Invalid bit mask: " + actual);
    }
}