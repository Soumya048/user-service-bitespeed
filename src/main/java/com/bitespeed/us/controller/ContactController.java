package com.bitespeed.us.controller;

import com.bitespeed.us.constants.StatusCode;
import com.bitespeed.us.model.IdentifyContactReq;
import com.bitespeed.us.response.BaseResponse;
import com.bitespeed.us.service.ContactService;
import com.bitespeed.us.util.BaseResponseUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(value = "v1/identify")
    public ResponseEntity createCustomerContactHandler(@Valid @RequestBody IdentifyContactReq request) {
        try {
            if (request.getEmail() == null && request.getPhoneNumber() == null) {
                return ResponseEntity.ok(BaseResponseUtil.createBaseResponse(new BaseResponse(), StatusCode.BAD_REQUEST, "At least one of email or phoneNumber must be provided."));
            }
            return ResponseEntity.ok(contactService.identifyContact(request));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponseUtil.createBaseResponse(StatusCode.INTERNAL_SERVER_ERROR));
        }
    }


    @GetMapping(value = "v1/app/version")
    public ResponseEntity fetchAppVersionHandler() {
        try {
            String version = "v1_03-03-2024";
            return ResponseEntity.ok(version);
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponseUtil.createBaseResponse(StatusCode.INTERNAL_SERVER_ERROR));
        }
    }

}
