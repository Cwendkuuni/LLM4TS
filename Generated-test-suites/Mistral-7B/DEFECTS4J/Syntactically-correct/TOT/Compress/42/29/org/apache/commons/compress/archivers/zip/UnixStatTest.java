import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 UnixStat unixStat = new UnixStat();
 int linkFile = unixStat.LINK_FLAG;
 int expectedLinkFile = 0120000;
 assertEquals(expectedLinkFile, linkFile);
 }

 @Test
 public void testFileFlag() {
 UnixStat unixStat = new UnixStat();
 int file = unixStat.FILE_FLAG;
 int expectedFile = 0100000;
 assertEquals(expectedFile, file);
 }

 @Test
 public void testDirFlag() {
 UnixStat unixStat = new UnixStat();
 int dir = unixStat.DIR_FLAG;
 int expectedDir = 040000;
 assertEquals(expectedDir, dir);
 }

 @Test
 public void testDefaultLinkPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultLinkPerm = unixStat.DEFAULT_LINK_PERM;
 int expectedDefaultLinkPerm = 0777;
 assertEquals(expectedDefaultLinkPerm, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 UnixStat unixStat = new UnixStat();
 int defaultDirPerm = unixStat.DEFAULT_DIR_PERM;
 int expectedDefaultDirPerm = 0755;
 assertEquals(expectedDefaultDirPerm, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 UnixStat unixStat = new UnixStat();
 int defaultFilePerm = unixStat.DEFAULT_FILE_PERM;
 int expectedDefaultFilePerm = 0644;
 assertEquals(expectedDefaultFilePerm, defaultFilePerm);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyingConstants() {
 UnixStat unixStat = new UnixStat() {
 @Override
 public int PERM_MASK() {
 return 0; // trying to modify a constant
 }
 };
 UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> unixStat.PERM_MASK());
 assertEquals("Cannot modify final class constants", exception.getMessage());
 }
}