package org.nastya.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "test-topic", groupId = "demo-group")
    public void listen(ConsumerRecord<String, Map<String, String>> record) {
        Map<String, String> event = record.value();
        sendNotifications(event);
    }

    private void sendNotifications(Map<String, String> event) {
        String adminEmailsStr = event.get("adminEmails");
        if (adminEmailsStr == null || adminEmailsStr.isEmpty()) {
            log.warn("No admin emails in event");
            return;
        }

        Set<String> adminEmails = Arrays.stream(adminEmailsStr.split(","))
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .collect(Collectors.toSet());

        String subject = String.format("User %s has been %s.",
                event.get("username"),
                event.get("action")
        );

        String text = String.format(
                "User with username '%s' has been %s.%n%nEmail: %s%nPassword: ******",
                event.get("username"),
                event.get("action"),
                event.get("email")
        );

        emailService.sendSeparateEmails(adminEmails, subject, text);
        log.info("Sent notifications to admins about user event: {}", event);
    }
}