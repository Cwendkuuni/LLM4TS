package ipac.whois;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setup() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
        assertEquals("APNIC", whois.WHOIS_NAME);
        assertEquals("whois.apnic.net", whois.WHOIS_ADDRESS);
        assertEquals(43, whois.WHOIS_PORT);
        assertNull(whois.queryTextField);
        assertNull(whois.queryButton);
        assertNull(whois.resetButton);
        assertNull(whois.lowercaseD);
        assertNull(whois.lowercaseL);
        assertNull(whois.uppercaseL);
        assertNull(whois.lowercaseM);
        assertNull(whois.uppercaseM);
        assertNull(whois.lowercaseX);
        assertNull(whois.resetRadioButton);
        assertNull(whois.radioButtonGroup);
        assertNull(whois.popupMenu);
        assertNull(whois.inverseQueries);
        assertNull(whois.miscList);
    }

    @Test
    public void testCreateQueryPanel() {
        final WhoISPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertNull(queryPanel.getBackground());
        assertNotNull(queryPanel.getLayout());
        assertEquals(BoxLayout.Y_AXIS, queryPanel.getLayout().getType());
        final JTextField queryTextField = (JTextField) queryPanel.findComponentByName("queryTextField");
        assertNotNull(queryTextField);
        assertFalse(queryTextField.isFocusOwner());
        assertEquals(40, queryTextField.getColumns());
        final JButton queryButton = (JButton) queryPanel.findComponentByName("queryButton");
        assertNotNull(queryButton);
        assertTrue(queryButton.getText().equalsIgnoreCase("Query"));
        final JButton resetButton = (JButton) queryPanel.findComponentByName("resetButton");
        assertNotNull(resetButton);
        assertTrue(resetButton.getText().equalsIgnoreCase("Reset"));
    }

    @Test
    public void testCreateAdditionalQueriesPanel() {
        final JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertNull(additionalQueriesPanel.getBackground());
        assertNotNull(additionalQueriesPanel.getLayout());
        assertEquals(BoxLayout.Y_AXIS, additionalQueriesPanel.getLayout().getType());
        final JRadioButton lowercaseL = (JRadioButton) additionalQueriesPanel.findComponentByName("lowercaseL");
        assertNotNull(lowercaseL);
        assertTrue(lowercaseL.getText().equalsIgnoreCase("1st level less specific"));
        final JRadioButton uppercaseL = (JRadioButton) additionalQueriesPanel.findComponentByName("uppercaseL");
        assertNotNull(uppercaseL);
        assertTrue(uppercaseL.getText().equalsIgnoreCase("All less specific"));
        final JRadioButton lowercaseM = (JRadioButton) additionalQueriesPanel.findComponentByName("lowercaseM");
        assertNotNull(lowercaseM);
        assertTrue(lowercaseM.getText().equalsIgnoreCase("1st level more specific"));
        final JRadioButton uppercaseM = (JRadioButton) additionalQueriesPanel.findComponentByName("uppercaseM");
        assertNotNull(uppercaseM);
        assertTrue(uppercaseM.getText().equalsIgnoreCase("All more specific"));
        final JRadioButton lowercaseX = (JRadioButton) additionalQueriesPanel.findComponentByName("lowercaseX");
        assertNotNull(lowercaseX);
        assertTrue(lowercaseX.getText().equalsIgnoreCase("Exact match only"));
        final JRadioButton resetRadioButton = (JRadioButton) additionalQueriesPanel.findComponentByName("resetRadioButton");
        assertNotNull(resetRadioButton);
        assertTrue(resetRadioButton.getText().equalsIgnoreCase("Reset Radio Button"));
        final JComboBox inverseQueries = (JComboBox) additionalQueriesPanel.findComponentByName("inverseQueries");
        assertNotNull(inverseQueries);
        assertEquals(25, inverseQueries.getItemCount());
        assertTrue(Arrays.asList(inverseQueries.getItemAt(0), inverseQueries.getItemAt(24)).containsAll(Arrays.asList("None", "zone-c")));
        final JList miscList = (JList) additionalQueriesPanel.findComponentByName("miscList");
        assertNotNull(miscList);
        assertFalse(miscList.isFocusOwner());
        assertEquals(16, miscList.getModel().getSize());
        assertEquals("All", miscList.getSelectedValue());
    }

    @Test
    public void testCreateQueryHintsPanel() {
        final JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertNull(queryHintsPanel.getBackground());
        assertNotNull(queryHintsPanel.getLayout());
        assertEquals(FlowLayout.LEFT, queryHintsPanel.getLayout().getType());
        final JPanel innerPanel = (JPanel) queryHintsPanel.findComponent(0);
        assertEquals(BoxLayout.Y_AXIS, innerPanel.getLayout().getType());
        final JLabel queryHintsText1 = (JLabel) innerPanel.findComponent(0);
        assertNotNull(queryHintsText1);
        assertTrue(queryHintsText1.getText().equalsIgnoreCase("Query hints"));
        final JLabel emptyLabel = (JLabel) innerPanel.findComponent(1);
        assertNotNull(emptyLabel);
        assertTrue(emptyLabel.getText().equalsIgnoreCase("EMPTY LABEL"));
        final JLabel queryHintsText2 = (JLabel) innerPanel.findComponent(2);
        assertNotNull(queryHintsText2);
        assertTrue(queryHintsText2.getText().equalsIgnoreCase("* Include \"AS\" in front of an AS number."));
        final JLabel queryHintsText3 = (JLabel) innerPanel.findComponent(3);
        assertNotNull(queryHintsText3);
        assertTrue(queryHintsText3.getText().equalsIgnoreCase("Example: AS4808"));
        final JLabel queryHintsText4 = (JLabel) innerPanel.findComponent(4);
        assertNotNull(queryHintsText4);
        assertTrue(queryHintsText4.getText().equalsIgnoreCase("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template."));
        final JLabel queryHintsText5 = (JLabel) innerPanel.findComponent(5);
        assertNotNull(queryHintsText5);
        assertTrue(queryHintsText5.getText().equalsIgnoreCase("Example: -t inetnum"));
    }

    @Test
    public void testCreatePopupMenu() {
        whois.createPopupMenu();
        final JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);
        assertFalse(popupMenu.isFocusOwner());
        assertEquals(4, popupMenu.getComponentCount());
        final JMenuItem cut = (JMenuItem) popupMenu.findComponent(0);
        assertNotNull(cut);
        assertTrue(cut.getText().equalsIgnoreCase("Cut"));
        final JMenuItem copy = (JMenuItem) popupMenu.findComponent(1);
        assertNotNull(copy);
        assertTrue(copy.getText().equalsIgnoreCase("Copy"));
        final JMenuItem paste = (JMenuItem) popupMenu.findComponent(2);
        assertNotNull(paste);
        assertTrue(paste.getText().equalsIgnoreCase("Paste"));
        final JMenuItem selectAll = (JMenuItem) popupMenu.findComponent(3);
        assertNotNull(selectAll);
        assertTrue(selectAll.getText().equalsIgnoreCase("Select All"));
    }

    @Test
    public void testActionPerformed() {
        final ActionEvent queryButtonEvent = new ActionEvent(whois.queryButton, 0, "Query Clicked");
        final ActionEvent resetButtonEvent = new ActionEvent(whois.resetButton, 0, "Reset Clicked");
        whois.actionPerformed(queryButtonEvent);
        whois.actionPerformed(resetButtonEvent);
    }

    @Test
    public void testStartQueryWithEmptyTextField() {
        whois.startQuery();
        assertTrue(whois.displayErrorCalled);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        final String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        assertTrue(whois.queryCalled);
    }

    @Test
    public void testDisplayError() {
        final String error = "Error";
        whois.displayError(error);
        assertTrue(JOptionPane.showMessageDialogCalled);
        assertEquals("Error", JOptionPane.showMessageDialogMessage);
    }

    @Test
    public void testQueryWithValidQuery() throws IOException {
        final String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        assertTrue(new WhoISOutput(whois.response).isSuccess());
    }

    @Test(expected = UnknownHostException.class)
    public void testQueryWithUnknownHost() throws IOException {
        final String query = "unknown.host";
        whois.queryTextField.setText(query);
        whois.startQuery();
    }

    @Test(expected = IOException.class)
    public void testQueryWithWhoisServerNotResponding() throws IOException {
        final String query = "example.com";
        whois.queryTextField.setText(query);
        when(whois.socketFactory.createSocket(anyString(), anyInt())).thenThrow(new IOException());
        whois.startQuery();
    }

    @Test
    public void testGetQuery() {
        final String query = "example.com";
        whois.queryTextField.setText(query);
        final String expected = "-T All -i zone-c example.com";
        assertEquals(expected, whois.getQuery());
    }

    @Test
    public void testReset() {
        final String query = "example.com";
        whois.queryTextField.setText(query);
        whois.startQuery();
        whois.reset();
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.booleanD);
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals("All", whois.miscList.getSelectedValue());
    }

    private static class WhoISOutput {

        private static boolean success;

        public WhoISOutput(final String response) {
            this.response = response;
        }

        public boolean isSuccess() {
            return WhoISOutput.success;
        }

        private static void parseSuccess(final String response) {
            WhoISOutput.success = true;
        }

        private String response;

        static {
            parseSuccess("");
        }

        @Override
        public String toString() {
            return response;
        }

    }

    private static boolean JOptionPane_showMessageDialogCalled = false;

    private static String JOptionPane_showMessageDialogMessage;

    @BeforeClass
    public static void setUpBeforeClass() {
        MockUtils.initMocks(JOptionPane.class);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        MockUtils.reset(JOptionPane.class);
    }

    @BeforeMethod
    public void setUp() {
        reset(WhoIS.class);
    }

    @AfterMethod
    public void tearDown() {
        verifyNoMoreInteractions(JOptionPane.class);
    }

    private static class MockUtils {

        public static void initMocks(Class<?> clazz) {
            when(clazz.getDeclaredMethod("showMessageDialog", JFrame.class, String.class, int.class, Object[].class)).thenAnswer(new Answer<Void>() {
                @Override
                public Void answer(final InvocationOnMock invocation) throws Throwable {
                    JOptionPane_showMessageDialogCalled = true;
                    JOptionPane_showMessageDialogMessage = invocation.getArgument(1);
                    return null;
                }
            });
        }

        public static void reset(Class<?> clazz) {
            reset(clazz.getDeclaredMethod("showMessageDialog", JFrame.class, String.class, int.class, Object[].class));
        }

    }

}