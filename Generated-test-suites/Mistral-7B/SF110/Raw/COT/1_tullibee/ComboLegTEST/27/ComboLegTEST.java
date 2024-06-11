import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testConstructor_validArgs() {
 ComboLeg comboLeg = createComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("BUY", comboLeg.getm_action());
 Assert.assertEquals("IBKR", comboLeg.getm_exchange());
 Assert.assertEquals(1, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("NYSE", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructor_nullArgs() {
 Assert.assertThrows("IllegalArgumentException", () -> new ComboLeg(0, 0, null, null, 0, 0, null));
 }

 @Test
 public void testConstructor_zeroArgs() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getm_conId());
 Assert.assertEquals(0, comboLeg.getm_ratio());
 Assert.assertNull(comboLeg.getm_action());
 Assert.assertNull(comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEquals_sameObject() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 ComboLeg comboLeg2 = comboLeg1;
 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEquals_differentObjects() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NASDAQ");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEquals_nullObject() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 Assert.assertFalse(comboLeg1.equals(null));
 }

 @Test
 public void testEquals_differentTypes() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 String stringObj = "test";
 Assert.assertFalse(comboLeg1.equals(stringObj));
 }

 @Test
 public void testEquals_differentValues() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "SELL", "IBKR", 1, 0, "NYSE");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testHashCode_sameObjects() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 ComboLeg comboLeg2 = comboLeg1;
 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 }

 @Test
 public void testHashCode_differentObjects() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NYSE");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NASDAQ");
 Assert.assertNotSame(comboLeg1.hashCode(), comboLeg2.hashCode());
 }

}