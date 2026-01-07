package org.example.enums;

public enum PrepaidAmount {
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100),
    HUNDRED_FIFTY(150),
    TWO_HUNDRED(200),

    //individual input in step definition
    ;
    int code;


    PrepaidAmount(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PrepaidAmount fromOption(int option) {
        return switch (option) {
            case 1 -> TWENTY;
            case 2 -> FIFTY;
            case 3 -> HUNDRED;
            case 4 -> HUNDRED_FIFTY;
            case 5 -> TWO_HUNDRED;
            default -> null;
        };
    }
}
