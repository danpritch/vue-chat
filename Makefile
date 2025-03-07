up:
	docker-compose up

down:
	docker-compose down

psql:
	PGPASSWORD=postgres psql -h localhost -U postgres

.PHONY: postgres
postgres:
	docker build -f postgres/Dockerfile -t vue-chess-db:latest postgres
	
.PHONY: build
build:
	$(MAKE) postgres
