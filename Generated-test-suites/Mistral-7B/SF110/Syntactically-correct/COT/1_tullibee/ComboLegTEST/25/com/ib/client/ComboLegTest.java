import org.junit.Assert;import org.junit.Test;import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testComboLegConstructorWithAllArguments() {
 ComboLeg comboLeg = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("BUY", comboLeg.getm_action());
 Assert.assertEquals("IDEALPRO", comboLeg.getm_exchange());
 Assert.assertEquals(1, comboLeg.getm_openClose());
 Assert.assertEquals(3, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("NYSE", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testComboLegDefaultConstructor() {
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
 public void testComboLegEqualsTrue() {
 ComboLeg comboLeg1 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 ComboLeg comboLeg2 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testComboLegEqualsFalse() {
 ComboLeg comboLeg1 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 ComboLeg comboLeg2 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NASDAQ");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullAction() {
 ComboLeg comboLeg = new ComboLeg(1, 2, null, "IDEALPRO", 1, 3, "NYSE");
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullExchange() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", null, 1, 3, "NYSE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegInvalidOpenClose() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IDEALPRO", 4, 3, "NYSE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeRatio() {
 ComboLeg comboLeg = new ComboLeg(1, -2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeShortSaleSlot() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IDEALPRO", 1, -3, "NYSE");
 }

 @Test
 public void testComboLegHashCode() {
 int hashCode1 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE").hashCode();
 ComboLeg comboLeg2 = createComboLeg(1, 2, "BUY", "IDEALPRO", 1, 3, "NYSE");
 int hashCode2 = comboLeg2.hashCode();
 Assert.assertEquals(hashCode1, hashCode2);
 }
}