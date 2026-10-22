package app;

import dados.Dados;
import dados.LeitorCSV;

import java.io.IOException;
import java.util.List;
import desempenho.PesquisaSequencial;
import estatistica.*;
import hash.TabelaHash;
import model.Estudante;

public class Main {
    public static void main(String[] args)throws IOException {
        LeitorCSV l = new LeitorCSV();
        Dados d = l.lerDados();

        List<Estudante> estudantes = d.getTabelaHash().listar();
        for(Estudante e : estudantes) {
            IO.println(e);
        }

        IO.println(d.getInvalidos());
        IO.println(d.getValidos());

        Estatistica stat3 = new EstatisticaHashTable(d.getTabelaHash());
        IO.println(stat3.toString());

        menu menu = new menu(); menu.executar();
    }

}

