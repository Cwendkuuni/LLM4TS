import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private final ComboLeg LEG1 = new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN);
 private final ComboLeg LEG2 = new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN);
 private final ComboLeg LEG3 = new ComboLeg(1, 1, "SELL", "IBKR", ComboLeg.CLOSE);

 @Test
 public void testConstructorWithDefaultParameter() {
 ComboLeg leg = new ComboLeg();
 Assert.assertEquals(0, leg.getm_conId());
 Assert.assertEquals(0, leg.getm_ratio());
 Assert.assertNull(leg.getm_action());
 Assert.assertNull(leg.getm_exchange());
 Assert.assertEquals(ComboLeg.UNKNOWN, leg.getm_openClose());
 Assert.assertNull(leg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithAllParameters() {
 ComboLeg leg = new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");
 Assert.assertEquals(1, leg.getm_conId());
 Assert.assertEquals(1, leg.getm_ratio());
 Assert.assertEquals("BUY", leg.getm_action());
 Assert.assertEquals("IBKR", leg.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, leg.getm_openClose());
 Assert.assertEquals(1, leg.getm_shortSaleSlot());
 Assert.assertEquals("NYSE", leg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithInvalidParameters() {
 try {
 new ComboLeg(-1, 1, "BUY", "IBKR", ComboLeg.OPEN);
 Assert.fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new ComboLeg(1, -1, "BUY", "IBKR", ComboLeg.OPEN);
 Assert.fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 1, null, "IBKR", ComboLeg.OPEN);
 Assert.fail("Should not reach this point");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 1, "BUY", null, ComboLeg.OPEN);
 Assert.fail("Should not reach this point");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.CLOSE);
 Assert.fail("Should not reach this point");
 } catch (IllegalArgumentException e) {
 // Expected, m_openClose should be either OPEN or CLOSE
 }
 }

 @Test
 public void testGetters() {
 Assert.assertEquals(1, LEG1.getm_conId());
 Assert.assertEquals(1, LEG1.getm_ratio());
 Assert.assertEquals("BUY", LEG1.getm_action());
 Assert.assertEquals("IBKR", LEG1.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, LEG1.getm_openClose());
 Assert.assertNull(LEG1.getm_designatedLocation());

 Assert.assertEquals(1, LEG2.getm_conId());
 Assert.assertEquals(1, LEG2.getm_ratio());
 Assert.assertEquals("BUY", LEG2.getm_action());
 Assert.assertEquals("IBKR", LEG2.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, LEG2.getm_openClose());
 Assert.assertNull(LEG2.getm_designatedLocation());

 Assert.assertEquals(1, LEG3.getm_conId());
 Assert.assertEquals(1, LEG3.getm_ratio());
 Assert.assertEquals("SELL", LEG3.getm_action());
 Assert.assertEquals("IBKR", LEG3.getm_exchange());
 Assert.assertEquals(ComboLeg.CLOSE, LEG3.getm_openClose());
 Assert.assertNull(LEG3.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 Assert.assertTrue(LEG1.equals(LEG2));
 Assert.assertTrue(LEG1.equals(new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN)));
 Assert.assertFalse(LEG1.equals(new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.CLOSE)));
 Assert.assertFalse(LEG1.equals(null));
 Assert.assertFalse(LEG1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 Assert.assertEquals(hashCode(LEG1), hashCode(LEG2));
 Assert.assertNotEquals(hashCode(LEG1), hashCode(LEG3));
 }

 private int hashCode(final ComboLeg leg) {
 return leg.m_conId + 31 * (leg.m_ratio + 31 * (leg.m_openClose + 31 * Util.hashCodeIgnoreCase(leg.m_action) + 31 * Util.hashCodeIgnoreCase(leg.m_exchange) + 31 * Util.hashCodeIgnoreCase(leg.m_designatedLocation)));
 }

}