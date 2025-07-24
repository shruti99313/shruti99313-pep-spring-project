package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    // If 'messageId' is not the primary key
    int deleteByMessageId(int messageId);

    // Also assuming 'postedBy' field exists
    List<Message> findByPostedBy(int postedBy);
}

