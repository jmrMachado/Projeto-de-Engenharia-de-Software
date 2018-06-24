# Adventure Builder
[![Build Status](https://travis-ci.com/tecnico-softeng/es18tg_11-project.svg?token=mX7ChECeB8UWG7pH5wSs&branch=develop](https://travis-ci.com/tecnico-softeng/es18tg_11-project) 
[![codecov](https://codecov.io/gh/tecnico-softeng/es18tg_11-project/branch/master/graph/badge.svg?token=CRc1h0p28T)](https://codecov.io/gh/tecnico-softeng/es18tg_11-project)
To run tests execute: mvn clean install

To see the coverage reports, go to <module name>/target/site/jacoco/index.html.


|   Number   |          Name           |            Email        |   GitHub Username  | Group |
| ---------- | ----------------------- | ----------------------- | -------------------| ----- |
|    84897   |       Dário de Sá       |darioandre.sa@hotmail.com|     dariosa        |   11  |
|    84609   |       Marco Coelho      |marcoavcoelho@hotmail.com|     OCoise         |   11  |
|    84601   |       João Machado      |joao_machado@live.com.pt |  JoaoMachadoIST    |   11  |
|    84593   |       Inês Vilhena      |fintavi@gmail.com        |   Ines-Vilhena     |   2   |
|            |                         |                         |                    |   2   |
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

And create the 7 databases for the project as specified in
the `resources/fenix-framework.properties`.
