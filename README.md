<h1 style=text-align:center>
  Star Wars Planet API (udemy-star-wars-planets-api)
</h1>

<p style=text-align:center>
  <a href="#-technologies">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-project">Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-configuration">Configuração</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-developing">Construir e Executar</a>
</p>

<p style=text-align:center>
  <a href="https://github.com/delonborges/udemy-star-wars-planets-api"><img src="https://img.shields.io/badge/GitHub-100000?logo=GitHub" alt="Udemy"/></a>
  <a href="https://www.udemy.com/certificate/UC-e1ede4fc-1ce7-46ca-a126-602bd1d19647/"><img src="https://img.shields.io/badge/Udemy-EC5252?logo=Udemy&logoColor=white&labelColor=purple&color=purple" alt="Udemy"/></a>
</p>

<br>

## ✨ Technologies

- [Mysql](https://dev.mysql.com/downloads/mysql/)
- [Java](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/download.cgi)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Testing](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testing-introduction)
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito](https://site.mockito.org)
- [AssertJ](https://github.com/assertj/assertj)
- [Hamcrest](http://hamcrest.org/JavaHamcrest/)
- [Jacoco](https://github.com/jacoco/jacoco)
- [Pitest](https://pitest.org)

## 💻 Projeto

udemy-star-wars-planets-api é um serviço web que provê dados sobre a franquia de Star Wars, mais especificamente sobre os planetas que aparecem nos filmes.

Esse projeto foi elaborado durante o
curso [Testes automatizados na prática com Spring Boot](https://www.udemy.com/course/testes-automatizados-na-pratica-com-spring-boot/?referralCode=7F6C5AA14AE558497FE0),
em que o foco foi a criação de testes automatizados.

## 🛠️ Configuração

O projeto requer um banco de dados Mysql, então é necessário criar uma base de dados com os seguintes comandos:

```
$ sudo mysql

CREATE USER 'username'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;

exit

$ mysql -u username -p

CREATE DATABASE udemy-star-wars-planets;

exit
```

Durante os testes, as tabelas de banco já serão criadas automaticamente no banco de dados.

## 🚀 Construir e Executar

Para construir e testar, execute o comando:

```sh
$ gradle clean test
```

Execuções específicas:

```sh
~/ Testes unitários:
$ gradle clean test -DincludeTags="unit"
```
```sh
~/ Testes integrados:
$ gradle clean test -DincludeTags="integration"
```
```sh
~/ Testes mutantes:
$ gradle piteste
```
