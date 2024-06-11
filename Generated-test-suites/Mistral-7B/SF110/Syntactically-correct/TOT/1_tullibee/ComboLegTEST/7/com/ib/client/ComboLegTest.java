import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;
import static com.ib.client.ComboLeg.SAME;
import static com.ib.client.ComboLeg.OPEN;
import static com.ib.client.ComboLeg.CLOSE;
import static com.ib.client.ComboLeg.UNKNOWN;

public class ComboLegTest {

 @Test
 public void testConstructorWithDefaultValues() {
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
 public void testConstructorWithValidValues() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("Action1", comboLeg.getm_action());
 Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
 Assert.assertEquals(OPEN, comboLeg.getm_openClose());
 Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
 Assert.assertNull(comboLeg.getm_designatedLocation());
 }

 @Test
 public void testConstructorWithAllValues() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN, 3, "Location1");
 Assert.assertEquals(1, comboLeg.getm_conId());
 Assert.assertEquals(2, comboLeg.getm_ratio());
 Assert.assertEquals("Action1", comboLeg.getm_action());
 Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
 Assert.assertEquals(OPEN, comboLeg.getm_openClose());
 Assert.assertEquals(3, comboLeg.getm_shortSaleSlot());
 Assert.assertEquals("Location1", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEqualsWithSameObject() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 ComboLeg comboLeg2 = comboLeg1;
 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsWithDifferentObjectsSameValues() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsWithDifferentObjectsDifferentValues() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 3, "Action2", "Exchange2", CLOSE);
 Assert.assertFalse(comboLeg1.equals(comboLeg2));
 }

 @Test
 public void testEqualsWithNullObject() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 Assert.assertFalse(comboLeg.equals(null));
 }

 @Test
 public void testHashCodeWithSameObjects() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentObjects() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 3, "Action2", "Exchange2", CLOSE);
 Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 }

}