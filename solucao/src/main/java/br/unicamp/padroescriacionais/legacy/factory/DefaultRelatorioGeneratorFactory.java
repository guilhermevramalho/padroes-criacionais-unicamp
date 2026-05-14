package br.unicamp.padroescriacionais.legacy.factory;

import br.unicamp.padroescriacionais.legacy.domain.FormatoRelatorio;
import br.unicamp.padroescriacionais.legacy.generator.*;

public class DefaultRelatorioGeneratorFactory implements RelatorioGeneratorFactory {

    @Override
    public RelatorioGenerator criarGenerator(FormatoRelatorio formato) {
        return switch (formato) {
            case PDF  -> new PdfRelatorioGenerator();
            case CSV  -> new CsvRelatorioGenerator();
            case JSON -> new JsonRelatorioGenerator();
            case XML  -> new XmlRelatorioGenerator();
            case HTML -> new HtmlRelatorioGenerator();
        };
    }
}
