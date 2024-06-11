package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {
    @Test
    public void testSalvar() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        String expectedArquivo = "test/expected/output.jaw";
        File expectedFile = new File(expectedArquivo);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(expectedFile);
        salvar.Salvar(null, fileChooser, null);
        assertEquals(expectedArquivo, salvar.getArquivo());
    }

    @Test(expected = Exception.class)
    public void testSalvarWithInvalidFormat() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("test/invalidFormat.txt"));
        salvar.Salvar(null, fileChooser, null);
    }

    @Test
    public void testSaveInInvalidDirectory() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("test/invalidDirectory/"));
        salvar.Salvar(null, fileChooser, null);
        assertNull(salvar.getArquivo());
    }

    @Test(expected = Exception.class)
    public void testSaveWithEmptyEntidades() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        salvar.Salvar(null, null, null);
    }

    @Test
    public void testExportXML() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR_XML);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n</entidades>";
        String xml = salvar.getXml();
        File xmlFile = File.createTempFile("test", ".xml");
        FileWriter xmlWriter = new FileWriter(xmlFile);
        xmlWriter.write(expectedXml);
        xmlWriter.close();
        salvar.setXml(xml);
        salvar.SalvarXML(xmlFile.getAbsolutePath());
        assertEquals(expectedXml, salvar.getXml());
    }

    @Test
    public void testCancelSave() throws Exception {
        Salvar salvar = new Salvar(new ProcessarEntidades(), Salvar.SALVAR);
        List<Entidade> entidades = new ArrayList<>();
        salvar.setEntidade(entidades);
        JFileChooser fileChooser = new JFileChooser();
        salvar.Salvar(null, fileChooser, null);
        assertNull(salvar.getArquivo());
    }
}