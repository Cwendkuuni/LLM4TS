import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

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

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("action1", comboLeg1.getm_action());
 Assert.assertEquals("exchange1", comboLeg1.getm_exchange());
 Assert.assertEquals(1, comboLeg1.getm_openClose());
 Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
 Assert.assertNull(comboLeg1.getm_designatedLocation());

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1, 3, "location1");
 Assert.assertEquals(1, comboLeg2.getm_conId());
 Assert.assertEquals(2, comboLeg2.getm_ratio());
 Assert.assertEquals("action1", comboLeg2.getm_action());
 Assert.assertEquals("exchange1", comboLeg2.getm_exchange());
 Assert.assertEquals(1, comboLeg2.getm_openClose());
 Assert.assertEquals(3, comboLeg2.getm_shortSaleSlot());
 Assert.assertEquals("location1", comboLeg2.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "action2", "exchange2", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test
 public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();
 comboLeg.setm_conId(1);
 comboLeg.setm_ratio(2);
 comboLeg.setm_action("action1");
 comboLeg.setm_exchange("exchange1");
 comboLeg.setm_openClose(1);
 comboLeg.setm_shortSaleSlot(3);
 comboLeg.setm_designatedLocation("location1");

 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("action1", comboLeg.getm_action());
 Assert.assertEquals("exchange1", comboLeg.getm_exchange());
 Assert.assertEquals(1, comboLeg.getm_openClose());
 Assert.assertEquals(3, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("location1", comboLeg.getm_designatedLocation());
 }

 private int getm_conId(ComboLeg obj) {
 return obj.m_conId;
 }

 private int getm_ratio(ComboLeg obj) {
 return obj.m_ratio;
 }

 private String getm_action(ComboLeg obj) {
 return obj.m_action;
 }

 private String getm_exchange(ComboLeg obj) {
 return obj.m_exchange;
 }

 private int getm_openClose(ComboLeg obj) {
 return obj.m_openClose;
 }

 private int getm_shortSaleSlot(ComboLeg obj) {
 return obj.m_shortSaleSlot;
 }

 private String getm_designatedLocation(ComboLeg obj) {
 return obj.m_designatedLocation;
 }

}