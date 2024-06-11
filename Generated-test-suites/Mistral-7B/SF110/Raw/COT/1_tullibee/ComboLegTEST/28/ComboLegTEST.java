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
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 3, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
        Assert.assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAction() {
        new ComboLeg(1, 2, null, "Exchange1", ComboLeg.OPEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullExchange() {
        new ComboLeg(1, 2, "Action1", null, ComboLeg.OPEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOpenClose() {
        new ComboLeg(1, 2, "Action1", "Exchange1", 4);
    }
}