import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

 @Test
 public void testConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getm_conId());
 Assert.assertEquals(0, comboLeg.getm_ratio());
 Assert.assertNull(comboLeg.getm_action());
 Assert.assertNull(comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("BUY", comboLeg1.getm_action());
 Assert.assertEquals("IBKR", comboLeg1.getm_exchange());
 Assert.assertEquals(1, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 2);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 new ComboLeg(0, 0, null, null, -1, 0, null);
 }

 @Test
 public void testGetters() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1);

 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("BUY", comboLeg.getm_action());
 Assert.assertEquals("IBKR", comboLeg.getm_exchange());
 Assert.assertEquals(1, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 private int getm_conId(ComboLeg comboLeg) {
 return comboLeg.m_conId;
 }

 private int getm_ratio(ComboLeg comboLeg) {
 return comboLeg.m_ratio;
 }

 private String getm_action(ComboLeg comboLeg) {
 return comboLeg.m_action;
 }

 private String getm_exchange(ComboLeg comboLeg) {
 return comboLeg.m_exchange;
 }

 private int getm_openClose(ComboLeg comboLeg) {
 return comboLeg.m_openClose;
 }

 private int getm_shortSaleSlot(ComboLeg comboLeg) {
 return comboLeg.m_shortSaleSlot;
 }

 private String getm_designatedLocation(ComboLeg comboLeg) {
 return comboLeg.m_designatedLocation;
 }
}