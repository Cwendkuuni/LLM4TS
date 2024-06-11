package ipac.test;

import org.junit.Assert;
import org.junit.Test;
import ipac.WhoIS;

public class WhoISTest {

    @Test
    public void testWhoISConstruction() {
        WhoIS whoIS = new WhoIS();
        Assert.assertNotNull(whoIS);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel queryPanel = whoIS.queryPanel();
        Assert.assertNotNull(queryPanel);
        Assert.assertEquals(3, queryPanel.getComponentCount());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        Assert.assertNotNull(additionalQueriesPanel);
        Assert.assertEquals(3, additionalQueriesPanel.getComponentCount());
    }

    @Test
    public void testIPAddressLookupsPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel ipAddressLookupsPanel = whoIS.IPAddressLookups();
        Assert.assertNotNull(ipAddressLookupsPanel);
        Assert.assertEquals(8, ipAddressLookupsPanel.getComponentCount());
    }

    @Test
    public void testMiscellaneousQueriesPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel miscellaneousQueriesPanel = whoIS.MiscellaneousQueries();
        Assert.assertNotNull(miscellaneousQueriesPanel);
        Assert.assertEquals(3, miscellaneousQueriesPanel.getComponentCount());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whoIS = new WhoIS();
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        Assert.assertNotNull(queryHintsPanel);
        Assert.assertEquals(1, queryHintsPanel.getComponentCount());
        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        Assert.assertEquals(6, innerPanel.getComponentCount());
    }

    @Test
    public void testCreatePopupMenu() {
        WhoIS whoIS = new WhoIS();
        JPopupMenu popupMenu = whoIS.popupMenu;
        Assert.assertNotNull(popupMenu);
        Assert.assertEquals(5, popupMenu.getComponentCount());
    }
}