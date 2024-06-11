import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;
import com.ib.client.Util;

public class ComboLegTest {

 @Test
 public void testConstructorWithAllParameters() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 5, "NYSE");
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("BUY", comboLeg.getm_action());
 Assert.assertEquals("IBKR", comboLeg.getm_exchange());
 Assert.assertEquals(1, comboLeg.getm_openClose());
 Assert.assertEquals(5, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("NYSE", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithNoParameters() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getm_conId());
 Assert.assertEquals(0, comboLeg.getm_ratio());
 Assert.assertNull(comboLeg.getm_action());
 Assert.assertNull(comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertNull(comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithInvalidParameters() {
 try {
 new ComboLeg(-1, 2, "BUY", "IBKR", 1);
 Assert.fail("Expected IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new ComboLeg(1, -1, "BUY", "IBKR", 1);
 Assert.fail("Expected IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 2, null, null, 1);
 Assert.fail("Expected NullPointerException but got no exception");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 2, "BUY", null, 1);
 Assert.fail("Expected NullPointerException but got no exception");
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 new ComboLeg(1, 2, "BUY", "IBKR", -1);
 Assert.fail("Expected IllegalArgumentException but got no exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 2);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 2);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 1);

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg3.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg4.hashCode());
 }
}