package openapi.openapitest.domain.stock;

import openapi.openapitest.domain.stock.repository.ApiTokenStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 인메모리 기반의 토큰 저장소
 * 단일 서버 환경 -> 토큰을 메모리에 저장해서 접근
 * 동시성을 위한 ReadWriteLock 사용
 */

@Component
@Primary
public class ApiTokenInMemoryStore implements ApiTokenStore {

    private String accessToken;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void saveAccessToken(String accessToken) {

        lock.writeLock().lock();
        try {
            this.accessToken = accessToken;
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public String getAccessToken() {

        lock.readLock().lock();
        try {
            return this.accessToken;
        } finally {
            lock.readLock().unlock();
        }
    }
}
