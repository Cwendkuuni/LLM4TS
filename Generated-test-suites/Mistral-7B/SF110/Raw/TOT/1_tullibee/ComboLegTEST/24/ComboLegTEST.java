import org.junit.Assert; import org.junit.Test; import com.ib.client.ComboLeg; import static org.junit.Assert.assertEquals; import static org.junit.Assert.assertNotEquals; import static org.junit.Assert.assertNull; import static org.junit.Assert.assertTrue;

public class ComboLegTest {

 @Test
 public void testDefaultConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 assertEquals(0, comboLeg.m_conId);
 assertEquals(0, comboLeg.m_ratio);
 assertNull(comboLeg.m_action);
 assertNull(comboLeg.m_exchange);
 assertEquals(0, comboLeg.m_openClose);
 assertEquals(0, comboLeg.m_shortSaleSlot);
 assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testConstructorWithValidArguments() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", 1);
 assertEquals(1, comboLeg.m_conId);
 assertEquals(2, comboLeg.m_ratio);
 assertEquals("BUY", comboLeg.m_action);
 assertEquals("ICE_FUTURES", comboLeg.m_exchange);
 assertEquals(1, comboLeg.m_openClose);
 assertEquals(0, comboLeg.m_shortSaleSlot);
 assertNull(comboLeg.m_designatedLocation);
 }

 @Test
 public void testEqualsMethod() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", 2);
 ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "ICE_FUTURES", 1);

 assertTrue(comboLeg1.equals(comboLeg2));
 assertFalse(comboLeg1.equals(comboLeg3));
 assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testToStringMethod() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "ICE_FUTURES", 1);
 String expected = "ComboLeg{m_conId=1, m_ratio=2, m_action='BUY', m_exchange='ICE_FUTURES', m_openClose=1, m_shortSaleSlot=0, m_designatedLocation=null}";
 assertEquals(expected, comboLeg.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringMethodWithNullAction() {
 ComboLeg comboLeg = new ComboLeg(1, 2, null, "ICE_FUTURES", 1);
 // Test throws an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringMethodWithNullExchange() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", null, 1);
 // Test throws an IllegalArgumentException
 }
}