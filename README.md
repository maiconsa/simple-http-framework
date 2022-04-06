
# O que é ?

Um framework simples para tratar  requisições HTTP 1.1 seguindo os padrões  web definidos pelos documentos RFC (Request for Comments)

#  Requisitos

 - Java 8+
 - Maven 


# Como roda?
O pacote deste mini framework não está publicado em nenhum repositório, para realizar os teste é necessário executar localmente utilizando o comando:
```bash
	 mvn install
```
Após a execução deste comando no repositório maven local .m2 terá um artefato: 

	 repository/simplehttp/framework/web/1.0.0/web-1.0.0.jar

	 
# Como utilizar?

Declare na classe main da aplicação o inicio do contexto da aplicação passando o pacote que contém os endpoints (@Controller)
	
	ApplicationContext.init("example.app");

Defina os endpoints dos controladores utilizandos as seguinte anotações:

| Anotação        | Descrição                                                                   |   
|---|---|
| @Controller     | Identifica que a classe é um controller                                     |  
| @Do             | Identifica o tipo  da requisição                                            |
| @Payload        | Identifica o payload que está sendo enviado na requisição                   |  
| @PathVariable   | Identifica a variável que será passada no path                              |
| @QueryParameter | Identifica um query parameter da requisição                                 |   
| @Header         | Identifica um campo header http que está na requisição                      |
| @ResponseStatus | Identifica o status code  http que será retornada ao completar a requisição | 
	
## IMPORTANTE
Apenas conteúdo do tipos json são aceitos como entrada e saídas das requisições;	
Isto apenas content-type  com valor application/json será aceito;

# Reference

HTTP/1.1 Abrange desde RFC[7230-7235]

 - RFC7230 - Hypertext Transfer Protocol (HTTP/1.1): Message Syntax and Routing (https://datatracker.ietf.org/doc/html/rfc7230)
 - RFC7231 - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content (https://datatracker.ietf.org/doc/html/rfc7231)
