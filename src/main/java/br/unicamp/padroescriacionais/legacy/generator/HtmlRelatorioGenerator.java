package br.unicamp.padroescriacionais.legacy.generator;

import br.unicamp.padroescriacionais.legacy.domain.Relatorio;

public class HtmlRelatorioGenerator implements RelatorioGenerator {

    @Override
    public String gerar(Relatorio relatorio) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"pt-BR\">\n<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <title>").append(escapeHtml(relatorio.getTitulo())).append("</title>\n");
        sb.append("</head>\n<body>\n");
        sb.append("  <h1>").append(escapeHtml(relatorio.getTitulo())).append("</h1>\n");
        sb.append("  <p><strong>Tipo:</strong> ").append(relatorio.getTipo().name()).append("</p>\n");
        sb.append("  <p><strong>Gerado em:</strong> ").append(relatorio.getDataGeracao()).append("</p>\n");
        sb.append("  <hr/>\n");
        sb.append("  <pre>").append(escapeHtml(relatorio.getConteudo())).append("</pre>\n");
        sb.append("</body>\n</html>\n");
        return sb.toString();
    }

    private String escapeHtml(String valor) {
        if (valor == null) return "";
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
