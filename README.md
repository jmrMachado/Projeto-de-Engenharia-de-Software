# Projeto-de-Engenharia-de-Software
Projeto realizado no âmbito da cadeira de Engenharia de Software no ano letivo de 2017/2018. Membros do grupo que desenvolveram o projeto:
- Pedro Maria;
- Dário Sá;
- Marco Coelho;
- Inês Vilhena;
- Daniela Rodrigues;
- João Machado;


Este projeto tinha como objetivo a implementação, em linguagem Java, de um sistema de reserva de atividades (Como por exemplo a atual aplicação Trivago). O projeto era composto por vários módulos sendo cada um deles descritos em baixo:

Módulo Car: Este módulo era responsável pela reserva de um veículo caso isso fosse requesitado pelo cliente que reserva a aventura. 

Módulo Bank: Este módulo era responsável pela gestão das contas de clientes e intermediários e era a este módulo que eram realizados os pagamentos das aventuras.

Módulo Hotel: Este módulo era responsável por garantir a reserva de um quarto de hotel de diversos tipos caso a duração da aventura fosse superior a 1 dia.

Módulo Broker: Este módulo pode ser considerado o módulo principal do projeto pois é aquele responsável por servir de intermediário entre todos os módulos e garantir a correta reserva de uma aventura por parte de um cliente.

Módulo IRS: Este módulo era responsável pela aplicação de impostos aos produtos oferecidos nas aventuras, sejam os carros, os quartos, etc...

Módulo Activity: Este módulo era responsável pela reserva de uma atividade que pode tomar diversos tipos.

Cada um destes módulos tinha uma base de dados onde a informação é persistida e tudo isto era atingido com recurso à Fenix Framework. Na parte final do projeto (Parte 4) o grupo teve inclusive de recorrer à Spring Framework de modo a conseguir implementar uma interface gráfica que o utilizador pudesse utilizar de modo a conseguir reservar uma aventura.

Este projeto em grande parte foi principalmente a implementação de testes unitários e na parte final do mesmo (parte 5) foi também sujeito a testes de carga com recurso à ferramenta Jmeter.

Como correr o Projeto:  
1º Passo: Abrir a parte 4 do projeto;  
2º Passo: Abrir 6 terminais, cada um deles dentro da diretoria de um módulos;  
3º Passo: Montar a base de dados recorrendo a MySQL;  
4º Passo: Correr o comando mvn install na diretoria raiz do projeto;  
5º Passo: Correr o comando em cada terminal mvn clean spring-boot:run;  
6º Passo: Interagir com o projeto através de um browser colocando como endereço http://localhost:[PortoDoMódulo];  
