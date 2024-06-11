package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.MissingResourceException;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        final String uiResourcesString = "menu={Menu1,Menu2,*Menu3|Menu3Label,Menu4,Menu5,Separator,Menu6={SubMenu1,SubMenu2,Menu6Label}}";
        final InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
        final MainMenu mainMenu = new MainMenu(mediatorMock);

        // Verify that the correct number of menus were created
        final int numberOfMenus = mainMenu.getMenuCount();
        assertTrue(numberOfMenus == 7);

        // Verify that the first menu was created correctly
        final JMenu menu1 = (JMenu) mainMenu.getMenu(0);
        assertEquals("Menu1", menu1.getText());

        // Verify that the second menu was created correctly with a label
        final JMenu menu2 = (JMenu) mainMenu.getMenu(1);
        assertEquals("Menu2", menu2.getText());
        assertNotNull(menu2.getMnemonic());

        // Verify that the third menu was created correctly with a label and an icon
        final JMenu menu3 = (JMenu) mainMenu.getMenu(2);
        assertEquals("Menu3Label", menu3.getText());
        assertNotNull(menu3.getMnemonic());

        // Verify that the fourth menu was created correctly
        final JMenu menu4 = (JMenu) mainMenu.getMenu(3);
        assertEquals("Menu4", menu4.getText());

        // Verify that the fifth menu was created correctly with a label and a shortcut
        final JMenuItem menu5Item = (JMenuItem) mainMenu.getItem(4);
        assertEquals("Menu5", menu5Item.getText());
        assertNotNull(menu5Item.getAccelerator());

        // Verify that the sixth menu was created correctly with a label, a shortcut, and an icon
        final JMenu menu6 = (JMenu) mainMenu.getMenu(5);
        assertEquals("Menu6Label", menu6.getText());
        assertNotNull(menu6.getMnemonic());

        // Verify that the first submenu was created correctly
        final JMenu subMenu1 = (JMenu) menu6.getMenuComponent(0);
        assertEquals("SubMenu1", subMenu1.getText());

        // Verify that the second submenu was created correctly
        final JMenu subMenu2 = (JMenu) menu6.getMenuComponent(1);
        assertEquals("SubMenu2", subMenu2.getText());
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingMenuLabel() {
        final InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
        new MainMenu(mediatorMock);
    }

    @Test(expected = MissingResourceException.class)
    public void testMissingSubmenuLabel() {
        final String uiResourcesString = "menu={Menu1,Menu2,*Menu3|Menu3Label,Menu4,Menu5,Separator,Menu6={SubMenu1,SubMenu2}}";
        final InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
        new MainMenu(mediatorMock);
    }

    @Test
    public void testItemActionPerformed() {
        final InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
        final MainMenu mainMenu = new MainMenu(mediatorMock);

        final JMenuItem jMenuItem = Mockito.mock(JMenuItem.class);
        mainMenu.add(jMenuItem);

        mainMenu.getItem(0).addActionListener(e -> {
            Mockito.verify(mediatorMock).handleEvent(e);
        });

        jMenuItem.doClick();
    }

    @Test
    public void testCheckboxItemStateChange() {
        final InspirentoMediator mediatorMock = Mockito.mock(InspirentoMediator.class);
        final MainMenu mainMenu = new MainMenu(mediatorMock);

        final JCheckBoxMenuItem jCheckBoxMenuItem = Mockito.mock(JCheckBoxMenuItem.class);
        mainMenu.add(jCheckBoxMenuItem);

        jCheckBoxMenuItem.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Mockito.verify(mediatorMock).handleEvent(jCheckBoxMenuItem.getText());
            }
        });

        jCheckBoxMenuItem.doClick();
    }

    private void assertEquals(JMenu expected, JMenu actual) {
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getMnemonic(), actual.getMnemonic());
        for (int i = 0; i < expected.getMenuComponentCount(); i++) {
            JComponent componentExpected = expected.getMenuComponent(i);
            JComponent componentActual = actual.getMenuComponent(i);
            assertEquals(componentExpected.getClass().getName(), componentActual.getClass().getName());
            if (componentExpected instanceof JMenu) {
                assertEquals("", componentExpected.getText(), componentActual.getText());
                assertEquals(componentExpected.getMnemonic(), componentActual.getMnemonic());
                assertEquals(componentExpected.getComponentCount(), componentActual.getComponentCount());
                assertArrayEquals(componentExpected.getMenuComponents(), componentActual.getMenuComponents());
            } else if (componentExpected instanceof JMenuItem) {
                assertEquals(componentExpected.getText(), componentActual.getText());
                assertEquals(componentExpected.getMnemonic(), componentActual.getMnemonic());
                assertEquals(componentExpected.getAccelerator(), componentActual.getAccelerator());
                assertEquals(componentExpected.getIcon(), componentActual.getIcon());
            }
        }
    }
}