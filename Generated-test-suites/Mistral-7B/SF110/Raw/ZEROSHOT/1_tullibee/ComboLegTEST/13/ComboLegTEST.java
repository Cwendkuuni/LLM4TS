import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test public void testConstructor() {
 ComboLeg comboLeg = new ComboLeg(); Assert.assertEquals(0, comboLeg.getm_conId()); Assert.assertEquals(0, comboLeg.getm_ratio()); Assert.assertNull(comboLeg.getm_action()); Assert.assertNull(comboLeg.getm_exchange()); Assert.assertEquals(0, comboLeg.getm_openClose()); Assert.assertEquals(0, comboLeg.getm_shortSaleSlot()); Assert.assertNull(comboLeg.getm_designatedLocation());

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3); Assert.assertEquals(1, comboLeg1.getm_conId()); Assert.assertEquals(2, comboLeg1.getm_ratio()); Assert.assertEquals("Action1", comboLeg1.getm_action()); Assert.assertEquals("Exchange1", comboLeg1.getm_exchange()); Assert.assertEquals(3, comboLeg1.getm_openClose()); Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot()); Assert.assertNull(comboLeg1.getm_designatedLocation());

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1"); Assert.assertEquals(1, comboLeg2.getm_conId()); Assert.assertEquals(2, comboLeg2.getm_ratio()); Assert.assertEquals("Action1", comboLeg2.getm_action()); Assert.assertEquals("Exchange1", comboLeg2.getm_exchange()); Assert.assertEquals(3, comboLeg2.getm_openClose()); Assert.assertEquals(4, comboLeg2.getm_shortSaleSlot()); Assert.assertEquals("DesignatedLocation1", comboLeg2.getm_designatedLocation());
 }

 @Test public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1"); ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1"); ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation2");

 Assert.assertTrue(comboLeg1.equals(comboLeg2)); Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 private int getM(ComboLeg comboLeg, int fieldIndex) {
 switch (fieldIndex) {
 case 0: return comboLeg.getm_conId();
 case 1: return comboLeg.getm_ratio();
 case 2: return comboLeg.getm_openClose();
 case 3: return comboLeg.getm_shortSaleSlot();
 default: return -1;
 }
 }

 @Test public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();

 comboLeg.setm_conId(1); Assert.assertEquals(1, getM(comboLeg, 0)); comboLeg.setm_ratio(2); Assert.assertEquals(2, getM(comboLeg, 1)); comboLeg.setm_action("Action1"); Assert.assertEquals("Action1", comboLeg.getm_action()); comboLeg.setm_exchange("Exchange1"); Assert.assertEquals("Exchange1", comboLeg.getm_exchange()); comboLeg.setm_openClose(3); Assert.assertEquals(3, comboLeg.getm_openClose()); comboLeg.setm_shortSaleSlot(4); Assert.assertEquals(4, comboLeg.getm_shortSaleSlot()); comboLeg.setm_designatedLocation("DesignatedLocation1"); Assert.assertEquals("DesignatedLocation1", comboLeg.getm_designatedLocation());
 }

 @Test public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1"); ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1"); ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation2");

 Assert.assertTrue(comboLeg1.equals(comboLeg2)); Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode()); Assert.assertFalse(comboLeg1.equals(comboLeg3)); Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
 }
}