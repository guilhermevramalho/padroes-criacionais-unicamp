package br.unicamp.padroescriacionais.legacy.service;

import br.unicamp.padroescriacionais.legacy.domain.ConfiguracaoSistema;
import br.unicamp.padroescriacionais.legacy.domain.FormatoRelatorio;
import br.unicamp.padroescriacionais.legacy.domain.Relatorio;
import br.unicamp.padroescriacionais.legacy.factory.DefaultRelatorioGeneratorFactory;
import br.unicamp.padroescriacionais.legacy.factory.RelatorioGeneratorFactory;
import br.unicamp.padroescriacionais.legacy.generator.RelatorioGenerator;

public class ExportacaoService {

    private final RelatorioGeneratorFactory generatorFactory;

    public ExportacaoService() {
        this.generatorFactory = new DefaultRelatorioGeneratorFactory();
    }

    // Construtor que permite injeção de fábrica (facilita testes)
    public ExportacaoService(RelatorioGeneratorFactory generatorFactory) {
        this.generatorFactory = generatorFactory;
    }

    public void exportar(Relatorio relatorio, FormatoRelatorio formato) {
        RelatorioGenerator generator = generatorFactory.criarGenerator(formato);
        String conteudoFormatado = generator.gerar(relatorio);

        ConfiguracaoSistema config = ConfiguracaoSistema.getInstance();

        String extensao = formato.name().toLowerCase();
        String nomeArquivo = relatorio.getTitulo()
                .replace(" ", "_")
                .toLowerCase()
                + "." + extensao;

        String caminhoCompleto = config.getDiretorioExportacao() + "/" + nomeArquivo;

        System.out.println("[EXPORTACAO] Empresa  : " + config.getNomeEmpresa());
        System.out.println("[EXPORTACAO] Ambiente : " + config.getAmbiente());
        System.out.println("[EXPORTACAO] Arquivo  : " + caminhoCompleto);
        System.out.println("[EXPORTACAO] Conteudo :");
        System.out.println(conteudoFormatado);
    }
}
