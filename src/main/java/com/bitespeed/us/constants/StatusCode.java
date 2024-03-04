package com.bitespeed.us.constants;

public enum StatusCode {

    DATA_ISSUE_FOR_THE_PO(10001,1,"DATA_ISSUE_FOR_THE_PURCHASE_ORDER"),








    // Base codes
    OK(200, 0, "Success"),
    SERVER_ERROR(400, 1, "Failed!"),
    SUCCESS(201, 0, "Success"),



    // Server & security error codes
    NO_DATA(300, 1, "No data found!"),
    NOT_APPLICABLE(301, 1, "Coupon can't be applicable on this order"),
    INTERNAL_SERVER_ERROR(500, 1, "Sorry! Something went wrong."),
    BAD_REQUEST(400, 1, "Bad Request");



    private Integer code;
    private Integer errorState;
    private String statusMessage;

    StatusCode(Integer code, Integer errorState, String statusMessage) {
        this.code = code;
        this.statusMessage = statusMessage;
        this.errorState = errorState;
    }

    public StatusCode getByStatusCode(Integer code) {
        StatusCode[] values = values();
        for (StatusCode statusCode : values) {
            if (statusCode.getCode().equals(code)) {
                return statusCode;
            }
        }
        return StatusCode.SERVER_ERROR;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public Integer getErrorState() {
        return errorState;
    }


}
