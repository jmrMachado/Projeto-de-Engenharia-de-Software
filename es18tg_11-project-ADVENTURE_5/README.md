# Adventure Builder [![Build Status](https://travis-ci.com/tecnico-softeng/prototype-2018.svg?token=fJ1UzWxWjpuNcHWPhqjT&branch=master)](https://travis-ci.com/tecnico-softeng/prototype-2018) [![codecov](https://codecov.io/gh/tecnico-softeng/prototype-2018/branch/master/graph/badge.svg?token=OPjXGqoNEm)](https://codecov.io/gh/tecnico-softeng/prototype-2018)


To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email        |   GitHub Username  | Group |
| ---------- | ----------------------- | ----------------------- | -------------------| ----- |
|   84601    |      João Machado       | joao_machado@live.com.pt|   JoaoMachadoIST   |  11   |
|   84593    |      Inês Vilhena       | fintavi@gmail.com       |    Ines-Vilhena    |  11   |
|   84897         | Dário de Sá                        | darioandre.sa@hotmail.com                         |  dariosa                  |   11   |
|   84609    |        Marco Coelho     |marcoavcoelho@hotmail.com|       OCoise       |   11   |
|   84618    |        Pedro Maria      |   pedro8745@gmail.com   |          pmsmm     |   11  |
|            |                         |                         |                    |   2   |

- **Group 1:**
- **Group 2:**

### Infrastructure

This project includes the persistent layer, as offered by the FénixFramework.
This part of the project requires to create databases in mysql as defined in `resources/fenix-framework.properties` of each module.

See the lab about the FénixFramework for further details.

#### Docker (Alternative to installing Mysql in your machine)

To use a containerized version of mysql, follow these stesp:

```
docker-compose -f local.dev.yml up -d
docker exec -it mysql sh
```

Once logged into the container, enter the mysql interactive console

```
mysql --password
```

And create the 6 databases for the project as specified in
the `resources/fenix-framework.properties`.

To launch a server execute in the module's top directory: mvn clean spring-boot:run

To launch all servers execute in bin directory: startservers

To stop all servers execute: bin/shutdownservers

To run jmeter (nogui) execute in project's top directory: mvn -Pjmeter verify. Results are in target/jmeter/results/, open the .jtl file in jmeter, by associating the appropriate listeners to WorkBench and opening the results file in listener context

