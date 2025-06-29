# Education repository for spring boot introduction to CAS-OOP-25-19

[https://github.zhaw.ch/bacn/cas-oop-spring-25-19](https://github.zhaw.ch/bacn/cas-oop-spring-25-19)

## Branches in this repository

- le04-configuration-magic
- le06-spring-data-jpa
- le06b-spring-data-jpa-advanced
- le07-spring-mvc
- le07b-spring-mvc-rest
- le07c-spring-mvc-websocket
- le08-spring-security
- le08-spring-security-rest-jwt
- le09-spring-testing

## Create a Docker Container, Run and Publish to Docker

Replace **uportal** with your **dockerhub id** in the script files build.sh and build-arm.sh.

**For intel architecture:**

A preconfigured shell script includes the build command:

```shell
./build.sh
```

**For arm64v8 architecture (e.g. MAC Mx):** [https://hub.docker.com/r/arm64v8/nginx/](https://hub.docker.com/r/arm64v8/nginx/)

A preconfigured shell script includes the build command:

```shell
./build-arm.sh
```

```
$  docker login
$  docker login --username uportal --password
$  docker push uportal/demo-initial
```

<br/>

Alternative way for login:

```
cat ~/.key/my_password.txt | docker login --username uportal --password-stdin
```

## Target Deployment Platform (Linux, Mac, Windows)

Login to deployment platform with a container infrastructure:

<br/>

Replace **uportal** with your **dockerhub id**.

<br/>

```
$  docker pull uportal/demo-initial
```

<br/>

### Run the app with a docker-compose file

Start the App in detached mode with:

```
$  docker compose -f docker-compose-h2.yml up -d
```

<br/>

Start the App with log output in the console:

```
$  docker compose -f docker-compose-h2.yml up
```

or with specific docker-compose file (e.g. on linux with a traefik reverse proxy)

```
$  docker compose -f docker-compose-h2-traefik-v3.yml up
```

<br/>

Delete containers:

```
$  docker compose rm
$  docker compose -f docker-compose-h2-traefik-v3.yml rm
```


