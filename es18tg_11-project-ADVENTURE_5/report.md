# Introdução
A FenixFramework segue uma política otimista. Isto quer dizer que não há locks nos acessos à base de dados. Assume-se assim que podem ser completadas várias transações sem existir interferência entre elas, na grande maioria dos casos.
Esta política é utilizada em ambientes
* Com pouca quantidade de dados
* Onde os conflitos são raros

# Testes JMeter
* Com o aumento do número de utilizadores a fazerem pedidos em simultânio ao servidor, o tempo de resposta aumenta.
* Com o aumento do número de utilizadores também houve um aumento do número de erros.

# Conclusão
No caso deste projeto há vários dados, o que torna os conflitos frequentes. Como se está a utilizar um política otimista, sempre que há um conflito a transação terá de ser recomeçada, o que é um processo com custo elevado em termos de eficiência. Conclui-se assim que a política da FenixFramework não é a mais indicada para a aplicação desenvolvida.