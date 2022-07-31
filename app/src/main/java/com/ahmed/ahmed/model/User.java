package com.ahmed.ahmed.model;

import java.io.Serializable;


    public class User implements Serializable {
        private String id;
        private String phoneNumber;
        private String fullName;
        private String email;
        private boolean isPhoneNumberVerified;
        private String image;
        private String password;
        private String address;
        private String desc;

        public User(String id, String phoneNumber, String fullName, String email, boolean isPhoneNumberVerified, String image, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
            this.email = email;
            this.isPhoneNumberVerified = isPhoneNumberVerified;
            this.image = image;
            this.password = password;
        }

        public User(String id, String phoneNumber, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.password = password;
        }

        public User(String id, String phoneNumber, String fullName, String password) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
            this.password = password;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
