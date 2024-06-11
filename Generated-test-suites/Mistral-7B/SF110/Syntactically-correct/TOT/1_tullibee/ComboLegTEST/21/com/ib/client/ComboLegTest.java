import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test
 public void testConstructorWithAllArguments() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", 0, 1, "LOCATION1");
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("ACTION1", comboLeg.getm_action());
 Assert.assertEquals("EXCHANGE1", comboLeg.getm_exchange());
 Assert.assertEquals(0, comboLeg.getm_openClose());
 Assert.assertEquals(1, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("LOCATION1", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", 0, 1, "LOCATION1");
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", 0, 1, "LOCATION1");
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE2", 0, 1, "LOCATION1");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test
 public void testNullObject() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", 0, 1, "LOCATION1");
 Assert.assertFalse(comboLeg.equals(null));
 }

 @Test
 public void testEmptyString() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION1", "", 0, 1, "LOCATION1");
 String emptyString = "";
 Assert.assertEquals(0, Util.StringCompareIgnCase(comboLeg.getm_action(), emptyString));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeRatio() {
 ComboLeg comboLeg = new ComboLeg(1, -1, "ACTION1", "EXCHANGE1", 0, 1, "LOCATION1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeShortSaleSlot() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", 0, -1, "LOCATION1");
 }
}