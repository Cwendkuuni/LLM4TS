import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;;

public class UnixStatTest {
    @Test
    public void testPermMask() {
        int result = UnixStat.PERM_MASK;
        int expected = 07777;
        assertEquals(expected, result);
    }

    @Test
    public void testLinkFlag() {
        int result = UnixStat.LINK_FLAG;
        int expected = 0120000;
        assertEquals(expected, result);
    }

    // Add test cases for FILE_FLAG and DIR_FLAG following the same pattern
}