#!/bin/sh

# ./mvnw clean package -Dmaven.test.skip=true
docker buildx build --platform linux/arm64 -t uportal/todo-app -f Dockerfile .
