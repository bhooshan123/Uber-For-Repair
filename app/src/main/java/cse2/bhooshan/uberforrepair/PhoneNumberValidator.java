package cse2.bhooshan.uberforrepair;

import android.util.Patterns;

public class PhoneNumberValidator {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Remove spaces and dashes from the phone number
        String cleanedPhoneNumber = phoneNumber.replaceAll("[\\s\\-]+", "");

        // Use Android's Patterns class to validate the phone number
        return Patterns.PHONE.matcher(cleanedPhoneNumber).matches();
    }
}

