/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http.apache;

import com.google.api.client.http.HttpSerializer;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

final class ApacheHttpRequest implements LowLevelHttpRequest {
  private final HttpClient httpClient;

  private final HttpRequestBase request;

  ApacheHttpRequest(HttpClient httpClient, HttpRequestBase request) {
    // TODO: test with HTTPS
    this.httpClient = httpClient;
    this.request = request;
  }

  public void addHeader(String name, String value) {
    this.request.addHeader(name, value);
  }

  public LowLevelHttpResponse execute() throws IOException {
    // TODO: handle redirect on POST
    return new ApacheHttpResponse(this.httpClient.execute(request));
  }

  public void setContent(HttpSerializer serializer) {
    GDataEntity entity =
        new GDataEntity(serializer.getContentLength(), serializer);
    entity.setContentEncoding(serializer.getContentEncoding());
    entity.setContentType(serializer.getContentType());
    ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
  }

  public String getRequestLine() {
    RequestLine requestLine = this.request.getRequestLine();
    return requestLine == null ? null : requestLine.toString();
  }
}