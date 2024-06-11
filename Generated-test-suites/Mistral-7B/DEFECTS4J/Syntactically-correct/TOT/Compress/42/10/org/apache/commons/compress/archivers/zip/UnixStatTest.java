import org.junit.Test;
import org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkPermissions() {
 int linkPermissions = UnixStat.LINK_FLAG | UnixStat.PERM_MASK & UnixStat.DEFAULT_LINK_PERM;
 assertTrue(linkPermissions == 0120777);
 }

 @Test
 public void testDirPermissions() {
 int dirPermissions = UnixStat.DIR_FLAG | UnixStat.PERM_MASK & UnixStat.DEFAULT_DIR_PERM;
 assertTrue(dirPermissions == 040755);
 }

 @Test
 public void testFilePermissions() {
 int filePermissions = UnixStat.FILE_FLAG | UnixStat.PERM_MASK & UnixStat.DEFAULT_FILE_PERM;
 assertTrue(filePermissions == 0100644);
 }

 @Test
 public void testInvalidPermissions() {
 int invalidPerm = UnixStat.PERM_MASK | 0b101010101; // random bits
 assertFalse("Invalid permissions should not match perm_mask", (invalidPerm & UnixStat.PERM_MASK) == UnixStat.PERM_MASK);
 }
}