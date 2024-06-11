import static org.junit.Assert.assertEquals;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test PERM_MASK
        assertEquals(07777, UnixStat.PERM_MASK);

        // Test LINK_FLAG
        assertEquals(0120000, UnixStat.LINK_FLAG);

        // Test FILE_FLAG
        assertEquals(0100000, UnixStat.FILE_FLAG);

        // Test DIR_FLAG
        assertEquals(040000, UnixStat.DIR_FLAG);

        // Test DEFAULT_LINK_PERM
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);

        // Test DEFAULT_DIR_PERM
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);

        // Test DEFAULT_FILE_PERM
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testUnixStatEdgeCases() {
        // Test PERM_MASK edge cases
        assertEquals(minPermMask, UnixStat.PERM_MASK & minPermMask);
        assertEquals(maxPermMask, UnixStat.PERM_MASK & maxPermMask);

        // Test LINK_FLAG edge cases
        assertEquals(minLinkFlag, UnixStat.PERM_MASK & minLinkFlag);
        assertEquals(maxLinkFlag, UnixStat.PERM_MASK & maxLinkFlag);

        // Test FILE_FLAG edge cases
        assertEquals(minFileFlag, UnixStat.PERM_MASK & minFileFlag);
        assertEquals(maxFileFlag, UnixStat.PERM_MASK & maxFileFlag);

        // Test DIR_FLAG edge cases
        assertEquals(minDirFlag, UnixStat.PERM_MASK & minDirFlag);
        assertEquals(maxDirFlag, UnixStat.PERM_MASK & maxDirFlag);

        // Test DEFAULT_LINK_PERM edge cases
        assertEquals(minDefaultLinkPerm, UnixStat.DEFAULT_LINK_PERM & minDefaultLinkPerm);
        assertEquals(maxDefaultLinkPerm, UnixStat.DEFAULT_LINK_PERM & maxDefaultLinkPerm);

        // Test DEFAULT_DIR_PERM edge cases
        assertEquals(minDefaultDirPerm, UnixStat.DEFAULT_DIR_PERM & minDefaultDirPerm);
        assertEquals(maxDefaultDirPerm, UnixStat.DEFAULT_DIR_PERM & maxDefaultDirPerm);

        // Test DEFAULT_FILE_PERM edge cases
        assertEquals(minDefaultFilePerm, UnixStat.DEFAULT_FILE_PERM & minDefaultFilePerm);
        assertEquals(maxDefaultFilePerm, UnixStat.DEFAULT_FILE_PERM & maxDefaultFilePerm);
    }
}