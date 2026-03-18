package com.winecellar.auth.domain;

public enum IdentityEventType {
    ACCOUNT_REGISTERED,
    ACTIVATION_ISSUED,
    ACTIVATION_SUCCEEDED,
    ACTIVATION_REJECTED,
    SIGN_IN_SUCCEEDED,
    SIGN_IN_FAILED,
    SESSION_ENDED
}
