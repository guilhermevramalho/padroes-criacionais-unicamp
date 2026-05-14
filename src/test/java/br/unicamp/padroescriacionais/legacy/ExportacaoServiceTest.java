package br.unicamp.padroescriacionais.legacy;

import br.unicamp.padroescriacionais.legacy.domain.FormatoRelatorio;
import br.unicamp.padroescriacionais.legacy.domain.Relatorio;
import br.unicamp.padroescriacionais.legacy.domain.TipoRelatorio;
import br.unicamp.padroescriacionais.legacy.service.ExportacaoService;
import br.unicamp.padroescriacionais.legacy.service.RelatorioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ExportacaoServiceTest {

    private ExportacaoService exportacaoService;
    private RelatorioService relatorioService;

    private final PrintStream saidaOriginal = System.out;
    private ByteArrayOutputStream saidaCapturada;

    @BeforeEach
    void setUp() {
        exportacaoService = new ExportacaoService();
        relatorioService  = new RelatorioService();
        saidaCapturada    = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saidaCapturada));
    }

    @AfterEach
    void tearDown() {
        System.setOut(saidaOriginal);
    }

    // Testes originais

    @Test
    void deveExportarRelatorioEmPdfSemErro() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.VENDAS);
        assertDoesNotThrow(() -> exportacaoService.exportar(relatorio, FormatoRelatorio.PDF));
    }

    @Test
    void deveExportarRelatorioEmCsvSemErro() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.ESTOQUE);
        assertDoesNotThrow(() -> exportacaoService.exportar(relatorio, FormatoRelatorio.CSV));
    }

    @Test
    void deveExportarRelatorioEmJsonSemErro() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.CLIENTES);
        assertDoesNotThrow(() -> exportacaoService.exportar(relatorio, FormatoRelatorio.JSON));
    }

    @Test
    void exportacaoDeveExibirCaminhoDoArquivo() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.VENDAS);
        exportacaoService.exportar(relatorio, FormatoRelatorio.PDF);

        String saida = saidaCapturada.toString();
        assertTrue(saida.contains("EXPORTACAO"), "Saida deve conter cabecalho de exportacao");
        assertTrue(saida.contains(".pdf"), "Saida deve informar o nome do arquivo com extensao .pdf");
    }

    @Test
    void exportacaoDeveExibirNomeDaEmpresa() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.CLIENTES);
        exportacaoService.exportar(relatorio, FormatoRelatorio.CSV);

        String saida = saidaCapturada.toString();
        assertFalse(saida.isBlank());
        assertTrue(saida.contains("Empresa"), "Saida deve mencionar a empresa");
    }

    @Test
    void exportacaoDeveExibirConteudoDoRelatorio() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.ESTOQUE);
        exportacaoService.exportar(relatorio, FormatoRelatorio.JSON);

        String saida = saidaCapturada.toString();
        assertTrue(saida.contains("{"), "Saida JSON deve conter '{'");
    }

    @Test
    void todosFormatosDevemSerExportadosSemErro() {
        for (FormatoRelatorio formato : FormatoRelatorio.values()) {
            Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.VENDAS);
            assertDoesNotThrow(
                    () -> exportacaoService.exportar(relatorio, formato),
                    "Exportacao falhou para formato: " + formato
            );
        }
    }

    // Novos testes: formatos XML e HTML

    @Test
    void deveExportarRelatorioEmXmlSemErro() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.VENDAS);
        assertDoesNotThrow(() -> exportacaoService.exportar(relatorio, FormatoRelatorio.XML));
    }

    @Test
    void exportacaoXmlDeveConterTagsXml() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.VENDAS);
        exportacaoService.exportar(relatorio, FormatoRelatorio.XML);

        String saida = saidaCapturada.toString();
        assertTrue(saida.contains("<relatorio>"), "XML deve conter tag <relatorio>");
        assertTrue(saida.contains("</relatorio>"), "XML deve fechar tag </relatorio>");
        assertTrue(saida.contains(".xml"), "Extensao do arquivo deve ser .xml");
    }

    @Test
    void deveExportarRelatorioEmHtmlSemErro() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.CLIENTES);
        assertDoesNotThrow(() -> exportacaoService.exportar(relatorio, FormatoRelatorio.HTML));
    }

    @Test
    void exportacaoHtmlDeveConterTagsHtml() {
        Relatorio relatorio = relatorioService.criarRelatorio(TipoRelatorio.CLIENTES);
        exportacaoService.exportar(relatorio, FormatoRelatorio.HTML);

        String saida = saidaCapturada.toString();
        assertTrue(saida.contains("<html"), "HTML deve conter tag <html");
        assertTrue(saida.contains("</html>"), "HTML deve fechar </html>");
        assertTrue(saida.contains(".html"), "Extensao do arquivo deve ser .html");
    }
}
