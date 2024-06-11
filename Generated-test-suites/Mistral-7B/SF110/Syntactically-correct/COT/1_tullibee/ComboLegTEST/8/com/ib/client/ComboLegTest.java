import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.getm_conId());
        Assert.assertEquals(0, comboLeg.getm_ratio());
        Assert.assertNull(comboLeg.getm_action());
        Assert.assertNull(comboLeg.getm_exchange());
        Assert.assertEquals(0, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());

        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        Assert.assertEquals(1, comboLeg1.getm_conId());
        Assert.assertEquals(2, comboLeg1.getm_ratio());
        Assert.assertEquals("Action1", comboLeg1.getm_action());
        Assert.assertEquals("Exchange1", comboLeg1.getm_exchange());
        Assert.assertEquals(ComboLeg.OPEN, comboLeg1.getm_openClose());
        Assert.assertEquals(0, comboLeg1.getm_shortSaleSlot());
        Assert.assertNull(comboLeg1.getm_designatedLocation());

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 3, "Location1");
        Assert.assertEquals(1, comboLeg2.getm_conId());
        Assert.assertEquals(2, comboLeg2.getm_ratio());
        Assert.assertEquals("Action1", comboLeg2.getm_action());
        Assert.assertEquals("Exchange1", comboLeg2.getm_exchange());
        Assert.assertEquals(ComboLeg.OPEN, comboLeg2.getm_openClose());
        Assert.assertEquals(3, comboLeg2.getm_shortSaleSlot());
        Assert.assertEquals("Location1", comboLeg2.getm_designatedLocation());
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsWithNull() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testGetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("Action1", comboLeg.getm_action());
        Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
        Assert.assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNull() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setm_action(null);
    }

    @Test
    public void testToString() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        String expected = "ComboLeg{m_conId=1, m_ratio=2, m_action='Action1', m_exchange='Exchange1', m_openClose=" + ComboLeg.OPEN + ", m_shortSaleSlot=0, m_designatedLocation=null}";
        Assert.assertEquals(expected, comboLeg.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidOpenCloseValue() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setm_openClose(-1);
    }
}