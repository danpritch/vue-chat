FROM postgres:latest
ENV POSTGRES_PASSWORD postgres
COPY db /docker-entrypoint-initdb.d/db
COPY postgres-init.sh /docker-entrypoint-initdb.d/
