package com.bitespeed.us.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyContactReq {

    private String email;
    private String phoneNumber;

}
