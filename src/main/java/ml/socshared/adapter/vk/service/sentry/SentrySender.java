package ml.socshared.adapter.vk.service.sentry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SentrySender {

    private final SentryService sentry;

    public void sentryMessage(String message, Map<String, Object> additionalData, List<SentryTag> tags) {
        Map<String, String> tm = new HashMap<>();
        tm.put("service", SentryTag.SERVICE_NAME);
        for(SentryTag tag : tags) {
            tm.put(tag.type(), tag.value());
        }
        sentry.logMessage(message, tm, additionalData);
    }
}