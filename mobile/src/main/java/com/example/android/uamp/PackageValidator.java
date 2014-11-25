/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.uamp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.util.Base64;

import com.example.android.uamp.utils.LogHelper;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Validates that the calling package is authorized to use this
 * {@link android.service.media.MediaBrowserService}.
 */
public class PackageValidator {
    private static final String TAG = LogHelper.makeLogTag(PackageValidator.class);

    // Replace with your package whitelist
    static final byte[][] VALID_PUBLIC_SIGNATURES = new byte[][]{
        // Android Auto release public key
        extractKey(
        "\060\202\003\275\060\202\002\245\240\003\002\001\002\002\011\000\307\217\236\113" +
        "\223\101\060\006\060\015\006\011\052\206\110\206\367\015\001\001\005\005\000\060" +
        "\165\061\013\060\011\006\003\125\004\006\023\002\125\123\061\023\060\021\006\003" +
        "\125\004\010\014\012\103\141\154\151\146\157\162\156\151\141\061\026\060\024\006" +
        "\003\125\004\007\014\015\115\157\165\156\164\141\151\156\040\126\151\145\167\061" +
        "\024\060\022\006\003\125\004\012\014\013\107\157\157\147\154\145\040\111\156\143" +
        "\056\061\020\060\016\006\003\125\004\013\014\007\101\156\144\162\157\151\144\061" +
        "\021\060\017\006\003\125\004\003\014\010\147\145\141\162\150\145\141\144\060\036" +
        "\027\015\061\064\060\065\062\067\062\063\060\065\063\064\132\027\015\064\061\061" +
        "\060\061\062\062\063\060\065\063\064\132\060\165\061\013\060\011\006\003\125\004" +
        "\006\023\002\125\123\061\023\060\021\006\003\125\004\010\014\012\103\141\154\151" +
        "\146\157\162\156\151\141\061\026\060\024\006\003\125\004\007\014\015\115\157\165" +
        "\156\164\141\151\156\040\126\151\145\167\061\024\060\022\006\003\125\004\012\014" +
        "\013\107\157\157\147\154\145\040\111\156\143\056\061\020\060\016\006\003\125\004" +
        "\013\014\007\101\156\144\162\157\151\144\061\021\060\017\006\003\125\004\003\014" +
        "\010\147\145\141\162\150\145\141\144\060\202\001\042\060\015\006\011\052\206\110" +
        "\206\367\015\001\001\001\005\000\003\202\001\017\000\060\202\001\012\002\202\001" +
        "\001\000\323\235\027\016\103\110\261\124\114\137\154\023\275\132\145\244\053\270" +
        "\072\331\362\064\255\257\344\036\317\113\340\340\202\141\366\312\346\142\302\224" +
        "\356\255\322\203\103\324\175\123\074\107\365\116\045\260\057\246\043\025\344\210" +
        "\026\012\041\143\125\200\313\142\116\014\144\023\056\334\201\153\335\140\170\015" +
        "\142\221\156\360\214\131\051\200\362\135\353\076\323\152\137\276\233\272\334\302" +
        "\001\017\363\347\275\121\142\246\215\150\122\266\337\172\330\376\232\272\004\246" +
        "\071\300\357\130\024\113\103\244\370\176\227\131\153\046\157\314\105\035\005\114" +
        "\241\225\204\043\073\024\047\151\341\233\301\034\234\371\000\075\363\131\000\157" +
        "\276\134\263\321\072\204\120\011\253\060\311\213\035\343\142\156\140\003\367\013" +
        "\006\156\204\067\024\154\305\246\223\272\301\213\320\125\103\310\046\222\266\360" +
        "\252\217\170\003\272\222\264\265\051\334\334\202\232\122\222\130\166\231\323\224" +
        "\254\244\103\360\261\367\055\221\255\050\134\156\133\206\004\372\353\261\014\013" +
        "\064\076\142\301\115\326\202\121\057\264\052\372\143\020\214\122\154\337\002\003" +
        "\001\000\001\243\120\060\116\060\035\006\003\125\035\016\004\026\004\024\032\360" +
        "\137\140\327\256\350\224\211\122\162\131\012\046\201\032\311\327\316\333\060\037" +
        "\006\003\125\035\043\004\030\060\026\200\024\032\360\137\140\327\256\350\224\211" +
        "\122\162\131\012\046\201\032\311\327\316\333\060\014\006\003\125\035\023\004\005" +
        "\060\003\001\001\377\060\015\006\011\052\206\110\206\367\015\001\001\005\005\000" +
        "\003\202\001\001\000\224\153\003\143\101\017\273\163\101\110\176\144\352\054\077" +
        "\300\230\175\173\174\114\301\055\173\022\262\206\226\034\226\242\014\111\063\062" +
        "\343\000\336\240\321\240\217\037\020\170\320\204\002\373\312\200\227\344\113\355" +
        "\124\061\352\214\155\265\375\046\337\134\224\031\003\334\065\206\355\330\054\101" +
        "\114\040\053\363\316\150\054\256\155\331\060\042\346\324\063\205\336\231\021\210" +
        "\241\131\045\026\121\337\327\360\024\021\242\354\133\242\313\075\101\260\100\376" +
        "\042\061\320\352\103\153\030\200\162\256\302\157\256\323\205\345\331\017\021\256" +
        "\103\307\346\035\206\313\307\316\051\022\371\267\015\003\201\374\262\014\222\112" +
        "\120\111\361\002\325\377\250\077\134\301\336\352\317\123\367\122\274\100\377\054" +
        "\050\016\166\272\161\147\227\142\355\054\022\312\347\276\126\257\323\145\014\267" +
        "\342\323\362\200\114\303\331\337\041\026\130\177\311\370\126\220\310\263\071\342" +
        "\027\161\254\225\001\007\115\237\234\351\006\113\232\313\133\044\030\350\320\103" +
        "\231\023\154\067\003\316\050\016\331\035\253\252\176\207\011\337\145\345\235\026" +
        "\041"),

        // Android Auto debug public key
        extractKey(
        "\060\202\003\275\060\202\002\245\240\003\002\001\002\002\011\000\347\344\006\360" +
        "\327\303\226\363\060\015\006\011\052\206\110\206\367\015\001\001\005\005\000\060" +
        "\165\061\013\060\011\006\003\125\004\006\023\002\125\123\061\023\060\021\006\003" +
        "\125\004\010\014\012\103\141\154\151\146\157\162\156\151\141\061\026\060\024\006" +
        "\003\125\004\007\014\015\115\157\165\156\164\141\151\156\040\126\151\145\167\061" +
        "\024\060\022\006\003\125\004\012\014\013\107\157\157\147\154\145\040\111\156\143" +
        "\056\061\020\060\016\006\003\125\004\013\014\007\101\156\144\162\157\151\144\061" +
        "\021\060\017\006\003\125\004\003\014\010\147\145\141\162\150\145\141\144\060\036" +
        "\027\015\061\064\060\065\062\067\062\063\060\062\065\061\132\027\015\064\061\061" +
        "\060\061\062\062\063\060\062\065\061\132\060\165\061\013\060\011\006\003\125\004" +
        "\006\023\002\125\123\061\023\060\021\006\003\125\004\010\014\012\103\141\154\151" +
        "\146\157\162\156\151\141\061\026\060\024\006\003\125\004\007\014\015\115\157\165" +
        "\156\164\141\151\156\040\126\151\145\167\061\024\060\022\006\003\125\004\012\014" +
        "\013\107\157\157\147\154\145\040\111\156\143\056\061\020\060\016\006\003\125\004" +
        "\013\014\007\101\156\144\162\157\151\144\061\021\060\017\006\003\125\004\003\014" +
        "\010\147\145\141\162\150\145\141\144\060\202\001\042\060\015\006\011\052\206\110" +
        "\206\367\015\001\001\001\005\000\003\202\001\017\000\060\202\001\012\002\202\001" +
        "\001\000\242\356\360\300\022\205\313\071\352\245\032\336\264\235\304\126\236\171" +
        "\375\212\364\343\320\040\347\011\106\276\260\247\214\203\374\016\263\053\123\353" +
        "\044\174\247\265\016\154\051\260\263\155\236\030\142\064\177\211\323\115\013\242" +
        "\115\341\163\310\335\130\247\212\072\212\163\050\140\315\274\277\307\276\164\273" +
        "\321\234\244\333\250\043\366\073\114\060\174\375\331\246\135\246\154\003\353\261" +
        "\115\231\071\106\330\121\021\257\344\360\060\076\132\201\243\347\260\124\166\316" +
        "\126\272\272\005\057\034\154\363\353\226\003\306\220\231\261\017\323\243\014\203" +
        "\056\174\140\061\250\057\206\364\276\071\354\167\312\035\205\067\272\111\177\004" +
        "\264\334\247\106\166\105\217\154\272\237\364\127\246\323\333\071\216\067\231\133" +
        "\363\267\106\011\312\241\023\310\047\204\013\053\275\036\176\060\031\250\234\201" +
        "\031\300\331\311\003\060\072\317\274\034\211\047\255\247\374\371\304\131\044\074" +
        "\352\073\036\353\266\331\174\063\162\206\007\141\005\226\064\351\353\361\162\304" +
        "\222\347\002\216\220\225\171\373\032\266\032\225\062\064\310\265\075\165\002\003" +
        "\001\000\001\243\120\060\116\060\035\006\003\125\035\016\004\026\004\024\365\003" +
        "\311\347\022\104\014\017\014\015\003\053\217\110\146\333\360\066\005\031\060\037" +
        "\006\003\125\035\043\004\030\060\026\200\024\365\003\311\347\022\104\014\017\014" +
        "\015\003\053\217\110\146\333\360\066\005\031\060\014\006\003\125\035\023\004\005" +
        "\060\003\001\001\377\060\015\006\011\052\206\110\206\367\015\001\001\005\005\000" +
        "\003\202\001\001\000\015\312\371\207\121\121\360\212\146\067\210\122\261\100\075" +
        "\112\160\220\127\045\332\324\144\041\316\224\040\105\261\176\236\231\040\072\175" +
        "\214\171\272\174\155\335\274\126\227\340\242\200\366\070\023\120\134\045\034\146" +
        "\111\373\245\150\376\372\353\175\036\023\233\035\126\225\344\123\140\322\227\103" +
        "\250\271\332\365\006\175\143\212\022\371\232\342\214\256\364\135\237\304\216\126" +
        "\024\036\370\156\322\222\043\144\006\303\360\051\202\026\132\060\111\036\171\250" +
        "\044\243\063\230\222\337\262\331\007\175\222\062\275\101\006\046\053\064\013\347" +
        "\160\250\330\101\122\274\162\324\321\316\032\115\101\003\301\201\160\100\367\305" +
        "\345\371\335\103\077\055\064\045\144\056\027\113\054\232\022\234\046\353\337\164" +
        "\111\305\027\261\357\153\034\377\200\044\075\237\066\253\100\215\302\044\037\035" +
        "\071\165\160\027\311\234\310\064\101\317\202\121\371\200\351\136\216\201\017\347" +
        "\306\267\136\150\277\354\346\250\057\061\151\077\117\327\362\140\240\065\342\062" +
        "\034\277\352\274\040\166\057\126\304\367\374\231\276\323\234\020\276\012\113\027" +
        "\320"),
    };

