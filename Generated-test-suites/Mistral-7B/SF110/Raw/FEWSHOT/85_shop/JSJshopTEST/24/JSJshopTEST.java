package umd.cs.shop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshopInstance() {
        JSJshop jsjshop = new JSJshop();
        assertNotNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopInstanceWithInvalidArguments() {
        String[] args = new String[0];
        JSJshop jsjshop = new JSJshop(args);
    }

    @Test
    public void testJSJshopWithValidArguments() throws IOException {
        String domainFileName = "sample_domain.shp";
        String problemFileName = "sample_problem.shp";
        JSJshop jsjshop = new JSJshop(domainFileName, problemFileName);
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNotNull(tree);
        assertNotNull(solution);
        List<JSJshopNode> listNodes = solution.listNodes();
        for (JSJshopNode node : listNodes) {
            node.print2();
        }
    }

    @Test
    public void testJSJshopWithFileAndPredicate() throws IOException {
        String nameFile = "sample.shp";
        JSTaskAtom predicate = new JSTaskAtom("sample_task");
        JSJshop jsjshop = new JSJshop(nameFile, predicate);
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNotNull(tree);
        assertNotNull(solution);
    }

    @Test
    public void testParser() throws IOException {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    @Test
    public void testBufferedReader() throws IOException {
        String dir = ".";
        String file = "sample.shp";
        BufferedReader br = JSJshop.getBufferedReader(dir, file);
        assertNotNull(br);
        br.close();
    }

    @Test
    public void testGetAppletURL() {
        JFrame frame = new JFrame();
        String file = "sample.shp";
        URL url = JSJshop.getAppletURL(file, frame.getAppletContext());
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        String input = "defdomain sample_domain";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop.processToken(tokenizer);
        assertNotNull(JSJshop.dom);
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenWithInvalidToken() {
        String input = "defdomain sample_domain%%%";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop.processToken(tokenizer);
    }
}