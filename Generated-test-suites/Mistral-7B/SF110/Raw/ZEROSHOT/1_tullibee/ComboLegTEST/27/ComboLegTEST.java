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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("action1", comboLeg1.getm_action());
 Assert.assertEquals("exchange1", comboLeg1.getm_exchange());
 Assert.assertEquals(1, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1, 2, "designatedLocation1");
 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("action1", comboLeg2.getm_action());
 Assert.assertEquals("exchange1", comboLeg2.getm_exchange());
 Assert.assertEquals(1, comboLeg2.getm_openClose());
 Assert.assertEquals(2, comboLeg2.getm_shortSaleSlot());
 Assert.assertEquals("designatedLocation1", comboLeg2.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(2, 2, "action1", "exchange1", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();

 comboLeg.setm_conId(1);
 Assert.assertEquals(1, comboLeg.getm_conId());

 comboLeg.setm_ratio(2);
 Assert.assertEquals(2, comboLeg.getm_ratio());

 comboLeg.setm_action("action1");
 Assert.assertEquals("action1", comboLeg.getm_action());

 comboLeg.setm_exchange("exchange1");
 Assert.assertEquals("exchange1", comboLeg.getm_exchange());

 comboLeg.setm_openClose(1);
 Assert.assertEquals(1, comboLeg.getm_openClose());

 comboLeg.setm_shortSaleSlot(2);
 Assert.assertEquals(2, comboLeg.getm_shortSaleSlot());

 comboLeg.setm_designatedLocation("designatedLocation1");
 Assert.assertEquals("designatedLocation1", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(2, 2, "action1", "exchange1", 1);

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg3.hashCode());
 }

 private int getm_conId(ComboLeg comboLeg) {
 return comboLeg.m_conId;
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