    /**
     * Disallow instantiation of this helper class.
     */
    private PackageValidator() {}

    /**
     * Throws when the caller is not authorized to get data from this MediaBrowserService
     */
    public static void checkCallerAllowed(Context context, String callingPackage, int callingUid) {
        if (!isCallerAllowed(context, callingPackage, callingUid)) {
            throw new SecurityException("signature check failed.");
        }
    }

    /**
     * @return false if the caller is not authorized to get data from this MediaBrowserService
     */
    public static boolean isCallerAllowed(Context context, String callingPackage, int callingUid) {
        // Always allow calls from the framework or development environment.
        if (Process.SYSTEM_UID == callingUid || !"user".equals(Build.TYPE)) {
            return true;
        }
        PackageInfo packageInfo;
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(
                    callingPackage, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException ignored) {
            LogHelper.w(TAG, "Package manager can't find package " + callingPackage
                        + ", defaulting to false");
            return false;
        }
        if (packageInfo == null) {
            LogHelper.w(TAG, "Package manager can't find package: " + callingPackage);
            return false;
        }

        if (packageInfo.signatures.length != 1) {
            LogHelper.w(TAG, "Package has more than one signature.");
            return false;
        }
        final byte[] signature = packageInfo.signatures[0].toByteArray();

        for (int i = 0; i < VALID_PUBLIC_SIGNATURES.length; i++) {
            byte[] validSignature = VALID_PUBLIC_SIGNATURES[i];
            if (Arrays.equals(validSignature, signature)) {
                return true;
            }
        }

        LogHelper.v(TAG, "Signature not valid.  Found: \n" + Base64.encodeToString(signature, 0));
        return false;
    }

    private static byte[] extractKey(String keyString) {
        try {
            return keyString.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
