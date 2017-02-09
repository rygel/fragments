/*
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.andromeda.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Alexander Brandt
 */
public class Utilities {
    /** The logger instance for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);

    private static Random random = new Random(0x2626);

    /**
     * Protect the constructor.
     */
    private Utilities() {

    }

    /**
     * Remove characters which are not allowed in a slug.
     * @param textToSlug The string which will be converted into a slug.
     * @return The converted string.
     */
    public static String slugify(String textToSlug) {
        return textToSlug.replaceAll("\\s+|/|\\|:", "_").toLowerCase();
    }

    /**
     * Remove the trailing slash, but beware of "/".
     * @param pattern the URL pattern to check.
     * @return the pattern without the trailing slash.
     */
    public static String removeTrailingSlash(String pattern) {
        if ((!"/".equals(pattern)) && (pattern.endsWith("/"))) {
            return pattern.substring(0, pattern.length() - 1);
        } else {
            return pattern;
        }
    }

  public static String obfuscate(String email) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < email.length(); i++) {
      char c = email.charAt(i);
      switch (random.nextInt(5)) {
        case 0:
        case 1:
          sb.append("&#").append((int) c).append(';');
          break;
        case 2:
        case 3:
          sb.append("&#x").append(Integer.toHexString(c)).append(';');
          break;
        case 4:
          String encoded = encode(c);
          if (encoded != null) sb.append(encoded); else sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String encode(char c) {
    switch (c) {
      case '&':  return "&amp;";
      case '<':  return "&lt;";
      case '>':  return "&gt;";
      case '"':  return "&quot;";
      case '\'': return "&#39;";
      default: return null;
    }
  }

}
