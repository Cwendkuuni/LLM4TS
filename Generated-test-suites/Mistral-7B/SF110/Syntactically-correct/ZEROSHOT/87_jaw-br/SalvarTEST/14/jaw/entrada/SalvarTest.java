package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Salvar;
import jaw.gui.ProcessarEntidades;
import jaw.metamodelo.Entidade;
import jaw.metamodelo.Pessoa;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;
    private List<Entidade> entidades;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        entidades = new ArrayList<Entidade>();
        entidades.add(new Pessoa("João da Silva", "12345678901-9"));
        salvar = new Salvar(form, Salvar.SALVAR);
    }

    @After
    public void tearDown() {
        File tempFile = new File("temp.jaw");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testInicializacao() {
        assertFalse(salvar.vela);
        assertNull(salvar.arquivo);
        assertNull(salvar.xml);
        assertSame(new Vector<Entidade>(), salvar.entidade);
    }

    @Test
    public void testSalvarJawArquivoExistente() {
        File file = new File("temp.jaw");
        file.createNewFile();
        salvar.arquivo = file.getAbsolutePath();
        salvar.vela = true;
        salvar.Salvar();
        assertFalse(file.exists());
        fail("Expected an IOException");
    }

    @Test(expected = FileNotFoundException.class)
    public void testSalvarJawArquivoNaoExistente() {
        salvar.arquivo = "temp.jaw";
        salvar.vela = true;
        salvar.Salvar();
    }

    @Test
    public void testSalvarJawArquivoNovo() {
        salvar.arquivo = "temp.jaw";
        salvar.vela = true;
        salvar.Salvar();
        assertTrue(new File("temp.jaw").exists());
        salvar.Salvar();
        assertTrue(new File("temp.jaw").exists());
    }

    @Test
    public void testSalvarJawArquivoNovoComNomeDiferente() {
        salvar.arquivo = "temp.jaw2";
        salvar.vela = true;
        salvar.Salvar();
        assertTrue(new File("temp.jaw2").exists());
    }

    @Test
    public void testSalvarXml() {
        salvar.arquivo = "temp.jaw.xml";
        salvar.xml = "<entidade><pessoa><nome>João da Silva</nome><cpf>12345678901-9</cpf></pessoa></entidade>";
        salvar.SalvarXml();
        File file = new File("temp.jaw.xml");
        assertTrue(file.exists());
        try {
            FileReader reader = new FileReader(file);
            assertEquals(salvar.xml, reader.readAllLines()[0]);
            reader.close();
        } catch (IOException e) {
            fail("Expected the file to be readable");
        }
    }

    @Test(expected = IOException.class)
    public void testSalvarXmlArquivoNaoExistente() {
        salvar.arquivo = "temp.jaw.xml";
        salvar.SalvarXml();
    }

    @Test
    public void testGetEntidades() {
        salvar.entidade = form.getEntidades();
        assertEquals(entidades.size(), salvar.entidade.size());
        for (Entidade e : entidades) {
            Entidade e2 = (Entidade) salvar.entidade.get(salvar.entidade.indexOf(e));
            assertEquals(e, e2);
        }
    }
}