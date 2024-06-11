package org.apache.commons.cli.gui; // assuming the package name is correct for the jgaapGUI class

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.io.File;

public class jgaapGUITest {

    private JgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new JgaapGUI();
    }

    @After
    public void tearDown() {
        if (jgaapGUI != null) {
            jgaapGUI.dispose();
        }
    }

    @Test
    public void testInitComponents() {
        // tests for initialization of various components
        assertNotNull(jgaapGUI.menuBar);
        assertNotNull(jgaapGUI.menuFile);
        assertNotNull(jgaapGUI.menuFileExit);
        assertNotNull(jgaapGUI.menuFileDemo);
        assertNotNull(jgaapGUI.menuHelp);
        assertNotNull(jgaapGUI.menuHelpContents);
        assertNotNull(jgaapGUI.menuHelpAbout);
        assertNotNull(jgaapGUI.paneOpts);
        assertNotNull(jgaapGUI.panelAcquire);
        assertNotNull(jgaapGUI.panelDocs);
        assertNotNull(jgaapGUI.labelKnown);
        assertNotNull(jgaapGUI.scrollPane1);
        assertNotNull(jgaapGUI.listKnown);
        assertNotNull(jgaapGUI.labelUnknown);
        assertNotNull(jgaapGUI.scrollPane2);
        assertNotNull(jgaapGUI.listUnknown);
        assertNotNull(jgaapGUI.panelOpts);
        assertNotNull(jgaapGUI.radioKnown);
        assertNotNull(jgaapGUI.radioUnknown);
        assertNotNull(jgaapGUI.buttonLocal);
        assertNotNull(jgaapGUI.panelCanonicize);
        assertNotNull(jgaapGUI.panelCOpts);
        assertNotNull(jgaapGUI.canonicizers);
        assertNotNull(jgaapGUI.eventifiers);
        assertNotNull(jgaapGUI.esButtonGroup);
        assertNotNull(jgaapGUI.panelProgress);
        assertNotNull(jgaapGUI.buttonCanonicize);
        assertNotNull(jgaapGUI.label1);
        assertNotNull(jgaapGUI.progressCanonicize);
        assertNotNull(jgaapGUI.labelCanon);
        assertNotNull(jgaapGUI.panelAnalyze);
        assertNotNull(jgaapGUI.labelStatInf);
        assertNotNull(jgaapGUI.panelMethods);
        assertNotNull(jgaapGUI.radioCrossEnt);
        assertNotNull(jgaapGUI.radioLZW);
        assertNotNull(jgaapGUI.panelAnalProg);
        assertNotNull(jgaapGUI.buttonAnalyze);
        assertNotNull(jgaapGUI.labelAnalProg);
        assertNotNull(jgaapGUI.progressAnalysis);
        assertNotNull(jgaapGUI.scrollPane3);
        assertNotNull(jgaapGUI.listResults);
        assertNotNull(jgaapGUI.buttonEventSet);
        assertNotNull(jgaapGUI.paneEvProgress);
        assertNotNull(jgaapGUI.labelevSet);
        assertNotNull(jgaapGUI.progressEventSet);
        assertNotNull(jgaapGUI.panelEventSet);
        assertNotNull(jgaapGUI.panelEOpts);
        assertNotNull(jgaapGUI.panelEvProgress);
    }

    @Test
    public void testJFileChooser() {
        // tests for JFileChooser functionality
        JFileChooser chooser = jgaapGUI.chooseFile;
        assertNotNull(chooser);
        assertEquals(JFileChooser.OPEN_DIALOG, chooser.getDialogType());
        assertFalse(chooser.isAccessible());
        chooser.setVisible(true);
        assertTrue(chooser.isAccessible());
        chooser.dispose();
    }

    @Test
    public void testMenuItems() {
        // tests for menu items and their functionality
        assertEquals("File", jgaapGUI.menuFile.getText());
        assertEquals("Exit", jgaapGUI.menuFileExit.getText());
        assertEquals("Load Demo", jgaapGUI.menuFileDemo.getText());
        assertEquals("Help", jgaapGUI.menuHelp.getText());
        assertEquals("Contents (F1)", jgaapGUI.menuHelpContents.getText());
        assertEquals("About", jgaapGUI.menuHelpAbout.getText());
        assertEquals(true, jgaapGUI.menuFileExit.isEnabled());
        assertEquals(false, jgaapGUI.menuFileDemo.isEnabled());
        jgaapGUI.menuFileDemo.doClick();
        assertEquals(true, jgaapGUI.menuFileDemo.isEnabled());
    }

    @Test
    public void testTextAreas() {
        // tests for JTextArea functionality
        JTextArea listKnown = jgaapGUI.listKnown;
        JTextArea listUnknown = jgaapGUI.listUnknown;
        JTextArea listResults = jgaapGUI.listResults;

        assertNotNull(listKnown);
        assertNotNull(listUnknown);
        assertNotNull(listResults);

        listKnown.setText("Test Text");
        assertEquals("Test Text", listKnown.getText());

        listUnknown.setText("Test Text 2");
        assertEquals("Test Text 2", listUnknown.getText());

        listResults.setText("Test Text 3");
        assertEquals("Test Text 3", listResults.getText());
    }

    @Test
    public void testFileSelection() {
        // tests for file selection functionality
        JFileChooser chooser = jgaapGUI.chooseFile;
        chooser.setCurrentDirectory(new File("."));

        assertNotNull(chooser);
        assertNull(jgaapGUI.currentAuthor.getText());
        chooser.setSelectedFile(new File("testFile.txt"));
        jgaapGUI.buttonLocal.doClick();
        assertEquals("testFile.txt", jgaapGUI.currentAuthor.getText());
    }

    @Test
    public void testButtonEvents() {
        // tests for button events
        JButton button = jgaapGUI.buttonLocal;
        JButton buttonEventSet = jgaapGUI.buttonEventSet;
        JButton buttonCanonicize = jgaapGUI.buttonCanonicize;
        JButton buttonAnalyze = jgaapGUI.buttonAnalyze;

        assertNotNull(button);
        assertNotNull(buttonEventSet);
        assertNotNull(buttonCanonicize);
        assertNotNull(buttonAnalyze);

        button.doClick();
        buttonEventSet.doClick();
        buttonCanonicize.doClick();
        buttonAnalyze.doClick();
    }

    @Test
    public void testRadioButtons() {
        // tests for radio buttons
        JRadioButton radioUnknown = jgaapGUI.radioUnknown;
        JRadioButton radioKnown = jgaapGUI.radioKnown;
        JRadioButton radioCrossEnt = jgaapGUI.radioCrossEnt;
        JRadioButton radioLZW = jgaapGUI.radioLZW;

        assertNotNull(radioUnknown);
        assertNotNull(radioKnown);
        assertNotNull(radioCrossEnt);
        assertNotNull(radioLZW);

        radioUnknown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                jgaapGUI.currentAuthor.setEnabled(false);
            }
        });

        radioKnown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                jgaapGUI.currentAuthor.setEnabled(true);
            }
        });

        radioCrossEnt.doClick();
        radioKnown.doClick();
        radioUnknown.doClick();
        radioCrossEnt.doClick();

        assertFalse(jgaapGUI.currentAuthor.isEnabled());
        assertTrue(jgaapGUI.currentAuthor.isEnabled());
        assertFalse(jgaapGUI.currentAuthor.isEnabled());
        assertTrue(jgaapGUI.currentAuthor.isEnabled());
    }

    @Test
    public void testLabelTexts() {
        // tests for label texts
        JLabel labelKnown = jgaapGUI.labelKnown;
        JLabel labelUnknown = jgaapGUI.labelUnknown;
        JLabel labelStatInf = jgaapGUI.labelStatInf;
        JLabel labelCanon = jgaapGUI.labelCanon;
        JLabel labelAnalProg = jgaapGUI.labelAnalProg;
        JLabel labelevSet = jgaapGUI.labelevSet;

        assertNotNull(labelKnown);
        assertNotNull(labelUnknown);
        assertNotNull(labelStatInf);
        assertNotNull(labelCanon);
        assertNotNull(labelAnalProg);
        assertNotNull(labelevSet);

        labelKnown.setText("Test Label 1");
        labelUnknown.setText("Test Label 2");
        labelStatInf.setText("Test Label 3");
        labelCanon.setText("Test Label 4");
        labelAnalProg.setText("Test Label 5");
        labelevSet.setText("Test Label 6");

        assertEquals("Test Label 1", labelKnown.getText());
        assertEquals("Test Label 2", labelUnknown.getText());
        assertEquals("Test Label 3", labelStatInf.getText());
        assertEquals("Test Label 4", labelCanon.getText());
        assertEquals("Test Label 5", labelAnalProg.getText());
        assertEquals("Test Label 6", labelevSet.getText());
    }

    @Test
    public void testJProgressBars() {
        // tests for JProgressBar functionality
        JProgressBar progressEventSet = jgaapGUI.progressEventSet;
        JProgressBar progressCanonicize = jgaapGUI.progressCanonicize;
        JProgressBar progressAnalysis = jgaapGUI.progressAnalysis;

        assertNotNull(progressEventSet);
        assertNotNull(progressCanonicize);
        assertNotNull(progressAnalysis);

        progressEventSet.setValue(50);
        progressCanonicize.setValue(75);
        progressAnalysis.setValue(100);

        assertEquals(50, progressEventSet.getValue());
        assertEquals(75, progressCanonicize.getValue());
        assertEquals(100, progressAnalysis.getValue());
    }
}