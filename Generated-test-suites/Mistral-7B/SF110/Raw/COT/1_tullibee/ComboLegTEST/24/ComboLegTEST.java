import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int conId, int ratio, String action, String exchange, int openClose, int shortSaleSlot, String designatedLocation) {
 return new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 }

 @Test
 public void testConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 assertNull(comboLeg.m_action);
 assertNull(comboLeg.m_exchange);
 assertEquals(0, comboLeg.m_conId);
 assertEquals(0, comboLeg.m_ratio);
 assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
 assertNull(comboLeg.m_shortSaleSlot);
 assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testConstructorWithValues() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "IBKR";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 3;
 String designatedLocation = "NYSE";
 ComboLeg comboLeg = createComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
 assertEquals(conId, comboLeg.m_conId);
 assertEquals(ratio, comboLeg.m_ratio);
 assertEquals(action, comboLeg.m_action);
 assertEquals(exchange, comboLeg.m_exchange);
 assertEquals(openClose, comboLeg.m_openClose);
 assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
 assertEquals(designatedLocation, comboLeg.m_designatedLocation);
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = createComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");
 ComboLeg comboLeg2 = createComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");
 ComboLeg comboLeg3 = createComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN, 2, "NYSE");
 ComboLeg comboLeg4 = createComboLeg(2, 1, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");

 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test(expected = NullPointerException.class)
 public void testNullAction() {
 ComboLeg comboLeg = new ComboLeg(1, 1, null, "IBKR", ComboLeg.OPEN);
 }

 @Test(expected = NullPointerException.class)
 public void testNullExchange() {
 ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", null, ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeRatio() {
 ComboLeg comboLeg = new ComboLeg(1, -1, "BUY", "IBKR", ComboLeg.OPEN);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOpenCloseValue() {
 ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", "IBKR", 4, 1, "NYSE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testShortSaleSlotLessThanZero() {
 ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", "IBKR", ComboLeg.OPEN, -1, "NYSE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDesignatedLocationNull() {
 ComboLeg comboLeg = new ComboLeg(1, 1, "BUY", null, ComboLeg.OPEN);
 }

}