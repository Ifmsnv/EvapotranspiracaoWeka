ClassifyInstance - Exemplo de treinamento de um algoritmo e classificar uma instancia.
CreateDatabase - Criar o banco de dados
Csv2Arff - Converte arquivo CSV para ARFF.
CsvSeparatorSeasons - Separa arquivos CSV em estações do ano.
GenerateModel - Faz o treinameto do algoritmo e salva o Modelo.
HiddenLayersVariation - Calcula a melhor distribuição de neoronios.
RunModel - Classifica uma instância de acordo com o modelo.

Processo 1
. O Lucas me envia os arquivos no formato Excel
. Converter o arquivo para CSV - O separador decimal deve ser . (ponto)
. Separar o arquivo em estações (CsvSeparatorSeasons)
. Gerar arquivos ARFF (Csv2Arff)
. Calcular a melhor distribuição de neorônios (HiddenLayersVariation)

Processo 2
. O Lucas me envia os arquivos no formato Excel
. Converter o arquivo para CSV - O separador decimal deve ser . (ponto)
. Separar o arquivo em meses (CsvSeparatorMonth)
. Gerar arquivos ARFF (Csv2Arff)
. Calcular a melhor distribuição de neorônios (HiddenLayersVariation)

Processo 3
. Lucas me envia os arquivos no formato Excel
. Converter as planilhas do arquivo em CSV
. Executar o Processo3.java (agrupador dos seguintes subtarefas)
    . Separar o arquivo em estações (CsvSeparatorSeasons)
    . Gerar arquivos ARFF (Csv2Arff)
    . Calcular a melhor distribuição de neorônios (HiddenLayersVariation)

Para gerar um JAR, seguir os passos de
https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-jar-with-dependencies-using-maven
