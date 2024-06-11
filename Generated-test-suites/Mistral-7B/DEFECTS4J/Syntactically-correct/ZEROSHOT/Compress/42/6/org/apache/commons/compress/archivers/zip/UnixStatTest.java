import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test UNIXStat constants
 assertEquals("Unexpected value for PERM_MASK", 07777, UnixStat.PERM_MASK);
 assertEquals("Unexpected value for LINK_FLAG", 0120000, UnixStat.LINK_FLAG);
 assertEquals("Unexpected value for FILE_FLAG", 0100000, UnixStat.FILE_FLAG);
 assertEquals("Unexpected value for DIR_FLAG", 040000, UnixStat.DIR_FLAG);
 assertEquals("Unexpected value for DEFAULT_LINK_PERM", 0777, UnixStat.DEFAULT_LINK_PERM);
 assertEquals("Unexpected value for DEFAULT_DIR_PERM", 0755, UnixStat.DEFAULT_DIR_PERM);
 assertEquals("Unexpected value for DEFAULT_FILE_PERM", 0644, UnixStat.DEFAULT_FILE_PERM);
 }
}