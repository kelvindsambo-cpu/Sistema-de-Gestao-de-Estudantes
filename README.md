# Sistema de Gestão de Estudantes com Tabela Hash

Trabalho prático de **Estruturas de Dados e Algoritmos** (UEM – Faculdade de Engenharia).

Sistema em Java que lê `estudantes.csv`, guarda os estudantes numa **Tabela Hash própria**
(endereçamento aberto com **Double Hashing**), e permite pesquisar, inserir e remover estudantes,
ver estatísticas dos estudantes e da tabela, e comparar a pesquisa Hash com a pesquisa sequencial.

**Grupo:** Euclides Gune · Gerson Teodósio Nogueira · Cíntia de Carmen Sílvio Saite · Kelvin Damião Sambo

## Requisitos

- **JDK 17 ou superior** (o código compila com `--release 17`; testado com JDK 26).
  Verificar com `java -version` e `javac -version`.
- Não são necessárias bibliotecas externas.

## Como executar

Todos os comandos são executados **na raiz do projecto** (a pasta que contém `src/`).

### Windows (PowerShell)

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out app.Main
```

### Linux / macOS / Git Bash

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

O programa procura o ficheiro em `src/dados/estudantes.csv`, por isso deve ser executado a partir
da raiz do projecto.

### VS Code / IntelliJ

Abrir a pasta do projecto e executar a classe `app.Main` (botão *Run*).
No VS Code a pasta `src` já está configurada como pasta de código-fonte.

## Menu

```
1 - Carregar ficheiro CSV            (lê, valida, insere e mostra o relatório estatístico)
2 - Pesquisar estudante              (por código; mostra as posições sondadas)
3 - Inserir estudante
4 - Remover estudante                (a posição fica REMOVIDO, não VAZIO)
5 - Estatisticas dos estudantes      (geral, sexo, curso, ano, médias, idade)
6 - Estatisticas da Hash Table
7 - Executar teste de desempenho     (1 000, 5 000 e 10 000 registos)
8 - Comparar pesquisa sequencial e Hash
9 - Demonstrar colisao e REMOVIDO    (mostra k, h1, h2 e cada posição sondada)
0 - Sair
```

As opções 2 a 6 e 9 precisam que o CSV tenha sido carregado (opção 1).
As opções 7 e 8 lêem o CSV por conta própria e demoram cerca de 15 a 30 segundos.

### Teste obrigatório da defesa (colisão + REMOVIDO)

1. Opção **1** (carregar o CSV).
2. Opção **9**: o programa indica duas chaves em colisão, por exemplo `20261739` e `20267732`
   (`20267732` tem a mesma posição inicial que `20261739`). Introduzir `20267732` para ver a sondagem.
3. Opção **4**: remover `20261739`.
4. Opção **9** com `20267732`: a posição inicial aparece como `REMOVIDO` e a pesquisa continua
   até encontrar o estudante. A opção **2** confirma que o estudante continua a ser encontrado.

## Estrutura do projecto

```
src/
├── app/            Main (ponto de entrada) e Menu (interface textual)
├── dados/          LeitorCSV (leitura e validação), Dados (contadores da leitura), estudantes.csv
├── model/          Estudante, Entrada, EstadoEntrada (VAZIO, OCUPADO, REMOVIDO)
├── hash/           TabelaHash (Double Hashing, redimensionamento), CodigoDuplicadoException
├── estatistica/    Estatísticas dos estudantes e da Tabela Hash
├── desempenho/     Desempenho (experiências) e PesquisaSequencial
└── utils/          Validacao, Resposta, EstadoValidacao, Rounder
docs/               Relatório (PDF e Word)
resultados/         Resultados dos testes experimentais
```

## Tabela Hash (resumo)

- Array unidimensional `Entrada[]`; capacidade inicial **1009** (primo).
- Chave: `codigo`, convertido para inteiro com `k = 31·k + carácter` (sempre não negativo).
- `h1(k) = k mod m`, `h2(k) = 1 + (k mod (m − 1))`, sondagem `h(k, i) = (h1(k) + i·h2(k)) mod m`.
- Quando `α = n / m > 0,70`, a capacidade passa para o **próximo primo ≥ 2m** e todos os
  elementos são reinseridos com h1 e h2.

Os detalhes e a análise dos resultados estão no relatório em [`docs/`](docs/).

## Notas de implementação

**Leitura e validação (`dados`, `utils`)**

- Cada linha do CSV tem de ter exactamente 7 campos (`split(",", -1)` mantém os campos vazios no fim,
  para que uma linha incompleta seja detectada). Linhas vazias, por exemplo no fim do ficheiro, são ignoradas.
- `lerDados(limite)` lê no máximo `limite` linhas (usado nos testes de desempenho);
  `lerEstudantes()` devolve os registos válidos pela ordem do ficheiro, sem os inserir em nenhuma tabela.
- A regra da média é escrita pela positiva (`media >= 0 && media <= 20`) para que `NaN` também seja rejeitado.
- `Dados` conta os inseridos durante a leitura, por isso as estatísticas gerais da leitura não mudam
  com inserções e remoções feitas depois pelo menu.
- `Rounder.formatar` usa sempre ponto decimal (como nos exemplos do enunciado), seja qual for a língua do sistema.

**Estatísticas (`estatistica`)**

- As estatísticas dos estudantes são calculadas sobre os estudantes que estão na Tabela Hash
  (e não relendo o CSV), pelo que reflectem as inserções e remoções feitas pelo menu.
- As percentagens são de 0 a 100; a idade média é calculada em `double`.
- O mínimo e o máximo são verificados com dois `if` separados, porque o mesmo estudante
  (o primeiro) pode ser ao mesmo tempo o novo mínimo e o novo máximo.
- Os cursos são detectados a partir dos dados; como são poucos, uma lista com pesquisa linear basta
  (e evita usar estruturas Hash prontas).
- `EstatisticaHashTable` não estende `Estatistica`, porque descreve a estrutura e não os estudantes.

**Desempenho (`desempenho`)**

- O CSV é lido uma só vez, fora das medições: o tempo de inserção mede só a inserção na tabela
  (incluindo os redimensionamentos).
- As pesquisas usam 1 000 códigos existentes, espalhados uniformemente pelo ficheiro, e 1 000 códigos
  inexistentes (`99990000` a `99990999`); apresenta-se o tempo médio por pesquisa.
- Antes de medir, tudo é executado uma vez (aquecimento), porque a máquina virtual Java compila o código
  durante as primeiras execuções. Cada medição é repetida 10 vezes e usa-se a mediana, que ignora
  repetições isoladas mais lentas (recolha de lixo, outros programas).
- O campo `encontrados` acumula os resultados das pesquisas apenas para o compilador JIT não as eliminar.
- `PesquisaSequencial` percorre os estudantes pela ordem do ficheiro, um a um (O(n)), e conta as comparações.

## Resultados experimentais

O ficheiro [`resultados/resultados_experimentais.txt`](resultados/resultados_experimentais.txt)
contém a saída das opções 6, 7 e 8. Para os gerar de novo:

```powershell
"1`n6`n7`n8`n0" | java -cp out app.Main | Out-File -Encoding utf8 resultados/nova_execucao.txt
```

Os tempos dependem do computador e variam entre execuções; as colisões, as sondagens e o factor
de carga são sempre iguais para o mesmo ficheiro.
