package com.ahmed.ahmed.model;

import java.io.Serializable;


    public class User implements Serializable {
        private int id;
        private String phoneNumber;
        private String fullName;
        private String email;
        private boolean isPhoneNumberVerified;
        private String image;
        private String password;
        private String address;
        private String desc;
        private String UserType;

        public User(int id, String phoneNumber, String fullName, String email, boolean isPhoneNumberVerified, String image, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
            this.email = email;
            this.isPhoneNumberVerified = isPhoneNumberVerified;
            this.image = image;
            this.password = password;
        }

        public User(int id, String fullName, String image, String password, String address, String desc, String userType) {
            this.id = id;
            this.fullName = fullName;
            this.image = image;
            this.password = password;
            this.address = address;
            this.desc = desc;
            UserType = userType;
        }

        public boolean isPhoneNumberVerified() {
            return isPhoneNumberVerified;
        }

        public void setPhoneNumberVerified(boolean phoneNumberVerified) {
            isPhoneNumberVerified = phoneNumberVerified;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUserType() {
            return UserType;
        }

        public void setUserType(String userType) {
            UserType = userType;
        }

        public User(int id, String phoneNumber, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.password = password;
        }

        public User(int id, String phoneNumber, String fullName, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public User() {
        }



        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean getIsPhoneNumberVerified() {
            return isPhoneNumberVerified;
        }

        public void setIsPhoneNumberVerified(boolean isPhoneNumberVerified) {
            this.isPhoneNumberVerified = isPhoneNumberVerified;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
}
