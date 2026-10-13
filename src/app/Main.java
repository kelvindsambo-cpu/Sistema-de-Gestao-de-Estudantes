package app;

import dados.Dados;
import dados.LeitorCSV;
import java.util.List;
import desempenho.PesquisaSequencial;
import estatistica.*;
import hash.TabelaHash;
import model.Estudante;

public class Main {
    public static void main(String[] args) {
        LeitorCSV l = new LeitorCSV();
        Dados d = l.lerDados();

        List<Estudante> estudantes = d.getTabelaHash().listar();
        for(Estudante e : estudantes) {
            IO.println(e);
        }

        IO.println(d.getInvalidos());
        IO.println(d.getValidos());

        PesquisaSequencial ps = new PesquisaSequencial();
        TabelaHash th = l.lerDados().getTabelaHash();
        String codigo = "20265555";

        long inicio = System.nanoTime();
        Estudante e = ps.pesquisar(codigo);
       IO.println("Pesquisa Sequencial "+e.toString());
        long fim = System.nanoTime();
        long total = (fim - inicio)/1000;

        long inicio1 = System.nanoTime();
        Estudante e1 = th.pesquisar(codigo);
        IO.println("Pesquisa Tabela Hash "+e.toString());
        long fim1 = System.nanoTime();
        long total1 = (fim1 - inicio1)/1000;
//Desempenho em micro-segundos (µs)
        System.out.println(
                "Desempenho"+"\n"+
                        "Pesquisa Sequencial: "+total+"µs\n"+
                        "Pesquisa Tabela Hash: "+total1+"µs\n"
        );

        Estatistica stat3 = new EstatisticaPorCurso();
        IO.println(stat3.toString());
    }

}

