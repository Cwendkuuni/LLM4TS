import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");

 assertEquals(0, comboLeg1.getm_conId());
 assertEquals(0, comboLeg1.getm_ratio());
 assertNull(comboLeg1.getm_action());
 assertNull(comboLeg1.getm_exchange());
 assertEquals(0, comboLeg1.getm_openClose());
 assertEquals(0, comboLeg1.getm_shortSaleSlot());
 assertNull(comboLeg1.getm_designatedLocation());

 assertEquals(1, comboLeg2.getm_conId());
 assertEquals(2, comboLeg2.getm_ratio());
 assertEquals("Action1", comboLeg2.getm_action());
 assertEquals("Exchange1", comboLeg2.getm_exchange());
 assertEquals(1, comboLeg2.getm_openClose());
 assertEquals(0, comboLeg2.getm_shortSaleSlot());
 assertNull(comboLeg2.getm_designatedLocation());

 assertEquals(1, comboLeg3.getm_conId());
 assertEquals(2, comboLeg3.getm_ratio());
 assertEquals("Action1", comboLeg3.getm_action());
 assertEquals("Exchange1", comboLeg3.getm_exchange());
 assertEquals(1, comboLeg3.getm_openClose());
 assertEquals(3, comboLeg3.getm_shortSaleSlot());
 assertEquals("DesignatedLocation1", comboLeg3.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange2", 1);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 1);

 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 new ComboLeg(0, 0, null, null, -1);
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