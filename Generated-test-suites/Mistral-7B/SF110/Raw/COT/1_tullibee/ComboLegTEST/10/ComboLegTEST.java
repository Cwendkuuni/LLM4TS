import org.junit.Assert;
import org.junit.Test;

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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 Assert.assertEquals(1, comboLeg1.m_conId);
 Assert.assertEquals(2, comboLeg1.m_ratio);
 Assert.assertEquals("Action1", comboLeg1.m_action);
 Assert.assertEquals("Exchange1", comboLeg1.m_exchange);
 Assert.assertEquals(1, comboLeg1.m_openClose);
 Assert.assertEquals(0, comboLeg1.m_shortSaleSlot);
 Assert.assertNull(comboLeg1.m_designatedLocation);
 }

 @Test
 public void testComboLegEquals() {
 ComboLeg comboLeg1 = createComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
 ComboLeg comboLeg2 = createComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));

 comboLeg2.m_conId = 3;
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.m_ratio = 3;
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.m_openClose = 3;
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.m_shortSaleSlot = 3;
 Assert.assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.m_designatedLocation = "DesignatedLocation2";
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullConstructor() {
 new ComboLeg(null, 2, null, null, 1);
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullConId() {
 new ComboLeg(0, null, "Action1", "Exchange1", 1);
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullRatio() {
 new ComboLeg(1, null, "Action1", "Exchange1", 1);
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullAction() {
 new ComboLeg(1, 2, null, "Exchange1", 1);
 }

 @Test(expected = NullPointerException.class)
 public void testComboLegNullExchange() {
 new ComboLeg(1, 2, "Action1", null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeOpenClose() {
 new ComboLeg(1, 2, "Action1", "Exchange1", -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegNegativeShortSaleSlot() {
 new ComboLeg(1, 2, "Action1", "Exchange1", 1, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComboLegEmptyDesignatedLocation() {
 new ComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "");
 }
}