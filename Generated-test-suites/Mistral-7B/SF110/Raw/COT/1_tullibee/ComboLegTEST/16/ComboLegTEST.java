import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.m_conId);
 Assert.assertEquals(0, comboLeg.m_ratio);
 Assert.assertNull(comboLeg.m_action);
 Assert.assertNull(comboLeg.m_exchange);
 Assert.assertEquals(0, comboLeg.m_openClose);
 Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
 Assert.assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testComboLegEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "Location1");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "Location1");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", 1, 1, "Location1");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNullConstructor() {
 new ComboLeg(null, 2, "Action1", "Exchange1", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNullRatio() {
 new ComboLeg(1, 0, "Action1", "Exchange1", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNullAction() {
 new ComboLeg(1, 2, null, "Exchange1", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNullExchange() {
 new ComboLeg(1, 2, "Action1", null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeOpenClose() {
 new ComboLeg(1, 2, "Action1", "Exchange1", -1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeShortSaleSlot() {
 new ComboLeg(1, 2, "Action1", "Exchange1", 1, -1, "Location1");
 }
}