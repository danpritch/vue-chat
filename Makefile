up:
	docker-compose up

down:
	docker-compose down

psql:
	PGPASSWORD=postgres psql -h localhost -U postgres

.PHONY: postgres
postgres:
	docker build -f postgres/Dockerfile -t chat-db:latest postgres
	
	
.PHONY: vue-chat-api
vue-chat-api:
	docker network create vue-chat-api-build-net
	docker run --name vue-chat-api-build-db --network vue-chat-api-build-net -e POSTGRES_PASSWORD=postgres -d chat-db:latest
	sleep 15
	docker build --network=vue-chat-api-build-net -t vue-chat-api:latest vue-chat-api
	docker stop vue-chat-api-build-db
	docker rm vue-chat-api-build-db
	docker network rm vue-chat-api-build-net
	
.PHONY: vue-chat-web
vue-chat-web:
	docker build -f vue-chat-web/Dockerfile --build-arg VUE_APP_API_URL=http://localhost:8080 -t vue-chat-web:latest vue-chat-web
	
.PHONY: build
build:
	$(MAKE) postgres
	$(MAKE) vue-chat-api
	$(MAKE) vue-chat-web
