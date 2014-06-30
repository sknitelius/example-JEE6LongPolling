/*
 * Copyright 2014 Stephan Knitelius.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.knitelius.sample.jee6longpolling.web;

import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletOutputStream;

/**
 * Notifies all registered peers when the observed event fires.
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Singleton
public class SampleLongPollingNotifier {

    private final Queue<AsyncContext> peers = new ConcurrentLinkedQueue();

    public void notifyPeers(@Observes Date date) {
        for (AsyncContext ac : peers) {
            try {
                final ServletOutputStream os = ac.getResponse().getOutputStream();
                os.println(date.toString());
                ac.complete();
            } catch (IOException ex) {
                // Nothing ToDo: Connection was most likely closed by client.
            } finally {
                peers.remove(ac);
            }
        }
    }

    /**
     * Add async-request for event notification.
     *
     * @param ac AsyncContext
     */
    public void addAsyncContext(final AsyncContext ac) {
        ac.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                peers.remove(ac);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        peers.add(ac);
    }
}
