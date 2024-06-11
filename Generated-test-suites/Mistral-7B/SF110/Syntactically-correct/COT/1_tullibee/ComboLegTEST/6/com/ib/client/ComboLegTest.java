import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

 private ComboLeg createComboLeg(int p_conId, int p_ratio, String p_action, String p_exchange, int p_openClose, int p_shortSaleSlot, String p_designatedLocation) {
 return new ComboLeg(p_conId, p_ratio, p_action, p_exchange, p_openClose, p_shortSaleSlot, p_designatedLocation);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.m_conId);
 Assert.assertEquals(0, comboLeg.m_ratio);
 Assert.assertNull(comboLeg.m_action);
 Assert.assertNull(comboLeg.m_exchange);
 Assert.assertEquals(0, comboLeg.m_openClose);
 Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
 Assert.assertNull(comboLeg.m_designatedLocation);

 // Test parameterized constructor
 ComboLeg comboLeg = createComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DL1");
 Assert.assertEquals(1, comboLeg.m_conId);
 Assert.assertEquals(2, comboLeg.m_ratio);
 Assert.assertEquals("Action1", comboLeg.m_action);
 Assert.assertEquals("Exchange1", comboLeg.m_exchange);
 Assert.assertEquals(1, comboLeg.m_openClose);
 Assert.assertEquals(1, comboLeg.m_shortSaleSlot);
 Assert.assertEquals("DL1", comboLeg.m_designatedLocation);
 }

 @Test
 @SuppressWarnings("deprecation")
 public void testEquals() {
 ComboLeg comboLeg1 = createComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DL1");
 ComboLeg comboLeg2 = createComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DL1");
 ComboLeg comboLeg3 = createComboLeg(1, 2, "Action1", "Exchange1", 1, 2, "DL1");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(null));
 Assert.assertFalse(comboLeg1.equals(new Object()));
 Assert.assertFalse(comboLeg1.equals(new ComboLeg()));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidRatio() {
 ComboLeg comboLeg = null;
 try {
 comboLeg = new ComboLeg(1, -1, null, null, 0, 0, null);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 Assert.assertNotNull(comboLeg);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullAction() {
 ComboLeg comboLeg = null;
 try {
 comboLeg = new ComboLeg(1, 2, null, null, 0, 0, null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 Assert.assertNotNull(comboLeg);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullExchange() {
 ComboLeg comboLeg = null;
 try {
 comboLeg = new ComboLeg(1, 2, null, null, 0, 0, null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 Assert.assertNotNull(comboLeg);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullDesignatedLocation() {
 ComboLeg comboLeg = null;
 try {
 comboLeg = new ComboLeg(1, 2, null, null, 0, 0, null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 Assert.assertNotNull(comboLeg);
 }
}