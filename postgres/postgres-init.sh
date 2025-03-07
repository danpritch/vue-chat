#!/bin/bash

# Directory containing the SQL files
DIRECTORY="/docker-entrypoint-initdb.d/db/migrations"

for file in $(ls "$DIRECTORY" | sort -V); do
  echo "Processing $file"
  PGPASSWORD=postgres psql -U postgres -f "$DIRECTORY/$file"
done
