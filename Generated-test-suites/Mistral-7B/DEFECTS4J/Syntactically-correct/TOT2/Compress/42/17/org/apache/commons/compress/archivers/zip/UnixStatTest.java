package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkFlag = UnixStat.LINK_FLAG;
        assertTrue(linkFlag == 0120000);
    }

    @Test
    public void testFileFlag() {
        int fileFlag = UnixStat.FILE_FLAG;
        assertTrue(fileFlag == 0100000);
    }

    @Test
    public void testDirFlag() {
        int dirFlag = UnixStat.DIR_FLAG;
        assertTrue(dirFlag == 040000);
    }

    @Test
    public void testDefaultLinkPerm() {
        int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
        assertTrue(defaultLinkPerm == 0777);
    }

    @Test
    public void testDefaultDirPerm() {
        int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
        assertTrue(defaultDirPerm == 0755);
    }

    @Test
    public void testDefaultFilePerm() {
        int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
        assertTrue(defaultFilePerm == 0644);
    }

    @Test
    public void testPermMask() {
        int permMask = UnixStat.PERM_MASK;
        assertTrue(permMask == 07777);
    }
}