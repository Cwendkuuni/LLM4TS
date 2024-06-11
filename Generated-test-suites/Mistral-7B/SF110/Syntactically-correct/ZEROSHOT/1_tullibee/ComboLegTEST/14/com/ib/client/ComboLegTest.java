import org.junit.Assert;import static org.junit.Assert.*;import com.ib.client.ComboLeg;

public class ComboLegTest {

 @ org.junit.BeforeClass public static void setUpBeforeClass() throws Exception {}

 @ org.junit.AfterClass public static void tearDownAfterClass() throws Exception {}

 @ org.junit.Before public void setUp() { throw new RuntimeException("Stub method! Real setup code went here."); }

 @ org.junit.After public void tearDown() { throw new RuntimeException("Stub method! Real teardown code went here."); }

 @Test public void testConstructor() { ComboLeg comboLeg = new ComboLeg(); Assert.assertEquals(0, comboLeg.m_conId); Assert.assertEquals(0, comboLeg.m_ratio); Assert.assertNull(comboLeg.m_action); Assert.assertNull(comboLeg.m_exchange); Assert.assertEquals(0, comboLeg.m_openClose); Assert.assertEquals(0, comboLeg.m_shortSaleSlot); Assert.assertNull(comboLeg.m_designatedLocation);

 ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "SMART", 1); Assert.assertEquals(1, comboLeg1.m_conId); Assert.assertEquals(2, comboLeg1.m_ratio); Assert.assertEquals("Buy", comboLeg1.m_action); Assert.assertEquals("SMART", comboLeg1.m_exchange); Assert.assertEquals(1, comboLeg1.m_openClose); Assert.assertEquals(0, comboLeg1.m_shortSaleSlot); Assert.assertNull(comboLeg1.m_designatedLocation);

 ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "SMART", 1, 3, "NYSE"); Assert.assertEquals(1, comboLeg2.m_conId); Assert.assertEquals(2, comboLeg2.m_ratio); Assert.assertEquals("Buy", comboLeg2.m_action); Assert.assertEquals("SMART", comboLeg2.m_exchange); Assert.assertEquals(1, comboLeg2.m_openClose); Assert.assertEquals(3, comboLeg2.m_shortSaleSlot); Assert.assertEquals("NYSE", comboLeg2.m_designatedLocation); }

 @Test public void testEqualsObject() { ComboLeg comboLeg1 = new ComboLeg(1, 2, "Buy", "SMART", 1); ComboLeg comboLeg2 = new ComboLeg(1, 2, "Buy", "SMART", 1); ComboLeg comboLeg3 = new ComboLeg(1, 2, "Buy", "SMART", 2);

 Assert.assertTrue(comboLeg1.equals(comboLeg1)); Assert.assertTrue(comboLeg1.equals(comboLeg2)); Assert.assertFalse(comboLeg1.equals(comboLeg3));

 ComboLeg comboLeg4 = new ComboLeg(); Assert.assertFalse(comboLeg1.equals(comboLeg4)); }

}