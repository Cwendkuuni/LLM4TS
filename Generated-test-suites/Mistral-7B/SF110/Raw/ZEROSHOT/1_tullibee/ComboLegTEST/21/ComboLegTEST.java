import org.junit.Assert;import org.junit.Test;import com.ib.client.ComboLeg;

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

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "London");
 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("BUY", comboLeg2.getm_action());
 Assert.assertEquals("IBKR", comboLeg2.getm_exchange());
 Assert.assertEquals(1, comboLeg2.getm_openClose());
 Assert.assertEquals(5, comboLeg2.getm_shortSaleSlot());
 Assert.assertEquals("London", comboLeg2.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(2, 2, "BUY", "IBKR", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));

 comboLeg2.setm_conId(3);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_ratio(3);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_openClose(3);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_shortSaleSlot(3);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_action("SELL");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_exchange("CBOE");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_designatedLocation("NYSE");
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();

 Assert.assertEquals(0, comboLeg.getm_conId());
 comboLeg.setm_conId(1);
 Assert.assertEquals(1, comboLeg.getm_conId());

 Assert.assertEquals(0, comboLeg.getm_ratio());
 comboLeg.setm_ratio(2);
 Assert.assertEquals(2, comboLeg.getm_ratio());

 Assert.assertNull(comboLeg.getm_action());
 comboLeg.setm_action("BUY");
 Assert.assertEquals("BUY", comboLeg.getm_action());

 Assert.assertNull(comboLeg.getm_exchange());
 comboLeg.setm_exchange("IBKR");
 Assert.assertEquals("IBKR", comboLeg.getm_exchange());

 Assert.assertEquals(0, comboLeg.getm_openClose());
 comboLeg.setm_openClose(1);
 Assert.assertEquals(1, comboLeg.getm_openClose());

 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 comboLeg.setm_shortSaleSlot(5);
 Assert.assertEquals(5, comboLeg.getm_shortSaleSlot());

 Assert.assertNull(comboLeg.getm_designatedLocation());
 comboLeg.setm_designatedLocation("London");
 Assert.assertEquals("London", comboLeg.getm_designatedLocation());
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