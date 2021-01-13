#!make
SHELL = /bin/sh
.DEFAULT: help

-include .env .env.local .env.*.local

# Defaults
BUILD_VERSION ?= SNAPSHOT
IMAGE_NAME := ${DOCKER_REPO}/${SERVICE_NAME}:${BUILD_VERSION}
IMAGE_NAME_LATEST := ${DOCKER_REPO}/${SERVICE_NAME}:latest
DOCKER_COMPOSE = USERID=$(shell id -u):$(shell id -g) docker-compose ${compose-files}
ALL_ENVS := local ci
env ?= local
gsa-instances ?= 1
docker-snapshot ?= true

ifndef SERVICE_NAME
$(error SERVICE_NAME is not set)
endif

ifndef DOCKER_REPO
$(error DOCKER_REPO is not set)
endif

ifdef ARTIFACTORY_USR
DOCKER_USER=${ARTIFACTORY_USR}
else
DOCKER_USER=${USER}
endif

ifdef ARTIFACTORY_PSW
DOCKER_PASSWORD=-p ${ARTIFACTORY_PSW}
endif

ifeq (${env}, ci)
compose-files=-f docker-compose.yml -f docker-compose.ci.yml
endif

.PHONY: help
help:
	@echo "GSA build pipeline"
	@echo ""
	@echo "Usage:"
	@echo "  build                          - Build artifact"
	@echo "  test.unit                      - Run unit tests"
	@echo "  test.integration               - Run integration tests"
	@echo "  test.api                       - Run api tests"
	@echo "  test.resiliency                - Run resiliency tests"
	@echo "  docker.publish                 - Publish docker image (used for internal/external testing purposes) to artifactory. Receives parameter docker-snapshot (default true)"
	@echo "  docker.wait                    - Waits until all docker containers have exited successfully and/or are healthy. Timeout: 180 seconds"
	@echo "  docker.logs                    - Generate one log file per each service running in docker-compose"
	@echo "  git.tag                        - Creates a new tag and pushes it to the git repository. Used to tag the current commit as a released artifact"
	@echo ""
	@echo "  ** The following tasks receive an env parameter to determine the environment they are being executed in. Default env=${env}, possible env values: ${ALL_ENVS}:"
	@echo "  docker.run.all                 - Run GSA service and all it's dependencies with docker-compose (default env=${env})"
	@echo "  docker.run.dependencies        - Run only GSA dependencies with docker-compose (default env=${env})". Note that `build` might need to be executed prior.
	@echo "  docker.run.gsa                - Build and run GSA container in detached mode. Recreate it if already running (default env=${env})"
	@echo "  docker.run.gsa.debug          - Build and run GSA container in un-detached mode. Recreate it if already running (default env=${env})"
	@echo "  docker.stop                    - Stop and remove all running containers from this project using docker-compose down (default env=${env})"
	@echo ""
	@echo "Project-level environment variables are set in .env file:"
	@echo "  SERVICE_NAME=gene-search-api"
	@echo "  DOCKER_PROJECT_NAME=gsa"
	@echo "  COMPOSE_PROJECT_NAME=gsa"
	@echo "  DOCKER_REPO="
	@echo "  COMPOSE_HTTP_TIMEOUT=360"
	@echo ""
	@echo "Note: Store protected environment variables in .env.local or .env.*.local"
	@echo ""

.PHONY: build
build: b.clean b.build

b.clean:
	./gradlew -no-build-cache -PbuildVersion=${BUILD_VERSION} clean

b.build:
	./gradlew build -PbuildVersion=${BUILD_VERSION} -x :test

.PHONY: test.unit
test.unit:
	./gradlew test

.PHONY: test.integration
test.integration:
	./gradlew cleanIntegrationTest integrationTest

.PHONY: test.api
test.api:

.PHONY: test.resiliency
test.resiliency:

.PHONY: docker.run.all
docker.run.all: d.compose.down
	make d.compose.up gsa-instances=1
	make docker.wait

.PHONY: docker.run.dependencies
docker.run.dependencies: d.compose.down
	make d.compose.up gsa-instances=0
	make docker.wait

.PHONY: docker.run.gsa
docker.run.gsa: build
	$(call DOCKER_COMPOSE) up -d --force-recreate --build gene-search-api

.PHONY: docker.run.gsa.debug
docker.run.gsa.debug: build
	$(call DOCKER_COMPOSE) up --force-recreate --build gene-search-api

.PHONY: docker.stop
docker.stop: d.compose.down

.PHONY: d.compose.up
d.compose.up:
	$(call DOCKER_COMPOSE) up -d --remove-orphans --build --scale gene-search-api=${gsa-instances}

.PHONY: d.compose.down
d.compose.down:
	$(call DOCKER_COMPOSE) down -v || true
	$(call DOCKER_COMPOSE) rm --force || true
	docker rm "$(docker ps -a -q)" -f || true

### ------------------------
### Pipeline's utility tasks
### ------------------------

.PHONY: docker.publish
docker.publish:
ifeq (${docker-snapshot}, true)
docker.publish: d.publish.snapshot
else
docker.publish: d.publish
endif

d.publish.snapshot:
	$(info Publishing docker image: '${IMAGE_NAME}-SNAPSHOT' to artifactory)
	make build d.login
	make d.build image-name=${IMAGE_NAME}-SNAPSHOT
	make d.push image-name=${IMAGE_NAME}-SNAPSHOT

d.publish:
	$(info Publishing docker image: '${IMAGE_NAME}' and '${IMAGE_NAME_LATEST}' to artifactory)
	@if [ ${BUILD_VERSION} = "SNAPSHOT" ]; then printf "\033[91mBuild version can't be SNAPSHOT\033[0m\n"; exit 1; fi
	make d.login d.snapshot.tag.latest
	make d.push image-name=${IMAGE_NAME}
	make d.push image-name=${IMAGE_NAME_LATEST}

d.login:
	docker login -u ${DOCKER_USER} ${DOCKER_PASSWORD} ${DOCKER_REPO}

d.build:
	docker build --no-cache --build-arg BUILD_VERSION=${BUILD_VERSION} -t ${image-name} .

d.push:
	docker push ${image-name}

d.snapshot.tag.latest:
	docker tag ${IMAGE_NAME}-SNAPSHOT ${IMAGE_NAME}
	docker tag ${IMAGE_NAME}-SNAPSHOT ${IMAGE_NAME_LATEST}

.PHONY: docker.wait
docker.wait:
	./bin/docker-wait

.PHONY: docker.logs
docker.logs:
	./bin/docker-logs

.PHONY: git.tag
git.tag: g.tag g.push

g.tag:
	git tag -a -m ${BUILD_VERSION} ${BUILD_VERSION} $(git rev-parse HEAD)

g.push:
	git push origin refs/tags/${BUILD_VERSION}:refs/tags/${BUILD_VERSION}