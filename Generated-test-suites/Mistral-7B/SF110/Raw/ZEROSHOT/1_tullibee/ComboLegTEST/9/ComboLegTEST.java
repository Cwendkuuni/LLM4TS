import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class ComboLegTest {

 @Test
 public void testComboLegConstructor() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 0);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 0, 5, "NYSE");

 assertEquals(0, comboLeg1.getm_conId());
 assertEquals(0, comboLeg1.getm_ratio());
 assertNull(comboLeg1.getm_action());
 assertNull(comboLeg1.getm_exchange());
 assertEquals(0, comboLeg1.getm_openClose());
 assertEquals(0, comboLeg1.getm_shortSaleSlot());
 assertNull(comboLeg1.getm_designatedLocation());

 assertEquals(1, comboLeg2.getm_conId());
 assertEquals(2, comboLeg2.getm_ratio());
 assertEquals("BUY", comboLeg2.getm_action());
 assertEquals("IBKR", comboLeg2.getm_exchange());
 assertEquals(0, comboLeg2.getm_openClose());
 assertEquals(0, comboLeg2.getm_shortSaleSlot());
 assertNull(comboLeg2.getm_designatedLocation());

 assertEquals(1, comboLeg3.getm_conId());
 assertEquals(2, comboLeg3.getm_ratio());
 assertEquals("BUY", comboLeg3.getm_action());
 assertEquals("IBKR", comboLeg3.getm_exchange());
 assertEquals(0, comboLeg3.getm_openClose());
 assertEquals(5, comboLeg3.getm_shortSaleSlot());
 assertEquals("NYSE", comboLeg3.getm_designatedLocation());
 }

 @Test
 public void testComboLegEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 0);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 0);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 0);

 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testComboLegGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 0);

 assertEquals(1, comboLeg.getm_conId());
 assertEquals(2, comboLeg.getm_ratio());
 assertEquals("BUY", comboLeg.getm_action());
 assertEquals("IBKR", comboLeg.getm_exchange());
 assertEquals(0, comboLeg.getm_openClose());
 assertEquals(0, comboLeg.getm_shortSaleSlot());
 assertNull(comboLeg.getm_designatedLocation());

 comboLeg.setm_conId(3);
 comboLeg.setm_ratio(3);
 comboLeg.setm_action("SELL");
 comboLeg.setm_exchange("NYSE");
 comboLeg.setm_openClose(1);
 comboLeg.setm_shortSaleSlot(5);
 comboLeg.setm_designatedLocation("CHI");

 assertEquals(3, comboLeg.getm_conId());
 assertEquals(3, comboLeg.getm_ratio());
 assertEquals("SELL", comboLeg.getm_action());
 assertEquals("NYSE", comboLeg.getm_exchange());
 assertEquals(1, comboLeg.getm_openClose());
 assertEquals(5, comboLeg.getm_shortSaleSlot());
 assertEquals("CHI", comboLeg.getm_designatedLocation());
 }

 private int getm_conId() {
 return m_conId;
 }

 private void setm_conId(int p_conId) {
 m_conId = p_conId;
 }

 private int getm_ratio() {
 return m_ratio;
 }

 private void setm_ratio(int p_ratio) {
 m_ratio = p_ratio;
 }

 private String getm_action() {
 return m_action;
 }

 private void setm_action(String p_action) {
 m_action = p_action;
 }

 private String getm_exchange() {
 return m_exchange;
 }

 private void setm_exchange(String p_exchange) {
 m_exchange = p_exchange;
 }

 private int getm_openClose() {
 return m_openClose;
 }

 private void setm_openClose(int p_openClose) {
 m_openClose = p_openClose;
 }

 private int getm_shortSaleSlot() {
 return m_shortSaleSlot;
 }

 private void setm_shortSaleSlot(int p_shortSaleSlot) {
 m_shortSaleSlot = p_shortSaleSlot;
 }

 private String getm_designatedLocation() {
 return m_designatedLocation;
 }

 private void setm_designatedLocation(String p_designatedLocation) {
 m_designatedLocation = p_designatedLocation;
 }
}