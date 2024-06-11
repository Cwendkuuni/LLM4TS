import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

 @Test
 public void testConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.m_conId);
 Assert.assertEquals(0, comboLeg.m_ratio);
 Assert.assertNull(comboLeg.m_action);
 Assert.assertNull(comboLeg.m_exchange);
 Assert.assertEquals(0, comboLeg.m_openClose);
 Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
 Assert.assertNull(comboLeg.m_designatedLocation);

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 Assert.assertEquals(1, comboLeg1.m_conId);
 Assert.assertEquals(2, comboLeg1.m_ratio);
 Assert.assertEquals("BUY", comboLeg1.m_action);
 Assert.assertEquals("IBKR", comboLeg1.m_exchange);
 Assert.assertEquals(1, comboLeg1.m_openClose);
 Assert.assertEquals(0, comboLeg1.m_shortSaleSlot);
 Assert.assertNull(comboLeg1.m_designatedLocation);

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NY");
 Assert.assertEquals(1, comboLeg2.m_conId);
 Assert.assertEquals(2, comboLeg2.m_ratio);
 Assert.assertEquals("BUY", comboLeg2.m_action);
 Assert.assertEquals("IBKR", comboLeg2.m_exchange);
 Assert.assertEquals(1, comboLeg2.m_openClose);
 Assert.assertEquals(5, comboLeg2.m_shortSaleSlot);
 Assert.assertEquals("NY", comboLeg2.m_designatedLocation);
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NY");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NY");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NJ");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 new ComboLeg(0, 0, null, null, -1, 5, "NY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRatio() {
 new ComboLeg(1, -1, "BUY", "IBKR", 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidShortSaleSlot() {
 new ComboLeg(1, 2, "BUY", "IBKR", 1, -1, "NY");
 }

}