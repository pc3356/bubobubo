package com.bubobubo.util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.HttpHeaders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: pjc
 * Date: 21/04/2012
 * Time: 21:28
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlUtilsTest {

    private String referrer = "referer";
    @Mock
    private HttpHeaders headers;

    @Test
    public void buildUrl_should_return_slash_if_referrer_and_target_are_blank() throws Exception {

        when(headers.getRequestHeader(referrer)).thenReturn(new ArrayList<String>(){{add(null);}});
        String actual = UrlUtils.buildUrl("", headers);
        assertEquals("/", actual);
    }

    @Test
    public void buildUrl_should_return_raw_target_if_fully_qualified() throws Exception {

        when(headers.getRequestHeader(referrer)).thenReturn(new ArrayList<String>(){{add(null);}});
        String expected = "http://something/path/file.html";
        String actual = UrlUtils.buildUrl("http://something/path/file.html", headers);
        assertEquals(expected, actual);
    }

    @Test
    public void buildUrl_should_return_referrer_path_if_no_target_provided() throws Exception {

        when(headers.getRequestHeader(referrer)).thenReturn(Arrays.asList("http://localhost/something"));
        String expected = "/something";
        String actual = UrlUtils.buildUrl("", headers);
        assertEquals(expected, actual);
    }

    @Test
    public void buildUrl_should_return_target_if_no_referrer_path_provided() throws Exception {

        when(headers.getRequestHeader(referrer)).thenReturn(new ArrayList<String>(){{add(null);}});
        String expected = "/something";
        String actual = UrlUtils.buildUrl("something", headers);
        assertEquals(expected, actual);
    }

    @Test
    public void buildUrl_should_append_relative_url_to_referrer_path() throws Exception {

        when(headers.getRequestHeader(referrer)).thenReturn(Arrays.asList("http://somehost/path"));
        String expected = "/path/uri_part_of_path.extension";
        String actual = UrlUtils.buildUrl("uri_part_of_path.extension", headers);
        assertEquals(expected, actual);
    }

    @Test
    public void buildUrl_should_append_absolute_url_to_root() throws Exception {
        when(headers.getRequestHeader(referrer)).thenReturn(Arrays.asList("http://somehost/path"));
        String expected = "/path/absolute/url.ext";
        String actual = UrlUtils.buildUrl("/absolute/url.ext", headers);
        assertEquals(expected, actual);
    }
}
