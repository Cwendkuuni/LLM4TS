import org.junit.Test;
import org.apache.commons.compress.archivers.zip.UnixStat;

import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkStat = UnixStat.LINK_FLAG;
        assertTrue(linkStat & UnixStat.PERM_MASK >= UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        int fileStat = UnixStat.FILE_FLAG;
        assertTrue(fileStat & UnixStat.PERM_MASK >= UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        int dirStat = UnixStat.DIR_FLAG;
        assertTrue(dirStat & UnixStat.PERM_MASK >= UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertEquals(defaultLinkPerm & UnixStat.PERM_MASK, 0777);
    }

    @Test
    public void testDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertEquals(defaultDirPerm & UnixStat.PERM_MASK, 0755);
    }

    @Test
    public void testDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertEquals(defaultFilePerm & UnixStat.PERM_MASK, 0644);
    }

    @Test (expected = IllegalStateException.class)
    public void testUnknownFlag() {
        int unknownFlag = 0b1111_1111_1111_1111_0111_1111_1111_1111; // random
        int stat = UnixStat.PERM_MASK | unknownFlag;
        UnixStat unixStat = new UnixStat() {
            @Override
            public int getFileType() {
                return stat;
            }
        };
        unixStat.checkValid();
    }
}