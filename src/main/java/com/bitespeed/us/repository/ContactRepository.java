package com.bitespeed.us.repository;

import com.bitespeed.us.constants.LinkPrecedence;
import com.bitespeed.us.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {


    Optional<ContactEntity> findByEmail(String email);

    Optional<ContactEntity> findByPhoneNumber(String phoneNumber);

    List<ContactEntity> getContactEntitiesByLinkedIdOrId(Long linkedId, Long id);

    @Query(value =
            "select c.*\n" +
            "from contact c \n" +
            "where (c.email =?1 or c.phone_number =?2) and c.link_precedence =?3"
            , nativeQuery = true)
    List<ContactEntity> getContactEntityByEmailOrPhoneNumberAndLinkPrecedence(String email, String phoneNumber, String linkPrecedence);

    @Query(value =
            "select c.*\n" +
            "from contact c \n" +
             "where (c.email =?1 or c.phone_number =?2) and (c.link_precedence ='PRIMARY' or c.link_precedence = 'SECONDARY')"
            , nativeQuery = true)
    List<ContactEntity> checkDuplicateContact(String email, String phoneNumber);


    @Query(value =
            "select c.* \n" +
            "from contact c \n" +
            "where (c.email =?1 and c.phone_number =?2) and c.link_precedence =?3"
            , nativeQuery = true)
    ContactEntity getContactByEmailAndPhoneNumberWithStrict(String email, String phoneNumber, String linkPrecedence);


}
