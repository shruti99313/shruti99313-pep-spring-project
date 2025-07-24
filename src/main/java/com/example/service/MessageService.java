package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message create(Message m) {
        if (m.getMessageText() == null || m.getMessageText().isBlank()
                || m.getMessageText().length() > 255
                || m.getPostedBy() == null
                || accountRepository.findById(m.getPostedBy()).isEmpty())
            throw new IllegalArgumentException();

        return messageRepository.save(m);
    }

    public List<Message> getAll() {
        List<Message> all = messageRepository.findAll();
        all.sort((a,b) -> a.getMessageId() - b.getMessageId());
        return all;
    }

    public Optional<Message> getById(int id) {
        return messageRepository.findById(id);
    }

   public int deleteMessage(int messageId) {
    if (messageRepository.existsById(messageId)) {
        messageRepository.deleteById(messageId);  // only if messageId is @Id
        return 1;
    } else {
        return 0;
    }
}


    public int updateText(int id, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255)
            throw new IllegalArgumentException();

        Optional<Message> opt = messageRepository.findById(id);
        if (opt.isEmpty())
            throw new IllegalArgumentException();

        Message m = opt.get();
        m.setMessageText(newText);
        messageRepository.save(m);
        return 1;
    }

    public List<Message> getByUser(int userId) {
        return messageRepository.findByPostedBy(userId);
    }

    public int delete(int messageId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
