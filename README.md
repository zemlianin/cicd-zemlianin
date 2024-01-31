# Задание

Написать java программу, которая принимает на вход два целочисоленных аргумента и выводит результат суммирования

Для написанной программы написать Dockerfile 

Создать в GitHub Actions два workflow:

### Workflow сборки
Запускается при пуше/мерже в мастер
1) Собрать проект
2) Собрать Docker образ
3) Запушить собранный образ в Docker Hub с двумя тэгами: latest и по хэшу коммита (первые 7 знаков)

### Workflow запуска
Запускается вручную, принимая на вход тэг образа и два слагаемых
1) Скачивает образ из Docker Hub
2) Запускает контейнер с образом и выводит результат в лог


Материалы:

+ https://docs.github.com/en/actions/learn-github-actions/variables#default-environment-variables
+ https://github.com/docker/login-action
+ https://docs.github.com/en/actions/security-guides/using-secrets-in-github-actions
+ https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#workflow_dispatch
+ https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#providing-inputs
