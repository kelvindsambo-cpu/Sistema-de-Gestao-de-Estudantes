package estatistica;
import dados.*;
public class EstatisticaGeral extends Estatistica{
    private final Dados dados;

    public EstatisticaGeral(Dados dados) {
        super(dados.getTabelaHash().listar());
        this.dados = dados;
    }

    @Override
    public String toString(){
        return "============= Estatistica Geral =============\n"+
                "Total de linhas lidas: "+dados.getLidas()+"\n"+
                "Registos validos: "+dados.getValidos()+"\n"+
                "Registos invalidos: "+dados.getInvalidos()+"\n"+
                "Registos duplicados (validos, nao inseridos): "+dados.getDuplicados()+"\n"+
                "Estudantes inseridos: "+dados.getInseridos()+"\n"+
                "Estudantes actualmente na tabela: "+totalEstudantes+"\n";
    }
}
