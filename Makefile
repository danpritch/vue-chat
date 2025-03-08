up:
	docker-compose up

down:
	docker-compose down

psql:
	PGPASSWORD=postgres psql -h localhost -U postgres

.PHONY: postgres
postgres:
	docker build -f postgres/Dockerfile -t chat-db:latest postgres
	
.PHONY: build
build:
	$(MAKE) postgres
