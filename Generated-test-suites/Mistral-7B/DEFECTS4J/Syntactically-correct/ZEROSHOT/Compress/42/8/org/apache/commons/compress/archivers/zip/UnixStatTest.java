import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test UnixStat interface constants

 assertEquals("PERM_MASK should be 07777", 07777, UnixStat.PERM_MASK);
 assertEquals("LINK_FLAG should be 0120000", 0120000, UnixStat.LINK_FLAG);
 assertEquals("FILE_FLAG should be 0100000", 0100000, UnixStat.FILE_FLAG);
 assertEquals("DIR_FLAG should be 040000", 040000, UnixStat.DIR_FLAG);
 assertEquals("DEFAULT_LINK_PERM should be 0777", 0777, UnixStat.DEFAULT_LINK_PERM);
 assertEquals("DEFAULT_DIR_PERM should be 0755", 0755, UnixStat.DEFAULT_DIR_PERM);
 assertEquals("DEFAULT_FILE_PERM should be 0644", 0644, UnixStat.DEFAULT_FILE_PERM);
 }
}