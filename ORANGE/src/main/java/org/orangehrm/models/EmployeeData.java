package org.orangehrm.models;

public class EmployeeData {
    private String adminUser;
    private String adminPass;
    private String firstName;
    private String middleName;
    private String lastName;
    private boolean createLoginDetails;
    private String loginUser;
    private String loginPass;
    private String otherId;
    private String driverLicense;
    private String licenseExpiry;
    private String nationality;
    private String maritalStatus;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private String customField;
    private String attachmentPath;
    private String attachmentComment;

    public String getAdminUser() { return adminUser; }
    public String getAdminPass() { return adminPass; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public boolean isCreateLoginDetails() { return createLoginDetails; }
    public String getLoginUser() { return loginUser; }
    public String getLoginPass() { return loginPass; }
    public String getOtherId() { return otherId; }
    public String getDriverLicense() { return driverLicense; }
    public String getLicenseExpiry() { return licenseExpiry; }
    public String getNationality() { return nationality; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getBloodType() { return bloodType; }
    public String getCustomField() { return customField; }
    public String getAttachmentPath() { return attachmentPath; }
    public String getAttachmentComment() { return attachmentComment; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
