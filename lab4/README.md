# Lab4 - gogo - 24.1

## Objetivo

Implementar as seguintes funcionalidades em **GoLang**:

1. A soma do **sum** de todos os arquivos;
2. A lista de arquivos com o mesmo **sum**;
3. A similaridade parcial entre arquivos.

Para este laboratório, há a implementação serial das duas primeiras funcionalidades [aqui](https://github.com/thiagomanel/fpc/tree/master/2024.1/lab4/go/serial).
Então, foram implementados duas versões concorrentes:

- ```concurrent-0``` - Na qual, as duas primeiras funcionalidades são implementadas de modo concorrente (espera-se que executem mais rapidamente);
- ```serial-partial``` - Na qual, a terceira funcionalidade
(similaridade parcial) é implementada de modo serial;
- ```concurrent-partial``` - Na qual, a terceira funcionalidade (similaridade parcial) é implementada de modo concorrente.

## SPEC Partial Similarity
Um arquivo pode apresentar algum grau de similaridade (um valor entre 0 e 1) com outro arquivo. Quanto maior a quantidade de pedaços (ou chunks) de um arquivo iguais a pedaços do outro arquivo (iguais conforme a função **sum**), mais similar os arquivos são.

O programa recebe uma lista de arquivos e retorna para
cada arquivo sua similaridade em relação aos demais.

Na saída padrão é indicado a similaridade em um estilo
semelhante ao output abaixo:
```
- Similarity between …/file.1 and …/file.2: 89.73469%
- Similarity between …/file.1 and …/file.3: 89.56112%
- Similarity between …/file.2 and …/file.3: 89.578285%
```
