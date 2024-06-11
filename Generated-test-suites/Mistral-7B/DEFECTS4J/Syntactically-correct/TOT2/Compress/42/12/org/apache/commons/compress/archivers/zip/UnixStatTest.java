import static org.junit.Assert.assertEquals;
import org.apache.commons.compress.archivers.zip.UnixStat;

public class UnixStatTest {
    // 

    @Test
    public void testPermMask() {
        int expected = 07777;
        int actual = UnixStat.PERM_MASK;
        assertEquals("Unexpected PERM_MASK value", expected, actual);
    }

    // 
}