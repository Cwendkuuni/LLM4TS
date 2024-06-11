import org.junit.Assert;import org.junit.Test;import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test
 public void testConstructorDefault() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getm_conId());
 Assert.assertEquals(0, comboLeg.getm_ratio());
 Assert.assertNull(comboLeg.getm_action());
 Assert.assertNull(comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithArguments() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IDEALPRO";
 int openClose = ComboLeg.OPEN;
 ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
 Assert.assertEquals(conId, comboLeg.getm_conId());
 Assert.assertEquals(ratio, comboLeg.getm_ratio());
 Assert.assertEquals(action, comboLeg.getm_action());
 Assert.assertEquals(exchange, comboLeg.getm_exchange());
 Assert.assertEquals(openClose, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithAllArguments() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IDEALPRO";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 3;
 String designatedLocation = "NY";
 ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertEquals(conId, comboLeg.getm_conId());
 Assert.assertEquals(ratio, comboLeg.getm_ratio());
 Assert.assertEquals(action, comboLeg.getm_action());
 Assert.assertEquals(exchange, comboLeg.getm_exchange());
 Assert.assertEquals(openClose, comboLeg.getm_openClose());
 Assert.assertEquals(shortSaleSlot, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals(designatedLocation, comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IDEALPRO", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IDEALPRO", ComboLeg.OPEN);
 Assert.assertTrue(comboLeg1.equals(comboLeg2));

 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IDEALPRO", ComboLeg.OPEN);
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
 Assert.assertFalse(comboLeg3.equals(comboLeg4));

 ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IDEALPRO", ComboLeg.OPEN);
 ComboLeg comboLeg6 = new ComboLeg(2, 2, "BUY", "IDEALPRO", ComboLeg.OPEN);
 Assert.assertFalse(comboLeg5.equals(comboLeg6));
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