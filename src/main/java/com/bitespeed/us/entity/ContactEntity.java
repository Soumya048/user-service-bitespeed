package com.bitespeed.us.entity;

import com.bitespeed.us.constants.DBConstants;
import com.bitespeed.us.constants.LinkPrecedence;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="CONTACT")
@Data
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.CONTACT_SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "linked_id")
    private Long linkedId;

    @Column(name = "link_precedence")
    private String linkPrecedence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
