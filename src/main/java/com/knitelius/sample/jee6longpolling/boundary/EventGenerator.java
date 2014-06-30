/*
 * Copyright 2014 Stephan Knitelius <stephan@knitelius.com>.
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
package com.knitelius.sample.jee6longpolling.boundary;

import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 *
 * @author Stephan Knitelius <stephan@knitelius.com>
 */
@Singleton
public class EventGenerator {

    @Inject
    private Event<Date> dateEvent;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void fireScheduledEvent() {
        dateEvent.fire(new Date());
        System.out.println("fired");
    }
}
