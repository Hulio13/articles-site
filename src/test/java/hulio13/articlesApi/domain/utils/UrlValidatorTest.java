package hulio13.articlesApi.domain.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlValidatorTest {
    @Test
    void nonHttpLinkTest() {
        assertTrue(UrlValidator.IsUrlValid("example.com"));
    }

    @Test
    void httpLinkTest() {
        assertTrue(UrlValidator.IsUrlValid("http://example.com"));
    }

    @Test
    void httpsLinkTest() {
        assertTrue(UrlValidator.IsUrlValid("https://example.com"));
    }

    @Test
    void validUrl1(){
        assertTrue(UrlValidator.IsUrlValid("https://example.com/some/link"));
    }

    @Test
    void validUrl2(){
        assertTrue(UrlValidator.IsUrlValid("example.com/some/link"));
    }

    @Test
    void validUrl3(){
        assertTrue(UrlValidator.IsUrlValid("https://sub.example.com/some/link"));
    }

    @Test
    void invalidNonHttpLinkTestWithoutTLD() {
        assertFalse(UrlValidator.IsUrlValid("example./"));
    }

    @Test
    void invalidHttpLinkTestWithoutTLD() {
        assertFalse(UrlValidator.IsUrlValid("http://example./"));
    }

    @Test
    void invalidHttpsLinkTestWithoutTLD() {
        assertFalse(UrlValidator.IsUrlValid("https://example./"));
    }

    @Test
    void justWord(){
        assertFalse(UrlValidator.IsUrlValid("word"));
    }

    @Test
    void invalidLinkTest1(){
        assertFalse(UrlValidator.IsUrlValid("./"));
    }

    @Test
    void invalidLinkTest2(){
        assertFalse(UrlValidator.IsUrlValid("."));
    }

    @Test
    void invalidLinkTest3(){
        assertFalse(UrlValidator.IsUrlValid("/"));
    }

    @Test
    void invalidLinkTest4(){
        assertFalse(UrlValidator.IsUrlValid(".com/"));
    }

    @Test
    void invalidLinkTest5(){
        assertFalse(UrlValidator.IsUrlValid(".com"));
    }

    @Test
    void invalidLinkTest6(){
        assertFalse(UrlValidator.IsUrlValid("http://./"));
    }

    @Test
    void invalidLinkTest7(){
        assertFalse(UrlValidator.IsUrlValid("http://."));
    }

    @Test
    void emptyStrTest(){
        assertFalse(UrlValidator.IsUrlValid(" "));
    }
}

