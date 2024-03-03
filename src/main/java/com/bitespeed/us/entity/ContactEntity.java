package com.bitespeed.us.entity;

import com.bitespeed.us.constants.LinkPrecedence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity {

    private Long id;
    private String phoneNumber;
    private String email;
    private String linkedId;
    private LinkPrecedence linkPrecedence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
