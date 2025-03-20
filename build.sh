#!/bin/sh

# ./mvnw clean package -Dmaven.test.skip=true
docker buildx build --platform linux/amd64 --add-host=host.docker.internal:host-gateway -t uportal/todo-app -f Dockerfile .
