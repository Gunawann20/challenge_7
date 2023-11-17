package org.binaracademy.challenge_7.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.binaracademy.challenge_7.entity.Message;
import org.binaracademy.challenge_7.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    private static final String COLLECTION_NAME = "MESSAGES";
    private final ExecutorService executorService;
    @Autowired
    public MessageServiceImpl(ExecutorService executorService){
        this.executorService = executorService;
    }

    //@Scheduled(fixedRate = 5000, initialDelay = 4000) // test
    @Scheduled(cron = "0 0 12 * * *")
    @Override
    public void scheduledMessage() {
        executorService.submit(() -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(COLLECTION_NAME);

            DatabaseReference newMessage = reference.push();

            Message message = new Message();
            message.setSender("ADMIN-BINARFUD");
            message.setReceiver("CUSTOMERS");
            message.setDescription("Saatnya makan siang, jangan lupa beli makan siang di Binarfud yaaa");
            message.setTime(LocalDateTime.now());

            newMessage.setValueAsync(message);
            log.info("Berhasil kirim pesan ke customers");
        });
    }
}
