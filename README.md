# Como subir este projeto para o GitHub (passo a passo)

Este guia ensina, do zero, a publicar este projeto (ecommerce-api) no GitHub. Os passos funcionam em Windows, macOS e Linux.

Pré‑requisitos:
- Ter uma conta no GitHub: https://github.com
- Git instalado: `git --version` deve funcionar no terminal
- (Opcional) Chave SSH configurada no GitHub se preferir usar SSH


## 1) Inicializar o repositório local
No diretório do projeto (pasta `ecommerce-api`), execute:

```bash
git init
# defina seu usuário (se ainda não estiver configurado globalmente)
git config user.name "Seu Nome"
git config user.email "seu-email@exemplo.com"

# verifique o que será versionado (a .gitignore já ignora target/, IDEs, etc.)
git status

# adicione e faça o primeiro commit
git add .
git commit -m "chore: projeto inicial"
```

Dica: se preferir que sua branch principal se chame "main", defina:
```bash
git branch -M main
```


## 2) Criar o repositório no GitHub
- Acesse https://github.com/new
- Defina um nome (ex.: `ecommerce-api`)
- Escolha Visibilidade (Public ou Private)
- NÃO adicione README/.gitignore/license pelo GitHub (já temos aqui)
- Clique em "Create repository"

Você verá instruções com a URL do repositório. Copie a URL HTTPS ou SSH.


## 3) Conectar o repositório local ao GitHub
Escolha UMA das opções abaixo.

Opção A — HTTPS:
```bash
git remote add origin https://github.com/SEU_USUARIO/ecommerce-api.git
```

Opção B — SSH (recomendado se já configurou chave SSH):
```bash
git remote add origin git@github.com:SEU_USUARIO/ecommerce-api.git
```


## 4) Enviar (push) para o GitHub
```bash
git push -u origin main   # se sua branch atual é main
# ou
git push -u origin master # se ainda estiver usando master
```

Após o push, atualize a página do repositório no GitHub: os arquivos devem aparecer lá.


## 5) Próximas alterações (fluxo do dia a dia)
```bash
git status                      # ver o que mudou
git add <arquivos>              # adicionar mudanças
git commit -m "mensagem"        # criar commit
git push                        # enviar para GitHub
```


## 6) Dicas e resolução de problemas comuns
- Alterar o nome da branch principal para main:
  ```bash
  git branch -M main
  git push -u origin main
  ```
- Trocar a URL do remoto (se adicionou errado):
  ```bash
  git remote set-url origin https://github.com/SEU_USUARIO/ecommerce-api.git
  # ou SSH
  git remote set-url origin git@github.com:SEU_USUARIO/ecommerce-api.git
  ```
- Ver remotos configurados:
  ```bash
  git remote -v
  ```
- Autenticação via HTTPS pedindo senha repetidamente: use um helper de credenciais (já vem por padrão em muitos sistemas):
  ```bash
  git config --global credential.helper store   # simples (guarda em texto)
  # ou, no macOS, usar o Keychain (geralmente já padrão)
  ```
- Usar tags (opcional):
  ```bash
  git tag -a v1.0.0 -m "primeiro release"
  git push origin v1.0.0
  ```


## 7) O que já está ignorado (.gitignore)
- Build e artefatos: `/target/`, `*.class`, `site/`
- Pastas/arquivos de IDE: `.idea/`, `*.iml`, `.vscode/`, etc.
- Arquivos temporários de SO (ex.: `.DS_Store`, `Thumbs.db`)
- Arquivos temporários e backups (`*.swp`, `*.tmp`, `*.bak`)

Se precisar versionar algo que está sendo ignorado, ajuste o arquivo `.gitignore`.


## 8) Rodar o projeto localmente
Com Java e Maven instalados:
```bash
./mvnw spring-boot:run
```
A API fica em: http://localhost:8080/api


## 9) Estrutura do projeto
- Spring Boot + Maven
- Banco H2 em memória (não gera arquivos no disco)
- Arquivo principal: `src/main/java/com/ecommerce/EcommerceApiApplication.java`
- Configurações: `src/main/resources/application.yml`


Pronto! Seu projeto está no GitHub. Se quiser, crie novas branches para features e abra Pull Requests para manter um fluxo de trabalho organizado.