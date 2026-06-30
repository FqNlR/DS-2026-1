package gerador.arquivo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/*
    Gerencia operações de leitura e escrita de arquivos (texto e MIDI).
    Responsabilidade única: I/O de arquivos.
*/
public class GerenciadorDeArquivos {

    private File ultimoArquivoAberto;

    /*
        Abre um diálogo para o usuário selecionar um arquivo de texto (.txt)
        e retorna seu conteúdo.
     
        @param componentePai componente pai para o diálogo
        @return conteúdo do arquivo ou null se cancelado
    */
    public String lerArquivoTexto(java.awt.Component componentePai) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Abrir arquivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));

        int resultado = chooser.showOpenDialog(componentePai);
        if (resultado != JFileChooser.APPROVE_OPTION) return null;

        ultimoArquivoAberto = chooser.getSelectedFile();

        try {
            return Files.readString(ultimoArquivoAberto.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    /*
        Salva o texto no arquivo original (se foi aberto de um arquivo).
    
        @param texto conteúdo a ser salvo
        @return true se salvou com sucesso
    */
    public boolean salvarTextoOriginal(String texto) {
        if (ultimoArquivoAberto == null) return false;

        try {
            Files.writeString(ultimoArquivoAberto.toPath(), texto, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /*
        Abre um diálogo para o usuário escolher onde salvar o arquivo MIDI.
    
        @param componentePai componente pai para o diálogo
        @return caminho do arquivo escolhido ou null se cancelado
    */
    public String escolherCaminhoMidi(java.awt.Component componentePai) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar arquivo MIDI");
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivo MIDI (*.mid)", "mid"));
        chooser.setSelectedFile(new File("musica_gerada.mid"));

        int resultado = chooser.showSaveDialog(componentePai);
        if (resultado != JFileChooser.APPROVE_OPTION) return null;

        String caminho = chooser.getSelectedFile().getAbsolutePath();
        if (!caminho.toLowerCase().endsWith(".mid")) {
            caminho += ".mid";
        }
        return caminho;
    }

    
    //Indica se há um arquivo de texto original associado.
    public boolean temArquivoOriginal() {
        return ultimoArquivoAberto != null;
    }

    public String getNomeArquivoOriginal() {
        return ultimoArquivoAberto != null ? ultimoArquivoAberto.getName() : "";
    }
}
