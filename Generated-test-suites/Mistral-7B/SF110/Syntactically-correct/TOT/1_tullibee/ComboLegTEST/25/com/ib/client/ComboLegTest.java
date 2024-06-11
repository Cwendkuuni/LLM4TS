import org.junit.Before;import org.junit.Test;import static org.junit.Assert.assertEquals;import static org.junit.Assert.assertFalse;import static org.junit.Assert.assertNotNull;import static org.junit.Assert.assertNull;import static org.junit.Assert.assertSame;import static org.junit.Assert.assertTrue;

import com.ib.client.ComboLeg;import com.ib.client.Util;

public class ComboLegTest {

 private ComboLeg comboLeg1;
 private ComboLeg comboLeg2;
 private ComboLeg comboLeg3;

 @Before
 public void setUp() {
 comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
 comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");
 comboLeg3 = new ComboLeg(1, 2, null, null, ComboLeg.UNKNOWN);
 }

 @Test
 public void testConstructorWithAllArguments() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");

 assertEquals(1, comboLeg.getm_conId());
 assertEquals(2, comboLeg.getm_ratio());
 assertEquals("BUY", comboLeg.getm_action());
 assertEquals("IBKR", comboLeg.getm_exchange());
 assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
 assertEquals(1, comboLeg.getm_shortSaleSlot());
 assertEquals("NY", comboLeg.getm_designatedLocation());
 }

 @Test
 public void testEqualsWithSameInstance() {
 assertTrue(comboLeg1.equals(comboLeg1));
 }

 @Test
 public void testEqualsWithNull() {
 assertFalse(comboLeg1.equals(null));
 }

 @Test
 public void testEqualsWithDifferentInstanceButSameValues() {
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");

 assertTrue(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testEqualsWithDifferentInstanceButDifferentValues() {
 ComboLeg comboLeg5 = new ComboLeg(2, 2, "SELL", "IBKR", ComboLeg.CLOSE, 2, "CHI");

 assertFalse(comboLeg1.equals(comboLeg5));
 }

 @Test
 public void testEqualsWithSameValuesButDifferentInstanceAndDifferentClass() {
 Object otherObject = new Object() {
 @Override
 public int getm_conId() {
 return 1;
 }

 @Override
 public int getm_ratio() {
 return 2;
 }

 @Override
 public String getm_action() {
 return "BUY";
 }

 @Override
 public String getm_exchange() {
 return "IBKR";
 }

 @Override
 public int getm_openClose() {
 return ComboLeg.OPEN;
 }

 @Override
 public int getm_shortSaleSlot() {
 return 1;
 }

 @Override
 public String getm_designatedLocation() {
 return "NY";
 }
 };

 assertFalse(comboLeg1.equals(otherObject));
 }

 @Test
 public void testEqualsWithDifferentValues() {
 ComboLeg comboLeg6 = new ComboLeg(1, 1, "SELL", "IBKR", ComboLeg.CLOSE, 1, "CHI");

 assertFalse(comboLeg1.equals(comboLeg6));
 }

 @Test
 public void testHashCode() {
 int hashCode1 = comboLeg1.hashCode();
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NY");
 int hashCode2 = comboLeg4.hashCode();

 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 assertEquals("ComboLeg{conId=1, ratio=2, action='BUY', exchange='IBKR', openClose=" + ComboLeg.OPEN + ", shortSaleSlot=0, designatedLocation=null}", comboLeg1.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullAction() {
 comboLeg3.setm_action(null);
 assertEquals("ComboLeg{conId=1, ratio=2, action=null, exchange=null, openClose=" + ComboLeg.UNKNOWN + ", shortSaleSlot=0, designatedLocation=null}", comboLeg3.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullExchange() {
 comboLeg3.setm_exchange(null);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullDesignatedLocation() {
 comboLeg3.setm_designatedLocation(null);
 }

 @Test
 public void testGettersAndSetters() {
 // setters
 comboLeg1.setm_conId(3);
 comboLeg1.setm_ratio(3);
 comboLeg1.setm_action("SELL");
 comboLeg1.setm_exchange("IBKR2");
 comboLeg1.setm_openClose(ComboLeg.CLOSE);
 comboLeg1.setm_shortSaleSlot(3);
 comboLeg1.setm_designatedLocation("LA");

 // getters
 assertEquals(3, comboLeg1.getm_conId());
 assertEquals(3, comboLeg1.getm_ratio());
 assertEquals("SELL", comboLeg1.getm_action());
 assertEquals("IBKR2", comboLeg1.getm_exchange());
 assertEquals(ComboLeg.CLOSE, comboLeg1.getm_openClose());
 assertEquals(3, comboLeg1.getm_shortSaleSlot());
 assertEquals("LA", comboLeg1.getm_designatedLocation());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithInvalidOpenCloseValue() {
 comboLeg1.setm_openClose(4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersWithNegativeShortSaleSlot() {
 comboLeg1.setm_shortSaleSlot(-1);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 ComboLeg clonedComboLeg1 = comboLeg1.clone();

 assertEquals(comboLeg1, clonedComboLeg1);
 assertEquals(clonedComboLeg1.getClass(), comboLeg1.getClass());
 }
}