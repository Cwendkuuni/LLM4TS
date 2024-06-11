import static org.apache.commons.compress.archivers.zip.UnixStat.*;
import org.junit.Test;

public class UnixStatTest {
    @Test
    public void testUnixStat_PERM_MASK() {
        int expected = 07777;
        int actual = PERM_MASK;
        assertEquals(expected, actual);
    }
}