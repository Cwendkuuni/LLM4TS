import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testPermMask() {
 int actual = UnixStat.PERM_MASK;
 int expected = 07777;
 assertEquals(expected, actual);
 }

 @Test
 public void testLinkFlag() {
 int actual = UnixStat.LINK_FLAG;
 int expected = 0120000;
 assertEquals(expected, actual);
 }

 @Test
 public void testDefaultLinkPerm() {
 int actual = UnixStat.DEFAULT_LINK_PERM;
 int expected = 0777;
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidConstant() {
 int invalidConstant = 999999999;
 int expected = UnixStat.LINK_FLAG;
 assertEquals(expected, invalidConstant); // This test will fail
 }

 @Test(expected = AssertionError.class)
 public void testNegativeDefaultLinkPerm() {
 int actual = UnixStat.DEFAULT_LINK_PERM;
 int expected = -1 * UnixStat.DEFAULT_LINK_PERM;
 assertEquals(expected, actual); // This test will fail
 }

 @Test(expected = AssertionError.class)
 public void testNullReference() {
 UnixStat unixStat = null;
 int expected = UnixStat.LINK_FLAG;
 int actual = unixStat.LINK_FLAG;
 assertEquals(expected, actual); // This test will fail
 }

}