import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg comboLeg1;
 private ComboLeg comboLeg2;

 @Before
 public void setup() {
 comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 }

 @Test
 public void testConstructor() {
 ComboLeg comboLeg3 = new ComboLeg();
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NY");

 assertEquals(0, comboLeg3.getm_conId());
 assertEquals(0, comboLeg3.getm_ratio());
 assertNull(comboLeg3.getm_action());
 assertNull(comboLeg3.getm_exchange());
 assertEquals(0, comboLeg3.getm_openClose());
 assertEquals(0, comboLeg3.getm_shortSaleSlot());
 assertNull(comboLeg3.getm_designatedLocation());

 assertEquals(1, comboLeg4.getm_conId());
 assertEquals(2, comboLeg4.getm_ratio());
 assertEquals("BUY", comboLeg4.getm_action());
 assertEquals("IBKR", comboLeg4.getm_exchange());
 assertEquals(1, comboLeg4.getm_openClose());
 assertEquals(0, comboLeg4.getm_shortSaleSlot());
 assertNull(comboLeg4.getm_designatedLocation());

 assertEquals(1, comboLeg5.getm_conId());
 assertEquals(2, comboLeg5.getm_ratio());
 assertEquals("BUY", comboLeg5.getm_action());
 assertEquals("IBKR", comboLeg5.getm_exchange());
 assertEquals(1, comboLeg5.getm_openClose());
 assertEquals(5, comboLeg5.getm_shortSaleSlot());
 assertNull(comboLeg5.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(new Object()));
 assertFalse(comboLeg1.equals(null));

 comboLeg2.setm_action("SELL");
 assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_action("BUY");
 comboLeg2.setm_exchange("IBKR-TEST");
 assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_exchange("IBKR");
 comboLeg2.setm_designatedLocation("CHI");
 assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_designatedLocation(null);
 assertFalse(comboLeg1.equals(comboLeg2));

 comboLeg2.setm_designatedLocation("NY");
 assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidOpenClose() {
 new ComboLeg(1, 2, "BUY", "IBKR", -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidShortSaleSlot() {
 new ComboLeg(1, 2, "BUY", "IBKR", 6);
 }
}