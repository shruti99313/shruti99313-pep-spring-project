package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SocialMediaController {
    @Autowired private AccountService accountService;
    @Autowired private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account acc) {
        try {
            Account saved = accountService.register(acc);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account acc) {
        try {
            Account found = accountService.login(acc);
            return ResponseEntity.ok(found);
        } catch (SecurityException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message m) {
        try {
            Message saved = messageService.create(m);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAll();
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable int messageId) {
        return messageService.getById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok().build());
    }

   @DeleteMapping("/messages/{id}")
public ResponseEntity<?> deleteMessage(@PathVariable int id) {
    int result = messageService.deleteMessage(id);
    return ResponseEntity.ok(result); // Always return 200 with result
}


    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(
            @PathVariable int messageId,
            @RequestBody Message stub
    ) {
        try {
            int count = messageService.updateText(messageId, stub.getMessageText());
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable int accountId) {
        return messageService.getByUser(accountId);
    }
}
