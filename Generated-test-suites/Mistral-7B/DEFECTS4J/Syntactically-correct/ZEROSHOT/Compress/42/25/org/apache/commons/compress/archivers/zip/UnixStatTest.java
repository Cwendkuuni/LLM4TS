import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test PERM_MASK
 int permMask = UnixStat.PERM_MASK;
 assertEquals(07777, permMask);

 // Test LINK_FLAG
 int linkFlag = UnixStat.LINK_FLAG;
 assertEquals(0120000, linkFlag);

 // Test FILE_FLAG
 int fileFlag = UnixStat.FILE_FLAG;
 assertEquals(0100000, fileFlag);

 // Test DIR_FLAG
 int dirFlag = UnixStat.DIR_FLAG;
 assertEquals(040000, dirFlag);

 // Test DEFAULT_LINK_PERM
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 assertEquals(0777, defaultLinkPerm);

 // Test DEFAULT_DIR_PERM
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 assertEquals(0755, defaultDirPerm);

 // Test DEFAULT_FILE_PERM
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 assertEquals(0644, defaultFilePerm);
 }
}