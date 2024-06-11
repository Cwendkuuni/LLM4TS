import org.junit.Assert;
import org.junit.Test;
import ipac.WhoIS;

public class WhoISTest1 {
    @Test
    public void testWhoISConstructor() {
        WhoIS whoIS = new WhoIS();
        // Basic functionality check - empty constructor should initialize all components correctly
        Assert.assertEquals("APNIC", whoIS.WHOIS_NAME);
        Assert.assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        Assert.assertEquals(43, whoIS.WHOIS_PORT);
        // Check the query panel
        Assert.assertNotNull(whoIS.queryTextField);
        Assert.assertNotNull(whoIS.queryButton);
        Assert.assertNotNull(whoIS.resetButton);
        // Check the additional queries panel
        Assert.assertNotNull(whoIS.IPAddressLookups());
        Assert.assertNotNull(whoIS.MiscellaneousQueries());
        // Check the query hints panel
        Assert.assertNotNull(whoIS.queryHintsPanel());
    }

    @Test
    public void testQueryFunctionality() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, 0, "Query"));
        // Check if the query is sent to the server
        // Add a mock server and verify the query sent
        // Check the output displayed in the WhoISOutput class
    }

    // Add test cases for edge cases and exception handling here
}