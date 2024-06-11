package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jaw.metamodelo.Entidade;
import jaw.metamodelo.Metamodelo;
import jaw.gui.ProcessarEntidades;
import jaw.salvar.Salvar;

public class SalvarTest {

    @Test
    public void testSalvarComArquivoValido() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        Metamodelo metamodelo = new Metamodelo();
        form.setMetamodelo(metamodelo);

        Entidade entidade1 = new Entidade("Entidade1", "Descricao1");
        Entidade entidade2 = new Entidade("Entidade2", "Descricao2");
        entidades.add(entidade1);
        entidades.add(entidade2);
        form.setEntidades(entidades);

        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        File arquivoSelecionado = new File("path/to/valid/save/file.jaw");
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(arquivoSelecionado);
        salvar.vela = true;

        salvar.salvar(entidades, arquivoSelecionado.getAbsolutePath());

        assertTrue(salvar.xml.isEmpty());
        assertTrue(new File(arquivoSelecionado.getAbsolutePath()).exists());
        assertTrue(new File(arquivoSelecionado.getAbsolutePath()).length() > 0);
    }

    @Test
    public void testSalvarComXMLValido() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        Metamodelo metamodelo = new Metamodelo();
        form.setMetamodelo(metamodelo);

        Entidade entidade1 = new Entidade("Entidade1", "Descricao1");
        Entidade entidade2 = new Entidade("Entidade2", "Descricao2");
        entidades.add(entidade1);
        entidades.add(entidade2);
        form.setEntidades(entidades);

        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        File arquivoSelecionado = new File("path/to/valid/save/file.jaw.xml");
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(arquivoSelecionado);
        salvar.vela = false;
        salvar.arquivo = arquivoSelecionado.getAbsolutePath();

        salvar.salvar(entidades, arquivoSelecionado.getAbsolutePath());

        String xmlEsperado = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entidades>\n<entidade>\n<nome>Entidade1</nome>\n<descricao>Descricao1</descricao>\n</entidade>\n<entidade>\n<nome>Entidade2</nome>\n<descricao>Descricao2</descricao>\n</entidade>\n</entidades>";

        String xmlObtido = salvar.xml;

        assertEquals(xmlEsperado, xmlObtido);
        assertTrue(new File(arquivoSelecionado.getAbsolutePath()).exists());
        assertTrue(new File(arquivoSelecionado.getAbsolutePath()).length() > 0);
    }

    @Test
    public void testSalvarSemArquivoSelecionado() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        Metamodelo metamodelo = new Metamodelo();
        form.setMetamodelo(metamodelo);

        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        salvar.salvar(entidades, null);

        assertTrue(salvar.xml.isEmpty());
        assertFalse(salvar.vela);
        assertFalse(new File("path/to/save/file.jaw").exists());
    }

    @Test
    public void testSalvarSemEntidades() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        salvar.salvar(null, null);

        assertTrue(salvar.xml.isEmpty());
        assertFalse(salvar.vela);
        assertFalse(new File("path/to/save/file.jaw").exists());
    }

    @Test(expected = Exception.class)
    public void testSalvarComErroDeSerializacao() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        Metamodelo metamodelo = new Metamodelo();
        form.setMetamodelo(metamodelo);

        Entidade entidade1 = new Entidade("Entidade1", "Descricao1");
        Entidade entidade2 = new Entidade("Entidade2", "Descricao2");
        entidades.add(entidade1);
        entidades.add(entidade2);
        form.setEntidades(entidades);

        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        File arquivoSelecionado = new File("path/to/save/file.jaw");
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(arquivoSelecionado);
        salvar.vela = true;

        try {
            salvar.salvar(null, arquivoSelecionado.getAbsolutePath());
        } catch (Exception e) {
            fail("Salvar.salvar() deve lançar uma exceção com a mensagem \"Não foi possível salvar esse arquivo. Verifique o formato das entidades !\"");
        }

        fail("Salvar.salvar() deve lançar uma exceção");
    }

    @Test(expected = Exception.class)
    public void testSalvarXMLComErroDeSerializacao() {
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> entidades = new ArrayList<>();
        Metamodelo metamodelo = new Metamodelo();
        form.setMetamodelo(metamodelo);

        Entidade entidade1 = new Entidade("Entidade1", "Descricao1");
        Entidade entidade2 = new Entidade("Entidade2", "Descricao2");
        entidades.add(entidade1);
        entidades.add(entidade2);
        form.setEntidades(entidades);

        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        File arquivoSelecionado = new File("path/to/save/file.jaw.xml");
        salvar.arquivo = arquivoSelecionado.getAbsolutePath();

        try {
            salvar.salvar(null, arquivoSelecionado.getAbsolutePath());
        } catch (Exception e) {
            fail("Salvar.salvar() deve lançar uma exceção com a mensagem \"Não foi possível salvar esse arquivo. Verifique o formato das entidades !\"");
        }

        fail("Salvar.salvar() deve lançar uma exceção");
    }

    @Test
    public void testGetArquivo() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        String arquivo = "path/to/save/file.jaw";
        salvar.arquivo = arquivo;

        assertEquals(arquivo, salvar.getArquivo());
    }

    @Test
    public void testGetEntidades() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        List<Entidade> entidades = new ArrayList<>();
        Entidade entidade1 = new Entidade("Entidade1", "Descricao1");
        Entidade entidade2 = new Entidade("Entidade2", "Descricao2");
        entidades.add(entidade1);
        entidades.add(entidade2);

        salvar.entidade = entidades;

        assertEquals(entidades, salvar.getEntidades());
    }

    @Test
    public void testIsVela() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        salvar.vela = true;

        assertTrue(salvar.isVela());
    }

    @Test
    public void testIsVelaFalse() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        salvar.vela = false;

        assertFalse(salvar.isVela());
    }

}