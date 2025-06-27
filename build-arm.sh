#!/bin/sh

# ./mvnw clean package -Dmaven.test.skip=true
docker buildx build --platform linux/arm64 -t uportal/demo-initial -f Dockerfile .
