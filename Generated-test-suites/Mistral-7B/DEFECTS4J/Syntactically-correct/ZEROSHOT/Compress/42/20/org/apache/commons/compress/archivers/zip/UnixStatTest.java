import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

 @Test
 public void testPermissionsMask() {
 int permissionsMask = UnixStat.PERM_MASK;
 assertEquals(07777, permissionsMask);
 }

 @Test
 public void testLinkFlag() {
 int linkFlag = UnixStat.LINK_FLAG;
 assertEquals(0120000, linkFlag);
 }

 @Test
 public void testFileFlag() {
 int fileFlag = UnixStat.FILE_FLAG;
 assertEquals(0100000, fileFlag);
 }

 @Test
 public void testDirFlag() {
 int dirFlag = UnixStat.DIR_FLAG;
 assertEquals(040000, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 assertEquals(0777, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 assertEquals(0755, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 assertEquals(0644, defaultFilePerm);
 }
}