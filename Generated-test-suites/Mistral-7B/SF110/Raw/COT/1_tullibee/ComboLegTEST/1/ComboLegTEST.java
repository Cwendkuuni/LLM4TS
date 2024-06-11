import org.junit.Test;
import static org.junit.Assert.*;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg defaultComboLeg;
 private ComboLeg sameComboLeg;
 private ComboLeg differentComboLeg;

 @Test
 public void testConstructorWithDefaultValues() {
 defaultComboLeg = new ComboLeg();
 assertEquals(0, defaultComboLeg.getm_conId());
 assertEquals(0, defaultComboLeg.getm_ratio());
 assertNull(defaultComboLeg.getm_action());
 assertNull(defaultComboLeg.getm_exchange());
 assertEquals(0, defaultComboLeg.getm_openClose());
 assertEquals(0, defaultComboLeg.getm_shortSaleSlot());
 assertNull(defaultComboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithCustomValues() {
 int conId = 1;
 int ratio = 2;
 String action = "BUY";
 String exchange = "SMART";
 int openClose = ComboLeg.OPEN;
 int shortSaleSlot = 5;
 String designatedLocation = "NY";
 sameComboLeg = new ComboLeg(conId, ratio, action, exchange, openClose);
 differentComboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

 assertEquals(conId, sameComboLeg.getm_conId());
 assertEquals(ratio, sameComboLeg.getm_ratio());
 assertEquals(action, sameComboLeg.getm_action());
 assertEquals(exchange, sameComboLeg.getm_exchange());
 assertEquals(openClose, sameComboLeg.getm_openClose());
 assertEquals(shortSaleSlot, sameComboLeg.getm_shortSaleSlot());
 assertNull(sameComboLeg.getm_designatedLocation());

 assertEquals(conId, differentComboLeg.getm_conId());
 assertEquals(ratio, differentComboLeg.getm_ratio());
 assertEquals(action, differentComboLeg.getm_action());
 assertEquals(exchange, differentComboLeg.getm_exchange());
 assertEquals(openClose, differentComboLeg.getm_openClose());
 assertEquals(shortSaleSlot, differentComboLeg.getm_shortSaleSlot());
 assertEquals(designatedLocation, differentComboLeg.getm_designatedLocation());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullExchange() {
 String nullExchange = null;
 assertNull(new ComboLeg(1, 2, "BUY", nullExchange, ComboLeg.OPEN));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeRatio() {
 int negativeRatio = -1;
 assertThrows(IllegalArgumentException.class, () -> new ComboLeg(1, negativeRatio, "BUY", "SMART", ComboLeg.OPEN));
 }

 @Test
 public void testEqualsMethod() {
 ComboLeg firstComboLeg = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg secondComboLeg = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg thirdComboLeg = new ComboLeg(1, 3, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg fourthComboLeg = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.CLOSE);

 assertTrue(firstComboLeg.equals(secondComboLeg));
 assertFalse(firstComboLeg.equals(thirdComboLeg));
 assertFalse(firstComboLeg.equals(fourthComboLeg));
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