/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2022 The ZAP Development Team
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
package org.zaproxy.addon.spider.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/** Unit test for {@link SpiderHttpHeaderParser}. */
class SpiderHttpHeaderParserUnitTest extends SpiderParserTestUtils {

    private static final String ROOT_PATH = "/";
    private static final int BASE_DEPTH = 0;

    @Test
    void shouldParseAnyMessage() {
        // Given
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        HttpMessage msg = createMessage();
        // When
        boolean canParse = headerParser.canParseResource(msg, ROOT_PATH, false);
        // Then
        assertThat(canParse, is(equalTo(true)));
    }

    @Test
    void shouldParseAnyMessageEvenIfAlreadyParsed() {
        // Given
        boolean alreadyParsed = true;
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        HttpMessage msg = createMessage();
        // When
        boolean canParse = headerParser.canParseResource(msg, ROOT_PATH, alreadyParsed);
        // Then
        assertThat(canParse, is(equalTo(true)));
    }

    @Test
    void shouldFailToParseAnUndefinedMessage() {
        // Given
        HttpMessage undefinedMessage = null;
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        // When / Then
        assertThrows(
                NullPointerException.class,
                () -> headerParser.parseResource(undefinedMessage, null, BASE_DEPTH));
    }

    @Test
    void shouldNotExtractUrlIfNoUrlHeadersPresent() {
        // Given
        HttpMessage msg = createMessage();
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), is(empty()));
    }

    @ParameterizedTest
    // TODO Adjust once targeting next core version
    // @ValueSource(strings = {HttpHeader.CONTENT_LOCATION, HttpHeader.REFRESH, HttpHeader.LINK})
    @ValueSource(strings = {"COntent-Location", "Refresh", "Link"})
    void shouldNotExtractUrlIfUrlHeaderIsEmpty(String header) {
        // Given
        HttpMessage msg = createMessage();
        msg.getResponseHeader().addHeader(header, "");
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, 0);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), is(empty()));
    }

    @Test
    void shouldExtractUrlFromContentLocationHeader() {
        // Given
        String value = "http://example.com/contentlocation";
        HttpMessage msg = createMessage();
        // TODO Adjust once targeting next core version
        // msg.getResponseHeader().addHeader(HttpHeader.CONTENT_LOCATION, value);
        msg.getResponseHeader().addHeader("Content-Location", value);
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), contains(value));
    }

    @Test
    void shouldExtractRelativeUrlFromContentLocationHeader() {
        // Given
        String url = "/rel/redirection";
        HttpMessage msg = createMessage();
        // TODO Adjust once targeting next core version
        // msg.getResponseHeader().addHeader(HttpHeader.CONTENT_LOCATION, url);
        msg.getResponseHeader().addHeader("Content-Location", url);
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), contains("http://example.com" + url));
    }

    @Test
    void shouldExtractUrlsFromLinkHeader() {
        // Given
        String url1 = "http://example.com/link1";
        String url2 = "/link2";
        HttpMessage msg = createMessage();
        msg.getResponseHeader()
                .addHeader(
                        // TODO Adjust once targeting next core version
                        // HttpHeader.LINK,
                        "Link", "<" + url1 + ">; param1=value1; param2=\"value2\";<" + url2 + ">");
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), contains(url1, "http://example.com" + url2));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "<http://example.com",
                "http://example.com>",
                "more>bad<stuff",
                "https://www.example.com"
            })
    void shouldIgnoreInvalidLinkHeaders(String value) {
        // Given
        HttpMessage msg = createMessage();
        // TODO Adjust once targeting next core version
        // msg.getResponseHeader().addHeader(HttpHeader.LINK, value);
        msg.getResponseHeader().addHeader("Link", value);
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), is(empty()));
    }

    @Test
    void shouldExtractUrlFromRefreshHeader() {
        // Given
        String url = "http://example.com/refresh";
        HttpMessage msg = createMessage();
        // TODO Adjust once targeting next core version
        // msg.getResponseHeader().addHeader(HttpHeader.REFRESH, "999; url=" + url);
        msg.getResponseHeader().addHeader("Refresh", "999; url=" + url);
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), contains(url));
    }

    @Test
    void shouldExtractRelativeUrlFromRefreshHeader() {
        // Given
        String url = "/rel/refresh";
        HttpMessage msg = createMessage();
        // TODO Adjust once targeting next core version
        // msg.getResponseHeader().addHeader(HttpHeader.REFRESH, "999; url=" + url);
        msg.getResponseHeader().addHeader("Refresh", "999; url=" + url);
        SpiderHttpHeaderParser headerParser = new SpiderHttpHeaderParser();
        TestSpiderParserListener listener = createAndAddTestSpiderParserListener(headerParser);
        // When
        boolean parsed = headerParser.parseResource(msg, null, BASE_DEPTH);
        // Then
        assertThat(parsed, is(equalTo(false)));
        assertThat(listener.getUrlsFound(), contains("http://example.com" + url));
    }

    private static HttpMessage createMessage() {
        HttpMessage msg = new HttpMessage();
        try {
            msg.setRequestHeader("GET / HTTP/1.1\r\nHost: example.com\r\n");
            msg.setResponseHeader("HTTP/1.1 200 OK\r\n");
        } catch (HttpMalformedHeaderException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }
}
