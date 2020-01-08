@echo off
docker run -p 6379:6379 -v %cd%/data:/data -v %cd%/conf/redis.conf:/etc/redis/redis.conf --privileged=true --name mall-redis -d redis redis-server /etc/redis/redis.conf
pause