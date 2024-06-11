import org.junit.Assert;
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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "EXCH1", 1);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("Action1", comboLeg1.getm_action());
 Assert.assertEquals("EXCH1", comboLeg1.getm_exchange());
 Assert.assertEquals(1, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "EXCH1", 1, 0, null);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "EXCH1", 1, 0, null);
 ComboLeg comboLeg3 = new ComboLeg(2, 2, "Action1", "EXCH1", 1, 0, null);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCH1", 1, 0, null);

 comboLeg.setm_conId(2);
 comboLeg.setm_ratio(3);
 comboLeg.setm_action("Action2");
 comboLeg.setm_exchange("EXCH2");
 comboLeg.setm_openClose(2);
 comboLeg.setm_shortSaleSlot(1);
 comboLeg.setm_designatedLocation("LOC1");

 Assert.assertEquals(2, comboLeg.getm_conId());
 Assert.assertEquals(3, comboLeg.getm_ratio());
 Assert.assertEquals("Action2", comboLeg.getm_action());
 Assert.assertEquals("EXCH2", comboLeg.getm_exchange());
 Assert.assertEquals(2, comboLeg.getm_openClose());
 Assert.assertEquals(1, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("LOC1", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testToString() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCH1", 1, 0, null);

 String str = comboLeg.toString();
 String expected = "ComboLeg{m_conId=1, m_ratio=2, m_action='Action1', m_exchange='EXCH1', m_openClose=1, m_shortSaleSlot=0, m_designatedLocation=null}";
 Assert.assertEquals(expected, str);
 }

}