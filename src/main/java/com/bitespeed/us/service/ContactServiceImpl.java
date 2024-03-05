package com.bitespeed.us.service;

import com.bitespeed.us.constants.LinkPrecedence;
import com.bitespeed.us.constants.StatusCode;
import com.bitespeed.us.entity.ContactEntity;
import com.bitespeed.us.model.Contact;
import com.bitespeed.us.model.IdentifyContactReq;
import com.bitespeed.us.repository.ContactRepository;
import com.bitespeed.us.response.ContactDetails;
import com.bitespeed.us.response.IdentifiedContactRes;
import com.bitespeed.us.response.SavedEntityResponse;
import com.bitespeed.us.util.BaseResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ContactRepository contactRepository;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IdentifiedContactRes identifyContact(IdentifyContactReq request) {
        IdentifiedContactRes response = new IdentifiedContactRes();
        try {

            List<ContactEntity> primaryContactEntityList = contactRepository.getContactEntityByEmailOrPhoneNumberAndLinkPrecedence(
                    request.getEmail(), request.getPhoneNumber(), "PRIMARY"
            );
            boolean isDuplicatePrimaryContact = false;
            if (CollectionUtils.isNotEmpty(primaryContactEntityList)) {
                if (primaryContactEntityList.size() >= 2) {
                    log.info("Already more than 1 primary contact exist: Mapping");
                    int index = 0;
                    Long linkId = null;
                    for (ContactEntity contactEntity : primaryContactEntityList) {
                        if (index != 0) {
                            contactEntity.setLinkPrecedence(LinkPrecedence.SECONDARY.name());
                            contactEntity.setLinkedId(linkId);
                            //mapped and saved
                            contactRepository.save(contactEntity);
                        } else {
                            linkId = contactEntity.getId();
                        }
                        index++;
                    }
                    response.setContact( getRelatedContacts(request) );
                    return BaseResponseUtil.createBaseResponse(response, StatusCode.OK);
                }
            }
            // check duplicate
            List<ContactEntity> existingContactEntityList = contactRepository.checkDuplicateContact(request.getEmail(), request.getPhoneNumber());

            if (CollectionUtils.isEmpty(existingContactEntityList)) {
                // creating primary
                ContactEntity savedEntity = saveContactEntity(request, LinkPrecedence.PRIMARY, null);
                log.info("Contact Saved as primary Id: {}", savedEntity.getId());
            } else {

                for (ContactEntity contactEntity : existingContactEntityList) {
                    if (contactEntity.getEmail().equals(request.getEmail())
                            && contactEntity.getPhoneNumber().equals(request.getPhoneNumber())
                            && contactEntity.getLinkPrecedence().equals(LinkPrecedence.PRIMARY.name()) ) {

                        response.setContact( getRelatedContacts(request) );
                        return BaseResponseUtil.createBaseResponse(response, StatusCode.OK);

                    } if (contactEntity.getEmail().equals(request.getEmail())
                            && contactEntity.getPhoneNumber().equals(request.getPhoneNumber())
                            && contactEntity.getLinkPrecedence().equals(LinkPrecedence.SECONDARY.name()) ) {

                        response.setContact( getRelatedContacts(request) );
                        return BaseResponseUtil.createBaseResponse(response, StatusCode.OK);

                    }
                }

                ContactEntity contactEntity  = existingContactEntityList.get(0);
                Long primaryId = null;
                if (ObjectUtils.isNotEmpty(contactEntity.getLinkedId())) {
                    primaryId = contactEntity.getLinkedId();
                } else {
                    primaryId = contactEntity.getId();
                }

//              check secondary exist with same email and phoneNo.
                ContactEntity secondaryContactEntity = contactRepository.getContactByEmailAndPhoneNumberWithStrict(
                        request.getEmail(), request.getPhoneNumber(), "SECONDARY"
                );

                if (ObjectUtils.isNotEmpty(secondaryContactEntity)) {
                    log.info("Already contact exist: {}", secondaryContactEntity.getId());
                } else {
                    // creating secondary
                    ContactEntity savedEntity = saveContactEntity(request, LinkPrecedence.SECONDARY, primaryId);
                    log.info("Contact Saved as secondary: {}, with primary {}", savedEntity.getId(), savedEntity.getLinkedId());
                }
            }
            // getting updated data
            response.setContact( getRelatedContacts(request) );
            return BaseResponseUtil.createBaseResponse(response, StatusCode.OK);
        } catch (Exception e) {
            log.error("Failed to create contact: {}", e.getMessage());
            return BaseResponseUtil.createBaseResponse(response, StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    private ContactEntity saveContactEntity(IdentifyContactReq request, LinkPrecedence linkPrecedence, Long linkedId) {
        LocalDateTime currDateTime = LocalDateTime.now();
        ContactEntity mappedCouponEntity = modelMapper.map(request, ContactEntity.class);
        mappedCouponEntity.setLinkPrecedence(linkPrecedence.name());
        mappedCouponEntity.setLinkedId(linkedId);
        mappedCouponEntity.setCreatedAt(currDateTime);
        mappedCouponEntity.setUpdatedAt(currDateTime);
        return contactRepository.save(mappedCouponEntity);
    }

    private ContactDetails getRelatedContacts(IdentifyContactReq request) {
        List<ContactEntity> foundContactEntittList = contactRepository.checkDuplicateContact(request.getEmail(), request.getPhoneNumber());

        System.out.println(foundContactEntittList.size());

        Long primaryContactId = null;
        ContactEntity firstContactEntity = foundContactEntittList.get(0);
        if (ObjectUtils.isNotEmpty(firstContactEntity.getLinkedId())) {
            primaryContactId = firstContactEntity.getLinkedId();
        } else {
            primaryContactId = firstContactEntity.getId();
        }
        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Long> secondaryContactIds = new ArrayList<>();


        List<ContactEntity> finalContactList = contactRepository.getContactEntitiesByLinkedIdOrId(primaryContactId, primaryContactId);
        for (ContactEntity contactEntity : finalContactList) {
            System.out.println(contactEntity);
            if (contactEntity.getLinkPrecedence().equalsIgnoreCase(LinkPrecedence.PRIMARY.name())) {
                primaryContactId = contactEntity.getId();
                log.info("Primary found: {}", contactEntity.getId());
                if (StringUtils.isNotEmpty(contactEntity.getEmail())) {
                    emails.add(0, contactEntity.getEmail());
                }
                if (StringUtils.isNotEmpty(contactEntity.getPhoneNumber())) {
                    phoneNumbers.add(0, contactEntity.getPhoneNumber());
                }
            } else {
                if (StringUtils.isNotEmpty(contactEntity.getEmail())) {
                    emails.add(contactEntity.getEmail());
                }
                if (StringUtils.isNotEmpty(contactEntity.getPhoneNumber())) {
                    phoneNumbers.add(contactEntity.getPhoneNumber());
                }
                secondaryContactIds.add(contactEntity.getId());
            }
        }
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPrimaryContactId(primaryContactId);
        contactDetails.setEmails(emails);
        contactDetails.setPhoneNumbers(phoneNumbers);
        contactDetails.setSecondaryContactIds(secondaryContactIds);
        return contactDetails;
    }
}
