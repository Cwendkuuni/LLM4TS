import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {
    @Test
    public void testWhoISConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryPanel());
    }

    @Test
    public void testAdditionalQueriesPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.additionalQueriesPanel());
    }

    @Test
    public void testIPAddressLookupsCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.IPAddressLookups());
    }

    @Test
    public void testMiscellaneousQueriesCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.MiscellaneousQueries());
    }

    @Test
    public void testQueryHintsPanelCreation() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryHintsPanel());
    }

    @Test
    public void testGetQuery_EmptyTextField_NoQueryDetected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        String query = whois.getQuery();
        assertEquals("No Query Detected", query);
    }

    @Test(expected = NullPointerException.class)
    public void testGetQuery_NullTextField() {
        WhoIS whois = new WhoIS();
        whois.queryTextField = null;
        String query = whois.getQuery();
    }

    @Test
    public void testGetQuery_ValidQuery_CorrectQueryFormation() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        String expected = "-i None -l example.com";
        assertEquals(expected, query);
    }

    @Test
    public void testGetQuery_ValidQuery_WithInverseQueries_CorrectQueryFormation() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1);
        String query = whois.getQuery();
        String expected = "-i admin-c -l example.com";
        assertEquals(expected, query);
    }

    @Test
    public void testGetQuery_ValidQuery_WithLookups_CorrectQueryFormation() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.lowercaseL.setSelected(true);
        String query = whois.getQuery();
        String expected = "-i None -l example.com -l";
        assertEquals(expected, query);
    }

    @Test
    public void testGetQuery_ValidQuery_WithMiscQueries_CorrectQueryFormation() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        Object[] selected = {"as-block", "as-set"};
        whois.miscList.setSelectedValues(selected);
        String query = whois.getQuery();
        String expected = "-i None -T as-block,as-set -l example.com";
        assertEquals(expected, query);
    }

    @Test
    public void testGetQuery_ValidQuery_WithBooleanD_CorrectQueryFormation() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.lowercaseD.setSelected(true);
        String query = whois.getQuery();
        String expected = "-i None -d -l example.com";
        assertEquals(expected, query);
    }
}