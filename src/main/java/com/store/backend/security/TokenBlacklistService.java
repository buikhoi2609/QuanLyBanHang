package com.store.backend.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lưu tạm các token đã đăng xuất để vô hiệu hóa ngay lập tức, thay vì phải
 * chờ token tự hết hạn. Lưu trong bộ nhớ vì hệ thống hiện chưa có hạ tầng
 * cache/DB riêng cho việc này; token tự động được dọn khi đã hết hạn.
 */
@Component
public class TokenBlacklistService {

    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, Instant expiresAt) {
        cleanupExpired();
        blacklistedTokens.put(token, expiresAt);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    private void cleanupExpired() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
