package estatistica;
import java.io.IOException;
import dados.*;
public class EstatisticaGeral extends Estatistica{
    private LeitorCSV leitor = new LeitorCSV();
    private Dados dados;
    public EstatisticaGeral() throws IOException {
        this.dados = leitor.lerDados();
    }
    @Override
    public String toString(){
        return "============= Estatistica Geral ===========\n"+
                "Total de Linhas: "+dados.getLidas()+"\n"+
                "Estudantes Inseridos: "+totalEstudantes+"\n"+
                "Registos válidos: "+dados.getValidos()+"\n"+
                "Registos inválidos: "+dados.getInvalidos()+"\n";
    }
}
