import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose, int p_shortSaleSlot, String p_designatedLocation) {
 return new ComboLeg(p_conId, p_ratio, p_action, p_exchange, p_openClose, p_shortSaleSlot, p_designatedLocation);
 }

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
 }

 @Test
 public void testConstructorWithParams() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg = createComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertEquals(conId, comboLeg.m_conId);
 Assert.assertEquals(ratio, comboLeg.m_ratio);
 Assert.assertEquals(action, comboLeg.m_action);
 Assert.assertEquals(exchange, comboLeg.m_exchange);
 Assert.assertEquals(openClose, comboLeg.m_openClose);
 Assert.assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
 Assert.assertEquals(designatedLocation, comboLeg.m_designatedLocation);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeRatio() {
 String action = "SELL";
 String exchange = "IBKR";
 int conId = 1;
 int ratio = -1;
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 try {
 ComboLeg comboLeg = createComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 } catch (IllegalArgumentException e) {
 // expected
 Assert.assertEquals("Ratio cannot be negative", e.getMessage());
 }
 }

 @Test
 public void testEquals() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsDifferentConId() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 ComboLeg comboLeg2 = new ComboLeg(conId + 1, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsDifferentRatio() {
 int conId = 1;
 int ratio1 = 2;
 int ratio2 = 3;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio1, action, exchange, openClose, shortSaleSlot, designatedLocation);
 ComboLeg comboLeg2 = new ComboLeg(conId, ratio2, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsDifferentOpenClose() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose1 = ComboLeg.OPEN;
 int openClose2 = ComboLeg.CLOSE;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose1, shortSaleSlot, designatedLocation);
 ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose2, shortSaleSlot, designatedLocation);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsDifferentShortSaleSlot() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot1 = 10;
 int shortSaleSlot2 = 11;
 String designatedLocation = "NY";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot1, designatedLocation);
 ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot2, designatedLocation);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsDifferentDesignatedLocation() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation1 = "NY";
 String designatedLocation2 = "NJ";
 ComboLeg comboLeg1 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation1);
 ComboLeg comboLeg2 = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation2);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsSameObject() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 10;
 String designatedLocation = "NY";
 ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 Assert.assertTrue(comboLeg.equals(comboLeg));
 }
}