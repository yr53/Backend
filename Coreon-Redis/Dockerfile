FROM redis:7.4-alpine

# 설정 파일 복사
COPY redis.conf /usr/local/etc/redis/redis.conf

# 6379 포트
EXPOSE 6379

# 설정 파일로 실행
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
