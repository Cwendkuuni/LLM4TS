import org.junit. Assert;
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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("Action1", comboLeg1.getm_action());
 Assert.assertEquals("Exchange1", comboLeg1.getm_exchange());
 Assert.assertEquals(3, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("Action1", comboLeg2.getm_action());
 Assert.assertEquals("Exchange1", comboLeg2.getm_exchange());
 Assert.assertEquals(3, comboLeg2.getm_openClose());
 Assert.assertEquals(4, comboLeg2.getm_shortSaleSlot());
 Assert.assertEquals("DesignatedLocation1", comboLeg2.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation1");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 3, 4, "DesignatedLocation2");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(null));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();

 comboLeg.setm_conId(1);
 Assert.assertEquals(1, comboLeg.getm_conId());

 comboLeg.setm_ratio(2);
 Assert.assertEquals(2, comboLeg.getm_ratio());

 comboLeg.setm_action("Action1");
 Assert.assertEquals("Action1", comboLeg.getm_action());

 comboLeg.setm_exchange("Exchange1");
 Assert.assertEquals("Exchange1", comboLeg.getm_exchange());

 comboLeg.setm_openClose(3);
 Assert.assertEquals(3, comboLeg.getm_openClose());

 comboLeg.setm_shortSaleSlot(4);
 Assert.assertEquals(4, comboLeg.getm_shortSaleSlot());

 comboLeg.setm_designatedLocation("DesignatedLocation1");
 Assert.assertEquals("DesignatedLocation1", comboLeg.getm_designatedLocation());
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
 return comboLeg.m_designedLocation;
 }
}