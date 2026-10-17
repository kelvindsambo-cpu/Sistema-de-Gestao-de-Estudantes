package desempenho;

import dados.LeitorCSV;
import hash.TabelaHash;
import model.Estudante;

import java.util.List;

public class PesquisaSequencial {
    private LeitorCSV leitura;
    private List<Estudante> listaEstudantes;

    public PesquisaSequencial(){
        this.leitura = new LeitorCSV();
        this.listaEstudantes = leitura.lerDados().getTabelaHash().listar();
    }

    public Estudante pesquisar(String codigo){
        Estudante estudante = null;
        for (Estudante e : listaEstudantes) {
            if(e.getCodigo().equals(codigo)){
                return e;
            }
        }
        return estudante;
    }

}
