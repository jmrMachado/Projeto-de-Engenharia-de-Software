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

Módulo Tax: Este módulo era responsável pela aplicação de impostos aos produtos oferecidos nas aventuras, sejam os carros, os quartos, etc...

Módulo Activity: Este módulo era responsável pela reserva de uma atividade que pode tomar diversos tipos.

Cada um destes módulos tinha uma base de dados onde a informação é persistida e tudo isto era atingido com recurso à Fenix Framework. Na parte final do projeto (Parte 4) o grupo teve inclusive de recorrer à Spring Framework de modo a conseguir implementar uma interface gráfica que o utilizador pudesse utilizar de modo a conseguir reservar uma aventura.

Este projeto em grande parte foi principalmente a implementação de testes unitários e na parte final do mesmo (parte 5) foi também sujeito a testes de carga com recurso à ferramenta Jmeter.

# Como correr o Projeto:  
1º Passo: Abrir a parte 4 do projeto;  
2º Passo: Abrir 6 terminais, cada um deles dentro da diretoria de um módulos;  
3º Passo: Montar a base de dados recorrendo a MySQL;  
4º Passo: Correr o comando mvn install na diretoria raiz do projeto;  
5º Passo: Correr o comando em cada terminal mvn clean spring-boot:run;  
6º Passo: Interagir com o projeto através de um browser colocando como endereço http://localhost:[PortoDoMódulo];  

############################################################################################

# Software Engineering Project
This project was made for the course of Software Engineering, 2017/2018. Members that helped in the development of the project:
-Pedro Maria;
-Marco Coelho;
-Dário de Sá;
-João Machado;
-Inês Vilhena;
-Daniela Rodrigues;

The objective of this project was to implement, in Java, an activity reservation system (Like Trivago for exemple). The project was composed of several modules, each one described below:
Car Module: This module is responsible for the reservation of a vehicle in case this was requested by the client who booked the adventure.

Bank Module: This module is responsible for managing clients and broker accounts and this module is the one that receives the payments of the booked adventures.

Hotel Module: This module is responsible for the reservation of a room that can be of several types (Single, Double) in case the duration of the activity exceeded one day.

Broker Module: This module can be considered the main module of the project because it's the one responsible to act as a middle man between all the modules and it's responsible for assuring a correct reservation of an adventure.

Tax Module: This module is the one responsible for applying taxes to the sold products like the cars, rooms, the activities, etc...

Activity Module: This module is the one responsible for providing and booking activities that can be of several types.

Each one of these modules has a database where the information is persisted and all this was achieved by resolving to the Fenix Framework. On part 4 of the project the group had to use Spring Framework as well in order to implement a graphical interface that the user could use in order to book an adventure.

This project also involved, in almost every part of it, the implementation of unit tests using the Junit Framework and in part 3 the implementation of Mock tests using the Jmockit Framework. In the last part (part 5) we also implemented load tests using the Jmeter framework.

# How to run the Project:  
1st Step: Open the part 4 directory of the project;  
2nd Step: Open 6 terminals, each one in one module;  
3rd Step: Mount the database of each module using MySQL;  
4th Step: Run mvn install on the projects root directory;  
5th Step: In each terminal run the command mvn clean spring-boot:run;  
6th Step: Interact with the project using a browser with the address http://localhost:[PortoDoMódulo]; 
