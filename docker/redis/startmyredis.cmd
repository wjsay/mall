docker run -p 6379:6379 -v /d/Documents/docker/redis/data:/data -v /d/Documents/docker/redis/conf/redis.conf:/etc/redis/redis.conf --privileged=true --name mall-redis -d redis redis-server /etc/redis/redis.conf