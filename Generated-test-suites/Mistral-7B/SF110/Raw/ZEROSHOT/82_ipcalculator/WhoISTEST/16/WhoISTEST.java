package ipac.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import ipac.WhoIS;
import ipac.WhoISOutput;

public class WhoISTest {
    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testConstructor() {
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanel() {
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        assertTrue(queryPanel.getBackground().equals(Color.WHITE));
        assertEquals(4, queryPanel.getComponentCount());

        JTextField queryTextField = (JTextField) queryPanel.getComponent(2);
        assertEquals(40, queryTextField.getColumns());

        JButton queryButton = (JButton) queryPanel.getComponent(3);
        assertEquals("Query", queryButton.getText());

        JButton resetButton = (JButton) queryPanel.getComponent(4);
        assertEquals("Reset", resetButton.getText());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
        assertTrue(additionalQueriesPanel.getBackground().equals(Color.WHITE));

        JRadioButton lowercaseL = (JRadioButton) additionalQueriesPanel.getComponent(0);
        assertEquals("1st level less specific", lowercaseL.getText());

        JRadioButton uppercaseL = (JRadioButton) additionalQueriesPanel.getComponent(1);
        assertEquals("All less specific", uppercaseL.getText());

        JRadioButton lowercaseM = (JRadioButton) additionalQueriesPanel.getComponent(2);
        assertEquals("1st level more specific", lowercaseM.getText());

        JRadioButton uppercaseM = (JRadioButton) additionalQueriesPanel.getComponent(3);
        assertEquals("All more specific", uppercaseM.getText());

        JRadioButton lowercaseX = (JRadioButton) additionalQueriesPanel.getComponent(4);
        assertEquals("Exact match only", lowercaseX.getText());

        JCheckBox lowercaseD = (JCheckBox) additionalQueriesPanel.getComponent(5);
        assertEquals("Associated reverse domain", lowercaseD.getText());

        JRadioButton resetRadioButton = (JRadioButton) additionalQueriesPanel.getComponent(6);
        assertEquals("Reset Radio Button", resetRadioButton.getText());
    }

    @Test
    public void testMiscellaneousQueries() {
        JPanel miscPanel = whois.MiscellaneousQueries();
        assertNotNull(miscPanel);
        assertTrue(miscPanel.getBackground().equals(Color.WHITE));

        JLabel inverseLabel = (JLabel) miscPanel.getComponent(0);
        assertEquals("Inverse Queries", inverseLabel.getText());

        JComboBox inverseQueries = (JComboBox) miscPanel.getComponent(1);
        assertEquals(20, inverseQueries.getItemCount());

        JLabel miscLabel = (JLabel) miscPanel.getComponent(2);
        assertEquals("Object Types", miscLabel.getText());

        JList miscList = (JList) miscPanel.getComponent(3);
        assertEquals(15, miscList.getItemCount());

        JScrollPane scrollingList = (JScrollPane) miscPanel.getComponent(4);
        assertTrue(scrollingList.getViewport().getView() instanceof JList);
    }

    @Test
    public void testQueryHintsPanel() {
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
        assertTrue(queryHintsPanel.getBackground().equals(Color.WHITE));

        JPanel innerPanel = (JPanel) queryHintsPanel.getComponent(0);
        assertEquals(5, innerPanel.getComponentCount());

        JLabel queryHintsText1 = (JLabel) innerPanel.getComponent(0);
        assertEquals("Query hints ", queryHintsText1.getText());

        JLabel emptyLabel = (JLabel) innerPanel.getComponent(1);
        assertTrue(emptyLabel.getText().isEmpty());

        JLabel queryHintsText2 = (JLabel) innerPanel.getComponent(2);
        assertEquals("* Include \"AS\" in front of an AS number.", queryHintsText2.getText());

        JLabel queryHintsText3 = (JLabel) innerPanel.getComponent(3);
        assertEquals("Example: AS4808", queryHintsText3.getText());

        JLabel queryHintsText4 = (JLabel) innerPanel.getComponent(4);
        assertEquals("* Include \"-t\" (template only) or \"-v\" (template and description) in front of an object name to view the template.", queryHintsText4.getText());

        JLabel queryHintsText5 = (JLabel) innerPanel.getComponent(5);
        assertEquals("Example: -t inetnum", queryHintsText5.getText());
    }

    @Test
    public void testCreatePopupMenu() {
        JPopupMenu popupMenu = whois.popupMenu;
        assertNotNull(popupMenu);

        JMenuItem cut = popupMenu.getComponent(0);
        assertEquals("Cut", cut.getText());

        JMenuItem copy = popupMenu.getComponent(1);
        assertEquals("Copy", copy.getText());

        JMenuItem paste = popupMenu.getComponent(2);
        assertEquals("Paste", paste.getText());

        JMenuItem selectAll = popupMenu.getComponent(3);
        assertEquals("Select All", selectAll.getText());
    }

    @Test
    public void testActionPerformed() {
        WhoISOutput output = new WhoISOutput("");
        InputStream inputStream = new ByteArrayInputStream("test output".getBytes());
        System.setIn(inputStream);

        whois.actionPerformed(new ActionEvent(whois, 0, "test event"));
        assertEquals("test output", output.getText());

        System.setIn(System.in);
    }

    @Test
    public void testStartQuery() throws IOException, UnknownHostException {
        String query = "test query";
        whois.queryTextField.setText(query);

        whois.startQuery();

        String expectedQuery = "-i None -l " + query;
        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);

        Socket socket = mockSocket();
        whois.query(query);

        String expectedOutput = "test output";
        String actualOutput = socket.getOutputStream().toString();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDisplayError() {
        whois.displayError("test error");
        String expectedError = "test error";
        String actualError = JOptionPane.showMessageDialog(whois, expectedError, "Error", 0).toString();
        assertEquals(expectedError, actualError);
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("test query");

        whois.reset();

        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    private Socket mockSocket() throws IOException {
        Socket socket = mock(Socket.class);
        doAnswer(invocation -> {
            OutputStream outputStream = invocation.getArgument(0);
            outputStream.write("test output".getBytes());
            return null;
        }).when(socket).getOutputStream();
        return socket;
    }
}