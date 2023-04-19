package io.keploy.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.security.PrivateKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class HttpPostMultipartTest {

  private AutoCloseable autoCloseable;
  private HttpPostMultipart httpPostMultipart;
  @Mock
  private HttpURLConnection httpURLConnection;
  @Mock
  private OutputStream outputStream;
  @Mock
  private PrintWriter writer;

  @BeforeEach
  void setUp() throws IOException {
    autoCloseable = MockitoAnnotations.openMocks(this);
    when(httpURLConnection.getOutputStream()).thenReturn(outputStream);
    when(writer.append(any(CharSequence.class))).thenReturn(writer);
    httpPostMultipart = new HttpPostMultipart("UTF-8",httpURLConnection);

  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  void addFormField() throws IOException {
    httpPostMultipart.addFormField("foo","bar");
    verify(writer,times(4)).append(any(CharSequence.class));
    verify(writer).append("--" + httpPostMultipart.getBoundary()).append("\r\n");
    verify(writer).append("Content-Disposition: form-data; name=\"foo\"").append("\r\n");
    verify(writer).append("Content-Type: text/plain; charset=UTF-8").append("\r\n");
    verify(writer).append("bar").append("\r\n");
    verify(writer).flush();
  }
}