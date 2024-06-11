package umd.cs.shop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JApplet;

import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {

    private JSJshop jsJshop;
    private JSUtil jsUtilMock;
    private JSPlanningDomain jsPlanningDomainMock;
    private JSPlanningProblem jsPlanningProblemMock;
    private JSListPlanningProblem jsListPlanningProblemMock;

    @Before
    public void setup() {
        jsUtilMock = mock(JSUtil.class);
        jsPlanningDomainMock = mock(JSPlanningDomain.class);
        jsPlanningProblemMock = mock(JSPlanningProblem.class);
        jsListPlanningProblemMock = mock(JSListPlanningProblem.class);
        when(jsUtilMock.println(anyString())).thenAnswer(invocation -> {});
        when(jsPlanningDomainMock.solveAll(any(JSPlanningProblem.class), anyBoolean())).thenReturn(new ArrayList<>());
        when(jsListPlanningProblemMock.size()).thenReturn(0);

        jsJshop = new JSJshop();
        jsJshop.dom = jsPlanningDomainMock;
        jsJshop.probSet = jsListPlanningProblemMock;
        jsJshop.applet = mock(JApplet.class);
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"domainDef-file-name", "problemDef-file-name"};
        jsJshop.main(args);

        verify(jsUtilMock).println("Usage :");
        verify(jsUtilMock).println(" java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]");
        verify(jsUtilMock, never()).println("Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> one");
        verify(jsUtilMock, never()).println("Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> all");
    }

    @Test
    public void testConstructorWithDomainAndProblemFiles() {
        String domainFileName = "domainFileName";
        String problemFileName = "problemFileName";

        jsJshop = new JSJshop(domainFileName, problemFileName);

        verify(jsUtilMock).println("Reading file " + domainFileName);
        verify(jsUtilMock).println("Domain file parsed successfully");
        verify(jsUtilMock).println("Reading file " + problemFileName);
        verify(jsUtilMock).println("Problem file parsed successfully");
        verifyNoMoreInteractions(jsUtilMock);
    }

    @Test
    public void testGetTree() {
        JSJshopNode expectedTree = mock(JSJshopNode.class);

        when(jsJshop.tree).thenReturn(expectedTree);

        JSJshopNode actualTree = jsJshop.getTree();

        assertEquals(expectedTree, actualTree);
    }

    @Test
    public void testGetSolution() {
        JSPairPlanTSListNodes expectedSolution = mock(JSPairPlanTSListNodes.class);

        when(jsJshop.solution).thenReturn(expectedSolution);

        JSPairPlanTSListNodes actualSolution = jsJshop.getSolution();

        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void testGetAddList() {
        JSListLogicalAtoms expectedAddList = mock(JSListLogicalAtoms.class);

        when(jsJshop.solution.planS().tState().addList()).thenReturn(expectedAddList);

        JSListLogicalAtoms actualAddList = jsJshop.getAddList();

        assertEquals(expectedAddList, actualAddList);
    }

    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms expectedDeleteList = mock(JSListLogicalAtoms.class);

        when(jsJshop.solution.planS().tState().deleteList()).thenReturn(expectedDeleteList);

        JSListLogicalAtoms actualDeleteList = jsJshop.getDeleteList();

        assertEquals(expectedDeleteList, actualDeleteList);
    }

    @Test
    public void testSetFileWithFileAndPredicate() {
        String filePath = "filePath";
        JSTaskAtom predicate = mock(JSTaskAtom.class);

        jsJshop.setFile(filePath, predicate);

        verify(jsUtilMock).println("File parsed");
        verify(jsUtilMock).println("<- domain");
        verify(jsPlanningDomainMock).print();
        verify(jsUtilMock).println("<- problem");
        verify(jsPlanningProblemMock).print();
        verifyNoMoreInteractions(jsUtilMock);
    }

    @Test
    public void testParser() {
        JSJshop jsJshopSpy = spy(jsJshop);

        jsJshopSpy.parserFile("farp.shp");

        verify(jsUtilMock).initParseTable(any(StreamTokenizer.class));
        verify(jsUtilMock).println("Error reading control parameters: null");
        verifyNoMoreInteractions(jsUtilMock);
    }

    @Test
    public void testParserFileWithValidFile() throws IOException {
        String libraryFile = "libraryFile";

        jsJshop.parserFile(libraryFile);

        verify(jsUtilMock).initParseTable(any(StreamTokenizer.class));
        verifyNoMoreInteractions(jsUtilMock);
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithInvalidFile() throws IOException {
        String libraryFile = "libraryFile";

        jsJshop.parserFile(libraryFile);

        fail("parserFile should throw JSParserError exception");
    }

    @Test
    public void testGetBufferedReaderWithAppletAndFile() {
        String file = "file";
        JApplet applet = mock(JApplet.class);

        InputStream expectedInputStream = mock(InputStream.class);
        URL expectedUrl = mock(URL.class);

        when(applet.getCodeBase()).thenReturn(expectedUrl);
        when(expectedUrl.openStream()).thenReturn(expectedInputStream);

        BufferedReader actualBufferedReader = jsJshop.getBufferedReader(null, file, applet);

        assertEquals(expectedInputStream, actualBufferedReader.underlyingStream());
    }

    @Test
    public void testGetBufferedReaderWithFile() {
        String file = "file";

        BufferedReader expectedBufferedReader = mock(BufferedReader.class);

        when(jsJshop.getBufferedReader(null, file, null)).thenReturn(expectedBufferedReader);

        BufferedReader actualBufferedReader = jsJshop.getBufferedReader(file);

        assertEquals(expectedBufferedReader, actualBufferedReader);
    }

    @Test
    public void testGetAppletURLWithFileAndApplet() {
        String file = "file";
        JApplet applet = mock(JApplet.class);

        URL expectedUrl = mock(URL.class);

        when(applet.getCodeBase()).thenReturn(expectedUrl);
        when(expectedUrl.toString()).thenReturn(file);

        URL actualUrl = jsJshop.getAppletURL(file, applet);

        assertEquals(file, actualUrl.toString());
    }

    @Test
    public void testProcessTokenWithExpectedToken() throws IOException {
        StreamTokenizer tokenizerMock = mock(StreamTokenizer.class);

        jsJshop.processToken(tokenizerMock);

        verify(tokenizerMock).nextToken();
        verify(tokenizerMock).nextToken();
        verify(tokenizerMock).nextToken();
        verify(tokenizerMock).expectToken(-3);
        verify(tokenizerMock, never()).expectToken(JSJshopVars.leftPar);
        verifyNoMoreInteractions(tokenizerMock);
    }

    @Test
    public void testProcessTokenWithUnexpectedToken() throws IOException {
        StreamTokenizer tokenizerMock = mock(StreamTokenizer.class);

        when(tokenizerMock.nextToken()).thenReturn('X');

        jsJshop.processToken(tokenizerMock);

        verify(tokenizerMock).nextToken();
        verify(jsUtilMock).println("Line : " + eq(JSUtil.getCurrentLineNumber(tokenizerMock)) + " Expected 'defdomain or defproblem' ");
        verify(jsUtilMock).println("Expected 'defdomain or defproblem' ");
        verify(tokenizerMock, never()).expectToken(JSJshopVars.leftPar);
        verifyNoMoreInteractions(tokenizerMock);
    }
}