import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

 @Test
 public void testConstructors() {
 ComboLeg comboLeg1 = new ComboLeg();
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NYSE");

 Assert.assertEquals(0, comboLeg1.getConId());
 Assert.assertEquals(0, comboLeg1.getRatio());
 Assert.assertNull(comboLeg1.getAction());
 Assert.assertNull(comboLeg1.getExchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg1.getOpenClose());
 Assert.assertEquals(0, comboLeg1.getShortSaleSlot());
 Assert.assertNull(comboLeg1.getDesignatedLocation());

 Assert.assertEquals(1, comboLeg2.getConId());
 Assert.assertEquals(2, comboLeg2.getRatio());
 Assert.assertEquals("BUY", comboLeg2.getAction());
 Assert.assertEquals("SMART", comboLeg2.getExchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg2.getOpenClose());
 Assert.assertEquals(0, comboLeg2.getShortSaleSlot());
 Assert.assertNull(comboLeg2.getDesignatedLocation());

 Assert.assertEquals(1, comboLeg3.getConId());
 Assert.assertEquals(2, comboLeg3.getRatio());
 Assert.assertEquals("BUY", comboLeg3.getAction());
 Assert.assertEquals("SMART", comboLeg3.getExchange());
 Assert.assertEquals(ComboLeg.OPEN, comboLeg3.getOpenClose());
 Assert.assertEquals(1, comboLeg3.getShortSaleSlot());
 Assert.assertEquals("NYSE", comboLeg3.getDesignatedLocation());
 }

 @Test
 public void testEquals() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.CLOSE);
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NYSE");

 Assert.assertTrue(comboLeg1.equals(comboLeg2));
 Assert.assertFalse(comboLeg1.equals(comboLeg3));
 Assert.assertFalse(comboLeg1.equals(comboLeg4));
 }

 @Test
 public void testHashCode() {
 ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN);
 ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.CLOSE);
 ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "SMART", ComboLeg.OPEN, 1, "NYSE");

 Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
 Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
 Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg4.hashCode());
 }
}