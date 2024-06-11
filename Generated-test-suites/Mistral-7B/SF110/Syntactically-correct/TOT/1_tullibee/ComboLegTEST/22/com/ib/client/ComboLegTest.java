import org.junit.Test;import static org.junit.Assert.*;import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test
 public void testConstructorWithAllArguments() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 assertEquals(1, comboLeg.getm_conId());
 assertEquals(2, comboLeg.getm_ratio());
 assertEquals("BUY", comboLeg.getm_action());
 assertEquals("IBKR", comboLeg.getm_exchange());
 assertEquals(1, comboLeg.getm_openClose());
 assertEquals(3, comboLeg.getm_shortSaleSlot());
 assertEquals("NYSE", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithNoArguments() {
 ComboLeg comboLeg = new ComboLeg();
 assertEquals(0, comboLeg.getm_conId());
 assertEquals(0, comboLeg.getm_ratio());
 assertEquals(null, comboLeg.getm_action());
 assertEquals(null, comboLeg.getm_exchange());
 assertEquals(0, comboLeg.getm_openClose());
 assertEquals(0, comboLeg.getm_shortSaleSlot());
 assertEquals(null, comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEqualsWithSameObject() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 ComboLeg comboLeg2 = comboLeg1;
 assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsWithDifferentObject() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NASDAQ");
 assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsWithNull() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 assertFalse(comboLeg.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 String wrongClass = new String("TestString");
 assertFalse(comboLeg.equals(wrongClass));
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 int hashCode = comboLeg.hashCode();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 int hashCode2 = comboLeg2.hashCode();
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testSettersAndGetters() {
 ComboLeg comboLeg = new ComboLeg();
 comboLeg.setm_conId(1);
 comboLeg.setm_ratio(2);
 comboLeg.setm_action("BUY");
 comboLeg.setm_exchange("IBKR");
 comboLeg.setm_openClose(1);
 comboLeg.setm_shortSaleSlot(3);
 comboLeg.setm_designatedLocation("NYSE");
 assertEquals(1, comboLeg.getm_conId());
 assertEquals(2, comboLeg.getm_ratio());
 assertEquals("BUY", comboLeg.getm_action());
 assertEquals("IBKR", comboLeg.getm_exchange());
 assertEquals(1, comboLeg.getm_openClose());
 assertEquals(3, comboLeg.getm_shortSaleSlot());
 assertEquals("NYSE", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testToString() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NYSE");
 String toString = comboLeg.toString();
 String expectedToString = "ComboLeg{m_conId=1, m_ratio=2, m_action='BUY', m_exchange='IBKR', m_openClose=1, m_shortSaleSlot=3, m_designatedLocation='NYSE'}";
 assertEquals(expectedToString, toString);
 }
}