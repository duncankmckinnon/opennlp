/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.tools.namefind;

import java.io.IOException;

import opennlp.maxent.DataStream;
import opennlp.tools.util.ObjectStream;

/**
 * The {@link NameSampleDataStream} class converts tagged {@link String}s
 * provided by a {@link DataStream} to {@link NameSample} objects.
 * It uses text that is is one-sentence per line and tokenized
 * with names identified by <code>&lt;START&gt;</code> and <code>&lt;END&gt;</code> tags.
 */
public class NameSampleDataStream implements ObjectStream<NameSample> {

  public static final String START_TAG_PREFIX = "<START:";
  public static final String START_TAG = "<START>";
  public static final String END_TAG = "<END>";


  private final ObjectStream<String> in;

  public NameSampleDataStream(ObjectStream<String> in) {
    this.in = in;
  }

  public NameSample read() throws IOException {
      String token = in.read();
      
      boolean isClearAdaptiveData = false;

      // An empty line indicates the begin of a new article
      // for which the adaptive data in the feature generators
      // must be cleared
      while (token != null && token.trim().length() == 0) {
          isClearAdaptiveData = true;
          token = in.read();
      }
      
      if (token != null) {
        return NameSample.parse(token, isClearAdaptiveData);
      }
      else {
        return null;
      }
  }

  public void reset() throws IOException,
      UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
  
  public void close() throws IOException {
    in.close();
  }
}
