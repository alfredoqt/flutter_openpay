/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.alfredoqt.flutter_openpay;

import java.io.UnsupportedEncodingException;

/**
 * Utilities for strings.
 *
 * @since 1.8
 * @author Yaniv Inbar
 */
public class StringUtils {

    /**
     * Line separator to use for this OS, i.e. {@code "\n"} or {@code "\r\n"}.
     *
     * @since 1.8
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");


    public static byte[] getBytesUtf8(String string) {
        if (string == null) {
            return null;
        }
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String newStringUtf8(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private StringUtils() {}
}