# Bitespeed User-Service

## Swagger Documentation
Explore the API documentation using Swagger:
[Swagger UI](https://user-service-bitespeed-production.up.railway.app/user-service/swagger-ui/index.html#/)


### Service Endpoint Configuration

```
baseUrl: https://user-service-bitespeed-production.up.railway.app
contextPath: user-service
```

### ER Diagram
![user_db_er_diagram](https://github.com/Soumya048/user-service-bitespeed/assets/91946820/88667484-fbd6-4cf3-99c0-2d780b895509)


## Identify contact API
```
https://user-service-bitespeed-production.up.railway.app/user-service/v1/identify
```
### Method 
- **POST**
- `/v1/identify`

### Description
This endpoint is used to identify a customer contact based on their email address or phone number.

### Request Payload
```json
{
  "email": "string",
  "phoneNumber": "string"
}
```
- `email` (string): The email address of the customer.
- `phoneNumber` (string): The phone number of the customer.

**Note**: At least one of `email` or `phoneNumber` must be provided.

### Response Payload
```json
{
  "es": 0,
  "message": "Success",
  "statusCode": 200,
  "timeStamp": "2024-03-05T05:52:07.250218123",
  "contact": {
    "primaryContactId": 0, 
    "emails": ["string"],
    "phoneNumbers": ["string"],
    "secondaryContactIds": [0] 
  }
}
```

- `es` (integer): Error status code. (if `es` is 0 success else failed)
- `message` (string): A message indicating the status of the request.
- `statusCode` (integer): HTTP status code (_custom_).
- `timeStamp` (string): Timestamp of the response.
- `contact` (object): Information about the identified customer contact.
    - `primaryContactId` (integer): ID of the primary contact.
    - `emails` (array of strings): List of email addresses associated with the contact.
    - `phoneNumbers` (array of strings): List of phone numbers associated with the contact.
    - `secondaryContactIds` (array of integers): IDs of secondary contacts associated with the primary contact.

### Error Handling
- In this case `es` will not be zero. Still `Server response` will give **200**. 
- By `es` we can identify the errors from backend.
- If both email and phoneNumber are not provided, a 400 Bad Request response will be returned.
```json
{
  "es": 1,
  "message": "At least one of email or phoneNumber must be provided.",
  "statusCode": 400,
  "timeStamp": "2024-03-05T11:43:09.8956821"
}
```

- If an internal server error occurs during processing, a 500 Internal Server Error response will be returned.
```json
{
  "es": 1,
  "message": "Sorry! Something went wrong.",
  "statusCode": 500,
  "timeStamp": "2024-03-05T11:46:21.8743594"
}
```

