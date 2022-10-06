ºººººººº Teste Técnico Itaú - Api de transferência de valores ºººººººº 

O projeto consiste em uma API que exponha padrão REST (JSON) e atenda as seguintes funcionalidades: 1º. Endpoint para cadastrar um cliente, com as seguintes informações: id (único), nome, número da conta (único) e saldo em conta; 2º. Endpoint para listar todos os clientes cadastrados; 3º. Endpoint para buscar um cliente pelo número da conta; 4º. Endpoint para realizar transferência entre 2 contas. A conta origem precisa ter saldo o suficiente para a realização da transferência e a transferência deve ser de no máximo R$ 1000,00 reais; 5º. Endpoint para buscar as transferências relacionadas à uma conta, por ordem de data decrescente. E as transferências sem sucesso também devem armazenadas.

ºººººººº Requisitos ºººººººº 1º. Solução desenvolvida em Java 8 ou superior; 
2º. Maven ou Gradle como gerenciador de dependências; 
3º. Banco de dados in memory; 
4º. Controle de concorrência na operação de transferência; 
5º. Utilize corretamente os padrões de HTTP response code para as APIs; 
6º. Controle de versão das APIs; 
7º. Testes unitários; 
8º. Testes integrados; 
9º. Documentação no código; 
10º. readme.md com a documentação de como utilizar a aplicação.

ººººººººRequisitos para rodar o projeto:ºººººººº
•JDK (Java Development Kit) 1.8 ou superior.
•Apache Maven 3.2.3

Para baixar as dependencias da aplicação
•Abre o terminal do seu sistema operacional
•Navegue até a pasta raiz do projeto e digite o seguinte comando:
  -> mvn clean install
   

Como Executar os testes da aplicação
   -> mvn test
   

Para executar a a aplicação executar o comando abaixo 
  -> mvn spring-boot:run
  

