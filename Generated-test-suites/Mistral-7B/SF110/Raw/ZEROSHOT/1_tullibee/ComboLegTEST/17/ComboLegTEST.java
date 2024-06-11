import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.OPEN, 3, "NY");

 Assert.assertEquals(0, comboLeg1.getm_conId());
 Assert.assertEquals(0, comboLeg1.getm_ratio());
 Assert.assertNull(comboLeg1.getm_action());
 Assert.assertNull(comboLeg1.getm_exchange());
 Assert.assertEquals(0, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());

 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("BUY", comboLeg2.getm_action());
 Assert.assertEquals("ICE_FUTURES", comboLeg2.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg2.getm_openClose());
 Assert.assertEquals(0, comboLeg2.getm_shortSaleSlot());
 Assert.assertNull(comboLeg2.getm_designatedLocation());

 Assert.assertEquals(1, comboLeg3.getm_conId());
 Assert.assertEquals(2, comboLeg3.getm_ratio());
 Assert.assertEquals("BUY", comboLeg3.getm_action());
 Assert.assertEquals("ICE_FUTURES", comboLeg3.getm_exchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg3.getm_openClose());
 Assert.assertEquals(3, comboLeg3.getm_shortSaleSlot());
 Assert.assertEquals("NY", comboLeg3.getm_designatedLocation());
 }

 @Test
 public void testComboLegEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.CLOSE);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullExchange() {
 ComboLeg comboLeg = new ComboLeg(1, 2, null, null, ComboLeg.OPEN);
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullAction() {
 ComboLeg comboLeg = new ComboLeg(1, 2, null, "ICE_FUTURES", ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeRatio() {
 ComboLeg comboLeg = new ComboLeg(1, -2, "BUY", "ICE_FUTURES", ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeShortSaleSlot() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", ComboLeg.OPEN, -3, "NY");
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