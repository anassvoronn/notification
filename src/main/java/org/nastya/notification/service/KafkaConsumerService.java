package org.nastya.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.nastya.common.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "test-topic", groupId = "demo-group")
    public void listen(ConsumerRecord<String, UserEvent> record) {
        UserEvent userEvent = record.value();
        sendNotifications(userEvent);
    }

    private void sendNotifications(UserEvent event) {
        if (event.getAdminEmails() == null || event.getAdminEmails().isEmpty()) {
            log.warn("No admin emails in event");
            return;
        }

        String subject = String.format("User %s has been %s.",
                event.getUsername(),
                event.getAction()
        );

        String text = String.format(
                "User with username '%s' has been %s.%n%nEmail: %s%nPassword: ******",
                event.getUsername(),
                event.getAction(),
                event.getEmail()
        );

        emailService.sendSeparateEmails(event.getAdminEmails(), subject, text);
        log.info("Sent notifications to admins about user event: {}", event);
    }
}