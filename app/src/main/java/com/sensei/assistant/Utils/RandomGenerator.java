package com.sensei.assistant.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Asad on 08-Jan-17.
 */

public class RandomGenerator {

    public static SecureRandom random = new SecureRandom();

    public static String getRandomID() {
        return new BigInteger(130, random).toString(32);
    }

}
