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

import net.sourceforge.cobertura.CoverageIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Alexander Brandt
 */
public class Utilities {
    /** The logger instance for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);

    private static Random random = new Random(0x2626);

    public static final String ACTIVE_ID = "active";
    public static final String FIRST_ID = "first";
    public static final String LAST_ID = "last";
    public static final String NEXT_ID = "next";
    public static final String PAGINATION_ID = "pagination";
    public static final String PREVIOUS_ID = "previous";
    public static final String TEXT_ID = "text";
    public static final String URL_ID = "url";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    /**
     * Protect the constructor.
     */
    @CoverageIgnore
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

    public static Map<String, Object> calculatePagination(int currentPage, int maxNoInPagination,  long totalHits) {
        Map<String, Object> result = new TreeMap<>();

        int currentPagination = maxNoInPagination;
        int maxPagesFromHits = (int)Math.ceil(totalHits/10.);

        if (maxNoInPagination > maxPagesFromHits) {
            maxNoInPagination = maxPagesFromHits;
        }
        int fromPage = currentPage - (maxNoInPagination - 1)/2;
        int toPage = currentPage + (maxNoInPagination - 1)/2;

        int lowerDifference = 0 - fromPage + 1;

        if (lowerDifference > 0) {
            fromPage = 1;
            toPage = toPage + lowerDifference;
        }

        while (toPage > maxPagesFromHits) {
            toPage = toPage - 1;
            fromPage = fromPage - 1;
        }
        LOGGER.debug("fromPage: {}, toPage: {}", fromPage, toPage);

        if (maxPagesFromHits <= maxNoInPagination) {
            currentPagination = maxPagesFromHits;
            result.put(NEXT_ID, getItem(NEXT_ID, false, Integer.toString(currentPagination)));
            result.put(LAST_ID, getItem(LAST_ID, false, Integer.toString(maxPagesFromHits)));
        } else {
            result.put(NEXT_ID, getItem(NEXT_ID, true, Integer.toString(currentPage + 1)));
            result.put(LAST_ID, getItem(LAST_ID, true, Integer.toString(maxPagesFromHits)));
        }

        if (currentPage == 1) {
            result.put(FIRST_ID, getItem(FIRST_ID, false, Integer.toString(1)));
            result.put(PREVIOUS_ID, getItem(PREVIOUS_ID, false, Integer.toString(1)));
        } else {
            result.put(FIRST_ID, getItem(FIRST_ID, true, Integer.toString(1)));
            result.put(PREVIOUS_ID, getItem(PREVIOUS_ID, true, Integer.toString(currentPage - 1)));
        }

        if (currentPage == maxPagesFromHits) {
            result.put(NEXT_ID, getItem(NEXT_ID, false, Integer.toString(currentPage)));
            result.put(LAST_ID, getItem(LAST_ID, false, Integer.toString(maxPagesFromHits)));
        }

        int counter = 0;
        for(int i = fromPage; i < toPage + 1; i++) {
            counter = counter + 1;
            result.put(Integer.toString(counter), getItem(Integer.toString(i), true, Integer.toString(i)));
        }

        return result;
    }

    public static Map<String, Object> getItem(String text, boolean active, String url) {
        Map<String, Object> result = new TreeMap<>();
        result.put(TEXT_ID, text);
        if (active) {
            result.put(ACTIVE_ID, TRUE);
        } else {
            result.put(ACTIVE_ID, FALSE);
        }
        result.put(URL_ID, url);
        return result;
    }

}
