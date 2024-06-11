import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

 @Test
 public void testConstructor() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 5, "Location1");

 Assert.assertEquals(0, comboLeg1.getM_conId());
 Assert.assertEquals(0, comboLeg1.getM_ratio());
 Assert.assertNull(comboLeg1.getM_action());
 Assert.assertNull(comboLeg1.getM_exchange());
 Assert.assertEquals(0, comboLeg1.getM_openClose());
 Assert.assertEquals(0, comboLeg1.getM_shortSaleSlot());
 Assert.assertNull(comboLeg1.getM_designatedLocation());

 Assert.assertEquals(1, comboLeg2.getM_conId());
 Assert.assertEquals(2, comboLeg2.getM_ratio());
 Assert.assertEquals("Action1", comboLeg2.getM_action());
 Assert.assertEquals("Exchange1", comboLeg2.getM_exchange());
 Assert.assertEquals(1, comboLeg2.getM_openClose());
 Assert.assertEquals(0, comboLeg2.getM_shortSaleSlot());
 Assert.assertNull(comboLeg2.getM_designatedLocation());

 Assert.assertEquals(1, comboLeg3.getM_conId());
 Assert.assertEquals(2, comboLeg3.getM_ratio());
 Assert.assertEquals("Action1", comboLeg3.getM_action());
 Assert.assertEquals("Exchange1", comboLeg3.getM_exchange());
 Assert.assertEquals(1, comboLeg3.getM_openClose());
 Assert.assertEquals(5, comboLeg3.getM_shortSaleSlot());
 Assert.assertEquals("Location1", comboLeg3.getM_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 2);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", 2);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 1);

 Assert.assertEquals(hashCode(comboLeg1), hashCode(comboLeg2));
 Assert.neq(hashCode(comboLeg1), hashCode(comboLeg3));
 Assert.neq(hashCode(comboLeg1), hashCode(comboLeg4));
 }

 private int hashCode(ComboLeg comboLeg) {
 return comboLeg.m_conId ^ comboLeg.m_ratio ^ comboLeg.m_openClose ^ comboLeg.m_shortSaleSlot ^ comboLeg.m_action.hashCode() ^ comboLeg.m_exchange.hashCode() ^ comboLeg.m_designatedLocation.hashCode();
 }
}