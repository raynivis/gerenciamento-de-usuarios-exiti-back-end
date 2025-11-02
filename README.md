
# Gerenciamento de Usuários (Back-End)

Este projeto é a API back-end para o sistema de gerenciamento de usuários. Construído com Java e Spring Boot, ele fornece os endpoints RESTful necessários para as operações de CRUD, além de um endpoint para importação de usuários em lote.

O projeto está configurado para se conectar a um banco de dados PostgreSQL, que pode ser facilmente inicializado usando o arquivo `docker-compose.yml` incluído.

## Funcionalidades (API)

* Endpoints de CRUD para usuários.
* Listagem paginada de usuários.
* Filtros na listagem por nome e status.
* Endpoint para importação de usuários em lote.

## Instalação e Execução Local

Siga os passos abaixo para executar a API localmente.

**Pré-requisitos:**
* JDK (Java 25)
* Maven
* Docker

---

**1. Clone o projeto e acesse a pasta:**

```bash
# Clone o repositório
git clone https://github.com/raynivis/gerenciamento-de-usuarios-exiti-back-end.git

# Acesse o diretório do projeto
cd gerenciamento-de-usuarios-exiti-back-end/usuario-gerenciador
````

**2. Inicie o Banco de Dados (Docker):**

Use o Docker Compose para iniciar o container do PostgreSQL em segundo plano.

```bash
docker-compose up -d
```

*Nota: Certifique-se que as credenciais (usuário e senha) no seu arquivo `docker-compose.yml` são as mesmas que estão no arquivo `src/main/resources/application.properties`.*

**3. Execute a Aplicação (Spring Boot):**

Use o Maven Wrapper para iniciar a aplicação.

```bash
# Para Linux/macOS
./mvnw spring-boot:run

# Para Windows
./mvnw.cmd spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

