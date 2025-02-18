@echo off
cd /d %~dp0
echo Starting Docker Compose...
docker-compose up --build --force-recreate
pause