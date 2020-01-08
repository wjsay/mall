@echo off
docker run -d -p 3306:3306 --privileged=true -v %cd%/conf/my.cnf:/etc/mysql/my.cnf -v %cd%/data:/var/lib/mysql --name mall-mysql mysql