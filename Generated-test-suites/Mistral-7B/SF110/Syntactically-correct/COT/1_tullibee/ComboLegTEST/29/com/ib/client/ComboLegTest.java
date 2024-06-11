import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getm_conId());
 Assert.assertEquals(0, comboLeg.getm_ratio());
 Assert.assertNull(comboLeg.getm_action());
 Assert.assertNull(comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "ICE", 1, 3, "NY");
 Assert.assertEquals(1, comboLeg1.getm_conId());
 Assert.assertEquals(2, comboLeg1.getm_ratio());
 Assert.assertEquals("BUY", comboLeg1.getm_action());
 Assert.assertEquals("ICE", comboLeg1.getm_exchange());
 Assert.assertEquals(1, comboLeg1.getm_openClose());
 Assert.assertEquals(3, comboLeg1.getm_shortSaleSlot());
 Assert.assertEquals("NY", comboLeg1.getm_designatedLocation());
 }

 @Test
 @SuppressWarnings("deprecation")
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "ICE", 1, 3, "NY");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "ICE", 1, 3, "NY");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "ICE", 1, 3, "NJ");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidRatio() {
 new ComboLeg(1, -1, "BUY", "ICE", 1, 3, "NY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullAction() {
 new ComboLeg(1, 2, null, "ICE", 1, 3, "NY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullExchange() {
 new ComboLeg(1, 2, "BUY", null, 1, 3, "NY");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullDesignatedLocation() {
 new ComboLeg(1, 2, "BUY", "ICE", 1, 3, null);
 }

 @Test
 public void testToString() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "ICE", 1, 3, "NY");
 String expected = "ComboLeg [conId=1, ratio=2, action=BUY, exchange=ICE, openClose=1, shortSaleSlot=3, designatedLocation=NY]";
 Assert.assertEquals(expected, comboLeg.toString());
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