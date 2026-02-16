package com.major.spoileralert.util;

import java.util.Random;

public class Utils{

    // Generate 6-digit OTP
    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

}
