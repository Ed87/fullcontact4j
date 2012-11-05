package com.fullcontact.api.libs.fullcontact4j;

import com.fullcontact.api.libs.fullcontact4j.entity.cardshark.ContactInfo;
import com.fullcontact.api.libs.fullcontact4j.entity.cardshark.UploadResponse;
import com.fullcontact.api.libs.fullcontact4j.entity.cardshark.UploadWebhookResponse;
import com.fullcontact.api.libs.fullcontact4j.entity.cardshark.ViewRequestsEntity;
import com.fullcontact.api.libs.fullcontact4j.entity.cardshark.contactinfo.Name;

import java.io.IOException;

public class CardSharkTest extends AbstractApiTest {

    public void test_upload_card() throws IOException, FullContactException {
        String json = loadJson("cardshark.upload.response.json");
        UploadResponse entity = new FullContact("fake_api_key").getCardSharkHandler().parseUploadJsonResponse(json);
        assertNotNull(entity);
        assertEquals(202, entity.getStatusCode());
        assertEquals("b23bb2dd-fe7c-4d83-ab8d-f23792b8e4cf", entity.getRequestId());
        assertEquals(true, entity.isQueued());

        System.out.println("Response Status: " + entity.getStatusCode());
        System.out.println("Request Id: " + entity.getRequestId());
        System.out.println("is Queued: " + entity.isQueued());
    }

    public void test_upload_card_webhook_response() throws IOException, FullContactException {
        String json = loadJson("cardshark.upload.webhook.response.json");
        UploadWebhookResponse entity = new FullContact("fake_api_key").getCardSharkHandler().parseUploadWebhookJsonResponse(json);
        assertNotNull(entity);
        assertEquals("b23bb2dd-fe7c-4d83-ab8d-f23792b8e4cf", entity.getRequestId());
        assertEquals("https://d1h3f0foa0xzdz.cloudfront.net/1/2I63W5XH0JPPYKTRWFGL0P75N9OEK7.vcf", entity.getvCardUrl());

        assertNotNull(entity.getContact().getName());
        assertEquals("Elliott", entity.getContact().getName().getFamilyName());
        assertEquals("Matt", entity.getContact().getName().getGivenName());
        assertNull(entity.getContact().getName().getMiddleName());

        assertEquals(1, entity.getContact().getEmails().size());
        assertEquals("matt@fullcontact.com", entity.getContact().getEmails().get(0).getValue());
        assertEquals("work", entity.getContact().getEmails().get(0).getType());

        assertEquals(1, entity.getContact().getOrganizations().size());
        assertEquals("Full contact", entity.getContact().getOrganizations().get(0).getName());
        assertEquals("Senior UI/UX Engineer", entity.getContact().getOrganizations().get(0).getTitle());
        assertEquals(true, entity.getContact().getOrganizations().get(0).isPrimary());

        assertEquals(1, entity.getContact().getPhoneNumbers().size());
        assertEquals("+1-720-334-2209", entity.getContact().getPhoneNumbers().get(0).getValue());
        assertEquals("work", entity.getContact().getPhoneNumbers().get(0).getType());

        assertEquals(1, entity.getContact().getPhotos().size());
        assertEquals("https://d1h3f0foa0xzdz.cloudfront.net/1/b23bb2dd-fe7c-4d83-ab8d-f23792b8e4cf-front.png", entity.getContact().getPhotos().get(0).getValue());
        assertEquals("BusinessCard", entity.getContact().getPhotos().get(0).getType());
        assertFalse(entity.getContact().getPhotos().get(0).isPrimary());

        assertEquals(1, entity.getContact().getUrls().size());
        assertEquals("www.fullcontact.com", entity.getContact().getUrls().get(0).getValue());
        assertEquals("other", entity.getContact().getUrls().get(0).getType());

        System.out.println("Request Id: " + entity.getRequestId());
        System.out.println("VCard URL: " + entity.getvCardUrl());

        ContactInfo contactInfo = entity.getContact();
        Name name = entity.getContact().getName();
        System.out.println("Given Name: " + name.getGivenName());
        System.out.println("Family Name: " + name.getFamilyName());
        System.out.println("Middle Name: " + name.getMiddleName());
        System.out.println("Name Prefix: " + name.getHonorificPrefix());
        System.out.println("Name Suffix: " + name.getHonorificSuffix());

        System.out.println("Emails count: " + contactInfo.getEmails().size());
        System.out.println("Email Address: " + contactInfo.getEmails().get(0).getValue());
        System.out.println("Email Type: " + contactInfo.getEmails().get(0).getType());

        System.out.println("Phone numbers count: " + contactInfo.getPhoneNumbers().size());
        System.out.println("Phone number: " + contactInfo.getPhoneNumbers().get(0).getValue());
        System.out.println("Phone number Type: " + contactInfo.getPhoneNumbers().get(0).getType());

        System.out.println("Photos count: " + contactInfo.getPhotos().size());
        System.out.println("Photo Url: " + contactInfo.getPhotos().get(0).getValue());
        System.out.println("Photo Type: " + contactInfo.getPhotos().get(0).getType());
        System.out.println("Is photo primary: " + contactInfo.getPhotos().get(0).isPrimary());

        System.out.println("URLs count: " + contactInfo.getUrls().size());
        System.out.println("URL: " + contactInfo.getUrls().get(0).getValue());
        System.out.println("URL Type: " + contactInfo.getUrls().get(0).getType());

        System.out.println("Organizations count: " + contactInfo.getOrganizations().size());
        System.out.println("Organization Name: " + contactInfo.getOrganizations().get(0).getName());
        System.out.println("Organization Title: " + contactInfo.getOrganizations().get(0).getTitle());
        System.out.println("Is primary organization: " + contactInfo.getOrganizations().get(0).isPrimary());
    }

