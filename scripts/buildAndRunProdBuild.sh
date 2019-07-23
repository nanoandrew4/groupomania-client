#!/bin/bash

cd ..
mvn clean package docker:build -Denvironment=production
cd docker/prod/
docker-compose build
docker-compose up
docker-compose down