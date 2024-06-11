import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test UnixStat constants
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testLinkFlag() {
        // Test if LINK_FLAG value represents a symbolic link
        int linkPermissions = UnixStat.PERM_MASK | UnixStat.LINK_FLAG;
        assertTrue(isSymbolicLink(linkPermissions));
    }

    @Test
    public void testFileFlag() {
        // Test if FILE_FLAG value represents a plain file
        int filePermissions = UnixStat.PERM_MASK & ~UnixStat.DIR_FLAG;
        assertFalse(isSymbolicLink(filePermissions));
        assertFalse(isDirectory(filePermissions));
    }

    @Test
    public void testDirFlag() {
        // Test if DIR_FLAG value represents a directory
        int dirPermissions = UnixStat.PERM_MASK | UnixStat.DIR_FLAG;
        assertTrue(isDirectory(dirPermissions));
        assertFalse(isSymbolicLink(dirPermissions));
        assertFalse(isFile(dirPermissions));
    }

    private boolean isSymbolicLink(int permissions) {
        return (permissions & UnixStat.LINK_FLAG) != 0;
    }

    private boolean isDirectory(int permissions) {
        return (permissions & UnixStat.DIR_FLAG) != 0;
    }

    private boolean isFile(int permissions) {
        return (permissions & UnixStat.FILE_FLAG) != 0;
    }
}