import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.assertEquals; import static org.junit.Assert.assertFalse; import static org.junit.Assert.assertNotNull; import static org.junit.Assert.assertNull; import static org.junit.Assert.assertTrue;

import com.ib.client.ComboLeg;

public class ComboLegTest {

 @Test public void testConstructor() {
 ComboLeg comboLeg = new ComboLeg();
 Assert.assertEquals(0, comboLeg.getConId());
 Assert.assertEquals(0, comboLeg.getRatio());
 Assert.assertNull(comboLeg.getAction());
 Assert.assertNull(comboLeg.getExchange());
 Assert.assertEquals(0, comboLeg.getOpenClose());
 Assert.assertEquals(0, comboLeg.getShortSaleSlot());
 Assert.assertNull(comboLeg.getDesignatedLocation());

 ComboLeg comboLegWithParams = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 Assert.assertEquals(1, comboLegWithParams.getConId());
 Assert.assertEquals(2, comboLegWithParams.getRatio());
 Assert.assertEquals("Action1", comboLegWithParams.getAction());
 Assert.assertEquals("EXCHANGE1", comboLegWithParams.getExchange());
 Assert.assertEquals(1, comboLegWithParams.getOpenClose());
 Assert.assertEquals(0, comboLegWithParams.getShortSaleSlot());
 Assert.assertNull(comboLegWithParams.getDesignatedLocation());
 }

 @Test public void testGettersAndSetters() {
 ComboLeg comboLeg = new ComboLeg();

 comboLeg.setConId(1);
 comboLeg.setRatio(2);
 comboLeg.setAction("Action1");
 comboLeg.setExchange("EXCHANGE1");
 comboLeg.setOpenClose(1);
 comboLeg.setShortSaleSlot(0);
 comboLeg.setDesignatedLocation("DESIGNATED_LOCATION");

 Assert.assertEquals(1, comboLeg.getConId());
 Assert.assertEquals(2, comboLeg.getRatio());
 Assert.assertEquals("Action1", comboLeg.getAction());
 Assert.assertEquals("EXCHANGE1", comboLeg.getExchange());
 Assert.assertEquals(1, comboLeg.getOpenClose());
 Assert.assertEquals(0, comboLeg.getShortSaleSlot());
 Assert.assertEquals("DESIGNATED_LOCATION", comboLeg.getDesignatedLocation());
 }

 @Test public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "EXCHANGE1", 1);
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "Action1", "EXCHANGE2", 1);

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "EXCHANGE1", 1);
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "Action1", "EXCHANGE2", 1);

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg3.hashCode());
 Assert.neq(comboLeg1.hashCode(), comboLeg4.hashCode());
 }

 @Test public void testToString() {
 ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
 String expectedToString = "ComboLeg{conId=1, ratio=2, action='Action1', exchange='EXCHANGE1', openClose=1, shortSaleSlot=0, designatedLocation=null}";
 String toString = comboLeg.toString();
 Assert.assertEquals(expectedToString, toString);
 }
}