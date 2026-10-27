package dados;

import hash.CodigoDuplicadoException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.Estudante;
import utils.EstadoValidacao;
import utils.Validacao;

public class LeitorCSV {

    private static final String NOME_FICHEIRO = "estudantes.csv";
    private static final int NUM_CAMPOS = 7;
    // Usado quando o CSV não está no classpath (ex.: compilado com javac -d out),
    // relativo à raiz do projecto.
    private static final Path CAMINHO_ALTERNATIVO = Path.of("src", "dados", NOME_FICHEIRO);

    // Procura o CSV ao lado de LeitorCSV.class (funciona no VS Code, que copia
    // src/dados/estudantes.csv para a pasta de saída), e depois em src/dados/.
    public BufferedReader abrirFicheiro() throws IOException {
        InputStream in = LeitorCSV.class.getResourceAsStream(NOME_FICHEIRO);
        if (in != null) {
            return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        }
        if (Files.exists(CAMINHO_ALTERNATIVO)) {
            return Files.newBufferedReader(CAMINHO_ALTERNATIVO, StandardCharsets.UTF_8);
        }
        throw new FileNotFoundException(NOME_FICHEIRO + " não encontrado no classpath nem em "
                + CAMINHO_ALTERNATIVO.toAbsolutePath());
    }

    public Dados lerDados() {
        return lerDados(Integer.MAX_VALUE);
    }

    public Dados lerDados(int limite){//Método para testar o desempenho

        Dados dados = new Dados();

        try(BufferedReader br = abrirFicheiro()) {
            br.readLine();
            String linha;

            while(dados.getLidas() < limite && (linha = br.readLine()) != null) {
                if (linha.isBlank()) {
                    continue;
                }
                dados.incrementarLidas();

                Estudante estudante = converterLinha(linha);

                if (estudante == null
                        || Validacao.validarEstudante(estudante).getEstado() == EstadoValidacao.INVALIDO) {
                    dados.incrementarInvalidos();
                    continue;
                }

                try {
                    dados.inserir(estudante);
                } catch (CodigoDuplicadoException e) {
                    dados.incrementarDuplicados();
                }
            }

        } catch(IOException e) {
            System.out.println("Erro ao ler dados do ficheiro estudantes.csv: " + e.getMessage() + "\n");
        }

        return dados;
    }

    public List<Estudante> lerEstudantes() {

        List<Estudante> estudantes = new ArrayList<>();

        try(BufferedReader br = abrirFicheiro()) {
            br.readLine();
            String linha;

            while((linha = br.readLine()) != null) {
                Estudante estudante = linha.isBlank() ? null : converterLinha(linha);

                if (estudante != null
                        && Validacao.validarEstudante(estudante).getEstado() == EstadoValidacao.VALIDO) {
                    estudantes.add(estudante);
                }
            }

        } catch(IOException e) {
            System.out.println("Erro ao ler dados do ficheiro estudantes.csv: " + e.getMessage() + "\n");
        }

        return estudantes;
    }

    private Estudante converterLinha(String linha) {
        String[] campos = linha.split(",", -1);

        if (campos.length != NUM_CAMPOS) {
            return null;
        }

        try {
            return new Estudante(
                    campos[0].trim(),
                    campos[1].trim(),
                    campos[2].trim(),
                    Integer.parseInt(campos[3].trim()),
                    Integer.parseInt(campos[4].trim()),
                    campos[5].trim().toUpperCase(),
                    Double.parseDouble(campos[6].trim())
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
