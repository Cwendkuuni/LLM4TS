import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg;

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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 Assert.assertEquals(1, comboLeg1.m_conId);
 Assert.assertEquals(2, comboLeg1.m_ratio);
 Assert.assertEquals("Action1", comboLeg1.m_action);
 Assert.assertEquals("Exchange1", comboLeg1.m_exchange);
 Assert.assertEquals(1, comboLeg1.m_openClose);
 Assert.assertEquals(0, comboLeg1.m_shortSaleSlot);
 Assert.assertNull(comboLeg1.m_designatedLocation);

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
 Assert.assertEquals(1, comboLeg2.m_conId);
 Assert.assertEquals(2, comboLeg2.m_ratio);
 Assert.assertEquals("Action1", comboLeg2.m_action);
 Assert.assertEquals("Exchange1", comboLeg2.m_exchange);
 Assert.assertEquals(1, comboLeg2.m_openClose);
 Assert.assertEquals(3, comboLeg2.m_shortSaleSlot);
 Assert.assertEquals("DesignatedLocation1", comboLeg2.m_designatedLocation);
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", 1);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", 1);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", 1);

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg3.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg4.hashCode());
 }
}