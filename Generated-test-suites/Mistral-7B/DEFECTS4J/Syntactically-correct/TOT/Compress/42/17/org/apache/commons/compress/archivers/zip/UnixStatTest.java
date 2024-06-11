import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int linkPermission = UnixStat.LINK_FLAG | UnixStat.PERM_MASK;
 int expected = 01207777; // 777 for permissions, 012 for link flag
 int result = linkPermission & ~UnixStat.LINK_FLAG; // remove link flag to check the remaining permissions
 assertEquals(expected, result);
 }

 @Test
 public void testDefaultPermissions() {
 int linkDefault = UnixStat.DEFAULT_LINK_PERM & UnixStat.PERM_MASK;
 int dirDefault = UnixStat.DEFAULT_DIR_PERM & UnixStat.PERM_MASK;
 int fileDefault = UnixStat.DEFAULT_FILE_PERM & UnixStat.PERM_MASK;

 assertEquals(0777, linkDefault);
 assertEquals(0755, dirDefault);
 assertEquals(0644, fileDefault);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidFlag() {
 int invalidFlag = 0xFFFF_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF_FF00; // invalid flag
 int result;
 try {
 result = invalidFlag & UnixStat.PERM_MASK;
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }
}