    public void test_view_requests() throws IOException, FullContactException {
        String json = loadJson("cardshark.view.requests.response.json");
        ViewRequestsEntity viewRequestsEntity = new FullContact("fake_api_key").getCardSharkHandler().parseViewRequestsJsonResponse(json);
        assertNotNull(viewRequestsEntity);
        assertEquals(200, viewRequestsEntity.getStatus());
        assertEquals(1, viewRequestsEntity.getTotalPages());
        assertEquals(0, viewRequestsEntity.getCurrentPage());
        assertEquals(2, viewRequestsEntity.getTotalRecords());
        assertEquals(2, viewRequestsEntity.getCount());
        assertEquals(2, viewRequestsEntity.getResults().size());
        UploadWebhookResponse requestEntity = viewRequestsEntity.getResults().get(0);
        assertNotNull(requestEntity.getContact().getName());
        assertEquals("Elliott", requestEntity.getContact().getName().getFamilyName());
        assertEquals(1, requestEntity.getContact().getEmails().size());
        assertEquals(1, requestEntity.getContact().getOrganizations().size());
        assertEquals("Senior UI/UX Engineer", requestEntity.getContact().getOrganizations().get(0).getTitle());
        assertEquals(1, requestEntity.getContact().getPhoneNumbers().size());
        assertEquals("work", requestEntity.getContact().getPhoneNumbers().get(0).getType());
        assertEquals(1, requestEntity.getContact().getPhotos().size());
        assertEquals("https://d1h3f0foa0xzdz.cloudfront.net/1/b23bb2dd-fe7c-4d83-ab8d-f23792b8e4cf-front.png", requestEntity.getContact().getPhotos().get(0).getValue());
        assertEquals(1, requestEntity.getContact().getUrls().size());
        assertEquals("www.fullcontact.com", requestEntity.getContact().getUrls().get(0).getValue());

        System.out.println("Status: " + viewRequestsEntity.getStatus());
        System.out.println("Count: " + viewRequestsEntity.getCount());
        System.out.println("Total Pages: " + viewRequestsEntity.getTotalPages());
        System.out.println("Current Page: " + viewRequestsEntity.getCurrentPage());
        System.out.println("Total Records: " + viewRequestsEntity.getTotalRecords());
        System.out.println("Results count: " + viewRequestsEntity.getResults().size());
    }

    public void test_view_request() throws IOException, FullContactException {
        String json = loadJson("cardshark.view.request.response.json");
        //new FullContact("fake_api_key").getCardSharkHandler().parseViewRequestJsonResponse(json);
    }

}