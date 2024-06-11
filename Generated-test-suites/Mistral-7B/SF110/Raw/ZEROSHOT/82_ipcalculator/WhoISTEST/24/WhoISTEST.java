import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @After
    public void tearDown() {
        whoIS = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(whoIS);
    }

    @Test
    public void testQueryPanel() {
        assertTrue(whoIS.queryPanel().isVisible());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        assertTrue(whoIS.additionalQueriesPanel().isVisible());
    }

    @Test
    public void testIPAddressLookups() {
        assertTrue(whoIS.IPAddressLookups().isVisible());
        assertEquals(6, whoIS.IPAddressLookups().getComponentCount());
    }

    @Test
    public void testMiscellaneousQueries() {
        assertTrue(whoIS.MiscellaneousQueries().isVisible());
        assertEquals(16, whoIS.MiscellaneousQueries().getComponentCount());
    }

    @Test
    public void testQueryHintsPanel() {
        assertTrue(whoIS.queryHintsPanel().isVisible());
    }

    @Test
    public void testCreatePopupMenu() {
        assertTrue(whoIS.popupMenu != null);
    }

    @Test
    public void testActionPerformed() {
        mockActionEvent(whoIS.queryButton);
        verify(whoIS).startQuery();

        mockActionEvent(whoIS.resetButton);
        verify(whoIS).reset();
    }

    @Test
    public void testStartQuery() {
        String query = "example.com";
        whoIS.queryTextField.setText(query);
        whoIS.startQuery();

        String expectedQuery = "-i None -l example.com";
        String actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testGetQuery() {
        String query = "example.com";
        String inverseQuery = "admin-c";
        whoIS.queryTextField.setText(query);
        whoIS.inverseQueries.setSelectedItem(inverseQuery);

        String expectedQuery = "-i admin-c -l example.com";
        String actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);

        whoIS.lowercaseL.doClick();
        expectedQuery = "-l example.com";
        actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);

        whoIS.lowercaseM.doClick();
        expectedQuery = "-m example.com";
        actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);

        whoIS.lowercaseX.doClick();
        expectedQuery = "-x example.com";
        actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);

        whoIS.lowercaseD.doClick();
        expectedQuery = "-d example.com";
        actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);

        String[] miscQueries = {"as-block", "mntner"};
        for (String miscQuery : miscQueries) {
            whoIS.miscList.setSelectedValue(miscQuery, true);
        }
        expectedQuery = "-T as-block,mntner -l example.com";
        actualQuery = whoIS.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testReset() {
        whoIS.reset();
        assertTrue(whoIS.queryTextField.getText().isEmpty());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }

    private void mockActionEvent(Component component) {
        ActionEvent actionEvent = new ActionEvent(component, 0, "actionPerformed");
        ActionEvent mockEvent = mock(ActionEvent.class);
        when(mockEvent.getSource()).thenReturn(component);
        when(mockEvent.getActionCommand()).thenReturn("");
        when(mockEvent.getModifiers()).thenReturn(0);
        when(mockEvent.getWhen()).thenReturn(0L);
        when(mockEvent.getID()).thenReturn(ActionEvent.ACTION_PERFORMED);
        doAnswer(invocation -> {
            Object source = invocation.getArgument(0);
            if (source == whoIS.queryButton) {
                whoIS.startQuery();
            } else if (source == whoIS.resetButton) {
                whoIS.reset();
            }
        }).when(whoIS).actionPerformed(any(ActionEvent.class));
    }
}