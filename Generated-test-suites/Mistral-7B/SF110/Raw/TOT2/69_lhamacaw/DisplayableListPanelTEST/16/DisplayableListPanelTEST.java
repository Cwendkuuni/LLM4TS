package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest {
    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private Log log;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;
    private UserInterfaceFactory userInterfaceFactory;
    private User currentUser;

    @Before
    public void setup() {
        sessionProperties = mock(SessionProperties.class);
        parentForm = mock(DisplayableListParentForm.class);
        log = mock(Log.class);
        panelTitleText = "Test Panel Title";
        allowWriteAccess = true;
        allowRecordEditing = true;
        userInterfaceFactory = mock(UserInterfaceFactory.class);
        currentUser = mock(User.class);
        when(sessionProperties.getProperty("currentUser")).thenReturn(currentUser);

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
    }

    @Test
    public void testConstructor() {
        assertNotNull(displayableListPanel);
        assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testEmptyDisplayableListPanel() {
        DisplayableList emptyDisplayableList = mock(DisplayableList.class);
        when(displayableListPanel.getDisplayableList()).thenReturn(emptyDisplayableList);

        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);

        JButton addButton = displayableListPanel.getAddButton();
        assertNotNull(addButton);
        addButton.doClick(); // Test add button click with empty list

        verify(parentForm, never()).commitChanges();
    }

    @Test
    public void testAddListItemWithValidData() {
        DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
        when(displayableListPanel.getDisplayableList()).thenReturn(displayableList);
        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        when(displayableListPanel.getListItemAdder()).thenReturn(listItemAdder);

        JButton addButton = displayableListPanel.getAddButton();
        addButton.doClick();

        verify(listItemAdder).addItems(eq(displayableListPanel.getListOwnerName()), eq(displayableList));
        verify(parentForm).commitChanges();
    }

    // Add more test cases for other methods, edge cases, and error scenarios

    @After
    public void tearDown() {
        displayableListPanel = null;
        sessionProperties = null;
        parentForm = null;
        log = null;
        panelTitleText = null;
        allowWriteAccess = false;
        allowRecordEditing = false;
        userInterfaceFactory = null;
        currentUser = null;
    }
}