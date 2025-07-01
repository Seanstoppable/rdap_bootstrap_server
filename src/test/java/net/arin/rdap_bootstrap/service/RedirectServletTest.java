/*
 * Copyright (C) 2013-2025 American Registry for Internet Numbers (ARIN)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */
package net.arin.rdap_bootstrap.service;

import jakarta.servlet.ServletConfig;
import net.arin.rdap_bootstrap.Constants;
import net.arin.rdap_bootstrap.service.JsonBootstrapFile.ServiceUrls;
import org.junit.Test;

import static net.arin.rdap_bootstrap.service.TestConstants.APNIC_HTTPS;
import static net.arin.rdap_bootstrap.service.TestConstants.ARIN_HTTP;
import static net.arin.rdap_bootstrap.service.TestConstants.EXAMPLE_HTTP;
import static net.arin.rdap_bootstrap.service.TestConstants.EXAMPLE_HTTPS;
import static net.arin.rdap_bootstrap.service.TestConstants.INFO_HTTPS;
import static net.arin.rdap_bootstrap.service.TestConstants.LACNIC_HTTPS;
import static net.arin.rdap_bootstrap.service.TestConstants.RIPE_HTTP;
import static net.arin.rdap_bootstrap.service.TestConstants.RIPE_HTTPS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class RedirectServletTest
{
    ServletConfig servletConfig = mock(ServletConfig.class);

    @Test
    public void testGetRedirectUrlDefault() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTP );
        urls.addUrl( EXAMPLE_HTTPS );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );
    }

    @Test
    public void testGetRedirectUrlDefaultOnlyHttp() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTP );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTP + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTP + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );
    }

    @Test
    public void testGetRedirectUrlDefaultOnlyHttps() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTPS );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );
    }

    @Test
    public void testGetRedirectUrlFalse() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
        System.setProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY, "false" );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTP );
        urls.addUrl( EXAMPLE_HTTPS );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );

        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
    }

    @Test
    public void testGetRedirectUrlTrue() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
        System.setProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY, "true" );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTP );
        urls.addUrl( EXAMPLE_HTTPS );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTP + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );

        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
    }

    @Test
    public void testGetRedirectUrlTrueOnlyHttp() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
        System.setProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY, "true" );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTP );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTP + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTP + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );

        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
    }

    @Test
    public void testGetRedirectUrlTrueOnlyHttps() throws Exception
    {
        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
        System.setProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY, "true" );

        ServiceUrls urls = new ServiceUrls();
        urls.addUrl( EXAMPLE_HTTPS );

        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "http", "/bar", urls ) );
        assertEquals( EXAMPLE_HTTPS + "/bar", servlet.getRedirectUrl( "https", "/bar", urls ) );

        System.clearProperty( Constants.MATCH_SCHEME_ON_REDIRECT_PROPERTY );
    }

    @Test
    public void testMakeAutNumInt() throws Exception
    {
        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( ARIN_HTTP, servlet.makeAutnumBase( "/autnum/10" ).getHttpUrl() );

        assertEquals( RIPE_HTTPS, servlet.makeAutnumBase( "/autnum/42222" ).getHttpsUrl() );
    }

    @Test
    public void testMakeIpBase() throws Exception
    {
        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( ARIN_HTTP, servlet.makeIpBase( "/ip/7.0.0.0/8" ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeIpBase( "/ip/7.0.0.0/16" ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeIpBase( "/ip/2620:0000:0000:0000:0000:0000:0000:0000" ).getHttpUrl() );

        assertEquals( LACNIC_HTTPS, servlet.makeIpBase( "/ip/191.0.1.0/24" ).getHttpsUrl() );
        assertEquals( LACNIC_HTTPS, servlet.makeIpBase( "/ip/2800:0000::/12" ).getHttpsUrl() );
        assertEquals( LACNIC_HTTPS, servlet.makeIpBase( "/ip/191.0.1.1/32" ).getHttpsUrl() );
        assertEquals( LACNIC_HTTPS, servlet.makeIpBase( "/ip/191.0.1.1" ).getHttpsUrl() );
    }

    @Test
    public void testMakeDomainBase() throws Exception
    {
        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( ARIN_HTTP, servlet.makeDomainBase( "/domain/0.0.0.7.in-addr.arpa." ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeDomainBase( "/domain/0.0.0.7.in-addr.arpa" ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeDomainBase( "/domain/0.7.in-addr.arpa" ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeDomainBase( "/domain/7.in-addr.arpa" ).getHttpUrl() );
        assertEquals( ARIN_HTTP, servlet.makeDomainBase( "/domain/0.2.6.2.ip6.arpa" ).getHttpUrl() );

        assertEquals( INFO_HTTPS, servlet.makeDomainBase( "/domain/example.INFO" ).getHttpsUrl() );
        assertEquals( INFO_HTTPS, servlet.makeDomainBase( "/domain/example.INFO." ).getHttpsUrl() );

        assertEquals( LACNIC_HTTPS, servlet.makeDomainBase( "/domain/0.0.8.2.ip6.arpa" ).getHttpsUrl() );
    }

    @Test
    public void testMakeNameserverBase() throws Exception
    {
        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( INFO_HTTPS, servlet.makeNameserverBase( "/nameserver/ns1.example.INFO" ).getHttpsUrl() );
        assertEquals( INFO_HTTPS, servlet.makeNameserverBase( "/nameserver/ns1.example.INFO." ).getHttpsUrl() );

        assertNull( servlet.makeNameserverBase( "/nameserver/ns1.5.in-addr.arpa." ) );
    }

    @Test
    public void testMakeEntityBase() throws Exception
    {
        RedirectServlet servlet = new RedirectServlet();
        servlet.init( servletConfig );

        assertEquals( APNIC_HTTPS, servlet.makeEntityBase( "/entity/ABC123-AP" ).getHttpsUrl() );

        assertEquals( ARIN_HTTP, servlet.makeEntityBase( "/entity/ABC123-ARIN" ).getHttpUrl() );

        assertEquals( LACNIC_HTTPS, servlet.makeEntityBase( "/entity/ABC123-LACNIC" ).getHttpsUrl() );

        assertEquals( RIPE_HTTP, servlet.makeEntityBase( "/entity/ABC123-RIPE" ).getHttpUrl() );
    }
}
