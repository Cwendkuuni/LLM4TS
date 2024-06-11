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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("Action1", comboLeg1.getm_action());
 Assert.assertEquals("Exchange1", comboLeg1.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 3, "DesignatedLocation1");
 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("Action1", comboLeg2.getm_action());
 Assert.assertEquals("Exchange1", comboLeg2.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg2.getm_openClose());
 Assert.assertEquals(3, comboLeg2.getm_shortSaleSlot());
 Assert.assertEquals("DesignatedLocation1", comboLeg2.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);

 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("Action1", comboLeg.getm_action());
 Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());

 comboLeg.setm_conId(3);
 comboLeg.setm_ratio(4);
 comboLeg.setm_action("Action2");
 comboLeg.setm_exchange("Exchange2");
 comboLeg.setm_openClose(ComboLeg.CLOSE);
 comboLeg.setm_shortSaleSlot(5);
 comboLeg.setm_designatedLocation("DesignatedLocation2");

 Assert.assertEquals(3, comboLeg.getm_conId());
 Assert.assertEquals(4, comboLeg.getm_ratio());
 Assert.assertEquals("Action2", comboLeg.getm_action());
 Assert.assertEquals("Exchange2", comboLeg.getm_exchange());
 Assert.assertEquals(ComboLeg.CLOSE, comboLeg.getm_openClose());
 Assert.assertEquals(5, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("DesignatedLocation2", comboLeg.getm_designatedLocation());
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

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
 Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg4.hashCode());
 }
}