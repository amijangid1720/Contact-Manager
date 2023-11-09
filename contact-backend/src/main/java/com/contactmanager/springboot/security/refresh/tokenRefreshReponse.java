package com.contactmanager.springboot.security.refresh;

public class tokenRefreshReponse {

        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";

        public tokenRefreshReponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
}
